package edu.pokemon.iut.tuttidex.ui.pokemoncreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import edu.pokemon.iut.tuttidex.data.repository.PokemonInsertCallBack
import edu.pokemon.iut.tuttidex.data.repository.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PokemonCreateViewModel(private val pokemonRepository: PokemonRepository) : ViewModel(),
    PokemonInsertCallBack {
    var number: LiveData<Int> = Transformations.map(pokemonRepository.getIdMaxPokemonLiveData()) {
        it.plus(1)
    }

    var errorName = MutableLiveData<String>()
    var errorHeight = MutableLiveData<String>()
    var errorWeight = MutableLiveData<String>()
    var errorBaseExp = MutableLiveData<String>()
    var errorTypes = MutableLiveData<String>()

    var name = MutableLiveData<String>()
    var baseExp = MutableLiveData<String>()
    var height = MutableLiveData<String>()
    var weight = MutableLiveData<String>()
    var types = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var imageShiny = MutableLiveData<String>()


    private var job = SupervisorJob()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    fun onCreatePokemon() {
        uiScope.launch {
            checkName()
            checkBaseExp()
            checkHeight()
            checkWeight()
            checkTypes()

            val number = number.value
            val name = name.value
            val baseExp = baseExp.value
            val height = height.value
            val weight = weight.value
            val types = types.value
            val image = image.value
            val imageShiny = imageShiny.value

            if (errorName.value == null
                && errorBaseExp.value == null
                && errorHeight.value == null
                && errorWeight.value == null
                && errorTypes.value == null
                && number != null
                && name != null
                && baseExp != null
                && height != null
                && weight != null
                && types != null
            ) {
                pokemonRepository.createPokemon(
                    number,
                    name,
                    Integer.parseInt(baseExp),
                    Integer.parseInt(height),
                    Integer.parseInt(weight),
                    types,
                    image ?: "",
                    imageShiny ?: "",
                    this@PokemonCreateViewModel
                )
            }

        }
    }

    private fun checkTypes() {
        if (types.value.isNullOrBlank()) {
            errorTypes.value = "Enter a valid list of type please"
        } else {
            errorTypes.value = null
        }
    }

    private fun checkWeight() {
        if (weight.value.isNullOrBlank()) {
            errorWeight.value = "Enter a valid weight please"
        } else {
            errorWeight.value = null
        }
    }

    private fun checkHeight() {
        if (height.value.isNullOrBlank()) {
            errorHeight.value = "Enter a valid height please"
        } else {
            errorHeight.value = null
        }
    }

    private fun checkBaseExp() {
        if (baseExp.value.isNullOrBlank()) {
            errorBaseExp.value = "Enter a valid base exp please"
        } else {
            errorBaseExp.value = null
        }

    }

    private fun checkName() {
        if (name.value.isNullOrBlank()) {
            errorName.value = "Enter a valid name please"
        } else {
            errorName.value = null
        }
    }

    override fun onInsert(idInserted: Long) {
        if (idInserted > 0) {
            name.postValue("")
            baseExp.postValue("")
            height.postValue("")
            weight.postValue("")
            types.postValue("")
            image.postValue("")
        }
    }
}