package pixel.aurora.tokenizer

import com.google.gson.GsonBuilder
import pixel.aurora.Aurora
import pixel.aurora.parser.BaseParseException
import pixel.aurora.parser.TokenizerParseException
import java.net.URI
import java.nio.CharBuffer

class Tokenizer(private val buffer: CharBuffer, private val uri: URI = Aurora.blankURI) {

    private val gson = GsonBuilder().create()

    fun getBuffer() = buffer
    fun getURI() = uri

    fun reset() = this.also {
        buffer.position(0)
    }

    fun makeError(message: String) = TokenizerParseException(message, buffer, uri, this)

    fun tokenize(): Sequence<Token> = sequence {
        var space = ""
        while (true) {
            if (buffer.isEmpty()) break
            val character = buffer.get()
            if (character.isWhitespace() || character in "\n\r") {
                space += character
                continue
            }
            if (space.isNotEmpty()) yield(TokenType.SPACE.token(space))
            space = ""
            buffer.position(buffer.position() - 1)
            if (character.isJavaIdentifierStart()) yield(lexIdentifier())
            else if (character == '"') yield(lexString())
            else if (isNumeric()) yield(lexNumeric())
            else if (character in TokenHelper.punctuations) yield(TokenType.PUNCTUATION.token(buffer.get().toString()))
            else throw makeError("Invalid syntax")
        }
    }

    fun lexNumeric(): Token {
        return if (buffer.startsWith("0x") || buffer.startsWith("0X")) lexHexNumber()
        else if (buffer.startsWith("0b") || buffer.startsWith("0B")) lexBinaryNumber()
        else if (buffer.startsWith("0o") || buffer.startsWith("0O")) lexOctalNumber()
        else lexDecimalNumber()
    }

    fun lexDecimalNumber(): Token {
        var raw = ""
        var character: Char
        while (true) {
            try {
                character = buffer.get()
            } catch (_: Throwable) {
                break
            }
            if (!(character.isDigit() || (character == '.' && '.' !in raw) || (character.lowercase() == "e" && 'e' !in raw.lowercase()) || (character in "+-" && (raw.last().lowercase() == "e")))) {
                buffer.position(buffer.position() - 1)
                break
            }
            raw += character
        }
        if (raw.isEmpty()) throw makeError("Invalid or unexpected token")
        return NumericToken(raw, raw.toBigDecimal())
    }

    fun lexOctalNumber(): Token {
        buffer.position(buffer.position() + 2)
        var raw = "0o"
        var character: Char
        while (true) {
            try {
                character = buffer.get()
            } catch (_: Throwable) {
                break
            }
            if (!(character.isDigit() && character !in "89")) {
                buffer.position(buffer.position() - 1)
                break
            }
            raw += character
        }
        if (raw.length == 2) throw makeError("Invalid or unexpected token")
        return NumericToken(raw, raw.slice(2 until raw.length).toBigInteger(8).toBigDecimal())
    }

    fun lexBinaryNumber(): Token {
        buffer.position(buffer.position() + 2)
        var raw = "0b"
        var character: Char
        while (true) {
            try {
                character = buffer.get()
            } catch (_: Throwable) {
                break
            }
            if (character !in "01") {
                buffer.position(buffer.position() - 1)
                break
            }
            raw += character
        }
        if (raw.length == 2) throw makeError("Invalid or unexpected token")
        return NumericToken(raw, raw.slice(2 until raw.length).toBigInteger(2).toBigDecimal())
    }

    fun lexHexNumber(): Token {
        buffer.position(buffer.position() + 2)
        var raw = "0x"
        var character: Char
        while (true) {
            try {
                character = buffer.get()
            } catch (_: Throwable) {
                break
            }
            if (!(character.isDigit() || character in 'a' .. 'f' || character in 'A' .. 'F')) {
                buffer.position(buffer.position() - 1)
                break
            }
            raw += character
        }
        if (raw.length == 2) throw makeError("Invalid or unexpected token")
        return NumericToken(raw, raw.slice(2 until raw.length).toBigInteger(16).toBigDecimal())
    }

    fun isNumeric(): Boolean {
        val character = buffer.get(buffer.position())
        if (character.isDigit()) return true
        if (character == '.') {
            try {
                if (buffer.get(buffer.position() + 1).isDigit()) return true
            } catch (_: Throwable) {}
        }
        return false
    }

    fun lexString(): Token = runCatching {
        var raw = buffer.get().toString()
        while (buffer.get(buffer.position()) != '"') {
            if (buffer.get(buffer.position()) == '\\') {
                raw += buffer.get()
            }
            raw += buffer.get()
        }
        raw += buffer.get()
        try {
            return@runCatching StringToken(raw, gson.fromJson(raw, String::class.java))
        } catch (_: Throwable) {
            throw makeError("Invalid string literal: $raw")
        }
    }.let {
        if (it.isFailure && it.exceptionOrNull() !is BaseParseException) throw makeError("Invalid or unexpected token")
        else if (it.isFailure) throw it.exceptionOrNull()!!
        else it.getOrThrow()
    }

    fun lexIdentifier(): Token {
        var identifier = "${buffer.get()}"
        try {
            while (true) {
                val character = buffer.get()
                if (character.isJavaIdentifierPart()) identifier += character
                else {
                    buffer.position(buffer.position() - 1)
                    break
                }
            }
        } catch (_: Throwable) {}
        return when (identifier) {
            "null" -> TokenType.NULL
            "true", "false" -> TokenType.BOOLEAN
            else -> TokenType.IDENTIFIER
        }.token(identifier)
    }

}