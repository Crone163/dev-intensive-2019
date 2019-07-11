package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getHeight


fun Activity.hideKeyboard() {
    val focusedView = this.currentFocus
    if (focusedView != null) {
        val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}

// source https://stackoverflow.com/a/9108219/5402662
fun Activity.isKeyboardOpen(): Boolean {
    val rootView = window.decorView.rootView
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootView.height
    val heightDifference = screenHeight - (r.bottom - r.top)
    return heightDifference > screenHeight / 4
}

fun Activity.isKeyboardClosed(): Boolean {
    val rootView = window.decorView.rootView
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootView.height
    val heightDifference = screenHeight - (r.bottom - r.top)
    return heightDifference < screenHeight / 4
}

/*
fun Activity.isKeyboardOpen(): Boolean {
    val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }
    val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = windowHeightMethod.invoke(imm) as Int
    return height > 0
}

fun Activity.isKeyboardClosed(): Boolean {
    val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }
    val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = windowHeightMethod.invoke(imm) as Int
    return height == 0
}*/
