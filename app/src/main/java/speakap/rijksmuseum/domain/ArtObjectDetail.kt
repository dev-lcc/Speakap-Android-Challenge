package speakap.rijksmuseum.domain

import speakap.rijksmuseum.obj.Dating

data class ArtObjectDetail(
        val id: String,
        val webImage: WebImg,
        val label: Labels,
        val dating: Dating
)
