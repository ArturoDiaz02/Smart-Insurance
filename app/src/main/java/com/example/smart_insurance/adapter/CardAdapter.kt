package com.example.smart_insurance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_insurance.R

class CardAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CardAdapter.CardViewHolder>(){

    //private val cards = ArrayList<Card>()

    private val titles = arrayOf("Don Quijote edicion 1", "MacBook Air", "Iphone 14")
    private val dates = arrayOf("01/05/2022 - 01/10/2022", "01/08/2022 - 01/10/2022", "01/05/2022 - 01/10/2022")
    private val images = arrayOf(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_phone_iphone_24)
    private val colors = arrayOf("#446CFF", "#58FF27", "#D547FF")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.date.text = dates[position]
        holder.title.text = titles[position]
        holder.image.setImageResource(images[position])
        holder.layout.background.setTint(android.graphics.Color.parseColor(colors[position]))
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var image: ImageView = itemView.findViewById(R.id.image_card)
        var title: TextView = itemView.findViewById(R.id.title_card)
        var date: TextView = itemView.findViewById(R.id.date_card)
        var layout: View = itemView.findViewById(R.id.relativeLayoutImage)

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