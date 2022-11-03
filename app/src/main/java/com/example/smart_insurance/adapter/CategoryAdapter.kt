package com.example.smart_insurance.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.smart_insurance.R
import com.example.smart_insurance.model.Category
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CategoryAdapter(
    private val itemClickListener: OnItemClickListener,
    private val categories: ArrayList<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])

        if (position == categories.size - 1) {
            itemClickListener.recyclerVisibility()
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var image: ImageView = itemView.findViewById(R.id.image_category)
        private var layout: View = itemView.findViewById(R.id.relativeLayoutCategory)
        private var title: TextView = itemView.findViewById(R.id.text_category)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(category: Category){
            title.text = category.name

            Firebase.storage.reference.child("categoriesImages").child(category.image).downloadUrl.addOnSuccessListener {
                Glide.with(image).load(it).into(image)
            }

            layout.setBackgroundColor(android.graphics.Color.parseColor(category.color))

        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun recyclerVisibility()
    }

}