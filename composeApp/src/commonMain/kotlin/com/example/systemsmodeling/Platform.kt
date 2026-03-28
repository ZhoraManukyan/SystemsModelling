package com.example.systemsmodeling

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform