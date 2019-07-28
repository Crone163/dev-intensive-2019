package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile_constraint.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.toSp
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel

    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_constraint)
        initViews(savedInstanceState)
        initViewModel()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
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
            viewModel.switchTheme()
        }

        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wr_repository.error = if (!validateURL(s)) getString(R.string.error_validation_profile_link) else ""
            }

        })
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
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
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme), PorterDuff.Mode.SRC_IN)
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
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
            if (it["firstName"].toString().isNotEmpty() || it["lastName"].toString().isNotEmpty()) {
                iv_avatar.setImageDrawable(getLetterTile(it["firstName"].toString(),it["lastName"].toString()))
            }
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = if (!validateURL(et_repository.text.toString())) "" else et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
            if (firstName.isNotBlank() || lastName.isNotBlank()) {
                iv_avatar.setImageDrawable(getLetterTile(firstName, lastName))
            } else {
                iv_avatar.setImageDrawable(ContextCompat.getDrawable(this@ProfileActivity, R.drawable.avatar_default))
            }
        }
    }


    private fun validateURL(url: CharSequence?): Boolean {
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

        val pattern = Regex("""^(https://)?(www\.)?github.com/(?!$wrongNames)[^/]+$""")
        return url.isNullOrBlank() || pattern.matches(url)
    }

    private fun getColorAccent(): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }

    private fun getLetterTile(firstName: String, lastName: String): Drawable {
        val width = resources.getDimensionPixelSize(R.dimen.avatar_round_size)
        val height = resources.getDimensionPixelSize(R.dimen.avatar_round_size)


        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val initials = Utils.toInitials(firstName, lastName)

        val bounds = Rect()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val c = Canvas()
        c.setBitmap(bitmap)

        val halfWidth = (width / 2).toFloat()
        val halfHeight = (height / 2).toFloat()
        paint.style = Paint.Style.FILL
        paint.color = getColorAccent()
        c.drawCircle(halfWidth, halfHeight, halfWidth, paint)



        paint.textSize = 52f.toSp()
        paint.color = resources.getColor(android.R.color.white, theme)
        paint.getTextBounds(initials, 0, initials!!.length, bounds)
        c.drawText(
            initials.toString(),
            halfWidth - paint.measureText(initials) / 2,
            halfHeight + bounds.height() / 2,
            paint
        )
        return BitmapDrawable(resources, bitmap)
    }

}
