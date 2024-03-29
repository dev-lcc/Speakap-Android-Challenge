package speakap.rijksmueum.domain.datamodels.arts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Labels(
    val title: String? = null,
    val makerLine: String? = null,
    val description: String? = null,
) : Parcelable
