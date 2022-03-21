package speakap.rijksmuseum.datasource.remote.retrofit.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObjectCollection
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObjectDetailResponse

interface ArtsRetrofitService {

    @GET("en/collection")
    suspend fun getArtObjectCollection(
        @Query("key") apiKey: String,
        /**
         * "json"|"xml"
         */
        @Query("format") format: String = "json",
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<DTOArtObjectCollection>

    @GET("en/collection/{objectNumber}")
    suspend fun getArtObjectDetail(
        @Query("key") apiKey: String,
        /**
         * "json"|"xml"
         */
        @Query("format") format: String = "json",
        @Path("objectNumber") objectNumber: String
    ): Response<DTOArtObjectDetailResponse>

}