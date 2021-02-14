package edu.pokemon.iut.tuttidex.ui.pokemoncreate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.image.loadImageWithoutCache
import edu.pokemon.iut.tuttidex.databinding.FragmentPokemonCreateBinding
import edu.pokemon.iut.tuttidex.hideKeyboardFrom
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 *
 */
class PokemonCreateFragment : Fragment() {
    private val requestImageCapture = 1

    val viewModel: PokemonCreateViewModel by viewModel()
    private lateinit var binding: FragmentPokemonCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonCreateBinding.inflate(inflater, container, false)

        binding.pokemonCreateViewModel = viewModel
        binding.lifecycleOwner = this
        binding.buttonAddPhoto.setOnClickListener {
            takePhoto()
        }

        requireActivity().findNavController(R.id.tuttidexNavHostFragment)
            .addOnDestinationChangedListener { _, destination, _ ->
                if ((destination as FragmentNavigator.Destination).className != PokemonCreateFragment::class.qualifiedName) {
                    val imagePath = viewModel.image.value
                    if (imagePath != null && imagePath.isNotBlank()) {
                        val file = File(imagePath)
                        file.delete()
                    }
                }
            }

        return binding.root
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Timber.e(ex)
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(requireContext(), "edu.pokemon.iut.tuttidex", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestImageCapture)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestImageCapture && resultCode == AppCompatActivity.RESULT_OK) {
            loadImageWithoutCache(binding.previewPokemon, viewModel.image.value)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val fileName = viewModel.number.value.toString()
        val file = File(requireContext().filesDir.path + File.separator + fileName + ".png")
        viewModel.image.value = file.path
        return file
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboardFrom(requireContext(), binding.root)
    }
}
