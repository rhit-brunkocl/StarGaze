package edu.rosehulman.stargaze.ui.Camera

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import edu.rosehulman.stargaze.Constants
import edu.rosehulman.stargaze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    lateinit var binding: FragmentCameraBinding

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        checkPermissions()

        if(checkCameraHardware(requireContext())) {
            mCamera = getCameraInstance()
            mPreview = mCamera?.let {
                // Create our Preview view
                CameraPreview(requireContext(), it)
            }

            // Set the Preview view as the content of our activity.
            mCamera!!.setDisplayOrientation(90)
            mPreview?.also {
                val preview: FrameLayout = binding.cameraPreview
                preview.addView(it)
            }
        }

        return binding.root
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            Log.d(Constants.TAG, "Failed to get camera")
            null // returns null if camera is unavailable
        }
    }

    companion object{
        enum class PERMS {
            PERM_CAMERA,
            PERM_GPS
        }
    }

    private fun checkPermissions() {
        // Check to see if we already have permissions
        if (ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If we do not, request them from the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                PERMS.PERM_CAMERA.ordinal
            )
        }

        if (ContextCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If we do not, request them from the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMS.PERM_GPS.ordinal
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMS.PERM_CAMERA.ordinal -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                } else {
                    // permission denied
                }
                return
            }
            PERMS.PERM_GPS.ordinal -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(Constants.TAG, "Permission granted")
                } else {
                    // permission denied
                }
            }
        }
    }

}