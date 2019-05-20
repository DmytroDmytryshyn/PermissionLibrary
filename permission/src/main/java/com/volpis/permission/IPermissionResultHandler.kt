package com.volpis.permission

interface IPermissionResultHandler {

    fun onPermissionsGranted()

    fun showRationale(reGrantPermissions: () -> Unit)

    fun onDoNotAskAgainChecked()
}