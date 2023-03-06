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


class PartyFragment : Fragment() {

    private lateinit var viewModel: PartyViewModel
    private lateinit var adapter: PartyAdapter
    private lateinit var binding: FragmentPartyBinding

    override fun onCreateView
                (inflater: LayoutInflater, container: ViewGroup?,
                 savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(PartyViewModel::class.java)

        adapter = PartyAdapter(emptyList())

        binding = FragmentPartyBinding.inflate(inflater, container, false)
        binding.recyclerViewParty.adapter = adapter
        binding.recyclerViewParty.layoutManager = LinearLayoutManager(context)


        viewModel.parties.observe(viewLifecycleOwner) { parties ->
            adapter.parties = parties
            adapter.notifyDataSetChanged()
        }
        return binding.root

    }

}


//tästä alaspäin
class PartyAdapter(var parties: List<String>) : RecyclerView.Adapter<PartyAdapter.PartyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partyitemlist, parent, false)
        return PartyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return parties.size
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textViewParty).apply {
            text = parties[position]
            val party = text.toString()
            setOnClickListener {
                val action = PartyFragmentDirections.actionPartyFragmentToMemberFragment(party)
                it.findNavController().navigate(action)
            }
        }
    }


    class PartyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

class PartyViewModel: ViewModel() {
    var parties = Transformations.map(Repository.logData) {
        it.map { it.party }.toSortedSet().toList()
    }

}
