package speakap.rijksmuseum.ui.artobjectslist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import speakap.rijksmueum.domain.datamodels.art.ArtObject

class ArtObjectAdapter(
    val onClick : (objectNumber : String) -> Unit
) : RecyclerView.Adapter<ArtObjectViewHolder>() {

    override fun getItemCount(): Int = data.size

    var data = arrayListOf<speakap.rijksmueum.domain.datamodels.art.ArtObject>()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ArtObjectViewHolder {
        return ArtObjectViewHolder(parent, onClick)
    }

    override fun onBindViewHolder(
            holder: ArtObjectViewHolder,
            position: Int
    ) = holder.bindTo(getItem(position))

    private fun getItem(position: Int): speakap.rijksmueum.domain.datamodels.art.ArtObject? = data[position]!!

    fun addData(artObjects: ArrayList<speakap.rijksmueum.domain.datamodels.art.ArtObject>) {
        val diffResult = DiffUtil.calculateDiff(Diff(data, artObjects))
        this.data = artObjects
        diffResult.dispatchUpdatesTo(this)
    }

    class Diff(private val old: List<speakap.rijksmueum.domain.datamodels.art.ArtObject>, private val newItem: List<speakap.rijksmueum.domain.datamodels.art.ArtObject>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPos: Int, newPosition: Int): Boolean =
                old[oldItemPos].id == newItem[newPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition] == newItem[newItemPosition]

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = newItem.size
    }



}
