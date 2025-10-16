package com.example.myapplication

// Déclaration data class pour les liens externes d'un livre
data class Link(
    val url: String?,
    val title: String?
)

// Data class BookDoc représentant un livre de l'API Open Library
data class BookDoc(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val links: List<Link>?  // Liste des liens associés au livre (ex : PDF, epub)
)

// Fonction utilitaire pour extraire un lien PDF ou EPUB à partir d'un BookDoc
fun getPdfUrlFromBook(book: BookDoc): String? {
    return book.links?.firstOrNull {
        it.url?.endsWith(".pdf") == true || it.url?.endsWith(".epub") == true
    }?.url
}
