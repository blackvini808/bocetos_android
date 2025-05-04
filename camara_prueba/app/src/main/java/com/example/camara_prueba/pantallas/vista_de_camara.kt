package com.example.camara_prueba.pantallas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Display.Mode
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import jp.co.cyberagent.android.gpuimage.*
import java.io.ByteArrayOutputStream

@Composable
fun PantallaCamara() {
    val contexto = LocalContext.current
    val cicloDeVida = LocalLifecycleOwner.current

    val gpuImageView = remember {
        GPUImageView(contexto).apply {
            setScaleType(GPUImage.ScaleType.CENTER_CROP)
        }
    }

    val pixelFilter = GPUImagePixelationFilter()
    pixelFilter.setPixel(0f)
    val filtros = listOf(
        GPUImageGrayscaleFilter(),
        GPUImageSaturationFilter(2f),
        GPUImageContrastFilter(2f),
        GPUImageBrightnessFilter(0.5f),
        GPUImageColorInvertFilter(),
        GPUImagePixelationFilter().apply { setPixel(5.0f) },
        GPUImageSphereRefractionFilter(),
        GPUImageCrosshatchFilter(),
        GPUImageSolarizeFilter()
    )
    val filtroSeleccionado = remember { mutableStateOf(0f) }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)  // Cámara trasera
        .build()

    LaunchedEffect(filtroSeleccionado.value) {
        val cameraProvider = contexto.obtenerProveedorDeCamara()
        cameraProvider.unbindAll()

        val imageAnalyzer = ImageAnalysis.Builder().build().also { analysis ->
            analysis.setAnalyzer(ContextCompat.getMainExecutor(contexto)) { imageProxy ->
                val bitmap = imageProxy.toBitmap() // Convertir imagen a Bitmap
                gpuImageView.setImage(bitmap)
                gpuImageView.filter = filtros[filtroSeleccionado.value.toInt()]
                imageProxy.close()
            }
        }

        cameraProvider.bindToLifecycle(
            cicloDeVida,
            cameraSelector,
            imageAnalyzer,
        )
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        AndroidView(factory = { gpuImageView }, modifier = Modifier.fillMaxSize())

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Slider(
                value = filtroSeleccionado.value,
                onValueChange = { filtroSeleccionado.value = it },
                valueRange = 0f..(filtros.size - 1).toFloat(),
                steps = filtros.size - 2,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Black.copy(alpha = 0.38f),
                    inactiveTrackColor = Color.Black.copy(alpha = 0.38f),
                    activeTickColor = Color.Black,
                    inactiveTickColor = Color.Black
                )
            )

            Button(onClick = {
                guardarFotoConFiltro(contexto, gpuImageView)},
                shape = RoundedCornerShape(10.dp), // Bordes redondeados
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Fondo transparente
                    contentColor = Color.Transparent // Sin color en el contenido
                ),
                border = BorderStroke(5.dp, Color.Red), // Contorno blanco visible
                modifier = Modifier.size(60.dp)
            ){

            }
        }
    }
}

private suspend fun Context.obtenerProveedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuacion ->
        ProcessCameraProvider.getInstance(this).also { proveedorCamara ->
            proveedorCamara.addListener({
                continuacion.resume(proveedorCamara.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

fun guardarFotoConFiltro(context: Context, gpuImageView: GPUImageView) {
    val nombreArchivo = "foto_${System.currentTimeMillis()}.jpg"
    val valores = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FotosConFiltro")
        }
    }

    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores)
    if (uri != null) {
        try {
            // Aquí es donde se obtiene el OutputStream
            context.contentResolver.openOutputStream(uri)?.let { outputStream ->
                // Capturamos el bitmap de la vista GPUImageView
                val bitmap = gpuImageView.capture()

                // Comprimimos la imagen y la escribimos en el OutputStream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                // Cerramos el outputStream después de escribir la imagen
                outputStream.close()

                // Log para confirmar que la foto se guardó correctamente
                Log.v("GuardarFoto", "Foto guardada exitosamente: $nombreArchivo")
            } ?: run {
                // Si no podemos abrir el OutputStream, lanzamos un error
                Log.v("GuardarFoto", "Error al abrir el OutputStream.")
            }
        } catch (e: Exception) {
            // Log para capturar cualquier error
            Log.v("GuardarFoto", "Error al guardar la foto: ${e.message}")
        }
    } else {
        // Log si el URI es nulo, es decir, no se pudo insertar la foto
        Log.v("GuardarFoto", "Error: URI de la foto es nulo.")
    }
}


fun ImageProxy.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}