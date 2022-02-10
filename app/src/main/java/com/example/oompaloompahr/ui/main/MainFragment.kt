package com.example.oompaloompahr.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oompaloompahr.R
import com.example.oompaloompahr.api.OompaLoompa
import com.example.oompaloompahr.databinding.DialogBottomSheetFilterBinding
import com.example.oompaloompahr.databinding.MainFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Fragmento principal de la aplicación, el primero que se carga al entrar.
 * Contiene el listado de empleados y su filtrado.
 */
class MainFragment : Fragment(R.layout.main_fragment), AdapterView.OnItemSelectedListener {

    /**
     * Se inicializa el viewModel
     */
    private val viewModel: MainViewModel by activityViewModels()

    private var currentList: MutableList<OompaLoompa> = mutableListOf()
    private var pageValue = 1
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: OompaAdapter
    private var professionList = mutableListOf("None")

    private var filterProfession = ""
    private var filterGender = ""
    private var isFiltering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Llamada para obtener lista de oompaLoompas, faltar */
        viewModel.getOompaLoompas(pageValue)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = MainFragmentBinding.bind(view)


        binding.apply {

            binding.loadContent.visibility = View.VISIBLE

            adapter = OompaAdapter { id ->
                val action = MainFragmentDirections.actionNavMainToDetailsFragment(id)
                findNavController().navigate(action)
            }
            rvOmpa.adapter = adapter


            viewModel.oompaLoompaList.observe(viewLifecycleOwner) {
                binding.loadContent.visibility = View.GONE

                for(oompa in it) {
                    if(!professionList.contains(oompa.profession)) professionList.add(oompa.profession)
                }

                currentList.addAll(it)
                adapter.submitList(currentList)

                /**
                 * Al llegar al final del RecyclerView, hacer una nueva petición para obtener más empleados.
                 * A MEJORAR: Hacer scroll dinámico, cuando se llega al nuevo final, cargar nuevas páginas.
                 * Solo lo hace una vez.
                 */
                rvOmpa.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if(!isFiltering) {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val totalItemCount = layoutManager.itemCount
                            val lastVisible = layoutManager.findLastVisibleItemPosition()
                            val endHasBeenReached = lastVisible + 5 >= totalItemCount

                            if (totalItemCount > 0 && endHasBeenReached) {
                                // Hemos alcanzado el final del recyclerview, cargar nueva página
                                pageValue++
                                binding.loadContent.visibility = View.VISIBLE
                                viewModel.getOompaLoompas(pageValue)
                            }
                        }
                    }
                })
            }
        }
    }

    /**
     * Creación y gestión del menú de opciones para filtrar contenido.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_gender -> {
                doFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Gestión de filtros para el listado.
     *
     * He decidido utilizar iconos para filtrar por género, ya que es más intuitivo.
     *
     * Para el caso de las profesiones, al iniciar la lista guardo los distintos tipos de profesiones
     * de los empleados del listado, y lo utilizo para el menú dropdown en la selección.
     *
     * A MEJORAR: Al volver de la vista de detalles, que el filtro se mantenga.
     */

    private fun doFilter() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = DialogBottomSheetFilterBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)


        bottomSheetBinding.apply {

            //if (filterGender == "M") cardMale.setBackgroundColor(requireContext().getColor(R.color.purple_200))
            //else if(filterGender == "F") cardFemale.setBackgroundColor(requireContext().getColor(R.color.purple_200))


            spinnerProfession.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, professionList)

            spinnerProfession.onItemSelectedListener = this@MainFragment


            cardMale.setOnClickListener {
                if(filterGender == "M") {
                    filterGender=""
                    it.setBackgroundColor(requireContext().getColor(R.color.white))
                } else {
                    filterGender = "M"
                    cardFemale.setBackgroundColor(requireContext().getColor(R.color.white))
                    it.setBackgroundColor(requireContext().getColor(R.color.purple_200))
                }
            }
            cardFemale.setOnClickListener {
                if(filterGender == "F") {
                    filterGender=""
                    it.setBackgroundColor(requireContext().getColor(R.color.white))
                } else {
                    filterGender = "F"
                    cardMale.setBackgroundColor(requireContext().getColor(R.color.white))
                    it.setBackgroundColor(requireContext().getColor(R.color.purple_200))
                }
            }
            var newList = listOf<OompaLoompa>()


            btnApplyFilter.setOnClickListener {
                if(filterGender.isNotEmpty()) {
                    if(filterProfession.isNotEmpty()) {
                        Log.d(
                            "JPS",
                            "GENDER NOT EMPTY, PROFESSION NOT EMPTY $filterGender y $filterProfession"
                        )
                        newList =
                            currentList.filter { x -> x.gender == filterGender && filterProfession == x.profession }
                    }else {
                        Log.d("JPS","GENDER NOT EMPTY, PROFESSION EMPTY $filterGender y $filterProfession")
                        newList = currentList.filter { x -> x.gender == filterGender }
                    }
                } else if(filterProfession.isNotEmpty()) {
                    newList = currentList.filter { x -> filterProfession == x.profession }
                }

                if(newList.isNotEmpty()) {
                    isFiltering = true
                    adapter.submitList(newList)
                    bottomSheetDialog.dismiss()
                } else {
                    isFiltering = false
                    bottomSheetDialog.dismiss()
                }
            }

            btnRemoveFilter.setOnClickListener {
                adapter.submitList(currentList)
                bottomSheetDialog.dismiss()
            }

        }

        bottomSheetDialog.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(professionList[position] != "None") filterProfession = professionList[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Do nothing
    }

}