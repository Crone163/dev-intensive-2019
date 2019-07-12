package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardClosed
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {

    object SavedInstancesConst {
        const val STATUS = "status"
        const val QUESTION = "question"
        const val TEXT = "text"
    }

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    //region=================================== LIFE_CYCLES ===================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //findViewById
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        //


        val status = savedInstanceState?.getString(SavedInstancesConst.STATUS) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(SavedInstancesConst.QUESTION) ?: Bender.Question.NAME.name
        val text = savedInstanceState?.getString(SavedInstancesConst.TEXT) ?: Bender.Question.NAME.question

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = text

        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClick(sendBtn)
                hideKeyboard()
                true
            } else {
                false
            }
        }

        sendBtn.setOnClickListener(this)
        benderImage.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(SavedInstancesConst.STATUS, benderObj.status.name)
        outState?.putString(SavedInstancesConst.QUESTION, benderObj.question.name)
        outState?.putString(SavedInstancesConst.TEXT, textTxt.text.toString())
        super.onSaveInstanceState(outState)

    }
    //endregion

    //region=================================== CLICKS ===================================
    override fun onClick(v: View?) {
        if (v == sendBtn) {
            if (!messageEt.text.isEmpty()) {
                if (benderObj.question != Bender.Question.IDLE) {
                    val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().trim())
                    val (r, g, b) = color
                    benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
                    textTxt.text = phrase
                }
                messageEt.setText("")
                hideKeyboard()
            } else {
                Toast.makeText(this, "Введите ответ", Toast.LENGTH_LONG).show()
            }
        }
        //debug
        if (v == benderImage) {
            if (isKeyboardOpen()) {
                Toast.makeText(this, "Клавиатура открыта", Toast.LENGTH_LONG).show()
            }
            if (isKeyboardClosed()) {
                Toast.makeText(this, "Клавиатура закрыта", Toast.LENGTH_LONG).show()
            }
        }
    }
    //endregion


}
