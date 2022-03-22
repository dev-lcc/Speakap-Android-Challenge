package speakap.rijksmuseum.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import speakap.rijksmuseum.data.usecase.GetArtObjectCollections
import speakap.rijksmuseum.data.usecase.GetArtObjectDetail

object UseCaseModule {

    object Arts {

        fun build(): Module = module {

            single {
                GetArtObjectCollections(
                    repository = get(),
                )
            }

            single {
                GetArtObjectDetail(
                    repository = get(),
                )
            }

        }

    }

}