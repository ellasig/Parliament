package com.esigvart.parliament.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.esigvart.parliament.App.Companion.appContext
import com.esigvart.parliament.database.Member
import com.esigvart.parliament.database.MemberDao
import com.esigvart.parliament.database.OpsDatabase

//6.3.2023, Ella Sigvart, 2201316

object Repository {

    private val dao = OpsDatabase.getInstance(appContext).memberDao
    val logData: LiveData<List<Member>> = dao.getAll()



    fun insertMembers(members: List<Member>) {
        dao.insert(members)
        Log.d("Repository", "Inserted ${members.size} members")
    }


    fun getMembersFromParty(party: String): LiveData<List<Member>> {
        return dao.getMembersFromParty(party)

    }

    fun getMemberDetails(hetekaId: Int?): LiveData<List<Member>> {
        return dao.getMemberDetails(hetekaId)
    }
}