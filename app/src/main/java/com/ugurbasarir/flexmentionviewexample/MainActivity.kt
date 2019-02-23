package com.ugurbasarir.flexmentionviewexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ugurbasarir.flexmentionsearchview.listener.MentionSearchViewListener
import com.ugurbasarir.flexmentionsearchview.model.MentionableUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MentionSearchViewListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<MentionableUser>()
        list.add(
            MentionableUser(
                1,
                "ugur",
                "https://picsum.photos/100/100/?random"
            )
        )
        list.add(
            MentionableUser(
                2,
                "basarir",
                "https://picsum.photos/100/100/?random"
            )
        )
        list.add(
            MentionableUser(
                3,
                "mentionable",
                "https://picsum.photos/100/100/?random"
            )
        )
        list.add(
            MentionableUser(
                4,
                "zirzop",
                "https://picsum.photos/100/100/?random"
            )
        )
        list.add(
            MentionableUser(
                5,
                "games",
                "https://picsum.photos/100/100/?random"
            )
        )

        setMentionAdapter(list)

    }

    private fun setMentionAdapter(list: ArrayList<MentionableUser>) {
        mentionSearchView.setListener(this@MainActivity)
        mentionSearchView.insertMentionList(list)
    }

    override fun sendButtonClicked() {
        print("asda")
    }

    override fun mentionClicked(mentionableUser: MentionableUser) {
        print("asda")
    }

}
