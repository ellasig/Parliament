package com.esigvart.parliament

import android.app.Application
import android.content.Context
import com.esigvart.parliament.database.OpsDatabase

//6.3.2023, Ella Sigvart, 2201316
// This class is a application class that initializes app context and database object for entire application

class App : Application() {

    companion object{
        lateinit var appContext: Context
            private set
        lateinit var database: OpsDatabase
            private set
    }

    // This method is called when the app is launched
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        database = OpsDatabase.getInstance(appContext)
    }
}