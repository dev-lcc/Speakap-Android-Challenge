package speakap.rijksmuseum.ui.artobjectdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import speakap.rijksmueum.domain.datamodels.arts.ArtObjectDetail
import speakap.rijksmueum.domain.datamodels.arts.Dating
import speakap.rijksmuseum.data.usecase.GetArtObjectDetail
import java.text.SimpleDateFormat
import java.util.*

class ArtObjectDetailViewModel(
    private val objectNumber: String,
    private val getArtObjectDetail: GetArtObjectDetail,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState.initial())
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val _singleEvent = MutableSharedFlow<SingleEvent>(extraBufferCapacity = 64)
    val singleEvent: SharedFlow<SingleEvent> = _singleEvent.asSharedFlow()

    val intent = object : ViewIntent {
        override fun refresh() {
            viewModelScope.launch {
                viewModelScope.launch {
                    doPerformGetArtObjectDetail()
                }
            }
        }

        override fun navigateQueryByInvolvedMaker() {
            val maker = artObjectDetail?.principalMaker ?: return
            // EMIT Navigate Query
            _singleEvent.tryEmit(
                SingleEvent.NavigateQueryByInvolvedMaker(
                    query = maker
                )
            )
        }

        override fun navigateQueryByDatingPeriod() {
            val dating = artObjectDetail?.dating ?: return
            // EMIT Navigate Query
            _singleEvent.tryEmit(
                SingleEvent.NavigateQueryByDatingPeriod(
                    query = dating
                )
            )
        }
    }

    // Store Fetch Art Object Detail
    private var artObjectDetail: ArtObjectDetail? = null

    init {
        viewModelScope.launch {
            doPerformGetArtObjectDetail()
        }
    }

    private suspend fun doPerformGetArtObjectDetail() {

        getArtObjectDetail(
            objectNumber = objectNumber
        )
            .collect { result ->
                val vs = _viewState.value
                // Update ViewState
                _viewState.value = when (result) {
                    is GetArtObjectDetail.Result.Loading ->
                        vs.copy(
                            isLoading = true
                        )
                    is GetArtObjectDetail.Result.Success ->
                        vs.copy(
                            isLoading = false,
                            title = result.data.label?.title ?: "",
                            bannerImageUrl = result.data.webImage?.url,
                            principalMaker = result.data.principalMaker ?: "",
                            description = result.data.label?.description ?: "",
                            acquisitionDate = run {
                                result.data.acquisition?.date?.let { date ->
                                    UTC_DATE_FORMATTER.parse(date)
                                }
                            },
                            periodDate = result.data.dating?.presentingDate,
                        )
                            .also {
                                // Store Fetched Art Object Detail
                                this@ArtObjectDetailViewModel.artObjectDetail = result.data
                            }
                    is GetArtObjectDetail.Result.Error ->
                        vs.copy(
                            isLoading = false
                        )
                            .also {
                                // EMIT Error
                                _singleEvent.emit(SingleEvent.ErrorGetArtObjectDetail)
                            }
                }
            }

    }

    companion object {
        private val UTC_DATE_FORMATTER =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    }

}

data class ViewState(
    val isLoading: Boolean,
    val title: String,
    val bannerImageUrl: String? = null,
    val principalMaker: String,
    val description: String,
    val acquisitionDate: Date? = null,
    val periodDate: String? = null,
) {
    companion object {
        fun initial(): ViewState =
            ViewState(
                isLoading = true,
                title = "",
                principalMaker = "",
                description = "",
            )
    }
}

interface ViewIntent {

    fun refresh()

    fun navigateQueryByInvolvedMaker()

    fun navigateQueryByDatingPeriod()

}

sealed class SingleEvent {

    object ErrorGetArtObjectDetail : SingleEvent()

    data class NavigateQueryByInvolvedMaker(val query: String) : SingleEvent()

    data class NavigateQueryByDatingPeriod(
        val query: Dating
    ) : SingleEvent()

}