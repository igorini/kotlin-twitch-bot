package com.igorini.kotlintwitchbot

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/** Represents a Configuration */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Configuration(val debug: Boolean, val bot: Map<String, String>, val api: Map<String, String>, val credentials: Map<String, String>, val channels: List<String>)
