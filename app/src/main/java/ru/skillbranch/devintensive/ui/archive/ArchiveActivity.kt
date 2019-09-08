package ru.skillbranch.devintensive.ui.archive

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.BaseActivity
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.custom.VerticalItemDecorator
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel

class ArchiveActivity : BaseActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.archive_title)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {

        }
        val divider = VerticalItemDecorator(this)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter) {
            val item = it
            viewModel.restoreFromArchive(item.id)
            val snackbar =
                Snackbar.make(
                    rv_archive_list,
                    "Восстановить чат ${item.title} из архива?",
                    Snackbar.LENGTH_LONG
                )
            snackbar.setAction(R.string.archive_undo) { viewModel.addToArchive(item.id) }
            with(snackbar.view) {
                backgroundTintList = ColorStateList.valueOf(resolveColor(R.attr.colorSnackbarBackground))
                val textView = findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(resolveColor(R.attr.colorSnackbarTextColor))
            }
            snackbar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_archive_list)

        with(rv_archive_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}
