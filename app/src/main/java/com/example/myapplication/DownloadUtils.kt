package com.example.myapplication

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.activity.ComponentActivity


suspend fun downloadFile(
    context: Context,
    fileUrl: String,
    fileName: String,
    saveUri: Uri? = null
): File? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }
            if (saveUri != null) {
                // Utilisation de SAF et ContentResolver
                val resolver: ContentResolver = context.contentResolver
                resolver.openOutputStream(saveUri)?.use { outputStream ->
                    connection.inputStream.copyTo(outputStream)
                }
                // Pas de fichier File classique ici, juste retour null (ou gérer inacces)
                null
            } else {
                // Sauvegarde dans filesDir par défaut
                val file = File(context.filesDir, fileName)
                connection.inputStream.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
