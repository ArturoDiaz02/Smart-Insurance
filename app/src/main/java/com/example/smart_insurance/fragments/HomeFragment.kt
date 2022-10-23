package com.example.smart_insurance.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_insurance.views.ObjectDetails
import com.example.smart_insurance.adapter.CardAdapter
import com.example.smart_insurance.databinding.FragmentHomeBinding
import com.example.smart_insurance.model.User

class HomeFragment(private val user: User) : Fragment(), CardAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter = CardAdapter(this)

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.name.text = user.name + " " + user.lassName

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
       fun newInstance(user: User) = HomeFragment(user)

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@HomeFragment.requireContext(), ObjectDetails::class.java)
        startActivity(intent)
    }

}