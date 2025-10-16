package com.example.myapplication

// Classe représentant un lien associé à un livre (par exemple un fichier PDF ou EPUB)
data class Link(
    val url: String?,
    val title: String?
)

// Classe représentant un livre, avec ses infos principales et une liste de liens
data class BookDoc(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val links: List<Link>? = null // Liste optionnelle de liens
)

// Fonction utilitaire pour extraire un lien PDF ou EPUB depuis un BookDoc
fun getPdfUrlFromBook(book: BookDoc): String? {
    return book.links?.firstOrNull {
        it.url?.endsWith(".pdf") == true || it.url?.endsWith(".epub") == true
    }?.url
}
