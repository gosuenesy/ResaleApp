package com.example.resaleapp

import java.io.Serializable
import java.text.SimpleDateFormat

data class Item(val id: Int, val title: String, val description: String, val price: Int, val seller: String, val date: Int, var pictureUrl: String) : Serializable {
    constructor(title: String, description: String, price: Int, seller: String, date: Int, pictureUrl: String) : this(-1, title, description, price, seller, date, pictureUrl)

    override fun toString(): String {
        pictureUrl = "";
        val format = SimpleDateFormat.getDateTimeInstance()
        val str = format.format(date * 1000L)
        return "$title \n$description \n$price$ \n$seller \n$str"
    }
}