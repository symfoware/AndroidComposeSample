package com.example.camera

import android.content.Context
import android.view.Surface
import android.view.View
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.impl.ImageOutputConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner

class CameraProvider {
    data class CameraPreviewConfig(
        @AspectRatio.Ratio val aspectRatio: Int,
        @ImageOutputConfig.RotationValue val rotation: Int,
    )

    private fun getProvider(context: Context): ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(context).get()
    }

    fun hasBackCamera(context: Context): Boolean {
        val provider = getProvider(context)
        return provider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    fun hasFrontCamera(context: Context): Boolean {
        val provider = getProvider(context)
        return provider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    }
    private fun buildCameraSelector(
        context: Context,
    ): CameraSelector {
        return CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    }

    private fun getCameraProviderConfig(previewView: View): CameraPreviewConfig {
        //val rotation = previewView.context.display?.rotation ?: Surface.ROTATION_0
        return CameraPreviewConfig(
            aspectRatio = AspectRatio.RATIO_4_3,
            rotation = Surface.ROTATION_0,
        )
    }

    private fun buildPreview(previewView: View): Preview {
        val (screenAspectRatio, rotation) = getCameraProviderConfig(previewView)

        return Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
    }

    fun bindCameraToPreview(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val provider = getProvider(context)

        val cameraSelector = buildCameraSelector(context)
        val preview = buildPreview(previewView)
        //val imageCapture = buildImageCapture(previewView)
        //val imageAnalysis = buildImageAnalysis(previewView)

        provider.unbindAll()

        provider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            //imageCapture,
            //imageAnalysis,
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
}

@Composable
internal fun rememberCameraProvider(): CameraProvider {
    return remember {
        CameraProvider()
    }
}