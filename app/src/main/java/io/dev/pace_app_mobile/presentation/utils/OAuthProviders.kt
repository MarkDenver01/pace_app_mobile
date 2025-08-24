package io.dev.pace_app_mobile.presentation.utils

object OAuthProviders {
    // ---- Twitter (X) OAuth 2.0 with PKCE ----
    const val TW_CLIENT_ID =
        "RUNwWkZ6SFFJVl9ua0xNa1pBMVY6MTpjaQ" // from Twitter developer portal (User authentication settings)
    const val TW_REDIRECT_URI =
        "io.dev.pace://oauth2redirect/twitter" // register in manifest + Twitter portal
    const val TW_AUTH_URI = "https://twitter.com/i/oauth2/authorize"
    const val TW_TOKEN_URI = "https://api.twitter.com/2/oauth2/token"
    val TW_SCOPES =
        listOf("tweet.read", "users.read", "offline.access") // email not available in v2

    // ---- Instagram (Basic Display) ----
    // Instagram Basic Display does NOT support PKCE; token exchange requires CLIENT SECRET.
    // Best practice: get AUTHORIZATION CODE in app, send it to backend, backend exchanges for access_token.
    const val IG_CLIENT_ID = "<INSTAGRAM_APP_ID>"
    const val IG_REDIRECT_URI =
        "io.dev.pace://oauth2redirect/instagram" // add to IG app settings + manifest
    const val IG_AUTH_URI = "https://api.instagram.com/oauth/authorize"

    // ---- Facebook SDK ----
    const val FACEBOOK_APP_ID = "1193623572571079"
    const val FB_LOGIN_PROTOCOL_SCHEME = "fb1193623572571079"
    const val FACEBOOK_CLIENT_TOKEN = "49bba05b2b94adfc7c2a29c4fad13888"

}