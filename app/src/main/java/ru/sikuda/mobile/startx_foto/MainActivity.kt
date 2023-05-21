package ru.sikuda.mobile.startx_foto

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import ru.sikuda.mobile.startx_foto.ui.theme.Startx_fotoTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Startx_fotoTheme {

//                val result = remember {mutableStateOf<Bitmap?>(null)}
//                val launcher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
//                    result.value = it
//                }
                // A surface container using the 'background' color from the theme
                //RegisterForActivityResult33(result, launcher)
                MainScreen()
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "$packageName.provider", tmpFile)
    }
}

@Composable
fun RegisterForActivityResult33(
    result: MutableState<Bitmap?>,
    launcher: ActivityResultLauncher<Void?>
) {

    Button(onClick = { launcher.launch() }) {
        Text(text = "Take a picture")
    }

    result.value?.let { image ->
        Image(image.asImageBitmap(), null, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun MainScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
//            Image(
//                painterResource(R.drawable.ic_launcher_background),
//                contentDescription = "Take photo",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//             )

        AsyncImage(
            model = "",
            contentDescription = stringResource(R.string.image_content_desc),
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center
        )

        Button(
            //modifier = Modifier.size(72.dp),
            onClick = {
//                if (shouldShowRequestPermissionRationale(Activity(), Manifest.permission.CAMERA)) {
//                    // we need to tell user why do we need permission
//                    showToast(R.string.need_permission)
//                } else {
//                    //cameraPermission.launch(Manifest.permission.CAMERA)
//                }
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

private fun showToast(textId: Int) {
    //Toast.makeText(this, textId, Toast.LENGTH_SHORT).show()
}