package com.esigvart.parliament.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esigvart.parliament.databinding.FragmentDetailsBinding
import com.esigvart.parliament.repositories.Repository



//6.3.2023, Ella Sigvart, 2201316
//this code defines the details on member and displays the information about member object retrieved from repository
//on progress

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    private lateinit var vielModelFactory : DetailsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = DetailsFragmentArgs.fromBundle(requireArguments())
        vielModelFactory = DetailsViewModelFactory(args.hetekaId)
        Log.d("querty", args.toString())
        viewModel = ViewModelProvider(this, vielModelFactory)[DetailsViewModel::class.java]
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewModel.member.observe(viewLifecycleOwner) {
            binding.textName.text = it.toString()
        }
/*
        viewModel.member.observe(viewLifecycleOwner) {
                binding.textName.text = "${it.firstname} ${it.lastname}"
                binding.textParty.text = "${it.party}"
                binding.textSeatNumber.text = "${it.seatNumber}"
                binding.textMinister.text = ""
            }
*/
        return binding.root
    }

}
/*
class DetailsViewModel(private val hetekaId: Int) : ViewModel() {
    val member: LiveData<List<Member>> = Repository.getMemberDetails(hetekaId)
}*/

class DetailsViewModel(val hetekaId: Int?): ViewModel(){

    var member: LiveData<List<String>> = Transformations.map(Repository.getMemberDetails(hetekaId)) {
        it.map { "ID: ${it.hetekaId} \nName: ${it.firstname} ${it.lastname} \nSeat number: ${it.seatNumber} \nParty: ${it.party}"}
    }

}

class DetailsViewModelFactory(private val hetekaId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == DetailsViewModel::class.java) { "Unknown ViewModel class" }
        return DetailsViewModel(hetekaId) as T
    }
}
