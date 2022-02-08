package edu.rosehulman.stargaze.ui.Camera

import android.view.View

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.SizeF

class PointerController(val parent: CameraFragment, val icon: View) {
    var focalLength: Float
    var size: SizeF
    init{
        var camera = parent.getCameraInstance()

        //SizeF size = new SizeF(0,0);
        val manager: CameraManager = parent.requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristics: CameraCharacteristics = manager.getCameraCharacteristics(manager.cameraIdList[0])
        size = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)!!
        focalLength = characteristics.get(CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION)!!.toFloat()
    }

    fun setXY(){
        icon.x = xDisplacementFromAngle()
        icon.y = yDisplacementFromAngle()
    }

    fun xDisplacementFromAngle(): Float{
        return 0.0f
    }

    fun yDisplacementFromAngle(): Float{
        return 0.0f
    }

}