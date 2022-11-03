package com.example.smart_insurance.model

import java.io.Serializable

class Card(
    val image: String,
    val title: String,
    val date: String,
    val layout: String

) : Serializable {

    constructor() : this("", "", "", "")
}