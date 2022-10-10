package com.example.smart_insurance.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_insurance.adapter.CardAdapter
import com.example.smart_insurance.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), CardAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter = CardAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recycler = binding.listView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

}