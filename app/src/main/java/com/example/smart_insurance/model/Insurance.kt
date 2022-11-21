package com.example.smart_insurance.model

import java.io.Serializable

data class Insurance(
    val id: Int,
    val name: String,
    val initDate: String,
    val endDate: String,
    val price: String,
    val description: String,
    val category: Int,
    val category_image: String,
    val category_color: String,
    val images: String,
    val state: String

) : Serializable {
    override fun toString(): String {
        return super.toString()
    }

    constructor() : this(0, "", "", "", "", "", -1, "", "", "", "")
}
