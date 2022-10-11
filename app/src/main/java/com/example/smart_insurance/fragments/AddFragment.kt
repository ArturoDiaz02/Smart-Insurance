package com.example.smart_insurance.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smart_insurance.CreateObject
import com.example.smart_insurance.ObjectDetails
import com.example.smart_insurance.adapter.CategoryAdapter
import com.example.smart_insurance.databinding.FragmentAddBinding

class AddFragment : Fragment(), CategoryAdapter.OnItemClickListener  {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val adapter = CategoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val recycler = binding.listViewCategories
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(activity, 3)
        recycler.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        @JvmStatic
        fun newInstance() = AddFragment()
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@AddFragment.requireContext(), CreateObject::class.java)
        startActivity(intent)
    }

}