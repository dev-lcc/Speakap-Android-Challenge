package speakap.rijksmuseum.datasource.remote.retrofit.dto.art.mapper

import speakap.rijksmueum.domain.datamodels.art.ArtObject
import speakap.rijksmueum.domain.datamodels.art.ArtObjectCollection
import speakap.rijksmuseum.datasource.remote.retrofit.dto.Mapper
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObject
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObjectCollection

object ArtObjectCollectionResponseToDomainMapper :
    Mapper<DTOArtObjectCollection, ArtObjectCollection> {

    override fun map(input: DTOArtObjectCollection): ArtObjectCollection =
        ArtObjectCollection(
            count = input.count?.toInt(),
            artObjects = input.artObjects.map(ArtObjectResponseToDomainMapper::map),
        )
}

object ArtObjectResponseToDomainMapper : Mapper<DTOArtObject, ArtObject> {
    override fun map(input: DTOArtObject): ArtObject =
        ArtObject(
            id = input.id,
            title = input.title,
            principalOrFirstMaker = input.principalOrFirstMaker,
            webImage = input.webImage?.let(WebImgResponseToDomainMapper::map),
            objectNumber = input.objectNumber,
        )
}