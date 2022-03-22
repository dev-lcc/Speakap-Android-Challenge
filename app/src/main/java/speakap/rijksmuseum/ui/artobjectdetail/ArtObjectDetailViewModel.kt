package speakap.rijksmuseum.ui.artobjectdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import speakap.rijksmuseum.data.usecase.GetArtObjectDetail

class ArtObjectDetailViewModel(
    private val getArtObjectDetail: GetArtObjectDetail,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

}