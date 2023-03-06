package com.esigvart.parliament.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.esigvart.parliament.App

//6.3.2023, Ella Sigvart, 2201316

//this class is for room database for storing Member objects
@Database(entities = [Member::class], version = 1, exportSchema = false)
abstract class OpsDatabase : RoomDatabase()    {
    abstract val memberDao : MemberDao
    companion object { //provides getInstance
        @Volatile
        private var INSTANCE: OpsDatabase? = null
        fun getInstance(context: Context): OpsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(App.appContext, OpsDatabase::class.java, "database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}