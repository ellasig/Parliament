package com.esigvart.parliament.fragments

import android.os.Bundle
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


//6.3.2023, Ella Sigvart, 2201316
//this class shows a list of members based on political party selected in the partyfragment.
//MemberAdapter class is used to display each member's name and navigate to detailsfragment when clicked.
//memberviewmodel class gets data from repository and filters members based on selected party


class MemberFragment : Fragment() {

    private lateinit var viewModel: MemberViewModel
    private lateinit var adapter: MemberAdapter
    private lateinit var binding: FragmentMemberBinding
    private lateinit var viewModelFactory: MemberViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // This line retrieves arguments passed to the fragment when it is created using frombundle method
        //The arguments are declared in navigation graph
        val args = MemberFragmentArgs.fromBundle(requireArguments())


        viewModelFactory = MemberViewModelFactory(args.party)
        //viewmodel initialized
        viewModel = ViewModelProvider(this, viewModelFactory)[MemberViewModel::class.java]

        adapter = MemberAdapter(emptyList())

        //inflates the layout using data binding
        binding = FragmentMemberBinding.inflate(layoutInflater)

        //sets the layout manager for the recyclerview
        binding.recyclerViewMember.layoutManager = LinearLayoutManager(context)

        //sets the observer to members livedata object in the viewmodel. whenever the value changes, the observer is noticed and a new instance of memberadapter is created
        viewModel.members.observe(viewLifecycleOwner) {
            binding.recyclerViewMember.adapter = MemberAdapter(it)

        }

        return binding.root
    }


    class MemberAdapter(var members: List<String>) :
        RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

        // This function inflates the layout for single item in recyclerview and return viewholder object
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.memberitemlist, parent, false)
            return MemberViewHolder(view)
        }

        // This function returns the number of items from the members list.
        override fun getItemCount(): Int {
            return members.size
        }

        // This method is responsible for binding the data from list to the views in recyclerview.
        override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.textViewMember).apply {
                text = members[position]
                val hetekaId = text.get(position)
                //  when the textview is clicked, it navigates to next fragment and passing in the hetekaid of the member as an argument using safeargs
                setOnClickListener {
                    val action =
                        MemberFragmentDirections.actionMemberFragmentToDetailsFragment(hetekaId.toInt())
                   it.findNavController().navigate(action)
                }
            }
        }

        // this class extends recyclerview.viewholder and takes a view as a parameter
        class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view)

    }

    // Contains the livedata object "members". The list is obtained  from repository and gets called by getmembersfromparty method.
    // then filtered to only include members belonging to the specific party using the "filter" method.
    // The names are then mapped and concatenated to form a single string using the "map" method and the string is sorted using the "sorted" method.
    // The resulting list is then set as the value of the LiveData object "members".
    class MemberViewModel(private val party: String) : ViewModel() {
        val members: LiveData<List<String>> =
            Transformations.map(Repository.getMembersFromParty(party)) { members ->
                members.filter { it.party == party }
                    .map { "${it.firstname} ${it.lastname}" }
                    .sorted()
            }
    }

    //MemberViewModelFactory is used to create an instance of the MemberViewModel class.
    class MemberViewModelFactory(private val party: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
                return MemberViewModel(party) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}