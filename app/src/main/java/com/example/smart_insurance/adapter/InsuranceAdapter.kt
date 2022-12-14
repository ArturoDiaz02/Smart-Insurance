package com.example.smart_insurance.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smart_insurance.R
import com.example.smart_insurance.model.Insurance

class InsuranceAdapter(
    private val itemClickListener: OnItemClickListener,
    private val insurances: ArrayList<Insurance>

    ) : RecyclerView.Adapter<InsuranceAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(insurances[position])
    }

    override fun getItemCount(): Int {
        return insurances.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var image: ImageView = itemView.findViewById(R.id.image_card)
        private var title: TextView = itemView.findViewById(R.id.title_card)
        private var date: TextView = itemView.findViewById(R.id.date_card)
        private var layout: View = itemView.findViewById(R.id.relativeLayoutImage)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(insurance: Insurance) {

            Glide.with(image).load(insurance.category_image).into(image)
            title.text = insurance.name
            date.text = insurance.initDate + " - " + insurance.endDate
            layout.background.setTint(Color.parseColor(insurance.category_color))

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
    }

}