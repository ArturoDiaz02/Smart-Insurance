package com.example.smart_insurance.model

import java.io.Serializable

data class Category(
    var id: String,
    var name: String,
    var image: String,
    var color : String

) : Serializable {
    override fun toString(): String {
        return super.toString()
    }

    constructor() : this("", "", "", "")

}