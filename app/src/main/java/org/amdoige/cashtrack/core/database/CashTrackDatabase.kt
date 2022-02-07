package org.amdoige.cashtrack.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movement::class, Wallet::class], version = 8, exportSchema = false)
abstract class CashTrackDatabase : RoomDatabase() {
    abstract val dao: CashTrackDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: CashTrackDatabase? = null

        fun getInstance(context: Context): CashTrackDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CashTrackDatabase::class.java,
                        "movements_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
