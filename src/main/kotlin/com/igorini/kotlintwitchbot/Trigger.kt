package com.igorini.kotlintwitchbot

import kotlin.annotation.Retention
import kotlin.annotation.Target
import kotlin.annotation.AnnotationRetention

/** Represents */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Trigger(val regex: String)