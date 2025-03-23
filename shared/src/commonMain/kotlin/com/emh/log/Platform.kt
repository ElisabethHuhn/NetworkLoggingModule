package com.emh.log

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform