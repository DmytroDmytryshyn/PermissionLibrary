package com.volpis.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

class PermissionRequestHelper constructor(private val context: Context) : IPermissionRequestHelper {

    //region Fields
    private val nonGrantedPermissionsHolder: PermissionsHolder = PermissionsHolder()
    private var permissionsRequestCode: Int = 0x0
    //endregion

    //region IPermissionRequestHelper
    override fun isPermissionsGranted(permissions: Array<String>) = getNonGrantedPermissions(permissions).isEmpty()

    override fun grantPermissions(
        permissionResultHandler: IPermissionResultHandler,
        permissions: Array<String>,
        permissionsRequestCode: Int
    ) {
        this.permissionsRequestCode = permissionsRequestCode
        nonGrantedPermissionsHolder.addNonGrantedPermissions(getNonGrantedPermissions(permissions))
        if (!nonGrantedPermissionsHolder.isAllPermissionsGranted) {
            performPermissionsRequest(permissionResultHandler)
        } else {
            permissionResultHandler.onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        permissionResultHandler: IPermissionResultHandler,
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == permissionsRequestCode) {
            nonGrantedPermissionsHolder.removeGrantedPermissions(grantResults)
            if (nonGrantedPermissionsHolder.isAllPermissionsGranted) {
                permissionResultHandler.onPermissionsGranted()
            } else if (grantResults.isNotEmpty()) {
                handlePermissionDenied(permissionResultHandler)
            }
        }
    }
    //endregion

    //region Utility API
    private fun performPermissionsRequest(permissionResultHandler: IPermissionResultHandler) {
        when (permissionResultHandler) {
            is Activity -> ActivityCompat.requestPermissions(
                permissionResultHandler,
                nonGrantedPermissionsHolder.permissionsAsArray,
                permissionsRequestCode
            )
            is Fragment -> permissionResultHandler.requestPermissions(
                nonGrantedPermissionsHolder.permissionsAsArray,
                permissionsRequestCode
            )
            else -> throw IllegalArgumentException("IPermissionResultHandler must be Activity or Fragment!")
        }
    }

    private fun handlePermissionDenied(
        permissionResultHandler: IPermissionResultHandler
    ) {
        if (shouldShowRationale(permissionResultHandler)) {
            permissionResultHandler.showRationale {
                performPermissionsRequest(permissionResultHandler)
            }
        } else {
            permissionResultHandler.onDoNotAskAgainChecked()
        }
    }

    private fun shouldShowRationale(permissionResultHandler: IPermissionResultHandler): Boolean {
        val activity: Activity
        when(permissionResultHandler) {
            is Activity -> activity = permissionResultHandler
            is Fragment -> activity = permissionResultHandler.activity!!
            else -> throw IllegalArgumentException("IPermissionResultHandler must be Activity or Fragment!")
        }

        for (permission in nonGrantedPermissionsHolder.getPermissions()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    private fun getNonGrantedPermissions(permissions: Array<String>) = permissions.filter { !isPermissionGranted(it) }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    //endregion

    //region Utility Structures
    class PermissionsHolder {

        // TODO: Think about naming, maybe change some logic to make it more clear

        private val permissions = ArrayList<String>()
        val permissionsAsArray: Array<String> get() = permissions.toTypedArray()
        val isAllPermissionsGranted: Boolean get() = permissions.isEmpty()

        fun getPermissions(): List<String> {
            return permissions
        }

        fun addNonGrantedPermissions(nonGrantedPermissions: List<String>) {
            permissions.clear()
            permissions.addAll(nonGrantedPermissions)
        }

        fun removeGrantedPermissions(grantResults: IntArray) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    permissions.removeAt(grantResults.indexOf(result))
                }
            }
        }
    }
    //endregion
}