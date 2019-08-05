package ru.skillbranch.devintensive.extensions

import android.content.res.Resources
import android.util.TypedValue

fun Float.toSp(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    Resources.getSystem().displayMetrics
)

fun Float.toDp(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
)