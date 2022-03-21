package speakap.rijksmuseum.data.repository.arts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import speakap.rijksmueum.domain.datamodels.art.ArtObjectCollection
import speakap.rijksmueum.domain.datamodels.art.ArtObjectDetail
import speakap.rijksmuseum.datasource.remote.ArtsRestApiDatasource

class ArtsRepository(
    // TODO:: Remote Datasource
    private val remoteDatasource: ArtsRestApiDatasource,
    // Include other Datasource in here(Local Database, etc.)
) {

    suspend fun getArtObjectCollections(
        offset: Int,
        limit: Int,
    ): ArtObjectCollection {

        // Perform Fetch from Remote Datasource
        val response = withContext(Dispatchers.IO) {
            remoteDatasource.getArtObjectCollection(
                offset = offset,
                limit = limit,
            )
        }

        // TODO:: For caching OR local persistence, it can be done here...

        return response
    }

    suspend fun getArtObjectDetail(
        objectNumber: String
    ): ArtObjectDetail {

        // Perform Fetch from Remote Datasource
        val response = withContext(Dispatchers.IO) {
            remoteDatasource.getArtObjectDetail(
                objectNumber = objectNumber
            )
        }

        // TODO:: For caching OR local persistence, it can be done here...

        return response
    }

}