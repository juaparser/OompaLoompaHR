package com.example.oompaloompahr.ui.details

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.oompaloompahr.R
import com.example.oompaloompahr.api.OompaLoompa
import com.example.oompaloompahr.databinding.FragmentDetailsBinding
import com.example.oompaloompahr.databinding.MainFragmentBinding
import com.example.oompaloompahr.ui.main.MainFragmentDirections
import com.example.oompaloompahr.ui.main.MainViewModel

/**
 * Fragmento de detalles del empleado. Recibe como parámetro un Integer con el id
 * del Oompa Loompa que se quiere ver más información.
 */
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: MainViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private var oompaLoompa: OompaLoompa? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        val oompaLoompaId: Int = args.oompaLoompaId

        binding.loadContent.visibility = View.VISIBLE

        viewModel.getDetailOompaLoompa(oompaLoompaId) {
            oompaLoompa = it
            loadUI(binding)

        }
    }

    /**
     * Método al que se llama cuando se completa la petición de obtener más detalles del Oompa Loompa.
     * Se utiliza para cargar toda la información del empleado una vez se tengan los datos listos.
     */
    private fun loadUI(binding: FragmentDetailsBinding) {
        binding.apply {
            binding.loadContent.visibility = View.GONE
            Glide.with(ivPicture).load(oompaLoompa?.image).into(ivPicture)

            tvName.text = oompaLoompa?.first_name + " " + oompaLoompa?.last_name
            tvProfession.text = oompaLoompa?.profession

            tvCountry.text = oompaLoompa?.country
            tvAge.text = oompaLoompa?.age.toString()
            tvEmail.text = oompaLoompa?.email
            tvHeight.text = oompaLoompa?.height.toString() + " cm"

            oompaLoompa?.description.let {
                tvDescription.text = it
            }


            if(oompaLoompa?.gender == "F") {
                ivGender.setImageResource(R.drawable.ic_female)
            } else
                ivGender.setImageResource(R.drawable.ic_male)

            var favoriteString = ""

            for(fav in oompaLoompa?.favorite!!) {
                favoriteString += "<b>" + fav.key.replaceFirstChar { x -> x.uppercaseChar() } + "</b>: <br>" + fav.value.replace("\n","<br>") + "<br>"
            }

            val spanned = Html.fromHtml(favoriteString)
            tvFavorite.text = spanned
        }

    }
}