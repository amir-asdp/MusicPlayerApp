package com.example.musicplayerapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionUtils {

    val mediaPermission: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ Manifest.permission.READ_MEDIA_AUDIO }
        else { Manifest.permission.READ_EXTERNAL_STORAGE }

    fun isMediaPermissionGranted(activity: Activity): Boolean {
        return activity.checkSelfPermission(mediaPermission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestMediaPermission(activity: AppCompatActivity, onPermissionGranted: () -> Unit){
        val appSettingsLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (isMediaPermissionGranted(activity)){
                onPermissionGranted()
            }
        }

        Dexter.withContext(activity)
            .withPermissions(mediaPermission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(permissionsReport: MultiplePermissionsReport) {
                    onPermissionGranted()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequests: MutableList<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    buildPermissionRationaleDialog(activity, appSettingsLauncher).show()
                }
            })
            .onSameThread()
            .check()
    }

    fun buildPermissionRationaleDialog(context: Context, appSettingsLauncher: ActivityResultLauncher<Intent>): AlertDialog {
        return MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setIcon(R.drawable.ic_permission_media)
            .setTitle(context.getString(R.string.permission_dialog_title))
            .setMessage(context.getString(R.string.permission_dialog_message))
            .setPositiveButton(context.getString(R.string.permission_dialog_button)){ dialog, which ->
                appSettingsLauncher.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
                )
            }
            .create()
    }

}