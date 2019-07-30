package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    val newString = this.trim()
    return when {
        newString.length <= length -> newString
        else -> "${newString.take(length).trimEnd()}..."
    }
}

fun String.stripHtml(): String = this
    .replace(Regex("\\s{2,}"), " ")
    .replace(Regex("<.*?>|&#\\d+?|\\w+?;"), "")



fun String.isCorrectURL(): Boolean {
    val wrongNames = listOf(
        "enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    ).joinToString("|")
    return this.matches(Regex("""^(https://)?(www\.)?github\.com/(?!($wrongNames)/?$)[\-\w]+/?$"""))
}