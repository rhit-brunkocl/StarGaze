package edu.rosehulman.stargaze.ui.Camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionManager(val parent: CameraFragment, val permissionID: Int = 42, val permission: String) {

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(parent.requireActivity(), permission) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    fun requestPermissions() {
        parent.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            permissionID
        )
    }
}