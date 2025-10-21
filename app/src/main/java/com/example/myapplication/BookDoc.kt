package com.example.myapplication

data class Link(
    val url: String?,
    val title: String?
)

data class BookDoc(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val links: List<Link>? = null // Liste optionnelle de liens
)


fun getPdfUrlFromBook(book: BookDoc): String? {
    return book.links?.firstOrNull {
        it.url?.endsWith(".pdf") == true || it.url?.endsWith(".epub") == true
    }?.url
}
