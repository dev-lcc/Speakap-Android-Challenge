package speakap.rijksmuseum.datasource.remote.retrofit.dto.arts

import kotlinx.serialization.Serializable

@Serializable
data class DTOArtObjectCollection(
    val elapsedMilliseconds: Long? = null,
    val count: Long? = null,
    val countFacets: CountFacets? = null,
    val artObjects: List<DTOArtObject> = listOf(),
    // TODO:: facets
) {

    @Serializable
    data class CountFacets(
        val hasimage: Long? = null,
        val ondisplay: Long? = null,
    )

}