package speakap.rijksmuseum.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import speakap.rijksmuseum.data.repository.arts.ArtsRepository
import speakap.rijksmuseum.datasource.remote.di.RemoteDatasourceModule

class RepositoryModule(
    private val restApiUrlEndpoint: String,
    private val apiKey: String,
    private val isDebug: Boolean,
) {

    fun build(): List<Module> = module {

        single {
            ArtsRepository(
                remoteDatasource = get(),
            )
        }
        // TODO:: Add more Repository class here...

    } + module {
        single {
            RemoteDatasourceModule(
                restApiUrlEndpoint = restApiUrlEndpoint,
                apiKey = apiKey,
                isDebug = isDebug,
            )
        }
    }

}