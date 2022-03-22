package speakap.rijksmuseum.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import speakap.rijksmuseum.ui.artobjectdetail.ArtObjectDetailViewModel
import speakap.rijksmuseum.ui.artobjectslist.ArtObjectsViewModel

object ViewModelModules {

    fun build(): Module = module {

        viewModel {
            ArtObjectsViewModel(
                getArtObjectCollections = get(),
            )
        }

        viewModel {
            ArtObjectDetailViewModel(
                getArtObjectDetail = get(),
                savedStateHandle = get(),
            )
        }

    }

}