package ru.skillbranch.devintensive.utils

object Utils {


    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        if (firstName.equals("")) firstName = null
        var lastName = parts?.getOrNull(1)
        if (lastName.equals("")) lastName = null
        //return Pair(firstName , lastName)
        return firstName to lastName
    }


    fun transliteration(payload: String, divider: String = " "): String {
        return payload.map {
            val isUpper = it.isUpperCase()
            val transLetter = when (it.toLowerCase()) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е', 'ё', 'э' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и', 'й', 'ы' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "shch"
                'ъ', 'ь' -> ""
                'ю' -> "yu"
                'я' -> "ya"
                ' ' -> divider
                else -> "$it"
            }
            if (isUpper) transLetter.capitalize() else transLetter
        }.joinToString("")
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return when {
            firstName.isNullOrBlank() && lastName.isNullOrBlank() -> null
            firstName.isNullOrBlank() -> "${lastName?.get(0)}".toUpperCase()
            lastName.isNullOrBlank() -> "${firstName[0]}".toUpperCase()
            else -> "${firstName[0]}${lastName[0]}".toUpperCase()
        }
    }

}