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

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: MainActivity.MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.partiesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_partyFragment)

            lifecycleScope.launch {
                viewModel.insertMembersToDatabase(requireContext())
            }

        }

        viewModel = ViewModelProvider(this).get(MainActivity.MainActivityViewModel::class.java)


        return binding.root
    }
}
