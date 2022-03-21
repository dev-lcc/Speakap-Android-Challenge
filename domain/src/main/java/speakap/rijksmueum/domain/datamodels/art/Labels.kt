package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Labels(
    val title: String,
    val makerLine: String,
    val description: String
) : Parcelable
