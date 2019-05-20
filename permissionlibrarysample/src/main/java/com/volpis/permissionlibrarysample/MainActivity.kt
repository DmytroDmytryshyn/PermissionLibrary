package com.volpis.permissionlibrarysample

import android.Manifest
import android.app.AlertDialog
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
                activity = this,
                permissionResultHandler = this,
                permissions = MAIN_ACTIVITY_PERMISSIONS,
                permissionsRequestCode = PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onRequestPermissionsResult(
            activity = this,
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
        window.decorView.postDelayed({
            AlertDialog.Builder(this)
                .setMessage("Please, grant permission so we can continue!")
                .setPositiveButton("Ok") { _, _ ->
                    reGrantPermissions.invoke()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }, 300)
    }

    override fun onDoNotAskAgainChecked() {
        AlertDialog.Builder(this)
            .setMessage("Please go to settings and grand permission by yourself!")
            .setPositiveButton("Ok") { _, _ ->
                Toast.makeText(this@MainActivity, "Go to settings!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    //endregion
}
