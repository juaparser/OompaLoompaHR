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
                 * A MEJORAR: Hacer scroll dinámico, cuando se llega al nuevo final, cargar nuevas páginas.
                 * Solo lo hace una vez.
                 */
                rvOmpa.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
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
                })
            }
        }
    }

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


    private fun doFilter() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = DialogBottomSheetFilterBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)


        bottomSheetBinding.apply {

            if (filterGender == "M") cardMale.setBackgroundColor(requireContext().getColor(R.color.purple_200))
            else if(filterGender == "F") cardFemale.setBackgroundColor(requireContext().getColor(R.color.purple_200))


            spinnerProfession.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, professionList)

            spinnerProfession.onItemSelectedListener = this@MainFragment


            cardMale.setOnClickListener {
                if(filterGender == "M") {
                    filterGender=""
                    it.setBackgroundColor(requireContext().getColor(R.color.white))
                } else {
                    filterGender = "M"
                    Log.d("JPS","GENDER MALE PICKED")
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
                    Log.d("JPS","GENDER FEMALE PICKED")
                    cardMale.setBackgroundColor(requireContext().getColor(R.color.white))
                    it.setBackgroundColor(requireContext().getColor(R.color.purple_200))
                }
/*                val newList = currentList.filter { x -> x.gender == "F" }
                adapter.submitList(newList)
                bottomSheetDialog.dismiss()*/
            }
            var newList = listOf<OompaLoompa>()

            if(filterGender.isNotEmpty()) {
                if(filterProfession.isNotEmpty())
                    newList = currentList.filter { x -> x.gender == filterGender && filterProfession == x.profession }
                else {
                    Log.d("JPS","GENDER NOT EMPTY, PROFESSION EMPTY $filterGender y $filterProfession")
                    newList = currentList.filter { x -> x.gender == filterGender }
                }
            } else if(filterProfession.isNotEmpty()) {
                newList = currentList.filter { x -> filterProfession == x.profession }
            }

            btnApplyFilter.setOnClickListener {
                Log.d("JPS","NEWLIST IS " + newList)
                Log.d("JPS","FILTERS ARE GENDER $filterGender Y profession $filterProfession")
                if(newList.isNotEmpty()) {
                    adapter.submitList(newList)
                    bottomSheetDialog.dismiss()
                } else {
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