package com.example.smart_insurance.model

import java.io.Serializable

data class User(
    val id: String,
    val name: String,
    val lassName: String,
    val dateOfBirth: String,
    val cc: String,
    val email: String,
    var profileImage: String? = null,
    val totalInsurance: Int = 0

) : Serializable {
    override fun toString(): String {
        return super.toString()
    }

    constructor() : this("", "", "", "", "", "", "", 0)

}

