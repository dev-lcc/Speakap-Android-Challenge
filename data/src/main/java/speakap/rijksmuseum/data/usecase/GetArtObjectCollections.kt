package speakap.rijksmuseum.data.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import speakap.rijksmueum.domain.datamodels.arts.ArtObjectCollection
import speakap.rijksmuseum.data.repository.arts.ArtsRepository

class GetArtObjectCollections(
    private val repository: ArtsRepository,
) {

    suspend operator fun invoke(
        offset: Int,
        limit: Int,
    ): Flow<Result> = flow {
        val isFetchMore = offset > 1

        // EMIT In-Progress
        emit(Result.Loading)

        val response = repository.getArtObjectCollections(
            offset = offset,
            limit = limit,
        )

        if (!isFetchMore && response.artObjects.isEmpty()) {
            // Initial fetch is empty
            emit(Result.Empty)
        } else {
            // Emit List
            emit(
                Result.Success(
                    data = response
                )
            )
        }

    }
        .retryWhen { cause, attempt ->
            // Attempt retry(at most 3x) when Network or I/O Error encountered
            (/*cause::class.java !in listOf(
                ArtObjectNotFoundError::class.java,
            ) && */attempt < 3)
                // Apply Exponential Back-off Delay
                .also {
                    cause.printStackTrace()
                    delay(2000L * attempt)
                }
        }
        .catch { err ->
            // EMIT Error
            emit(
                when (err) {
                    else -> Result.Error.Exception(err.localizedMessage)
                }
            )
        }

    sealed class Result {
        object Loading : Result()

        data class Success(
            val data: ArtObjectCollection
        ) : Result()

        object Empty : Result()

        sealed class Error : Result() {
            data class Exception(val message: String?) : Error()
        }

    }

}