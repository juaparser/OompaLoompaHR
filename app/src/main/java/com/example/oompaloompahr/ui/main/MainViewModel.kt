package com.example.oompaloompahr.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oompaloompahr.api.Api
import com.example.oompaloompahr.api.ArgumentCallback
import com.example.oompaloompahr.api.Data
import com.example.oompaloompahr.api.OompaLoompa
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val oompaLoompaList = MutableLiveData<List<OompaLoompa>>()
    val error = MutableLiveData<Int>()

    fun getOompaLoompas() {
        Api().getOmpas {
            Log.d("JPS", "GET NEW OOMPAS " + it)
            oompaLoompaList.value = it.results
        }
    }

    fun getDetailOompaLoompa(ompaId: Int, next:(oompaLoompa: OompaLoompa) -> Unit) {
        Api().getOmpa(ompaId) {
            next(it)
        }
    }
}