package pixel.aurora.data

import pixel.aurora.type.Identifier

class Variable (private val identifier: Identifier, private var value: Any? = null) {

    fun getIdentifier() = identifier
    fun getValue() = value
    fun setValue(value: Any?) { this.value = value }

    override fun toString() = "Variable($identifier)[$value]"

}