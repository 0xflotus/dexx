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

package com.github.andrewoma.dexx.collection;

import com.github.andrewoma.dexx.collection.internal.base.AbstractList;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ConsList<E> extends AbstractList<E> implements LinkedList<E> {
    private static ConsList<Object> EMPTY = new Nil<Object>();

    @NotNull
    public static <E> BuilderFactory<E, ConsList<E>> factory() {
        return new BuilderFactory<E, ConsList<E>>() {
            @NotNull
            @Override
            public Builder<E, ConsList<E>> newBuilder() {
                // TODO ... sort out the contract for builders.
                // Should they be build once? Thread safe?
                return new AbstractBuilder<E, ConsList<E>>() {
                    private Vector<E> buffer = Vector.empty();

                    @NotNull
                    @Override
                    public Builder<E, ConsList<E>> add(E element) {
                        buffer = buffer.append(element);
                        return this;
                    }

                    @NotNull
                    @Override
                    public ConsList<E> build() {
                        ConsList<E> result = ConsList.empty();
                        for (int i = buffer.size() - 1; i >= 0; i--) {
                            result = result.prepend(buffer.get(i));
                        }
                        return result;
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> ConsList<A> empty() {
        return (ConsList<A>) EMPTY;
    }

    @NotNull
    @Override
    public ConsList<E> prepend(E elem) {
        return new Cons<E>(elem, this);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private ConsList<E> current = ConsList.this;

            @Override
            public boolean hasNext() {
                return !(current instanceof Nil);
            }

            @Override
            public E next() {
                if (current instanceof Nil) {
                    throw new NoSuchElementException("Empty list");
                }
                Cons<E> cons = (Cons<E>) current;
                current = cons.tail();
                return cons.first();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

/**
 * Nil is the empty list
 */
class Nil<E> extends ConsList<E> {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    @Nullable
    public E first() {
        return null;
    }

    @Override
    @NotNull
    public ConsList<E> tail() {
        return this; // Scala throws UnsupportedOperationException. Should this?
    }

    @NotNull
    @Override
    public ConsList<E> set(int i, E elem) {
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    @Override
    public ConsList<E> append(E elem) {
        return new Cons<E>(elem, this);
    }

    @NotNull
    @Override
    public ConsList<E> drop(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> take(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Nullable
    @Override
    public E last() {
        return null;
    }
}

/**
 * Cons constructs a new list by prepending a new element to an existing list
 */
class Cons<E> extends ConsList<E> {
    private E head;
    private ConsList<E> tail;


    Cons(E head, ConsList<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nullable
    public E first() {
        return head;
    }

    @Override
    @NotNull
    public ConsList<E> tail() {
        return tail;
    }

    @NotNull
    @Override
    public ConsList<E> set(int i, E elem) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> append(E elem) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> drop(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> take(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ConsList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int i) {
        int count = 0;
        for (E e : this) {
            if (i == count++) {
                return e;
            }
        }

        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Nullable
    @Override
    public E last() {
        E last = null;
        for (E e : this) {
            last = e;
        }
        return last;
    }
}