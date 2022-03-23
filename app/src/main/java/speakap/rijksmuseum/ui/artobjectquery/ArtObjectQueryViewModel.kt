package speakap.rijksmuseum.ui.artobjectquery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import speakap.rijksmueum.domain.datamodels.arts.ArtObject
import speakap.rijksmuseum.data.usecase.GetArtObjectCollections

class ArtObjectQueryViewModel(
    private val queryType: QueryType,
    private val getArtObjectCollections: GetArtObjectCollections,
) : ViewModel() {

    private val _viewState = MutableStateFlow(
        ViewState.initial(queryType = queryType)
    )
    val viewState: StateFlow<ViewState>
        get() = _viewState.asStateFlow()

    private val _singleEvent = MutableSharedFlow<SingleEvent>(extraBufferCapacity = 4)
    val singleEvent: SharedFlow<SingleEvent>
        get() = _singleEvent.asSharedFlow()

    val intent = object : ViewIntent {
        override fun refresh() {
            viewModelScope.launch {
                this@ArtObjectQueryViewModel.currentPage = PAGE_INITIAL
                performGetArtObjectCollections(
                    page = this@ArtObjectQueryViewModel.currentPage
                )
            }
        }

        override fun fetchNextPage() {
            val vs = _viewState.value
            if (vs.hasMoreToFetch) {
                viewModelScope.launch {
                    performGetArtObjectCollections(
                        page = this@ArtObjectQueryViewModel.currentPage
                    )
                }
            }
        }
    }

    // Store Pagination Metadata
    private var currentPage: Int = PAGE_INITIAL
    private var totalCount: Int = 0

    init {
        viewModelScope.launch {
            performGetArtObjectCollections(page = PAGE_INITIAL)
        }
    }

    private suspend fun performGetArtObjectCollections(page: Int) {
        val isFetchMore = page > 1

        when(queryType) {
            is QueryType.ByInvolvedMaker ->
                getArtObjectCollections(
                    page = page,
                    resultsPerPage = RESULTS_PER_PAGE,
                    involvedMaker = queryType.query,
                )
            is QueryType.ByDatingPeriod ->
                getArtObjectCollections(
                    page = page,
                    resultsPerPage = RESULTS_PER_PAGE,
                    datingPeriod = "${queryType.dating.period ?: 0}",
                )
        }
            .collect { result ->
                val vs = _viewState.value
                // Update ViewState
                _viewState.value = when (result) {
                    is GetArtObjectCollections.Result.Loading -> vs.copy(isLoading = true)
                    is GetArtObjectCollections.Result.LoadingNextPage -> vs.copy(isLoadingNextPage = true)
                    is GetArtObjectCollections.Result.Success -> {
                        val updatedList = when (isFetchMore) {
                            // Append results on fetch next page
                            true -> vs.artObjects + result.data.artObjects
                            false -> result.data.artObjects
                        }
                            .distinctBy { it.id }

                        // Update Pagination Metadata
                        this@ArtObjectQueryViewModel.currentPage += 1
                        this@ArtObjectQueryViewModel.totalCount =
                            result.data.count ?: this@ArtObjectQueryViewModel.totalCount

                        vs.copy(
                            isLoading = false,
                            isLoadingNextPage = false,
                            hasMoreToFetch = updatedList.size < this@ArtObjectQueryViewModel.totalCount,
                            artObjects = updatedList,
                            isEmpty = false,
                        )
                    }
                    is GetArtObjectCollections.Result.Empty ->
                        vs.copy(
                            isLoading = false,
                            isLoadingNextPage = false,
                            isEmpty = true,
                            artObjects = emptyList(),
                        )
                    is GetArtObjectCollections.Result.Error ->
                        vs.copy(
                            isLoading = false,
                            isLoadingNextPage = false,
                        )
                            .also {
                                // EMIT Error
                                _singleEvent.emit(
                                    when (isFetchMore) {
                                        false -> SingleEvent.ErrorGetArtObjectCollections
                                        true -> SingleEvent.ErrorGetArtObjectCollectionsNextPage
                                    }
                                )
                            }
                }
            }

    }

    companion object {
        private const val PAGE_INITIAL = 1
        private const val RESULTS_PER_PAGE = 10
    }

}

data class ViewState(
    val queryType: QueryType,
    val isLoading: Boolean,
    val isLoadingNextPage: Boolean,
    val hasMoreToFetch: Boolean,
    val artObjects: List<ArtObject> = listOf(),
    val isEmpty: Boolean,
) {
    companion object {
        fun initial(
            queryType: QueryType
        ): ViewState =
            ViewState(
                queryType = queryType,
                isLoading = true,
                isLoadingNextPage = false,
                hasMoreToFetch = false,
                isEmpty = false,
            )
    }
}

interface ViewIntent {

    fun refresh()

    fun fetchNextPage()

}

sealed class SingleEvent {

    object ErrorGetArtObjectCollections : SingleEvent()

    object ErrorGetArtObjectCollectionsNextPage : SingleEvent()

}