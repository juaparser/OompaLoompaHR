package com.example.oompaloompahr.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oompaloompahr.api.Api
import com.example.oompaloompahr.api.OompaLoompa

class MainViewModel : ViewModel() {

    val oompaLoompaList = MutableLiveData<List<OompaLoompa>>()

    /**
     * Llamada a la API para obtener el listado de Oompa Loompas, guardando el resultado
     * en un LiveData para propagar su contenido mediante un observer al listado.
     */
    fun getOompaLoompas(page: Int) {
        Api().getOmpas(page) {
            oompaLoompaList.value = it.results
        }
    }

    /**
     * Llamada a la API para obtener los detalles del Oompa Loompa, el cual se realiza al acceder
     * a la vista asociada para mayor fluidez de la UI.
     */
    fun getDetailOompaLoompa(ompaId: Int, next:(oompaLoompa: OompaLoompa) -> Unit) {
        Api().getOmpa(ompaId) {
            next(it)
        }
    }
}