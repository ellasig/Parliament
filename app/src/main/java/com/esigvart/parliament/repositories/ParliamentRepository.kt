package com.esigvart.parliament.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.esigvart.parliament.App.Companion.appContext
import com.esigvart.parliament.database.Member
import com.esigvart.parliament.database.OpsDatabase

//6.3.2023, Ella Sigvart, 2201316
// This repository object provides interface between the data source and SQLite database and with the rest of the application.
// It contains methods for inserting members into the database, retrieving members by party or id and obtaining livedata object containing all members in the database


object Repository {

    // This code initializes an instance of the memberdao from database singleton object
    // It creates livedata object "logdata" which holds a list of all members in the database. It can be used by calling the dao's getall method.
    private val dao = OpsDatabase.getInstance(appContext).memberDao
    val logData: LiveData<List<Member>> = dao.getAll()

    // This function inserts list of member objects into the database using memberdao instance.
    fun insertMembers(members: List<Member>) {
        dao.insert(members)
        Log.d("Repository", "Inserted ${members.size} members")
    }

    // This function returns livedata object that holds a list of members objects that are filtered by their party.
    fun getMembersFromParty(party: String): LiveData<List<Member>> {
        return dao.getMembersFromParty(party)
    }

    // This function retrieves the details of a single member by using hetekaid
    fun getMemberDetails(hetekaId: Int?): LiveData<List<Member>> {
        return dao.getMemberDetails(hetekaId)
    }
}