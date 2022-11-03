package com.example.smart_insurance.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smart_insurance.views.CreateObject
import com.example.smart_insurance.adapter.CategoryAdapter
import com.example.smart_insurance.databinding.FragmentAddBinding
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.User
import com.example.smart_insurance.views.ProgressCicleBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

class AddFragment(private val user: User) : Fragment(), CategoryAdapter.OnItemClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val progressBar = ProgressCicleBar()

    private lateinit var adapter: CategoryAdapter

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        progressBar.show(requireActivity().supportFragmentManager, "progress")

        val sp = requireActivity().getSharedPreferences("smart_insurance", AppCompatActivity.MODE_PRIVATE)
        val json = sp.getString("categories", "NO_CATEGORIES")
        var categories = ArrayList<Category>()
        categories = Gson().fromJson(json, object : TypeToken<ArrayList<Category>>() {}.type)

        adapter = CategoryAdapter(this, categories)
        val recycler = binding.listViewCategories
        recycler.visibility = View.INVISIBLE
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(activity, 3)
        recycler.adapter = adapter



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = ArrayList<Category>()
                for (item in categories) {
                    if (item.name.lowercase().contains(newText.toString().lowercase())) {
                        filteredList.add(item)
                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(activity, "No se encontraron resultados", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    adapter.setFilter(filteredList)
                }

                if (newText == "") {
                    adapter.setFilter(categories)
                }
                return true
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = AddFragment(user)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@AddFragment.requireContext(), CreateObject::class.java)
        startActivity(intent)
    }

    override fun recyclerVisibility() {
        binding.listViewCategories.visibility = View.VISIBLE
        progressBar.dismiss()

    }

}