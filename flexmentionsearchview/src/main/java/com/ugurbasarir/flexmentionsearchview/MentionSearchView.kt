package com.ugurbasarir.flexmentionsearchview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.ugurbasarir.flexmentionsearchview.listener.MentionSearchViewListener
import com.ugurbasarir.flexmentionsearchview.model.MentionableUser


/**
 * Created by ugurbasarir on 10.09.2018.
 */

class MentionSearchView : LinearLayout, SearchView.Listener {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: MentionSearchViewListener? = null
    private var searchView: SearchView
    private var editText: EditText
    private var sendButton: ImageView

    private var mentionList: ArrayList<MentionableUser> = ArrayList()
    private var filteredMentionList: ArrayList<MentionableUser> = ArrayList()

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    init {
        val v = mInflater.inflate(R.layout.mention_search_view, this, true)

        searchView = v.findViewById(R.id.searchView)
        searchView.visibility = View.GONE

        editText = v.findViewById(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                createFilteredMentionList(p0!!.toString())

            }
        })

        sendButton = v.findViewById(R.id.sendButton)
        sendButton.setOnClickListener { if (listener != null) listener!!.sendButtonClicked() }

    }

    override fun mentionListScrolled(isNewPage: Boolean) {

    }

    override fun mentionClicked(position: Int) {
        searchView.visibility = View.GONE

        val selectionPos = editText.selectionEnd

        val lastText = editText.text.substring(selectionPos)
        val normalText = editText.text.substring(0, selectionPos)
        val reversedText = normalText.reversed()

        val cleanTextNormal = reversedText.split(Regex("@"), 2)[1].reversed()
        val cleanTextPart = reversedText.split(Regex("@"), 2)[0].reversed()

        val newSelectionPos = selectionPos - cleanTextPart.length - 1

        val mentionPart = "@${filteredMentionList[position].username} "

        val text = StringBuilder(cleanTextNormal).insert(newSelectionPos, mentionPart).append(lastText)

        editText.setText(text)
        editText.setSelection(newSelectionPos + mentionPart.length)

        if (listener != null) listener!!.mentionClicked(filteredMentionList[position])
    }

    fun setListener(listener: MentionSearchViewListener) {
        this.listener = listener
    }

    fun insertMentionList(list: ArrayList<MentionableUser>) {
        mentionList.addAll(list)
        searchView.setAdapter(mentionList, this@MentionSearchView)
    }

    private fun createFilteredMentionList(phrase: String) {
        val text = phrase.substring(0, editText.selectionEnd).reversed().split(Regex(" "), 2)[0].reversed()

        searchView.visibility = if (checkIsMention(text)) View.VISIBLE else View.GONE

        filteredMentionList.clear()
        for (i in mentionList) {
            if (i.username.contains(text.replace("@", "")))
                filteredMentionList.add(i)
        }
        searchView.setAdapter(filteredMentionList, this@MentionSearchView)
    }

    private fun checkIsMention(text: String): Boolean {
        return text.contains(Regex("@[a-z0-9._]"))
    }

}