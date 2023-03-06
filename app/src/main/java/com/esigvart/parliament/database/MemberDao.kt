package com.esigvart.parliament.database

import androidx.lifecycle.LiveData
import androidx.room.*

//6.3.2023, Ella Sigvart, 2201316

//this code defines member data class and interfaces memberdao what is used to perform database tasks on member objects.
@Entity
data class Member(
    @PrimaryKey(autoGenerate = true)
    val hetekaId: Int,
    val seatNumber: Int,
    val lastname: String,
    val firstname: String,
    val party: String,
    val minister: Boolean = false,
    val pictureUrl: String = "",
)
//sql queries will communicate with the database
@Dao
interface MemberDao {
    //insert data to room database
    @Insert(onConflict = OnConflictStrategy.REPLACE) //avoid duplication
    fun insert(members: List<Member>)

    //get all members from the database
    @Query("select * from Member")
    fun getAll(): LiveData<List<Member>>

    //gets all members from specific party
    @Query("select * from Member where party = :party")
    fun getMembersFromParty(party: String): LiveData<List<Member>>

    //get member info via id
    @Query("SELECT * FROM member WHERE hetekaId = :hetekaId")
    fun getMemberDetails(hetekaId: Int?): LiveData<List<Member>>
    }
