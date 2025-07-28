package com.github.keunwon.techblogscrap

import io.kotest.core.config.AbstractProjectConfig

internal object KotestProjectConfig : AbstractProjectConfig() {
    override val parallelism = 8
}
