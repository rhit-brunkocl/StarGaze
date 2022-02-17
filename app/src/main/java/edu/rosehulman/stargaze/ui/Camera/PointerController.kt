package edu.rosehulman.stargaze.ui.Camera

import android.Manifest
import android.view.View

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.util.SizeF
import androidx.core.app.ActivityCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.stargaze.Constants
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.models.StarViewModel
import java.util.*
import kotlin.math.PI

class PointerController(val parent: CameraFragment, val icon: View) {
    var focalLength: Float
    var sensorSize: SizeF
    var viewportWidth: Int
    var viewportHeight: Int
    var sensorFusionManager: SensorFusionManager = SensorFusionManager(parent, this)

    val model = ViewModelProvider(parent.requireActivity()).get(StarViewModel::class.java)
    init{
        //var camera = parent.getCameraInstance()

        //SizeF size = new SizeF(0,0);
        val manager: CameraManager = parent.requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristics: CameraCharacteristics = manager.getCameraCharacteristics(manager.cameraIdList[0])
        sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)!!
        focalLength = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)!![0]
        viewportWidth = parent.binding.viewFinder.measuredWidth
        viewportHeight = parent.binding.viewFinder.measuredHeight

        getStarToView()
        setupGPS()
    }

    fun updateViewportSize(){
        viewportWidth = parent.binding.viewFinder.measuredWidth
        viewportHeight = parent.binding.viewFinder.measuredHeight
    }

    var phoneRotQuat = FloatArray(4)
    fun updatePointer(rQuat: FloatArray){
        phoneRotQuat = rQuat
        updatePointer()
    }

    fun multiplyQ(q1: FloatArray, q2: FloatArray): FloatArray {
        val res = FloatArray(4)
        res[0] = q1[0] * q2[0] - q1[1] * q2[1] - q1[2] * q2[2] - q1[3] * q2[3]
        res[1] = q1[1] * q2[0] + q1[0] * q2[1] + q1[2] * q2[3] - q1[3] * q2[2]
        res[2] = q1[2] * q2[0] + q1[0] * q2[2] + q1[3] * q2[1] - q1[1] * q2[3]
        res[3] = q1[3] * q2[0] + q1[0] * q2[3] + q1[1] * q2[2] - q1[2] * q2[1]
        return res
    }

    fun conjQ(q: FloatArray): FloatArray {
        val res = FloatArray(4)
        res[0] = q[0]
        res[1] = -q[1]
        res[2] = -q[2]
        res[3] = -q[3]
        return res
    }

    fun rotateQ(vec: FloatArray, q1: FloatArray): FloatArray{
        val opRes = rotateQ(vecToQuaternion(vec), q1, true)
        val res = FloatArray(3)
        res[0] = opRes[1]
        res[1] = opRes[2]
        res[2] = opRes[3]
        return res
    }

    fun rotateQ(p: FloatArray, q: FloatArray, flag: Boolean): FloatArray{
        return multiplyQ(multiplyQ(conjQ(q), p), q)
    }

    fun quaternionFromRotationAxis(vec: FloatArray): FloatArray{
        val theta = vectorLength(vec)
        val res = FloatArray(4)
        val sTheta = Math.sin((theta/2).toDouble()).toFloat()
        res[0] = Math.cos((theta/2).toDouble()).toFloat()
        res[1] = (vec[0] / theta) * sTheta
        res[2] = (vec[1] / theta) * sTheta
        res[3] = (vec[2] / theta) * sTheta
        return res
    }

    fun vectorLength(vec: FloatArray) : Float {
        return Math.sqrt((vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]).toDouble()).toFloat()
    }

    fun vectorLength2(vec: FloatArray) : Float {
        return (vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2])
    }

    fun vecToQuaternion(vec: FloatArray): FloatArray{
        val res = FloatArray(4)
        res[0] = 0.0f
        res[1] = vec[0]
        res[2] = vec[1]
        res[3] = vec[2]
        return res
    }

    fun updatePointer(){
        /* convert star data to relative longitude + absolute latitude in radians*/


        val dayLengthInS = 86400L
        val timeOfDayMS = (Calendar.getInstance().timeInMillis
                - (946684800L * 1000L) /* < unix to j2000 conversion */
                + 6852900 /* < j2000 to y2000 vernal  equinox */) % (dayLengthInS * 1000L)
        val earthRadians = ((timeOfDayMS.toFloat() / (dayLengthInS * 1000L).toFloat()) * 2.0f * PI)
        //Log.d(Constants.TAG, "$starRA : $earthRadians : $longitude : $timeOfDayMS")
        val starRelLong = (starRA + earthRadians).toFloat() // NOTE: removed - longitude bc it's accounted for in vector transform
        //Log.d(Constants.TAG, "Updating Pointer $starRelLong")


        //starDEC = (PI/4.0f).toFloat()
        //latitude = 0.0
        //longitude = 0.0
        //latitude = .68
        //starRelLong = 0f

        // This produces global coordinates (z is always from origin to N, y is origin to 0DEC 0RA)
        val starDir = FloatArray(3)
        starDir[0] = (Math.sin((starRelLong).toDouble()) * Math.cos((starDEC).toDouble())).toFloat()
        starDir[1] = (Math.cos((starRelLong).toDouble()) * Math.cos((starDEC).toDouble())).toFloat()
        starDir[2] = (Math.sin(starDEC.toDouble())).toFloat()

        // Either going to fix the quaternion transform (unlikely) or use
        // a projection onto a function for each resultant axis

        // Z up in gravity
        val selfZ = FloatArray(3)
        selfZ[0] = (Math.sin((longitude).toDouble()) * Math.cos((latitude).toDouble())).toFloat()
        selfZ[1] = (Math.cos((longitude).toDouble()) * Math.cos((latitude).toDouble())).toFloat()
        selfZ[2] = (Math.sin(latitude.toDouble())).toFloat()

        // Y points north
        val selfY = FloatArray(3)
        selfY[0] = -(Math.sin(longitude) * Math.sin(-latitude)).toFloat()
        selfY[1] = -(Math.cos(longitude) * Math.sin(-latitude)).toFloat()
        selfY[2] = -Math.cos(-latitude).toFloat()

        // X is YxZ -> points parallel to the lines of longitude
        val selfX = FloatArray(3)
        selfX[0] = selfY[1] * selfZ[2] - selfZ[1] * selfY[2]
        selfX[1] = -(selfY[0] * selfZ[2] - selfZ[0] * selfY[2])
        selfX[2] = selfY[0] * selfZ[1] - selfZ[0] * selfY[1]

        val vertRelativeStarVec = FloatArray(3)
        vertRelativeStarVec[0] = projectMag(starDir, selfX)
        vertRelativeStarVec[1] = projectMag(starDir, selfY)
        vertRelativeStarVec[2] = projectMag(starDir, selfZ)

        // TODO: convert this slow + ugly (+ broken?) vector transform into a quaternion transform
        // This should transform the spherical gyroscope vector to the ground reference vector
        //val verticalityRotation = quaternionFromRotationAxis(vec3(-(PI/2f-latitude).toFloat(), 0.0f, 0.0f))
        //val vertRelativeStarVec = rotateQ(starDir, verticalityRotation)

        val phoneAlignedDir = rotateQ(vertRelativeStarVec, phoneRotQuat)
        //Log.d(Constants.TAG, "rot: ${verticalityRotation[0]} : ${verticalityRotation[1]} : ${verticalityRotation[2]} : ${verticalityRotation[3]}")
        //Log.d(Constants.TAG, "VR:  ${vertRelativeStarVec[0]} : ${vertRelativeStarVec[1]} : ${vertRelativeStarVec[2]}")
        //Log.d(Constants.TAG, "SD:  ${starDir[0]} : ${starDir[1]} : ${starDir[2]}")

        //parent.binding.debugText.text = "<${phoneAlignedDir[0]},\n ${phoneAlignedDir[1]},\n ${phoneAlignedDir[2]}>"
        updateViewportSize()
        val yi = phoneAlignedDir[1] * (focalLength / phoneAlignedDir[2]) * (viewportWidth.toFloat() / sensorSize.width) * 2.0f
        val xi = -phoneAlignedDir[0] * (focalLength / phoneAlignedDir[2]) * (viewportWidth.toFloat() / sensorSize.width) * 2.0f
        val inFrontOfCamera = -phoneAlignedDir[2] > 0

        //Log.d(Constants.TAG, "$viewportWidth : ${sensorSize.width}")

        updateIcon(xi, yi, inFrontOfCamera)
    }

    fun projectMag(from: FloatArray, onto: FloatArray): Float{
        return dot(from, onto) / (vectorLength(onto))
    }

    fun dot(v1: FloatArray, v2: FloatArray): Float{
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2]
    }

    fun vec3(x: Float, y: Float, z: Float): FloatArray{
        val res = FloatArray(3)
        res[0] = x
        res[1] = y
        res[2] = z
        return res
    }

    fun updateIcon(x: Float, y: Float, shown: Boolean){
        icon.translationY = (y + viewportHeight / 2.0f) - icon.height
        icon.translationX = (x + viewportWidth / 2.0f) - icon.width
        Log.d(Constants.TAG, "$x")
        if(shown) {
            icon.visibility = View.VISIBLE
        }else{
            icon.visibility = View.INVISIBLE
        }
    }

    var starRA: Float = 0.0f
    var starDEC: Float = 0.0f
    fun getStarToView(){
        if(model.selectedToView == null){
            starRA = 0.0f
        }else {
            starRA = convertRA(model.selectedToView!!.WDS_RA.toFloat())
            //starRA = convertRA(64508.9f)
        }
        //starDEC = -16.7f
        starDEC = model.selectedToView?.WDS_DEC?.toFloat() ?: 0.0f
    }

    fun convertRA(date: Float): Float{
        val dayLengthInS = (24 * 60 * 60) * 1.0f
        var temp = date
        val second = date % 100
        temp -= second
        temp /= 100
        val minute = temp % 100
        temp -= minute
        temp /= 100
        val hour = temp
        Log.d(Constants.TAG, "$hour::$minute::$second")
        Log.d(Constants.TAG,"${((((hour * 60.0f) + minute) * 60.0f) + second)}")
        Log.d(Constants.TAG, "${dayLengthInS}")
        return ((((((hour * 60.0f) + minute) * 60.0f) + second)/dayLengthInS) * 2.0f * PI).toFloat()
    }

    var latitude = 0.0
    var longitude = 0.0
    lateinit var locationListener: LocationListener
    fun setupGPS(){
        locationListener = LocationListener {
            location: Location ->
            latitude = (location.latitude / 180.0f) * PI
            longitude = (location.longitude / 180.0f) * PI
            Log.d(Constants.TAG, "new lat: $longitude")
            updatePointer()
        }
        val lm = parent.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                parent.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                parent.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(Constants.TAG, "GPS Permission failed.")
            parent.findNavController().navigate(R.id.navigation_user)
            return
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2.0f, locationListener)
    }





}