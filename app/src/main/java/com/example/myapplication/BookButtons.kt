package com.example.myapplication.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch
import com.example.myapplication.BookDoc
import com.example.myapplication.getPdfUrlFromBook
import com.example.myapplication.downloadFile
import java.io.File
import android.net.Uri

@Composable
fun BookButtons(
    book: BookDoc,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDownloadComplete: ((File?) -> Unit)? = null,
    saveUri: Uri? = null
) {
    Button(
        onClick = {
            val pdfUrl = getPdfUrlFromBook(book)
            if (pdfUrl != null) {
                scope.launch {
                    val file = downloadFile(context, pdfUrl, "${book.title ?: "book"}.pdf", saveUri)
                    if (file != null) {
                        onDownloadComplete?.invoke(file)
                    } else {
                        snackbarHostState.showSnackbar("Échec du téléchargement")
                    }
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Aucun lien PDF pour ce livre")
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Télécharger ce livre")
    }

}
