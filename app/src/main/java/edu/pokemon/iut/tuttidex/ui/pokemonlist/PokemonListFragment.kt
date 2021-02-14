package edu.pokemon.iut.tuttidex.ui.pokemonlist


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.databinding.FragmentPokemonListBinding
import edu.pokemon.iut.tuttidex.ui.model.Pokemon
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class PokemonListFragment : Fragment() {
    private lateinit var binding: FragmentPokemonListBinding
    val viewModel: PokemonListViewModel by viewModel()

    private var maxId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pokemon_list,
            container,
            false
        )

        val adapter = PokemonsAdapter(viewModel)
        binding.rvPokemonList.adapter = adapter
        binding.rvPokemonList.layoutManager = LinearLayoutManager(context)
        viewModel.pokemons.observe(viewLifecycleOwner, Observer<List<Pokemon>> { pokemons ->
            pokemons?.apply {
                adapter.submitList(pokemons)
            }
        })

        viewModel.maxPokemonId.observe(viewLifecycleOwner, Observer { maxId ->
            this.maxId = maxId
        })

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.lastPokemonIdViewed in 1 until maxId) {
            binding.rvPokemonList.scrollToPosition(viewModel.lastPokemonIdViewed)
        }

        if(viewModel.lastSearch.isNotBlank()){
            viewModel.search(viewModel.lastSearch)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)

        initSearch(menu)

        initFilter(menu)

        initSwitch(menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initFilter(menu: Menu) {
        val filterItem = menu.findItem(R.id.filterByCaptured)
        val filterView = filterItem.actionView as CheckBox
        filterView.setButtonDrawable(R.drawable.pokemon_ball_selector)


        viewModel.pokemonFilter.observe(this, Observer {
            filterView.isChecked = it.filterCaptured
            if (it.filterCaptured) {
                closeSearch(menu)
            }
        })
        filterView.setOnCheckedChangeListener { _, isChecked -> viewModel.onFilterByCaptured(isChecked) }
    }

    private fun closeSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        if (searchItem.isActionViewExpanded) {
            searchItem.collapseActionView()
            searchView.setQuery("", false)
        }
    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val filter = viewModel.pokemonFilter.value

        if (filter != null && filter.searchQuery.isNotBlank()) {
            searchItem.expandActionView()
            searchView.setQuery(filter.searchQuery, true)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query ?: "")
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return false
            }
        })
    }

    private fun initSwitch(menu: Menu) {

        val filterItem = menu.findItem(R.id.dark_mode_switch).actionView as Switch
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isNightTheme) {
            Configuration.UI_MODE_NIGHT_YES ->
            {
                filterItem.isChecked = true

            }
            Configuration.UI_MODE_NIGHT_NO ->
            {
                filterItem.isChecked = false

            }
        }
        filterItem.setOnCheckedChangeListener {


            _, isCheck ->
            if(isCheck){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
            else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = view?.findNavController()
        return if (navController != null) {
            NavigationUI.onNavDestinationSelected(item, navController)
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
