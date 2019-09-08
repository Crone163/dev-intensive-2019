package ru.skillbranch.devintensive.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.viewmodels.BaseViewModel
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(BaseViewModel::class.java)
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })

    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    fun switchTheme() {
        viewModel.switchTheme()
    }

    fun resolveColor(colorInt: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(colorInt, typedValue, true)
        return typedValue.data
    }
}
