package com.esigvart.parliament.api

import com.esigvart.parliament.database.Member
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//6.3.2023, Ella Sigvart, 2201316
//This Kotlin data class represents a member object with properties.
//It is designed to hold and manipulate data associated with a member

//base url
private const val BASE_URL =
    "https://users.metropolia.fi/~peterh/"

//this line creates moshi object, what will be used to parse the json data and the data will be returned via API
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//this creates retrofit object and can be used to make API requests to get a list of members from a remote server
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//This retrieves the list of members of parliament
interface MemberApiService {
    @GET("seating.json")
    suspend fun getMemberList(): List<Member>

}
//This object will create singleton instance using retrofit
object MemberApi {
    val retrofitService : MemberApiService by lazy {
        retrofit.create(MemberApiService::class.java)
    }
}