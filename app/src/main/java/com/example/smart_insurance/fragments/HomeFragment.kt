package com.example.smart_insurance.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_insurance.views.ObjectDetails
import com.example.smart_insurance.adapter.InsuranceAdapter
import com.example.smart_insurance.databinding.FragmentHomeBinding
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.Insurance
import com.example.smart_insurance.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment(private val user: User) : Fragment(), InsuranceAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: InsuranceAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.name.text = user.name + " " + user.lassName

        var insurance = ArrayList<Insurance>()
        var categories = ArrayList<Category>()

        val sp = requireActivity().getSharedPreferences(
            "smart_insurance",
            AppCompatActivity.MODE_PRIVATE
        )
        var json = sp.getString("insurances", "NO_INSURANCES")

        if (json != "NO_INSURANCES") {
            insurance = Gson().fromJson(json, object : TypeToken<ArrayList<Insurance>>() {}.type)
            json = sp.getString("categories", "NO_CATEGORIES")
            categories = Gson().fromJson(json, object : TypeToken<ArrayList<Category>>() {}.type)
        }

        adapter = InsuranceAdapter(this, insurance, categories)
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

    companion object {
        @JvmStatic
        fun newInstance(user: User) = HomeFragment(user)

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@HomeFragment.requireContext(), ObjectDetails::class.java)
        startActivity(intent)
    }

}