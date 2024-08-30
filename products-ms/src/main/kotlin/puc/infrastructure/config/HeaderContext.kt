package puc.infrastructure.config

object HeaderContext {
    private val currentHeader = ThreadLocal<String>()

    fun setHeader(value: String?) {
        currentHeader.set(value)
    }

    fun getHeader(): String? {
        return currentHeader.get()
    }

    fun clear() {
        currentHeader.remove()
    }
}