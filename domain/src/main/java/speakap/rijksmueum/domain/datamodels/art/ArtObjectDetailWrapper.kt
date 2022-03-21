package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectDetailWrapper(
    val artObject: ArtObjectDetail
) : Parcelable