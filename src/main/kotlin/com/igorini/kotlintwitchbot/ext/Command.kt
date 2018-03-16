package com.igorini.kotlintwitchbot.ext

import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent
import me.philippheuer.twitch4j.message.commands.Command
import mu.KLogger
import mu.KotlinLogging

/* Contains extension functions and properties for [Command] */

val Command.logger: KLogger
    get() = KotlinLogging.logger {}

/** @return Viewers that are online in this channel. */
fun Command.viewers(messageEvent: ChannelMessageEvent): List<String> {
    val chatter = twitchClient.tmiEndpoint.getChatters(messageEvent.channel.name)
    val viewers = chatter.viewers + chatter.admins + chatter.globalMods + chatter.moderators + chatter.staff
    logger.info { "All viewers: $viewers" }
    return viewers
}

/** @return A random viewer that is online in this channel, except provided users. */
fun Command.randomViewerExcept(messageEvent: ChannelMessageEvent, users: List<String>) = viewers(messageEvent).map { it.toLowerCase() }.minus(users).shuffled().firstOrNull()

/** Checks if provided viewer is online, except provided users. */
fun Command.viewerOnlineExcept(messageEvent: ChannelMessageEvent, viewer: String, users: List<String>) = viewers(messageEvent).map { it.toLowerCase() }.minus(users).contains(viewer)