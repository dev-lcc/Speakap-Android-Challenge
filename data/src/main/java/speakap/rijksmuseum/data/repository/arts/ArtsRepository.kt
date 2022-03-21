package speakap.rijksmuseum.data.repository.arts

import speakap.rijksmueum.domain.datamodels.art.ArtObjectCollection
import speakap.rijksmueum.domain.datamodels.art.ArtObjectDetail

class ArtsRepository(
    // TODO:: Remote Datasource
    // Include other Datasource in here(Local Database, etc.)
) {

    suspend fun getArtObjectCollections(
        offset: Int,
        limit: Int,
    ): ArtObjectCollection {
        // TODO:: getArtObjectCollections()
        TODO("Not yet implemented...")
    }

    suspend fun getArtObject(
        objectId: String
    ): ArtObjectDetail {
        // TODO:: getArtObject()
        TODO("Not yet implemented...")
    }

}