package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dating(
    val presentingDate: String? = null,
    val period: Int? = null
) : Parcelable
