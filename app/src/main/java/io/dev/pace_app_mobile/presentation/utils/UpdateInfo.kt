package io.dev.pace_app_mobile.presentation.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class UpdateInfo(
    val latestVersionName: String,
    val latestVersionCode: Int,
    val apkUrl: String,
    val releaseNotes: String
)

@RequiresApi(Build.VERSION_CODES.P)
suspend fun checkForUpdate(context: Context): UpdateInfo? = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://pace-app-frontend.onrender.com/apk-uploads/version.json")
        val json = url.readText()
        val obj = JSONObject(json)

        val remoteVersionCode = obj.getInt("latestVersionCode")
        val localVersionCode = context.packageManager
            .getPackageInfo(context.packageName, 0).longVersionCode.toInt()

        return@withContext if (remoteVersionCode > localVersionCode) {
            UpdateInfo(
                obj.getString("latestVersionName"),
                remoteVersionCode,
                obj.getString("apkUrl"),
                obj.getString("releaseNotes")
            )
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}