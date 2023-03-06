package com.esigvart.parliament.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esigvart.parliament.MainActivity
import com.esigvart.parliament.R
import com.esigvart.parliament.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

//6.3.2023, Ella Sigvart, 2201316
// this code defines homefragment that displays the header for the app and button.

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: MainActivity.MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflates the layout using data binding
        val binding = FragmentHomeBinding.inflate(layoutInflater)

        //when the button is clicked, fragment uses navigation to navigate next fragment.
        binding.partiesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_partyFragment)

            //button also will use mainactivityviewmodel to insert all members to database using cororutines.
            lifecycleScope.launch {
                viewModel.insertMembersToDatabase(requireContext())
            }

        }
        // this code creates viewmodelprovider for this fragment uses it to get instance oh the mainactivityviewmodel
        viewModel = ViewModelProvider(this).get(MainActivity.MainActivityViewModel::class.java)

        //returns root view(contains all child views what are defined in the layout
        return binding.root
    }
}
