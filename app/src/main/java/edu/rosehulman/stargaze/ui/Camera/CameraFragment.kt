package edu.rosehulman.stargaze.ui.Camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import edu.rosehulman.stargaze.Constants
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    lateinit var binding: FragmentCameraBinding
    var pointerController: PointerController? = null
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        attemptStart()

        return binding.root
    }

    fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(activity as LifecycleOwner, cameraSelector, preview)
    }

    fun attemptStart(){
        if(!checkPermissions()){
            return
        }

        if(checkCameraHardware(requireContext())) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener(Runnable {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            }, ContextCompat.getMainExecutor(requireContext()))
        }

        pointerController = PointerController(this, binding.icon)
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    companion object{
        enum class PERMS {
            PERM_CAMERA,
            PERM_GPS,
            PERM_FINE_LOC,
            PERM_COURSE_LOC
        }
    }

    private fun checkPermissions(): Boolean{
        // Check to see if we already have permissions
        if (ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If we do not, request them from the user
            Log.d(Constants.TAG, "Requesting permission")
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                PERMS.PERM_CAMERA.ordinal
            )
            return false
        }

        if (ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If we do not, request them from the user
            Log.d(Constants.TAG, "Requesting permission")
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMS.PERM_GPS.ordinal
            )
            return false
        }
        val courseLocPerm = PermissionManager(this, PERMS.PERM_COURSE_LOC.ordinal, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocPerm = PermissionManager(this, PERMS.PERM_FINE_LOC.ordinal, Manifest.permission.ACCESS_FINE_LOCATION)
        if(!courseLocPerm.checkPermissions()){
            Log.d(Constants.TAG, "Requesting permission")
            courseLocPerm.requestPermissions()
            return false
        }
        if(!fineLocPerm.checkPermissions()){
            Log.d(Constants.TAG, "Requesting permission")
            fineLocPerm.requestPermissions()
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(Constants.TAG, "PERM RESULT")
        when (requestCode) {
            PERMS.PERM_CAMERA.ordinal -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                    attemptStart()
                } else {
                    // permission denied
                    findNavController().navigate(R.id.navigation_user)
                }
                return
            }
            PERMS.PERM_GPS.ordinal -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                    attemptStart()
                } else {
                    // permission denied
                    findNavController().navigate(R.id.navigation_user)
                }
            }
            PERMS.PERM_FINE_LOC.ordinal -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                    attemptStart()
                } else {
                    // permission denied
                    findNavController().navigate(R.id.navigation_user)
                }
            }
            PERMS.PERM_COURSE_LOC.ordinal -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                    attemptStart()
                } else {
                    // permission denied
                    findNavController().navigate(R.id.navigation_user)
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        pointerController?.sensorFusionManager?.unregister()
    }

    override fun onResume() {
        super.onResume()
        pointerController?.sensorFusionManager?.register()
    }

}