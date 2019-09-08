package ru.skillbranch.devintensive.ui.profile

import android.graphics.*

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile_constraint.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.isCorrectURL
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.BaseActivity
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.utils.Utils.getDrawableInitials
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class ProfileActivity : BaseActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel

    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_profile_constraint)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
        super.onSaveInstanceState(outState)
    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            switchTheme()
        }

        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //ColorAccent совпадает с цветом Error
                if (!s.toString().isCorrectURL()) {
                    wr_repository.isErrorEnabled = true
                    wr_repository.error = getString(R.string.error_validation_profile_link)
                } else {
                    wr_repository.error = ""
                    wr_repository.isErrorEnabled = false
                }
            }

        })
    }


    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            setOf(
                "firstName",
                "lastName",
                "about",
                "repository"
            ).contains(it.key)
        }
        for ((_, v) in info) {
            v.isEnabled = isEdit
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
    }



    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
            Utils.toInitials(profile.firstName, profile.lastName)?.let {
                val typedValue = TypedValue()
                theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
                iv_avatar.setImageDrawable(getDrawableInitials(this, it, typedValue.data))
            }
                ?: iv_avatar.setImageResource(R.drawable.avatar_default)
        }
    }


    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = if (!et_repository.text.toString().isCorrectURL()) "" else et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }


}
