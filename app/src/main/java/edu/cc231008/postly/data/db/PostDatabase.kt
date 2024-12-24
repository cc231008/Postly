package edu.cc231008.postly.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1, exportSchema = true)

abstract class PostDatabase: RoomDatabase() {
    abstract fun postDao(): PostDAO

    companion object {
        @Volatile //@Volatile is a keyword that helps to access database very quickly while other threads are running concurrently.
        private var Instance: PostDatabase? = null

        fun getDatabase(context: Context): PostDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance to ensure thread safety.
            return Instance ?: synchronized(this) {
                //This creates a new database instance using the Room database builder.
                val instance = Room.databaseBuilder(context, PostDatabase::class.java, "contact_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     * "migration" - refers to the process of updating the database schema to a new version.
                     * That is why we defined version (version = 1)
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}