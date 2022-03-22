package speakap.rijksmuseum.datasource.remote.error.arts

class ArtObjectNotFoundError(
    override val message: String?
): Throwable(message)