package com.example.oompaloompahr.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.oompaloompahr.R
import com.example.oompaloompahr.api.ArgumentCallback
import com.example.oompaloompahr.api.Data
import com.example.oompaloompahr.databinding.MainFragmentBinding
import javax.security.auth.callback.Callback
import javax.security.auth.callback.CallbackHandler

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("JPS", "PRE GET OOMPA LOOMPAS ")
        viewModel.getOompaLoompas()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MainFragmentBinding.bind(view)


        binding.apply {
            val adapter = OompaAdapter {
                findNavController().navigate(R.id.action_nav_main_to_detailsFragment)
            }
            rvRepos.adapter = adapter
            Log.d("JPS", "BINDING APPLY ONVIEWCREATED")
            viewModel.oompaLoompaList.observe(viewLifecycleOwner) {
                Log.d("JPS", "GET OOMPA LOOMPAS OBSERVER " + it)
                adapter.submitList(it)
            }
        }
    }


}