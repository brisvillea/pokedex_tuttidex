package edu.pokemon.iut.tuttidex.ui.pokemonlist

import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import edu.pokemon.iut.tuttidex.R
import edu.pokemon.iut.tuttidex.common.recyclerview.DataBindingAdapter
import edu.pokemon.iut.tuttidex.common.recyclerview.DataBindingViewHolder
import edu.pokemon.iut.tuttidex.ui.model.Pokemon

class PokemonsAdapter(viewModel: PokemonListViewModel?) : DataBindingAdapter<Pokemon, PokemonListViewModel>(DiffCallback(),viewModel){

    class DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return (oldItem.name == newItem.name
                    && oldItem.imageUrl == newItem.imageUrl
                    && oldItem.imageShinyUrl == newItem.imageShinyUrl
                    && oldItem.isCaptured == newItem.isCaptured)
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Pokemon, PokemonListViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            viewModel?.onPokemonClicked(getItem(holder.adapterPosition))
            val extras = FragmentNavigatorExtras(
                holder.itemView.findViewById<ImageView>(R.id.iv_pokemon_logo)
                        to holder.itemView.context.getString(R.string._transition),
                holder.itemView.findViewById<ImageView>(R.id.iv_pokemon_logo_Shiny)
                        to holder.itemView.context.getString(R.string._transition_Shiny))
            it.findNavController().navigate(PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(getItem(holder.adapterPosition).number),extras)
        }

    }

    override fun getItemViewType(position: Int) = R.layout.component_pokemon_line
}