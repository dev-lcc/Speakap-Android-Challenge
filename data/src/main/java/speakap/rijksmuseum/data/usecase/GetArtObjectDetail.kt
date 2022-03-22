package speakap.rijksmuseum.data.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import speakap.rijksmueum.domain.datamodels.arts.ArtObjectDetail
import speakap.rijksmuseum.data.repository.arts.ArtsRepository
import speakap.rijksmuseum.datasource.remote.error.arts.ArtObjectNotFoundError

class GetArtObjectDetail(
    private val repository: ArtsRepository,
) {

    suspend operator fun invoke(
        objectNumber: String
    ): Flow<Result> = flow {

        // EMIT In-Progress
        emit(Result.Loading)

        val response = repository.getArtObjectDetail(
            objectNumber = objectNumber
        )

        // Emit Success
        emit(
            Result.Success(
                data = response
            )
        )
    }
        .retryWhen { cause, attempt ->
            // Attempt retry(at most 3x) when Network or I/O Error encountered
            (cause::class.java !in listOf(
                ArtObjectNotFoundError::class.java,
            ) && attempt < 3)
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
                    is ArtObjectNotFoundError -> Result.Error.ArtObjectNotFound
                    else -> Result.Error.Exception(err.localizedMessage)
                }
            )
        }

    sealed class Result {
        object Loading : Result()

        data class Success(
            val data: ArtObjectDetail
        ) : Result()

        sealed class Error : Result() {
            object ArtObjectNotFound : Error()
            data class Exception(val message: String?) : Error()
        }
    }

}