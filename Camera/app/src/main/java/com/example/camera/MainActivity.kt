package com.example.camera

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.camera.ui.theme.CameraTheme
import androidx.camera.view.PreviewView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : ComponentActivity() {
    //private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
         */

        setContent {
            CameraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        context = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    /*
    fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder().build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }
     */
}



@Composable
fun Greeting(context: Context, modifier: Modifier = Modifier) {
    val cameraProvider: CameraProvider = CameraProvider()
    val lifecycleOwner = LocalLifecycleOwner.current
    CameraPreview(
        modifier = modifier,
        update = {
            cameraProvider.bindCameraToPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = it,
            )
        },
    )
}

