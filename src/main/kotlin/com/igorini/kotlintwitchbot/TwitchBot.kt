package com.igorini.kotlintwitchbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import mu.KotlinLogging
import com.fasterxml.jackson.module.kotlin.*
import me.philippheuer.twitch4j.TwitchClientBuilder
import me.philippheuer.twitch4j.message.commands.Command
import java.io.File

/** Represents a Twitch Bot */
open class TwitchBot {

    open val logger = KotlinLogging.logger {}

    val configuration: Configuration = ObjectMapper(YAMLFactory()).registerKotlinModule().readValue(File(ClassLoader.getSystemResource("config.yaml").file))

    val twitchClient = TwitchClientBuilder.init()
            .withClientId(configuration.api["twitch_client_id"])
            .withClientSecret(configuration.api["twitch_client_secret"])
            .withCredential(configuration.credentials["irc"])
            .withAutoSaveConfiguration(true)
            .withConfigurationDirectory(File("config"))
            .connect()

    init {
        // Register class to receive events with @EventSubscriber
        registerListener(this)
    }

    fun start() {
        configuration.channels.forEach {
            val channelId = twitchClient.userEndpoint.getUserIdByUserName(it).get()
            val channelEndpoint = twitchClient.getChannelEndpoint(channelId)
            channelEndpoint.registerEventListener()
        }
    }

    fun registerCommand(command: Command) = twitchClient.commandHandler.registerCommand(command)

    fun registerListener(any: Any) = twitchClient.dispatcher.registerListener(any)
}