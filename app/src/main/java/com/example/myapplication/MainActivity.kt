package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            var query by remember { mutableStateOf("") }
            var books by remember { mutableStateOf(listOf<BookDoc>()) }
            val scaffoldState = rememberScaffoldState()
            val context = LocalContext.current
            var downloading by remember { mutableStateOf(false) }
            var downloadedFile by remember { mutableStateOf<File?>(null) }

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { TopAppBar(title = { Text("Open Library Search") }) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val response = ApiClient.apiService.searchBooks(query.trim())
                                    books = response.docs ?: emptyList()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    scaffoldState.snackbarHostState.showSnackbar("Network error: ${e.localizedMessage}")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Search")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(books) { book ->
                            Card(
                                elevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .clickable {
                                        // Exemple URL de fichier à télécharger - à adapter
                                        val pdfUrl = "https://example.com/sample.pdf"
                                        val fileName = "downloaded_sample.pdf"
                                        scope.launch {
                                            downloading = true
                                            val file = downloadFile(context, pdfUrl, fileName)
                                            downloading = false
                                            if (file != null) {
                                                downloadedFile = file
                                            } else {
                                                scaffoldState.snackbarHostState.showSnackbar("Download failed")
                                            }
                                        }
                                    }
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "Title: ${book.title ?: "Unknown"}")
                                    Text(text = "Author: ${book.author_name?.joinToString(", ") ?: "Unknown"}")
                                    Text(text = "Year: ${book.first_publish_year ?: "Unknown"}")
                                }
                            }
                        }
                    }

                    downloadedFile?.let { file ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                // Ouvrir le fichier PDF avec un Intent
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf")
                                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Open Downloaded File")
                        }
                    }

                    if (downloading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
