package com.example.smart_insurance.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smart_insurance.views.CreateObject
import com.example.smart_insurance.adapter.CategoryAdapter
import com.example.smart_insurance.databinding.FragmentAddBinding
import com.example.smart_insurance.db.SqlOpenHelper
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.User
import com.example.smart_insurance.dialog.ProgressCycleBar
import kotlin.collections.ArrayList

class AddFragment(private val user: User) : Fragment(), CategoryAdapter.OnItemClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val progressBar = ProgressCycleBar()

    private lateinit var adapter: CategoryAdapter
    private lateinit var sqlOpenHelper: SqlOpenHelper

    companion object {
        @JvmStatic
        fun newInstance(user: User) = AddFragment(user)
        private const val SECONDS: Long = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        progressBar.show(requireActivity().supportFragmentManager, "progress")

        object: CountDownTimer(SECONDS, 500) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                recyclerVisibility()
            }
        }.start()

        sqlOpenHelper = SqlOpenHelper(requireContext())
        val categories = sqlOpenHelper.getAllCategories()

        setRecyclerView(categories)

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

    private fun setRecyclerView(categories: ArrayList<Category>) {
        adapter = CategoryAdapter(this, categories)
        val recycler = binding.listViewCategories
        recycler.visibility = View.INVISIBLE
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(activity, 3)
        recycler.adapter = adapter
    }

    override fun onDestroyView() {
        sqlOpenHelper.close()
        _binding = null
        super.onDestroyView()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(position: Int) {
        val connectivityManager = getSystemService(requireContext(), ConnectivityManager::class.java)
        val networkInfo = connectivityManager?.activeNetwork

        if (networkInfo != null) {
            val intent = Intent(this@AddFragment.requireContext(), CreateObject::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(activity, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
        }


    }

    fun recyclerVisibility() {
        binding.listViewCategories.visibility = View.VISIBLE
        progressBar.dismiss()

    }

}