package com.ugurbasarir.flexmentionsearchview.listener

import com.ugurbasarir.flexmentionsearchview.model.MentionableUser


/**
 * @author ugur
 * @Date 21.06.2018
 */

interface MentionSearchViewListener {

    fun sendButtonClicked()

    fun mentionClicked(mentionableUser: MentionableUser)

}