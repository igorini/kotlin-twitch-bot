package com.igorini.kotlintwitchbot.ext

import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent
import me.philippheuer.twitch4j.message.commands.Command

/* Contains extension functions and properties for [Command] */

/** @return Viewers that are online in this channel. */
fun Command.viewers(messageEvent: ChannelMessageEvent) = twitchClient.tmiEndpoint.getChatters(messageEvent.channel.name).viewers

/** @return A random viewer that is online in this channel, except provided users. */
fun Command.randomViewerExcept(messageEvent: ChannelMessageEvent, users: List<String>) = viewers(messageEvent).map { it.toLowerCase() }.minus(users).shuffled().firstOrNull()

/** Checks if provided viewer is online, except provided users. */
fun Command.viewerOnlineExcept(messageEvent: ChannelMessageEvent, viewer: String, users: List<String>) = viewers(messageEvent).map { it.toLowerCase() }.minus(users).contains(viewer)