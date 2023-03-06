package com.esigvart.parliament.database

import androidx.lifecycle.LiveData
import androidx.room.*

//6.3.2023, Ella Sigvart, 2201316


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

@Dao
interface MemberDao {
    //insert data to room database
    @Insert(onConflict = OnConflictStrategy.REPLACE) //korvaa olevan tiedon uuteen, vältetään duplicatio
    fun insert(members: List<Member>)

    @Query("select * from Member") // tuo kaikki rivit takasin livedatan sisällä
    fun getAll(): LiveData<List<Member>>// ei tarvitse olla suspend jos on livedata

    @Query("select * from Member where party = :party")
    fun getMembersFromParty(party: String): LiveData<List<Member>>


    @Query("SELECT * FROM member WHERE hetekaId = :hetekaId")
    fun getMemberDetails(hetekaId: Int?): LiveData<List<Member>>
    }

/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(membersList: List<Member>)

    @Update
    suspend fun update(member: Member)
}

   /* @Query("SELECT * FROM members_list")
    fun getMemberInfo(): MutableList<List<Member>>*/

/*


/*
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upDateMemberInfo(member: Member)
*/
}

 */