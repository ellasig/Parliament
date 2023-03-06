package com.esigvart.parliament.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esigvart.parliament.R
import com.esigvart.parliament.databinding.FragmentMemberBinding
import com.esigvart.parliament.repositories.Repository
import java.util.Locale.filter

//6.3.2023, Ella Sigvart, 2201316


class MemberFragment : Fragment() {

    private lateinit var viewModel: MemberViewModel
    private lateinit var adapter: MemberAdapter
    private lateinit var binding: FragmentMemberBinding

    private lateinit var viewModelFactory: MemberViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = MemberFragmentArgs.fromBundle(requireArguments())


        viewModelFactory = MemberViewModelFactory(args.party)
        //viewmodel initialized
        viewModel = ViewModelProvider(this, viewModelFactory)[MemberViewModel::class.java]

        adapter = MemberAdapter(emptyList())

        binding = FragmentMemberBinding.inflate(layoutInflater)

        binding.recyclerViewMember.layoutManager = LinearLayoutManager(context)


        viewModel.members.observe(viewLifecycleOwner) {
            binding.recyclerViewMember.adapter = MemberAdapter(it)

        }

        return binding.root
    }


    class MemberAdapter(var members: List<String>) :
        RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.memberitemlist, parent, false)
            return MemberViewHolder(view)
        }

        override fun getItemCount(): Int {
            return members.size
        }

        override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.textViewMember).apply {
                text = members[position]
                val hetekaId = text[position]
                setOnClickListener {
                    val action =
                        MemberFragmentDirections.actionMemberFragmentToDetailsFragment(hetekaId.toInt())
                   it.findNavController().navigate(action)
                }
            }
        }


        class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view)

    }

    class MemberViewModel(private val party: String) : ViewModel() {
        val members: LiveData<List<String>> =
            Transformations.map(Repository.getMembersFromParty(party)) { members ->
                members.filter { it.party == party }
                    .map { "${it.firstname} ${it.lastname}" }
                    .sorted()
            }
    }

/* TÄMÄ TOIMII
class MemberViewModel(savedStateHandle: SavedStateHandle): ViewModel(){
    val party: String? = savedStateHandle["party"]
    val members: LiveData<List<String>> =
        Transformations.map(Repository.getMembersFromParty(party.toString())) {
                it.map { "${it.firstname} ${it.lastname}" }
                .toSortedSet().toList()

        }
}
*/

    class MemberViewModelFactory(private val party: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
                return MemberViewModel(party) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}