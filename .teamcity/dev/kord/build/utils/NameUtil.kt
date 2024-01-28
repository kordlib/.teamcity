package dev.kord.build.utils

fun String.toCamelCase(): String =
    split("-")
        .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
        .replaceFirstChar(Char::lowercaseChar)
