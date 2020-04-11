package dk.fitfit.mykoinapplication.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.fitfit.mykoinapplication.repository.HelloRepository

class MyViewModel(private val repository: HelloRepository) : ViewModel() {
    fun sayHello() = "${repository.giveHello()} from MyViewModel!!!"
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
