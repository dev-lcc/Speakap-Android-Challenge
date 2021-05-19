package speakap.rijksmuseum.data

import speakap.rijksmuseum.domain.ArtObjectDetailWrapper
import speakap.rijksmuseum.obj.Collection
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RijksmuseumInternet {
    @GET("en/collection?key=$apiKey&format=$format")
    fun getArtObjects(@Query("p") page: Int, @Query("f.dating.period") period: Int?): Single<Collection>

    @GET("en/collection/{objectNumber}?key=$apiKey&format=$format")
    fun getArtObjectDetail(@Path("objectNumber") objectNumber: String): Single<ArtObjectDetailWrapper>
}
