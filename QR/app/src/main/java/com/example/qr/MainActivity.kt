package com.example.qr

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.qr.ui.theme.QRTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkCameraPermission {
            setContent {
                QRTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainContent(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun checkCameraPermission(onGranted: () -> Unit) {
        val cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> // パーミッションダイアログ表示後に呼び出されるコールバック
                if (isGranted) {
                    // パーミッションが与えられたら、onGrantedコールバックを呼び出す
                    onGranted()
                } else {
                    // バーミッションが与えられなかった時は、設定アプリを開き、このアプリを終了する
                    val settingsAppUri = "package:$packageName"
                    val intent =
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            settingsAppUri.toUri(),
                        )
                    startActivity(intent)
                    finish()
                }
            }

        // カメラパーミッションが与えられていれば、onGrantedコールバックを呼び出し、そうでなければパーミッションをリクエストする
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted()
        } else {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier
) {
    val detectedQrCode = remember { mutableStateOf("") }
    Box {
        CameraPreview(modifier = modifier.fillMaxSize()) {
            detectedQrCode.value = it
        }

        Text(
            text = detectedQrCode.value,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            modifier = modifier.offset(x = 10.dp, y = 80.dp)
        )
    }

}
