/*
 * Copyright (c) 2015 Andrew O'Malley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.andrewoma.dexx.kollection

import com.github.andrewoma.dexx.collection.HashSet
import com.github.andrewoma.dexx.collection.TreeSet
import java.util.*
import com.github.andrewoma.dexx.collection.Set as DSet

interface ImmutableSet<E : Any> : Set<E> {

    operator fun plus(value: E): ImmutableSet<E>

    operator fun plus(values: Iterable<E>): ImmutableSet<E>

    operator fun minus(value: E): ImmutableSet<E>

    operator fun minus(values: Iterable<E>): ImmutableSet<E>
}

internal class ImmutableSetAdapter<E : Any>(val underlying: DSet<E>) : ImmutableSet<E> {

    override val size: Int
        get() = underlying.size()

    override fun isEmpty() = underlying.isEmpty

    override fun contains(element: E) = underlying.contains(element)

    override fun iterator(): Iterator<E> = underlying.iterator()

    override fun containsAll(elements: Collection<E>) = underlying.asSet().containsAll(elements)

    override fun plus(value: E) = ImmutableSetAdapter(underlying.add(value))

    override fun plus(values: Iterable<E>) = ImmutableSetAdapter(values.fold(underlying) { r, e -> r.add(e) })

    override fun minus(value: E) = ImmutableSetAdapter(underlying.remove(value))

    override fun minus(values: Iterable<E>): ImmutableSet<E> = ImmutableSetAdapter(values.fold(underlying) { r, e -> r.remove(e) })

    override fun toString() = this.joinToString(", ", "ImmutableSet(", ")")

    override fun equals(other: Any?) = this === other || (other is Set<*> && this.size == other.size && this.containsAll(other))

    override fun hashCode(): Int = underlying.hashCode()
}

fun <E : Any> immutableSetOf(vararg elements: E): ImmutableSet<E> =
        ImmutableSetAdapter(elements.fold(HashSet.empty<E>()) { r, e -> r.add(e) })

fun <E : Any> Iterable<E>.toImmutableSet() = immutableSetOf<E>() + this

fun <E : Comparable<E>> immutableSortedSetOf(vararg elements: E): ImmutableSet<E> =
        ImmutableSetAdapter(elements.fold(TreeSet.empty<E>()) { r, e -> r.add(e) })

fun <E : Any> immutableCustomSortedSetOf(selector: (E) -> Comparable<*>?, vararg elements: E): ImmutableSet<E> {
    val ordering = Comparator<E> { e1, e2 -> compareValuesBy(e1, e2, selector) }
    return ImmutableSetAdapter(elements.fold(TreeSet(ordering)) { r, e -> r.add(e) })
}

fun <E : Comparable<E>> Iterable<E>.toImmutableSortedSet() = immutableSortedSetOf<E>() + this

fun <E : Any> Iterable<E>.toImmutableSortedSet(selector: (E) -> Comparable<*>?) = immutableCustomSortedSetOf<E>(selector) + this

