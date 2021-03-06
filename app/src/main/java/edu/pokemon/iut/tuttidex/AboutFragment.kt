package edu.pokemon.iut.tuttidex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import edu.pokemon.iut.tuttidex.databinding.FragmentAboutBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_about,
            container,
            false
        )
        // Inflate the layout for this fragment
        return binding.root
    }


}
