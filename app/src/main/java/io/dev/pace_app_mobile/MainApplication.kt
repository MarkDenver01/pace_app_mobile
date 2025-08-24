package io.dev.pace_app_mobile

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.common.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(this)
    }
}