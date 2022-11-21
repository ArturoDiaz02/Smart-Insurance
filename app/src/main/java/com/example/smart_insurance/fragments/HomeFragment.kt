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
import com.example.smart_insurance.adapter.InsuranceAdapter
import com.example.smart_insurance.databinding.FragmentHomeBinding
import com.example.smart_insurance.db.SqlOpenHelper
import com.example.smart_insurance.model.Insurance
import com.example.smart_insurance.model.User

class HomeFragment(private val user: User) : Fragment(), InsuranceAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: InsuranceAdapter
    private lateinit var sqlOpenHelper: SqlOpenHelper

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.name.text = user.name + " " + user.lassName

        sqlOpenHelper = SqlOpenHelper(requireContext())
        setAdapter(sqlOpenHelper.getAllInsurances())

        return binding.root
    }

    override fun onDestroyView() {
        sqlOpenHelper.close()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = HomeFragment(user)

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@HomeFragment.requireContext(), ObjectDetails::class.java)
        intent.putExtra("insurance", position)
        startActivity(intent)
    }

    private fun setAdapter(insurance: ArrayList<Insurance> ) {
        adapter = InsuranceAdapter(this, insurance)
        val recycler = binding.listView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter
    }

}