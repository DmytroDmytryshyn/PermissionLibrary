package com.volpis.permission

import android.app.Activity

interface IPermissionRequestHelper {

    fun isPermissionsGranted(permissions: Array<String>): Boolean

    fun grantPermissions(
        activity: Activity,
        permissionResultHandler: IPermissionResultHandler,
        permissions: Array<String>,
        permissionsRequestCode: Int
    )

    fun onRequestPermissionsResult(
        activity: Activity,
        permissionResultHandler: IPermissionResultHandler,
        requestCode: Int,
        grantResults: IntArray
    )
}