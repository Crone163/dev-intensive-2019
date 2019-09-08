package ru.skillbranch.devintensive.ui.group

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.BaseActivity
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.ui.custom.VerticalItemDecorator
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.GroupViewModel

class GroupActivity : BaseActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.enter_name)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if(item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.create_group_title)
    }

    private fun initViews() {
        userAdapter = UserAdapter { viewModel.handleSelectedItem(it.id) }
        val divider = VerticalItemDecorator(this)
        with(rv_user_list) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            viewModel.handleCreateGroup()
            finish()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        viewModel.getUsersData().observe(this, Observer { userAdapter.updateData(it) })
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if (isShow) fab.show()
        else fab.hide()
    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            text = user.fullName

            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(resolveColor(R.attr.colorChipButtonBackground))
            chipBackgroundColor = ColorStateList.valueOf(resolveColor(R.attr.colorChipBackground))
            setTextColor(Color.WHITE)
        }
        if (user.avatar == null) {
            chip.chipIcon = if (user.initials.isNullOrEmpty()) resources.getDrawable(
                R.drawable.avatar_default,
                theme
            ) else Utils.getDrawableInitials(this@GroupActivity,user.initials)
        } else {
            Glide.with(this)
                .asBitmap()
                .load(user.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        chip.chipIcon = resource.toDrawable(resources)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }

        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>) {
        chip_group.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE
        val users = listUsers.associateBy { user -> user.id }.toMutableMap()
        val views = chip_group.children.associateBy { view -> view.tag }

        for ((k, v) in views) {
            if (!users.contains(k)) chip_group.removeView(v)
            else users.remove(k)
        }

        users.forEach { (_, v) -> addChipToGroup(v) }
    }



}
