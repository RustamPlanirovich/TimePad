package com.nauka.timepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nauka.timepad.databinding.HomePadeBinding


class Home : Fragment(), TimeAdapter.ItemClickListener {

    // viewBinding
    private lateinit var binding: HomePadeBinding

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var timeAdapter: TimeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_pade,
            container,
            false
        )

        val firebaseUser = firebaseAuth.currentUser.uid

        val query: Query = db.collection(firebaseUser)
        val firestoreRecyclerOption: FirestoreRecyclerOptions<TimeResponse> = FirestoreRecyclerOptions.Builder<TimeResponse>()
            .setQuery(query, TimeResponse::class.java)
            .build()

        timeAdapter = TimeAdapter(firestoreRecyclerOption, this)
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.itemRecyclerView.adapter = timeAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                timeAdapter!!.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.itemRecyclerView)

        // Get context imageView5
        var fragmentManager = (binding.imageView5.context as FragmentActivity).supportFragmentManager

        // Open Add Time fragment
        binding.imageView5.setOnClickListener {
            AddTime().apply {
                show(fragmentManager, AddTime.TAG)
            }
        }

        // Open Profile fragment
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileFragment)
        }

        return binding.root
    }

    override fun onItemClick(position: Int, name: String, time: String, tag: String) {
        // timeAdapter?.deleteItem(position)
        Toast.makeText(context, "$name", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putString("name", "$name")
        bundle.putString("time", "$time")
        bundle.putString("tag", "$tag")
        Navigation.findNavController(binding.textView).navigate(
            R.id.action_global_detailTime,
            bundle
        )
    }

    override fun onStart() {
        super.onStart()
        timeAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeAdapter!!.stopListening()
    }
}
