package speakap.rijksmuseum.domain

data class ArtObject(

        val id: String,
        val title: String,

        val principalOrFirstMaker: String,
        val webImage: WebImg,
        val objectNumber: String
)
