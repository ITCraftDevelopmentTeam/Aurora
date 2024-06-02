package pixel.aurora.runtime.transformer

interface Transformer <T, R> {
    fun transform(input: T): R
}