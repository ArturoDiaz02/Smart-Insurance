package com.example.smart_insurance.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smart_insurance.views.CreateObject
import com.example.smart_insurance.adapter.CategoryAdapter
import com.example.smart_insurance.databinding.FragmentAddBinding
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.User
import com.example.smart_insurance.views.ProgressCicleBar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFragment(private val user: User) : Fragment(), CategoryAdapter.OnItemClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val progressBar = ProgressCicleBar()

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        progressBar.show(requireActivity().supportFragmentManager, "progress")

        Firebase.firestore.collection("categories").get().addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val categories = ArrayList<Category>()


                for (document in task.result!!) {
                    val category = document.toObject(Category::class.java)
                    categories.add(category)
                }

                categories.sortBy { it.id }

                adapter = CategoryAdapter(this, categories)
                val recycler = binding.listViewCategories
                recycler.visibility = View.INVISIBLE
                recycler.setHasFixedSize(true)
                recycler.layoutManager = GridLayoutManager(activity, 3)
                recycler.adapter = adapter
            }

        }.addOnFailureListener { exception ->
            progressBar.dismiss()
            println("Error getting documents: $exception")

        }

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