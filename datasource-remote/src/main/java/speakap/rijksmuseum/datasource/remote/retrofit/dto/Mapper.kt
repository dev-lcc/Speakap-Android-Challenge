package speakap.rijksmuseum.datasource.remote.retrofit.dto

interface Mapper<I, O> {
    fun map(input: I): O
}