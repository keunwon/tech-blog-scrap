package com.github.keunwon.techblogscrap.support

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

internal val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory())
    .registerKotlinModule()
    .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
