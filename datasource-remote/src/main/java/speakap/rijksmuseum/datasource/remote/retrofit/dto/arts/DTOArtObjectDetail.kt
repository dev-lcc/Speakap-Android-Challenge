package speakap.rijksmuseum.datasource.remote.retrofit.dto.arts

import kotlinx.serialization.Serializable

@Serializable
data class DTOArtObjectDetailResponse(
    val elapsedMilliseconds: Long? = null,
    val artObject: DTOArtObjectDetail? = null,
    // TODO:: artObjectPage
)

@Serializable
data class DTOArtObjectDetail(
    val links: DTOArtLink? = null,
    val id: String? = null,
    val priref: String? = null,
    val objectNumber: String? = null,
    val language: String? = null,
    val title: String? = null,
    val copyrightHolder: String? = null,
    val webImage: DTOWebImg? = null,
    // TODO:: colors
    // TODO:: colorsWithNormalization
    // TODO:: normalizedColors
    // TODO:: normalized32Colors
    val titles: List<String> = listOf(),
    val description: String? = null,
    val labelText: String? = null,
    val objectTypes: List<String> = listOf(),   // ex. "painting"
    val objectCollection: List<String> = listOf(),   // ex. "paintings"
    val makers: List<Maker> = listOf(),
    val principalMakers: List<Maker> = listOf(),
    val plaqueDescriptionDutch: String? = null,
    val plaqueDescriptionEnglish: String? = null,
    val principalMaker: String? = null,
    val artistRole: String? = null,
    val associations: List<String> = listOf(),
    val acquisition: Acquisition? = null,
    val exhibitions: List<String> = listOf(),
    val materials: List<String> = listOf(), // ex. "panel", "oil paint (paint)"
    val techniques: List<String> = listOf(),
    val productionPlaces: List<String> = listOf(),
    val dating: DTODating? = null,
    val classification: Classification? = null,
    val hasImage: Boolean? = null,
    // TODO:: historicalPersons
    val inscriptions: List<String> = listOf(),
    val documentation: List<String> = listOf(),
    // TODO:: catRefRPK
    val principalOrFirstMaker: String? = null,
    // TODO:: dimensions
    // TODO:: physicalProperties
    val physicalMedium: String? = null,
    val longTitle: String? = null,
    val subTitle: String? = null,
    val scLabelLine: String? = null,
    val label: DTOLabels? = null,
    val showImage: Boolean? = null,
    val location: String? = null,   // ex. "HG-0.6"
) {

    @Serializable
    data class Maker(
        val name: String? = null,
        val unFixedName: String? = null,
        val placeOfBirth: String? = null,
        val dateOfBirth: String? = null,
        val dateOfBirthPrecision: String? = null,
        val dateOfDeath: String? = null,
        val dateOfDeathPrecision: String? = null,
        val placeOfDeath: String? = null,
        val occupation: List<String> = listOf(),    // ex. "draughtsman"
        val roles: List<String> = listOf(),    // ex. "painter"
        val nationality: String? = null,
        val biography: String? = null,
        val productionPlaces: List<String> = listOf(),    // ex. "painter"
        val qualification: String? = null,
        val labelDesc: String? = null,
    )

    @Serializable
    data class Acquisition(
        val method: String? = null,
        val date: String? = null,   // ISODate format ex. "1888-02-01T00:00:00"
        val creditLine: String? = null,
    )

    @Serializable
    data class Classification(
        val iconClassIdentifier: List<String> = listOf(),
        val iconClassDescription: List<String> = listOf(),
        val motifs: List<String> = listOf(),
        val events: List<String> = listOf(),
        val periods: List<String> = listOf(),
        val places: List<String> = listOf(),
        val people: List<String> = listOf(),
        val objectNumbers: List<String> = listOf(), // ex. "SK-A-1451"
    )

}

@Serializable
data class DTODating(
    val presentingDate: String? = null, // "1566"
    val sortingDate: Int? = null, // 1566
    val period: Int? = null, // 16
    val yearEarly: Int? = null, // 1566
    val yearLate: Int? = null, // 1566
)

@Serializable
data class DTOLabels(
    val title: String? = null,
    val makerLine: String? = null,
    val description: String? = null,
    val notes: String? = null,
    val date: String? = null,   // ex. "2018-10-23"
)
