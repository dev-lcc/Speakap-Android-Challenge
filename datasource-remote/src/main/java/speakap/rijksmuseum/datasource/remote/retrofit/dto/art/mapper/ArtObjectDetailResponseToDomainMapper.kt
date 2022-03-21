package speakap.rijksmuseum.datasource.remote.retrofit.dto.art.mapper

import speakap.rijksmueum.domain.datamodels.art.ArtObjectDetail
import speakap.rijksmueum.domain.datamodels.art.Dating
import speakap.rijksmueum.domain.datamodels.art.Labels
import speakap.rijksmuseum.datasource.remote.retrofit.dto.Mapper
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOArtObjectDetailResponse
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTODating
import speakap.rijksmuseum.datasource.remote.retrofit.dto.art.DTOLabels

object ArtObjectDetailResponseToDomainMapper: Mapper<DTOArtObjectDetailResponse, ArtObjectDetail> {

    override fun map(input: DTOArtObjectDetailResponse): ArtObjectDetail =
        ArtObjectDetail(
            id = input.artObject?.id,
            webImage = input.artObject?.webImage?.let(WebImgResponseToDomainMapper::map),
            label = input.artObject?.label?.let(ArtLabelsResponseToDomainMapper::map),
            dating = input.artObject?.dating?.let(ArtDatingResponseToDomainMapper::map),
        )
}

object ArtLabelsResponseToDomainMapper: Mapper<DTOLabels, Labels> {
    override fun map(input: DTOLabels): Labels =
        Labels(
            title = input.title,
            makerLine = input.makerLine,
            description = input.description,
        )
}

object ArtDatingResponseToDomainMapper: Mapper<DTODating, Dating> {
    override fun map(input: DTODating): Dating =
        Dating(
            presentingDate = input.presentingDate,
            period = input.period,
        )
}