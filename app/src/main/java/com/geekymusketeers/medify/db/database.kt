package com.geekymusketeers.medify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geekymusketeers.medify.dao.PillDao
import com.geekymusketeers.medify.dao.ReminderDao
import com.geekymusketeers.medify.model.History
import com.geekymusketeers.medify.model.PillEntity
import com.geekymusketeers.medify.model.Reminder

@Database(
    entities = [PillEntity::class, Reminder::class, History::class],
    version = 6
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun getPillDao(): PillDao
    abstract fun getReminderDao(): ReminderDao
}