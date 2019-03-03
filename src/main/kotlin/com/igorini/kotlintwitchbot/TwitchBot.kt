package com.igorini.kotlintwitchbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KotlinLogging
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import me.philippheuer.twitch4j.TwitchClientBuilder
import me.philippheuer.twitch4j.message.commands.Command
import java.io.File
import java.util.concurrent.TimeUnit

/** Represents a Twitch Bot */
open class TwitchBot {

    companion object {
        val logger = KotlinLogging.logger {}

        val configuration: Configuration = ObjectMapper(YAMLFactory()).registerKotlinModule().readValue(File(ClassLoader.getSystemResource("config.yaml").file))

        val twitchClient = TwitchClientBuilder.init()
                .withClientId(configuration.api["twitch_client_id"])
                .withClientSecret(configuration.api["twitch_client_secret"])
                .withCredential(configuration.credentials["irc"])
                .withAutoSaveConfiguration(true)
                .withConfigurationDirectory(File("config"))
                .connect()

        val viewersCache: LoadingCache<String, List<String>> = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build { viewers(it) }

        private fun viewers(channel: String): List<String> {
            val chatter = twitchClient.tmiEndpoint.getChatters(channel)
            val viewers = chatter.viewers + chatter.admins + chatter.globalMods + chatter.moderators + chatter.staff
            logger.info { "All viewers: $viewers" }
            return viewers
        }
    }

    open val logger = KotlinLogging.logger {}

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