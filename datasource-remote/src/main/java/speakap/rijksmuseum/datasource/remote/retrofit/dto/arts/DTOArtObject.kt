package speakap.rijksmuseum.datasource.remote.retrofit.dto.arts

import kotlinx.serialization.Serializable

@Serializable
data class DTOArtObject(
    val links: DTOArtLink? = null,
    val id: String? = null,
    val objectNumber: String? = null,
    val title: String? = null,
    val hasImage: Boolean? = null,
    val principalOrFirstMaker: String? = null,
    val longTitle: String? = null,
    val showImage: Boolean? = null,
    val permitDownload: Boolean? = null,
    val webImage: DTOWebImg? = null,
    val headerImage: DTOWebImg? = null,
    val productionPlaces: List<String> = listOf(),
)

@Serializable
data class DTOArtLink(
    val self: String? = null,
    val web: String? = null,
)

@Serializable
data class DTOWebImg(
    val guid: String? = null,
    val url: String? = null,
    val offsetPercentageX: Int? = null,
    val offsetPercentageY: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
)