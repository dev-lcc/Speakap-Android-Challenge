package speakap.rijksmuseum.datasource.remote.retrofit.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTOArtObjectCollection
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTOArtObjectDetailResponse

interface ArtsRetrofitService {

    @GET("en/collection")
    suspend fun getArtObjectCollection(
        @Query("key") apiKey: String,
        /**
         * "json"|"xml"
         */
        @Query("format") format: String = "json",
        @Query("p") page: Int,
        @Query("ps") resultsPerPage: Int,
    ): Response<DTOArtObjectCollection>

    @GET("en/collection/{objectNumber}")
    suspend fun getArtObjectDetail(
        @Path("objectNumber") objectNumber: String,
        @Query("key") apiKey: String,
        /**
         * "json"|"xml"
         */
        @Query("format") format: String = "json",
    ): Response<DTOArtObjectDetailResponse>

}