package com.volpis.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import javax.inject.Inject

class PermissionRequestHelper @Inject constructor(private val context: Context) : IPermissionRequestHelper {

    //region Fields
    private val nonGrantedPermissionsHolder: PermissionsHolder = PermissionsHolder()
    private var permissionsRequestCode: Int = 0x0
    //endregion

    //region IPermissionRequestHelper
    override fun isPermissionsGranted(permissions: Array<String>) = getNonGrantedPermissions(permissions).isEmpty()

    override fun grantPermissions(
        activity: Activity,
        permissionResultHandler: IPermissionResultHandler,
        permissions: Array<String>,
        permissionsRequestCode: Int
    ) {
        this.permissionsRequestCode = permissionsRequestCode
        nonGrantedPermissionsHolder.addNonGrantedPermissions(getNonGrantedPermissions(permissions))
        if (!nonGrantedPermissionsHolder.isAllPermissionsGranted) {
            performPermissionsRequest(activity)
        } else {
            permissionResultHandler.onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        activity: Activity,
        permissionResultHandler: IPermissionResultHandler,
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == permissionsRequestCode) {
            nonGrantedPermissionsHolder.removeGrantedPermissions(grantResults)
            if (nonGrantedPermissionsHolder.isAllPermissionsGranted) {
                permissionResultHandler.onPermissionsGranted()
            } else if (grantResults.isNotEmpty()) {
                handlePermissionDenied(activity, permissionResultHandler)
            }
        }
    }
    //endregion

    //region Utility API
    private fun performPermissionsRequest(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            nonGrantedPermissionsHolder.permissionsAsArray,
            permissionsRequestCode
        )
    }

    private fun handlePermissionDenied(
        activity: Activity,
        permissionResultHandler: IPermissionResultHandler
    ) {
        if (shouldShowRationale(activity)) {
            permissionResultHandler.showRationale {
                performPermissionsRequest(activity)
            }
        } else {
            permissionResultHandler.onDoNotAskAgainChecked()
        }
    }

    private fun shouldShowRationale(activity: Activity): Boolean {
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