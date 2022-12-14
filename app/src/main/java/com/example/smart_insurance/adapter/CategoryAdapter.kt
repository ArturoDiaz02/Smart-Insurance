package com.example.smart_insurance.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.smart_insurance.R
import com.example.smart_insurance.model.Category

class CategoryAdapter(
    private val itemClickListener: OnItemClickListener,
    private var categories: ArrayList<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var amount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilter(newList: ArrayList<Category>) {
        this.categories = newList
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var image: ImageView = itemView.findViewById(R.id.image_category)
        private var layout: View = itemView.findViewById(R.id.relativeLayoutCategory)
        private var title: TextView = itemView.findViewById(R.id.text_category)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(category: Category) {

            Glide.with(image).load(category.image).listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    title.text = category.name
                    layout.setBackgroundColor(Color.parseColor(category.color))
                    amount += 1

                    if (amount == categories.size - 1) {
                        itemClickListener.recyclerVisibility()
                    }


                    return false
                }

            }).into(image)


        }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun recyclerVisibility()
    }

}