package com.volpis.permission

interface IPermissionRequestHelper {

    fun isPermissionsGranted(permissions: Array<String>): Boolean

    fun grantPermissions(
        permissionResultHandler: IPermissionResultHandler,
        permissions: Array<String>,
        permissionsRequestCode: Int
    )

    fun onRequestPermissionsResult(
        permissionResultHandler: IPermissionResultHandler,
        requestCode: Int,
        grantResults: IntArray
    )
}