package com.esigvart.parliament

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.esigvart.parliament.api.MemberApi
import com.esigvart.parliament.database.Member
import com.esigvart.parliament.databinding.ActivityMainBinding
import com.esigvart.parliament.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//6.3.2023, Ella Sigvart, 2201316
// This class represent the main activity.
// Class is responsible for fetching data from the API and inserting it into the local database.
// It also exposes the members liveData object from the Repository class.

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewmodel initialized
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //inflates the layout using data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navigation control
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }


    class MainActivityViewModel : ViewModel() {

        // This provides access to data layer
        private val repository = Repository

        // This holds the list of member objects retrieved from database through repository by using "logdata"
        val members: LiveData<List<Member>> = repository.logData

        // This function fetches the data from API through memberapi object.
        // After that it will insert it to database.
        suspend fun insertMembersToDatabase(context: Context) {
            withContext(Dispatchers.IO) {
                try {
                    val membersList = MemberApi.retrofitService.getMemberList()
                    repository.insertMembers(membersList)
                    Log.d("QWERTY", "SUCCESS")
                    Log.d("QWERTY", "${repository.logData.value?.size}")
                } catch (e: Exception) {
                    Log.d("QWERTY", "ERROR $e")
                }
            }
        }
    }
}
