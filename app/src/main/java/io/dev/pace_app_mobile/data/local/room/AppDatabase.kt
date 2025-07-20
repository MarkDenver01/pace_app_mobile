package io.dev.pace_app_mobile.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity

@Database(
    entities = [LoginEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
}