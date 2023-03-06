package com.esigvart.parliament.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esigvart.parliament.R
import com.esigvart.parliament.databinding.FragmentPartyBinding
import com.esigvart.parliament.repositories.Repository

//6.3.2023, Ella Sigvart, 2201316
// This code dispalys a list on parties and allows the user to navigate to list of members for each party.
//

class PartyFragment : Fragment() {

    private lateinit var viewModel: PartyViewModel
    private lateinit var adapter: PartyAdapter
    private lateinit var binding: FragmentPartyBinding

    override fun onCreateView
                (inflater: LayoutInflater, container: ViewGroup?,
                 savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(PartyViewModel::class.java)

        adapter = PartyAdapter(emptyList())

        //inflates the layout using data binding
        binding = FragmentPartyBinding.inflate(inflater)

        binding.recyclerViewParty.adapter = adapter

        //sets the layout manager for the recyclerview
        binding.recyclerViewParty.layoutManager = LinearLayoutManager(context)

        //listens for changes in list of parties and updates the recyclerview adapter with new data
        viewModel.parties.observe(viewLifecycleOwner) { parties ->
            adapter.parties = parties
            adapter.notifyDataSetChanged()
        }
        return binding.root

    }

}


class PartyAdapter(var parties: List<String>) : RecyclerView.Adapter<PartyAdapter.PartyViewHolder>() {

    // This function inflates the layout for single item in recyclerview and return viewholder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partyitemlist, parent, false)
        return PartyViewHolder(view)
    }
    // This function returns the number of items from the parties list.
    override fun getItemCount(): Int {
        return parties.size
    }

    // This method is responsible for binding the data from list to the views in recyclerview.
    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textViewParty).apply {
            text = parties[position]
            val party = text.toString()
            //  when the textview is clicked, it navigates to next fragment and passing in the party of the member as an argument
            setOnClickListener {
                val action = PartyFragmentDirections.actionPartyFragmentToMemberFragment(party)
                it.findNavController().navigate(action)
            }
        }
    }

    // this class extends recyclerview.viewholder and takes a view as a parameter
    class PartyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
//The data is retrieved from a repository using LiveData and transformations.
class PartyViewModel: ViewModel() {
    var parties = Transformations.map(Repository.logData) {
        it.map { it.party }.toSortedSet().toList()
    }

}
