package ru.sikuda.mobile.startx_foto

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.sikuda.mobile.startx_foto.ui.theme.Startx_fotoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Startx_fotoTheme {
                //RegisterForActivityResult33()
                MainScreen()
            }
        }
    }
}

val placeHolderBitmap: Bitmap = BitmapFactory.decodeResource(
    Resources.getSystem(),
    android.R.drawable.ic_menu_camera
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RegisterForActivityResult33() {

    val cameraPermission: PermissionState = cameraPermissionState()
    var resultBitmap: Bitmap? by rememberSaveable { mutableStateOf(placeHolderBitmap) }
    val launcherForImageCapture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        resultBitmap = if (it.toString().isEmpty()) {
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
    var resultBitmap: Bitmap? by rememberSaveable { mutableStateOf(placeHolderBitmap) }
    val launcherForImageCapture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        resultBitmap = if (it.toString().isEmpty()) {
            placeHolderBitmap
        } else {
            it
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        resultBitmap?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "Captured image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

//        AsyncImage(
//            model = "",
//            contentDescription = stringResource(R.string.image_content_desc),
//            modifier = Modifier.fillMaxSize(),
//            alignment = Alignment.Center
//        )

        Button(
            //modifier = Modifier.size(72.dp),
            onClick = {
                if (cameraPermission.status == PermissionStatus.Granted) {
                            launcherForImageCapture.launch()
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

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Startx_fotoTheme {
        MainScreen()
    }
}

