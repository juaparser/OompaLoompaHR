package com.example.oompaloompahr.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.oompaloompahr.R
import com.example.oompaloompahr.databinding.FragmentDetailsBinding
import com.example.oompaloompahr.databinding.MainFragmentBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)
        val oompaLoompa = args.oompaLoompa

        binding.apply {
            Glide.with(ivPicture).load(oompaLoompa.image).into(ivPicture)

            tvName.text = oompaLoompa.first_name + " " + oompaLoompa.last_name
            tvProfession.text = oompaLoompa.profession

            tvCountry.text = oompaLoompa.country
            tvAge.text = oompaLoompa.age.toString()
            tvEmail.text = oompaLoompa.email
            tvHeight.text = oompaLoompa.height.toString()


            if(oompaLoompa.gender == "F") {
                ivGender.setImageResource(R.drawable.ic_female)
            } else
                ivGender.setImageResource(R.drawable.ic_male)

            var favoriteString = ""

            for(fav in oompaLoompa.favorite) {
                favoriteString += fav.key + ": \n\n" + fav.value + "\n\n"
            }

            tvFavorite.text = favoriteString
        }

    }
}