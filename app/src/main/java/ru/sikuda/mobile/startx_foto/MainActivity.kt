package ru.sikuda.mobile.startx_foto

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.sikuda.mobile.startx_foto.ui.theme.Startx_fotoTheme
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Startx_fotoTheme {
                //MainScreenPreview()
                MainScreen()
            }
        }
    }
}

val placeHolderBitmap: Bitmap = BitmapFactory.decodeResource(
    Resources.getSystem(),
    android.R.drawable.ic_menu_camera
)

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreenPreview() {

    val cameraPermission: PermissionState = cameraPermissionState()
    var resultBitmap: Bitmap? by rememberSaveable { mutableStateOf(placeHolderBitmap) }
    val launcherForImageCapture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        resultBitmap = if (it.toString().isNotEmpty()) {
            placeHolderBitmap
        } else {
            it
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .clip(shape = CircleShape)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = CircleShape
            )
    ) {
        resultBitmap?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "Captured image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clickable {
                        if (cameraPermission.status == PermissionStatus.Granted) {
                            launcherForImageCapture.launch()
                        } else  {
                            cameraPermission.launchPermissionRequest()
                        }
                    }
            )
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun cameraPermissionState(): PermissionState {
    return rememberPermissionState(permission = Manifest.permission.CAMERA)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {

    val cameraPermission: PermissionState = cameraPermissionState()

    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        if (hasImage && imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.image_content_desc),
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center
            )
        }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //images from devices
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                },
            ) {
                Text(text = "Select Image")
            }
            //image from camera
            Button(
                onClick = {
                    if (cameraPermission.status == PermissionStatus.Granted) {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        cameraPermission.launchPermissionRequest()
                    }
                }
            ) {
                Image(
                    painterResource(R.drawable.ic_photo),
                    contentDescription = "Take photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp),
                    alignment = Alignment.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Startx_fotoTheme {
        MainScreen()
    }
}

