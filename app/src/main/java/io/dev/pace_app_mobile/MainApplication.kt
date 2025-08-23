package io.dev.pace_app_mobile

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(this)
    }
}