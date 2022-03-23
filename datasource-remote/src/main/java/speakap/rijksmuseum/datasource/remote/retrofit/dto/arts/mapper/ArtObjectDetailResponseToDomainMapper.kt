package speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.mapper

import speakap.rijksmueum.domain.datamodels.arts.ArtObjectDetail
import speakap.rijksmueum.domain.datamodels.arts.Dating
import speakap.rijksmueum.domain.datamodels.arts.Labels
import speakap.rijksmuseum.datasource.remote.retrofit.dto.Mapper
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTOArtObjectDetail
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTOArtObjectDetailResponse
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTODating
import speakap.rijksmuseum.datasource.remote.retrofit.dto.arts.DTOLabels

object ArtObjectDetailResponseToDomainMapper: Mapper<DTOArtObjectDetailResponse, ArtObjectDetail> {

    override fun map(input: DTOArtObjectDetailResponse): ArtObjectDetail =
        ArtObjectDetail(
            id = input.artObject?.id,
            webImage = input.artObject?.webImage?.let(WebImgResponseToDomainMapper::map),
            principalMaker = input.artObject?.principalMaker,
            label = input.artObject?.label?.let(ArtLabelsResponseToDomainMapper::map),
            acquisition = input.artObject?.acquisition?.let(ArtAcquisitionResponseToDomainMapper::map),
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

object ArtAcquisitionResponseToDomainMapper: Mapper<DTOArtObjectDetail.Acquisition, ArtObjectDetail.Acquisition> {
    override fun map(input: DTOArtObjectDetail.Acquisition): ArtObjectDetail.Acquisition =
        ArtObjectDetail.Acquisition(
            method = input.method,
            date = input.date,
            creditLine = input.creditLine,
        )
}

object ArtDatingResponseToDomainMapper: Mapper<DTODating, Dating> {
    override fun map(input: DTODating): Dating =
        Dating(
            presentingDate = input.presentingDate,
            period = input.period,
        )
}