package com.volpis.permissionlibrarysample

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.volpis.permission.IPermissionRequestHelper
import com.volpis.permission.IPermissionResultHandler
import com.volpis.permission.PermissionRequestHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPermissionResultHandler {

    //region Fields
    private val permission: IPermissionRequestHelper = PermissionRequestHelper(this)
    //endregion

    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 0x1000

        private val MAIN_ACTIVITY_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    //region MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityTestPermButton.setOnClickListener {
            permission.grantPermissions(
                permissionResultHandler = this,
                permissions = MAIN_ACTIVITY_PERMISSIONS,
                permissionsRequestCode = PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onRequestPermissionsResult(
            permissionResultHandler = this,
            requestCode = requestCode,
            grantResults = grantResults
        )
    }
    //endregion

    //region IPermissionResultHandler
    override fun onPermissionsGranted() {
        Toast.makeText(this, "onPermissionsGranted", Toast.LENGTH_SHORT).show()
    }

    override fun showRationale(reGrantPermissions: () -> Unit) {
        Toast.makeText(this, "showRationale", Toast.LENGTH_SHORT).show()
    }

    override fun onDoNotAskAgainChecked() {
        Toast.makeText(this, "onDoNotAskAgainChecked", Toast.LENGTH_SHORT).show()
    }
    //endregion
}
