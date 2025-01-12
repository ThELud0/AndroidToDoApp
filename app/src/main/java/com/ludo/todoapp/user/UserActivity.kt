package com.ludo.todoapp.user


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil3.compose.AsyncImage
import com.ludo.todoapp.detail.ui.theme.TodoAppLudoTheme
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import androidx.lifecycle.lifecycleScope


import com.ludo.todoapp.data.Api.userWebService
import com.ludo.todoapp.data.UserWebService
import com.ludo.todoapp.list.TaskListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


private fun Uri.toRequestBody(context: Context): MultipartBody.Part {
    val fileInputStream = context.contentResolver.openInputStream(this)!!
    val fileBody = fileInputStream.readBytes().toRequestBody()
    return MultipartBody.Part.createFormData(
        name = "avatar",
        filename = "avatar.jpg",
        body = fileBody
    )
}



private fun Bitmap.toRequestBody(): MultipartBody.Part {
    val tmpFile = File.createTempFile("avatar", "jpg")
    tmpFile.outputStream().use { // *use*: open et close automatiquement
        this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
    }
    return MultipartBody.Part.createFormData(
        name = "avatar",
        filename = "avatar.jpg",
        body = tmpFile.readBytes().toRequestBody()
    )
}


class UserActivity : ComponentActivity() {

    // propriété: une URI dans le dossier partagé "Images"
    private val captureUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppLudoTheme {
                //var bitmap: Bitmap? by remember { mutableStateOf(null) }
                var uri: Uri? by remember { mutableStateOf(null) }


                val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                    if (success)
                        uri = captureUri
                    // Envoyer l'image au serveur
                    val avatarPart = uri?.toRequestBody(this@UserActivity)
                    if (avatarPart != null) {
                        viewModel.editAvatar(avatarPart)
                    }

                }

                val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { selectedUri ->
                    if (selectedUri != null) {
                        uri = selectedUri
                        //bitmap = null;
                        // Envoyer l'image au serveur
                        val avatarPart = selectedUri.toRequestBody(this@UserActivity)

                        viewModel.editAvatar(avatarPart)

                    }
                }

                // Créez un launcher pour demander la permission
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        // La permission est accordée : lancez le picker
                        pickPhoto.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly // Limite à des images
                            )
                        )
                    } else {
                        // La permission est refusée
                        Toast.makeText(this, "Permission refusée. Impossible d'accéder aux fichiers.", Toast.LENGTH_SHORT).show()
                    }
                }


                Column {

                    AsyncImage(
                        modifier = Modifier.fillMaxHeight(.2f),
                        model = uri,
                        contentDescription = "Picked Image"
                    )

                    // Bouton pour déclencher l'appareil photo
                    Button(
                        onClick = { captureUri?.let { takePicture.launch(it) } }, // Lancer la capture
                        content = { Text("Take Picture") }
                    )

                    // Placeholder pour un autre bouton (ex. choisir une photo)
                    Button(
                        onClick = {
                            // Vérifiez si la permission est déjà accordée
                            if ((ContextCompat.checkSelfPermission(
                                    this@UserActivity,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            )|| (Build.VERSION.SDK_INT >= 29)) {
                                // Permission déjà accordée : lancez le picker
                                pickPhoto.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else {
                                // Demandez la permission
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        },
                        content = { Text("Pick Photo") }
                    )

                    Button(
                        onClick = { finish() },
                        content = { Text("Validate") }
                    )

                }
            }
        }
    }
}
