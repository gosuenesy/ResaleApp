package com.example.resaleapp

import retrofit2.Call
import retrofit2.http.*

interface ItemService {
    @GET("resaleitems")
    fun getAllItems(): Call<List<Item>>

    @GET("resaleitems/{itemId}")
    fun getItemById(@Path("itemId") itemId: Int): Call<Item>

    @POST("resaleitems")
    fun saveItem(@Body item: Item): Call<Item>

    @DELETE("resaleitems/{id}")
    fun deleteItem(@Path("id") id: Int): Call<Item>
}