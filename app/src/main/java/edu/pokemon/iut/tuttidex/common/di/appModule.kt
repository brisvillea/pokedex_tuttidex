import edu.pokemon.iut.tuttidex.common.di.remoteDataSourceModule
import edu.pokemon.iut.tuttidex.common.di.roomDataSourceModule
import edu.pokemon.iut.tuttidex.data.repository.PokemonRepository
import edu.pokemon.iut.tuttidex.data.repository.PokemonRepositoryImpl
import edu.pokemon.iut.tuttidex.ui.pokemoncreate.PokemonCreateViewModel
import edu.pokemon.iut.tuttidex.ui.pokemondetail.PokemonDetailViewModel
import edu.pokemon.iut.tuttidex.ui.pokemonlist.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // single instance of HelloRepository
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }


    // ViewModel
    viewModel { PokemonListViewModel(get()) }
    viewModel { (id: Int) -> PokemonDetailViewModel(id, get()) }
    viewModel { PokemonCreateViewModel(get()) }

}

val appModule = listOf(viewModelModule, roomDataSourceModule,
    remoteDataSourceModule
)
