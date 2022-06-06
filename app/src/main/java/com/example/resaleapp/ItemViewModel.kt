package com.example.resaleapp

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()
    val itemsLiveData: LiveData<List<Item>> = repository.itemsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getPosts()
    }

    operator fun get(index: Int): Item? {
        return itemsLiveData.value?.get(index)
    }

    fun add(item: Item) {
        repository.add(item)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun sortByPrice() {
        val sortedData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        sortedData.value = repository.itemsLiveData.value?.sortedBy { it.price }
        repository.itemsLiveData.value = sortedData.value
    }

    fun sortByDate() {
        val sortedData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        sortedData.value = repository.itemsLiveData.value?.sortedByDescending { it.date }
        repository.itemsLiveData.value = sortedData.value
    }

    fun filterMaxPrice(maxPrice: Int) {
        val sortedData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        sortedData.value = repository.itemsLiveData.value?.filter { it.price < maxPrice }
        repository.itemsLiveData.value = sortedData.value
    }
}