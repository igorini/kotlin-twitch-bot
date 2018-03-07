package com.igorini.kotlintwitchbot

import com.cavariux.twitchirc.Chat.Channel
import com.cavariux.twitchirc.Chat.User
import com.cavariux.twitchirc.Core.TwitchBot
import mu.KotlinLogging
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

/** Represents a Twitch Bot */
open class Bot : TwitchBot() {
    open val logger = KotlinLogging.logger {}

    val regexToMethod = this::class.memberFunctions
            .filter { it.annotations.any { it is Trigger } }
            .associate { it.findAnnotation<Trigger>()!!.regex.toRegex() to it }

    override fun onMessage(user: User?, channel: Channel?, message: String?) {
        regexToMethod.filterKeys { it.matches(message!!) }.values.forEach { it.call(user!!, channel!!, message!!) }
    }
}