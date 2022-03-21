package speakap.rijksmuseum.datasource.remote.retrofit.dto.art.mapper

import speakap.rijksmueum.domain.datamodels.art.WebImg
import speakap.rijksmuseum.datasource.remote.retrofit.dto.Mapper
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObject

object WebImgResponseToDomainMapper: Mapper<DTOArtObject.DTOWebImg, WebImg> {
    override fun map(input: DTOArtObject.DTOWebImg): WebImg =
        WebImg(
            url = input.url,
            width = input.width,
            height = input.height
        )
}