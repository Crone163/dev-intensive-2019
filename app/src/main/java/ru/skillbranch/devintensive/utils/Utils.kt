package ru.skillbranch.devintensive.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.graphics.drawable.toDrawable
import ru.skillbranch.devintensive.R

object Utils {


    fun parseFullName(fullName: String?): Pair<String?, String?> {
        var newName = fullName?.trim()
        newName = newName?.replace(Regex("\\s{2,}"), " ")
        val parts: List<String>? = newName?.split(" ")
        var firstName = parts?.getOrNull(0)
        if (firstName.isNullOrEmpty()) firstName = null
        var lastName = parts?.getOrNull(1)
        if (lastName.isNullOrEmpty()) lastName = null
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " ") = payload.map {
        val isUpper = it.isUpperCase()
        val transLetter = when (it.toLowerCase()) {
            'а' -> "a"
            'б' -> "b"
            'в' -> "v"
            'г' -> "g"
            'д' -> "d"
            'е' -> "e"
            'ё' -> "e"
            'ж' -> "zh"
            'з' -> "z"
            'и' -> "i"
            'й' -> "i"
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
            'щ' -> "sh'"
            'ъ' -> ""
            'ы' -> "i"
            'ь' -> ""
            'э' -> "e"
            'ю' -> "yu"
            'я' -> "ya"
            ' ' -> divider
            else -> "$it"
        }
        if (isUpper) transLetter.capitalize() else transLetter
    }.joinToString("")


    fun toInitials(firstName: String?, lastName: String?) = when {
        firstName.isNullOrBlank() && lastName.isNullOrBlank() -> null
        firstName.isNullOrBlank() -> "${lastName?.first()}".toUpperCase()
        lastName.isNullOrBlank() -> "${firstName.first()}".toUpperCase()
        else -> "${firstName[0]}${lastName[0]}".toUpperCase()
    }


    fun getDrawableInitials(context: Context, initials: String): Drawable {
        val size = context.resources.getDimensionPixelSize(R.dimen.avatar_round_size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)



        val bounds = Rect()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val c = Canvas()
        c.setBitmap(bitmap)

        val halfSize = (size / 2).toFloat()

        paint.style = Paint.Style.FILL

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)

        paint.color = typedValue.data
        c.drawCircle(halfSize, halfSize, halfSize, paint)

        paint.textSize = context.resources.getDimension(R.dimen.text_initials_size)
        paint.textAlign = Paint.Align.CENTER
        paint.color = context.resources.getColor(android.R.color.white, context.theme)
        paint.getTextBounds(initials, 0, initials.length, bounds)
        c.drawText(initials, halfSize, halfSize - ((paint.descent() + paint.ascent()) / 2), paint)
        return bitmap.toDrawable(context.resources)
    }
}
