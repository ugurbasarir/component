package com.ugurbasarir.flexmentionsearchview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ugurbasarir.flexmentionsearchview.model.MentionableUser
import kotlinx.android.synthetic.main.item_mention.view.*
import java.util.*


/**
 * Created by ugurbasarir on 10.09.2018.
 */

class SearchView : LinearLayout {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    private var mentionLay: LinearLayout
    private var mentionRecyclerview: RecyclerView
    private lateinit var editText: EditText

    private var listener: Listener? = null
    private var list: ArrayList<MentionableUser> = ArrayList()

    private var isScrollingLocked: Boolean = false

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    interface Listener {
        fun mentionListScrolled(isNewPage: Boolean)

        fun mentionClicked(position: Int)
    }

    init {
        val v = mInflater.inflate(R.layout.search_view, this, true)

        mentionLay = v.findViewById(R.id.mentionLay)

        mentionRecyclerview = v.findViewById(R.id.recyclerView)
        mentionRecyclerview.layoutManager = LinearLayoutManager(context)

        mentionRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerView!!.layoutManager.childCount
                val totalItemCount = recyclerView.layoutManager.itemCount
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (!isScrollingLocked && listener != null) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 3
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 5
                    ) {
                        listener!!.mentionListScrolled(true)
                    } else {
                        listener!!.mentionListScrolled(false)
                    }
                }
            }
        })

    }

    fun setScrollingLock(isLocked: Boolean) {
        this.isScrollingLocked = isLocked
    }

    fun setAdapter(list: ArrayList<MentionableUser>, mentionViewListener: Listener) {
        this.list = list
        this.listener = mentionViewListener
        this.mentionRecyclerview.adapter = MentionAdapter(this.list, mentionViewListener)

        setAutoHeight()
    }

    fun updateAdapter(list: ArrayList<MentionableUser>, isIndexZero: Boolean) {
        if (isIndexZero) {
            this.list.addAll(0, list)
            this.mentionRecyclerview.adapter.notifyItemRangeInserted(0, list.size)
        } else {
            val startPos = list.size + 1
            this.list.addAll(list)
            this.mentionRecyclerview.adapter.notifyItemRangeInserted(startPos, list.size)
        }
    }

    fun setEditText(editText: EditText) {
        this.editText = editText
    }

    private fun setAutoHeight() {
        var params = mentionLay.layoutParams
        if (list.size > 3) {
            params.height = dpToPx(context, 120)
        } else {
            params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mentionLay.layoutParams = params
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    inner class MentionAdapter(
        private val list: ArrayList<MentionableUser>,
        private val listener: Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount() = this.list.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return Mention(LayoutInflater.from(parent.context).inflate(R.layout.item_mention, parent, false))
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val item = list[position]
            holder as Mention

            // Set avatar
            holder.avatar.setImageURI(Uri.parse(item.photoUrl))

            // Set name
            holder.username.text = item.username

            // Set click event
            holder.lay.setOnClickListener {
                listener.mentionClicked(position)
            }
        }

        inner class Mention(view: View) : RecyclerView.ViewHolder(view) {
            val avatar = view.itemMentionAvatar!!
            val username = view.itemMentionName!!
            val lay = view.itemMentionLay!!
        }

    }

}