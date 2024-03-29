package speakap.rijksmuseum.datasource.remote

import retrofit2.HttpException
import speakap.rijksmueum.domain.datamodels.arts.ArtObjectCollection
import speakap.rijksmueum.domain.datamodels.arts.ArtObjectDetail
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.mapper.ArtObjectCollectionResponseToDomainMapper
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.mapper.ArtObjectDetailResponseToDomainMapper
import speakap.rijksmuseum.datasource.remote.retrofit.services.ArtsRetrofitService

class ArtsRestApiDatasource(
    private val service: ArtsRetrofitService,
    private val apiKey: String,
) {

    /**
     * Protocol HTTPS
     * Hostname www.rijksmuseum.nl
     * Method GET
     * Endpoint /api/en/collection
     * Query String Parameters
     * @param   page  Starting index
     * @param   resultsPerPage  Number of items requested
     *
     * @return  Art Object Collection
     */
    suspend fun getArtObjectCollection(
        page: Int,
        resultsPerPage: Int,
        involvedMaker: String? = null,
        datingPeriod: String? = null,
    ): ArtObjectCollection {

        val response = service.getArtObjectCollection(
            apiKey = apiKey,
            page = page,
            resultsPerPage = resultsPerPage,
            involvedMaker = involvedMaker,
            datingPeriod = datingPeriod,
        )

        if (response.isSuccessful) {
            val data = response.body()!!
            // Perform Object Model Mapping
            return ArtObjectCollectionResponseToDomainMapper.map(data)
        } else {
            throw HttpException(response)
        }
    }


    /**
     * Protocol HTTPS
     * Hostname www.rijksmuseum.nl
     * Method GET
     * Endpoint /api/en/collection/{objectId}
     * Query String Parameters
     * @param   objectNumber  ObjectId of the specified Art Object
     *
     * @return  Art Object Detail
     */
    suspend fun getArtObjectDetail(
        objectNumber: String
    ): ArtObjectDetail {

        val response = service.getArtObjectDetail(
            apiKey = apiKey,
            objectNumber = objectNumber,
        )

        if (response.isSuccessful) {
            val data = response.body()!!
            // Perform Object Model Mapping
            return ArtObjectDetailResponseToDomainMapper.map(data)
        } else {
            throw HttpException(response)
        }
    }

}