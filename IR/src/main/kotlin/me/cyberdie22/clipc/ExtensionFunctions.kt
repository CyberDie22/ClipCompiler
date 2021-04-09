package me.cyberdie22.clipc

import me.cyberdie22.clipc.ir.lexer.SyntaxToken
import java.util.stream.Stream
import kotlin.streams.toList

fun <T> MutableList<T>.toImmutableList(): List<T> {
    return this.stream().toList()
}

inline fun <reified T> Stream<T>.toTypedArray(): Array<T> {
    return this.toList().toTypedArray()
}

fun <T> Stream<T>.filter(toFilterOut: List<Any>): Stream<T> {
    var stream: Stream<T> = this
    toFilterOut.forEach { filter ->
        stream = stream.filter {
            if (it is SyntaxToken)
                it.kind == filter
            else
                it == filter
        }
    }
    return stream
}

fun <T> Stream<T>.filter(toFilterOut: Array<Any>): Any {
    var stream: Stream<T> = this
    toFilterOut.forEach { filter ->
        stream = stream.filter {
            if (it is SyntaxToken)
                it.kind == filter
            else
                it == filter
        }
    }
    return stream
}

fun <T> Stream<T>.filter(toFilterOut: ArrayList<Any>): Any {
    var stream: Stream<T> = this
    toFilterOut.forEach { filter ->
        stream = stream.filter {
            if (it is SyntaxToken)
                it.kind == filter
            else
                it == filter
        }
    }
    return stream
}