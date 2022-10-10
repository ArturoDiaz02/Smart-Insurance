package com.example.smart_insurance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_insurance.R

class CategoryAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    //private val categories = ArrayList<Category>()

    private val id = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11)
    private val images = arrayOf(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_phone_iphone_24, R.drawable.ic_baseline_pets_24, R.drawable.ic_baseline_photo_camera_24,
        R.drawable.ic_baseline_print_24, R.drawable.ic_baseline_radio_24, R.drawable.ic_baseline_tv_24, R.drawable.ic_baseline_videogame_asset_24, R.drawable.ic_baseline_watch_24, R.drawable.ic_baseline_mouse_24, R.drawable.ic_baseline_sports_basketball_24)
    private val colors = arrayOf("#58FF27", "#58FF27","#58FF27", "#446CFF", "#446CFF", "#446CFF",  "#F80000", "#F80000", "#F80000", "#F6D200", "#F6D200", "#F6D200")
    private val descriptor = arrayOf("Libros", "Computadoras", "Celulares", "Mascotas", "Cámaras", "Impresoras", "Radios", "Televisores", "Videojuegos", "Relojes", "Mouse", "Balones")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.image.setImageResource(images[position])
        holder.layout.setBackgroundColor(android.graphics.Color.parseColor(colors[position]))
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var image: ImageView = itemView.findViewById(R.id.image_category)
        var layout: View = itemView.findViewById(R.id.relativeLayoutCategory)

        init {
            itemView.setOnClickListener(this)
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
    }

}