/*
 * Copyright (c) 2014 Andrew O'Malley
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

package com.github.andrewoma.dexx.collection

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.junit.Test as test
import java.util.NoSuchElementException

abstract class AbstractListTest() : AbstractIterableTest() {

    private fun build<T>(vararg ts: T) = build_(*ts) as List<T>

    fun <T> List<T>.klist() : java.util.ArrayList<T> {
        val klist = arrayListOf<T>()
        for (i in this) {
            klist.add(i)
        }
        return klist
    }

    test fun insertOrderPreserved() {
        assertEquals(listOf(1, 3, 2), build(1, 3, 2).klist())
    }

    test fun duplicatesPreserved() {
        assertEquals(listOf(1, 2, 2, 1), build(1, 2, 2, 1).klist())
    }

    test fun indexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(1, list.indexOf(2))
        assertEquals(-1, list.indexOf(3))
    }

    test fun lastIndexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(2, list.lastIndexOf(2))
        assertEquals(-1, list.lastIndexOf(3))
    }

    test fun usingNulls() {
        assertEquals(listOf(1, null, 2, null), build(1, null, 2, null).klist())
    }
}
