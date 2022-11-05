package com.example.smart_insurance.model

import java.io.Serializable

data class Insurance(
    val id: String,
    val name: String,
    val initDate: String,
    val endDate: String,
    val price: String,
    val description: String,
    val category: String,
    val state: String

) : Serializable {
    override fun toString(): String {
        return super.toString()
    }

    constructor() : this("", "", "", "", "", "", "", "")
}
