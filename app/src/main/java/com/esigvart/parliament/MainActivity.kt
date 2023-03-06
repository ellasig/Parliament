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


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        /*
        viewModel.members.observe(this, Observer { members ->
            Log.d("Qwerty", "Members Updated: ${members.size}")
        })
    }*/
    }


    class MainActivityViewModel : ViewModel() {

        private val repository = Repository

        val members: LiveData<List<Member>> = repository.logData

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
