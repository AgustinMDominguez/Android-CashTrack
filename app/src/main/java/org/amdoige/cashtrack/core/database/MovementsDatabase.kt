package org.amdoige.cashtrack.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movement::class], version = 1, exportSchema = false)
abstract class MovementsDatabase : RoomDatabase() {
    abstract val dao: MovementsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: MovementsDatabase? = null

        fun getInstance(context: Context): MovementsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovementsDatabase::class.java,
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
