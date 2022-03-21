package speakap.rijksmuseum.ui.artobjectslist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import speakap.rijksmuseum.domain.ArtObject

class ArtObjectAdapter(
    val onClick : (objectNumber : String) -> Unit
) : RecyclerView.Adapter<ArtObjectViewHolder>() {

    override fun getItemCount(): Int = data.size

    var data = arrayListOf<ArtObject>()

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

    private fun getItem(position: Int): ArtObject? = data[position]!!

    fun addData(artObjects: ArrayList<ArtObject>) {
        val diffResult = DiffUtil.calculateDiff(Diff(data, artObjects))
        this.data = artObjects
        diffResult.dispatchUpdatesTo(this)
    }

    class Diff(private val old: List<ArtObject>, private val newItem: List<ArtObject>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPos: Int, newPosition: Int): Boolean =
                old[oldItemPos].id == newItem[newPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition] == newItem[newItemPosition]

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = newItem.size
    }



}
