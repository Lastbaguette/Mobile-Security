package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.myapplication.ui.BookButtons


@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity() {

    private lateinit var dbPassphrase: String
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val _saveUri = mutableStateOf<Uri?>(null) // Déclaration au bon niveau

        val directoryPickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                _saveUri.value = it  // Maintenant accessible ici
            }
        }

        setContent {
            val scope = rememberCoroutineScope()
            var query by remember { mutableStateOf("") }
            var books by remember { mutableStateOf(listOf<BookDoc>()) }
            val snackbarHostState = remember { SnackbarHostState() }
            val context = LocalContext.current
            var downloadedFile by remember { mutableStateOf<File?>(null) }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text("Open Library Search") },
                        actions = {
                            Button(onClick = { directoryPickerLauncher.launch(null) }) {
                                Text("Choisir dossier")
                            }
                        }
                    )
                }
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
                                    snackbarHostState.showSnackbar("Erreur réseau: ${e.localizedMessage}")
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "Titre: ${book.title ?: "Inconnu"}")
                                    Text(text = "Auteur(s): ${book.author_name?.joinToString(", ") ?: "Inconnu"}")
                                    Text(text = "Année: ${book.first_publish_year ?: "Inconnu"}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BookButtons(
                                        book,
                                        context,
                                        scope,
                                        snackbarHostState,
                                        onDownloadComplete = { file ->
                                            downloadedFile = file
                                            scope.launch {
                                                file?.let {
                                                    val entity = BookEntity(
                                                        id = book.title ?: "unknown_id",
                                                        title = book.title ?: "N/A",
                                                        author = book.author_name?.joinToString(", ") ?: "N/A",
                                                        year = book.first_publish_year?.toString(),
                                                        filePath = it.absolutePath
                                                    )
                                                    appDatabase.bookDao().insertBook(entity)
                                                }
                                            }
                                        },
                                        saveUri = _saveUri.value
                                    )
                                }
                            }
                        }
                        downloadedFile?.let { file ->
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(this@MainActivity, PdfViewerActivity::class.java).apply {
                                            putExtra("file_path", file.absolutePath)
                                        }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Ouvrir le fichier téléchargé")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
