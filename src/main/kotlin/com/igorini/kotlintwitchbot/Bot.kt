package com.igorini.kotlintwitchbot

import com.cavariux.twitchirc.Core.TwitchBot

/** Represents a Twitch Bot */
open class Bot : TwitchBot() {
    init {
        //javaClass.methods!!.filter { it.isAnnotationPresent() }
    }
}