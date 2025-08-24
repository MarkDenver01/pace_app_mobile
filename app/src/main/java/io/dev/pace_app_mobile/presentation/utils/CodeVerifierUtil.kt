package io.dev.pace_app_mobile.presentation.utils

object CodeVerifierUtil {
    fun generateRandomCodeVerifier(): String {
        val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-._~"
        return (1..128)
            .map { charset.random() }
            .joinToString("")
    }
}