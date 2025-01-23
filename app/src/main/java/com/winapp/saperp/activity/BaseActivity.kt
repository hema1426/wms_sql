package com.winapp.saperp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


open class BaseActivity : AppCompatActivity() {

    private val MY_CAMERA_REQUEST_CODE = 100
    private var permissionEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
//        showBottomSheetDialog(Utils.getProductList())
    }

    fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
            }
            return permissionEnabled
        } else
            return true
    }


    }
