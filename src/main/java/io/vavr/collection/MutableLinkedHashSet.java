package io.vavr.collection;


import io.vavr.collection.champ.AbstractChampSet;
import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.Enumerator;
import io.vavr.collection.champ.FailFastIterator;
import io.vavr.collection.champ.IdentityObject;
import io.vavr.collection.champ.IteratorFacade;
import io.vavr.collection.champ.KeySpliterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.NonNull;
import io.vavr.collection.champ.ReversedKeySpliterator;
import io.vavr.collection.champ.SequencedData;
import io.vavr.collection.champ.SequencedElement;
import io.vavr.collection.champ.SetSerializationProxy;

import java.io.Serial;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vavr.collection.champ.SequencedData.seqHash;


/**
 * Implements a mutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is mutable</li>
 *     <li>is not thread-safe</li>
 *     <li>iterates in the order, in which elements were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>add: O(1) amortized</li>
 *     <li>remove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toImmutable: O(1) + O(log N) distributed across subsequent updates in
 *     this set</li>
 *     <li>clone: O(1) + O(log N) distributed across subsequent updates in this
 *     set and in the clone</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(1) with bucket sort, O(log N) with heap sort</li>
 *     <li>getFirst, getLast: O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other sets, and nodes
 * that are exclusively owned by this set.
 * <p>
 * If a write operation is performed on an exclusively owned node, then this
 * set is allowed to mutate the node (mutate-on-write).
 * If a write operation is performed on a potentially shared node, then this
 * set is forced to create an exclusive copy of the node and of all not (yet)
 * exclusively owned parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1) in either
 * case.
 * <p>
 * This set can create an immutable copy of itself in O(1) time and O(1) space
 * using method {@link #toImmutable()}. This set loses exclusive ownership of
 * all its tree nodes.
 * Thus, creating an immutable copy increases the constant cost of
 * subsequent writes, until all shared nodes have been gradually replaced by
 * exclusively owned nodes again.
 * <p>
 * Insertion Order:
 * <p>
 * This set uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code add} is O(1) only in an amortized sense.
 * <p>
 * To support iteration, a second CHAMP trie is maintained. The second CHAMP
 * trie has the same contents as the first. However, we use the sequence number
 * for computing the hash code of an element.
 * <p>
 * In this implementation, a hash code has a length of
 * 32 bits, and is split up in little-endian order into 7 parts of
 * 5 bits (the last part contains the remaining bits).
 * <p>
 * We convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE
 * to it. And then we reorder its bits from
 * 66666555554444433333222221111100 to 00111112222233333444445555566666.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access this set concurrently, and at least
 * one of the threads modifies the set, it <em>must</em> be synchronized
 * externally.  This is typically accomplished by synchronizing on some
 * object that naturally encapsulates the set.
 * <p>
 * References:
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. BSD-2-Clause License</dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 * @param <E> the element type
 */
class MutableLinkedHashSet<E> extends AbstractChampSet<E, SequencedElement<E>> {
    @Serial
    private final static long serialVersionUID = 0L;

    /**
     * Counter for the sequence number of the last element. The counter is
     * incremented after a new entry is added to the end of the sequence.
     */
    private int last = 0;
    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement before a new entry is added to the start of the sequence.
     */
    private int first = 0;
    /**
     * The root of the CHAMP trie for the sequence numbers.
     */
    private @NonNull BitmapIndexedNode<SequencedElement<E>> sequenceRoot;

    /**
     * Constructs an empty set.
     */
    public MutableLinkedHashSet() {
        root = BitmapIndexedNode.emptyNode();
        sequenceRoot = BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a set containing the elements in the specified
     * {@link Iterable}.
     *
     * @param c an iterable
     */
    @SuppressWarnings("unchecked")
    public MutableLinkedHashSet(Iterable<? extends E> c) {
        if (c instanceof MutableLinkedHashSet<?>) {
            c = ((MutableLinkedHashSet<? extends E>) c).toImmutable();
        }
        if (c instanceof LinkedHashSet<?>) {
            LinkedHashSet<E> that = (LinkedHashSet<E>) c;
            this.root = that;
            this.size = that.size;
            this.first = that.first;
            this.last = that.last;
            this.sequenceRoot = that.sequenceRoot;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            this.sequenceRoot = BitmapIndexedNode.emptyNode();
            addAll(c);
        }
    }

    @Override
    public boolean add(final E e) {
        return addLast(e, false);
    }

    //@Override
    public void addFirst(E e) {
        addFirst(e, true);
    }



    private boolean addFirst(E e, boolean moveToFirst) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        SequencedElement<E> newElem = new SequencedElement<>(e, first);
        IdentityObject mutator = getOrCreateIdentity();
        root = root.update(mutator, newElem,
                Objects.hashCode(e), 0, details,
                moveToFirst ? getUpdateAndMoveToFirstFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            SequencedElement<E> oldElem = details.getOldData();
            boolean isReplaced = details.isReplaced();
            sequenceRoot = sequenceRoot.update(mutator,
                    newElem, seqHash(first), 0, details,
                    getUpdateFunction(),
                    SequencedData::seqEquals, SequencedData::seqHash);
            if (isReplaced) {
                sequenceRoot = sequenceRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        SequencedData::seqEquals);

                first = details.getOldData().getSequenceNumber() == first ? first : first - 1;
                last = details.getOldData().getSequenceNumber() == last ? last - 1 : last;
            } else {
                modCount++;
                first--;
                size++;
            }
            renumber();
        }
        return details.isModified();
    }

    //@Override
    public void addLast(E e) {
        addLast(e, true);
    }

    private boolean addLast(E e, boolean moveToLast) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        SequencedElement<E> newElem = new SequencedElement<>(e, last);
        IdentityObject mutator = getOrCreateIdentity();
        root = root.update(
                mutator, newElem, Objects.hashCode(e), 0,
                details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            SequencedElement<E> oldElem = details.getOldData();
            boolean isReplaced = details.isReplaced();
            sequenceRoot = sequenceRoot.update(mutator,
                    newElem, seqHash(last), 0, details,
                    getUpdateFunction(),
                    SequencedData::seqEquals, SequencedData::seqHash);
            if (isReplaced) {
                sequenceRoot = sequenceRoot.remove(mutator,
                        oldElem, seqHash(oldElem.getSequenceNumber()), 0, details,
                        SequencedData::seqEquals);

                first = details.getOldData().getSequenceNumber() == first - 1 ? first - 1 : first;
                last = details.getOldData().getSequenceNumber() == last ? last : last + 1;
            } else {
                modCount++;
                size++;
                last++;
            }
            renumber();
        }
        return details.isModified();
    }

    @Override
    public void clear() {
        root = BitmapIndexedNode.emptyNode();
        sequenceRoot = BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
        first = -1;
        last = 0;
    }

    /**
     * Returns a shallow copy of this set.
     */
    @Override
    public MutableLinkedHashSet<E> clone() {
        return (MutableLinkedHashSet<E>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return Node.NO_DATA != root.find(new SequencedElement<>((E) o),
                Objects.hashCode((E) o), 0, Objects::equals);
    }

    //@Override
    public E getFirst() {
        return Node.getFirst(sequenceRoot).getElement();
    }

    // @Override
    public E getLast() {
        return Node.getLast(sequenceRoot).getElement();
    }

    @Override
    public Iterator<E> iterator() {
        return iterator(false);
    }

    private @NonNull Iterator<E> iterator(boolean reversed) {
        Enumerator<E> i;
        if (reversed) {
            i = new ReversedKeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        } else {
            i = new KeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        }
        return new FailFastIterator<>(new IteratorFacade<>(i, this::iteratorRemove), () -> MutableLinkedHashSet.this.modCount);
    }

    private @NonNull Spliterator<E> spliterator(boolean reversed) {
        Spliterator<E> i;
        if (reversed) {
            i = new ReversedKeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        } else {
            i = new KeySpliterator<>(sequenceRoot, SequencedElement::getElement, Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED, size());
        }
        return i;
    }

    @Override
    public @NonNull Spliterator<E> spliterator() {
        return spliterator(false);
    }

    private void iteratorRemove(E element) {
        mutator = null;
        remove(element);
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        ChangeEvent<SequencedElement<E>> details = new ChangeEvent<>();
        IdentityObject mutator = getOrCreateIdentity();
        root = root.remove(
                mutator, new SequencedElement<>((E) o),
                Objects.hashCode(o), 0, details, Objects::equals);
        if (details.isModified()) {
            size--;
            modCount++;
            var elem = details.getOldData();
            int seq = elem.getSequenceNumber();
            sequenceRoot = sequenceRoot.remove(mutator,
                    elem,
                    seqHash(seq), 0, details, SequencedData::seqEquals);
            if (seq == last - 1) {
                last--;
            }
            if (seq == first) {
                first++;
            }
            renumber();
        }
        return details.isModified();
    }


    //@Override
    public E removeFirst() {
        SequencedElement<E> k = Node.getFirst(sequenceRoot);
        remove(k.getElement());
        return k.getElement();
    }

    //@Override
    public E removeLast() {
        SequencedElement<E> k = Node.getLast(sequenceRoot);
        remove(k.getElement());
        return k.getElement();
    }

    /**
     * Renumbers the sequence numbers if they have overflown.
     */
    private void renumber() {
        if (SequencedData.mustRenumber(size, first, last)) {
            IdentityObject mutator = getOrCreateIdentity();
            root = SequencedData.renumber(size, root, sequenceRoot, mutator,
                    Objects::hashCode, Objects::equals,
                    (e, seq) -> new SequencedElement<>(e.getElement(), seq));
            sequenceRoot = LinkedHashSet.buildSequenceRoot(root, mutator);
            last = size;
            first = -1;
        }
    }

    /**
     * Returns an immutable copy of this set.
     *
     * @return an immutable copy
     */
    public LinkedHashSet<E> toImmutable() {
        mutator = null;
        return size == 0 ? LinkedHashSet.of() : new LinkedHashSet<>(root, sequenceRoot, size, first, last);
    }

    @Serial
    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<E> extends SetSerializationProxy<E> {
        @Serial
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(Set<E> target) {
            super(target);
        }

        @Serial
        @Override
        protected Object readResolve() {
            return new MutableLinkedHashSet<>(deserialized);
        }
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateFunction() {
        return (oldK, newK) -> oldK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToLastFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }


    private BiFunction<SequencedElement<E>, SequencedElement<E>, SequencedElement<E>> getUpdateAndMoveToFirstFunction() {
        return (oldK, newK) -> oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    public <U> U transform(Function<? super io.vavr.collection.Set<Long>, ? extends U> f) {
        // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
        //     wants us to have a transform() method although this class
        //     is a standard Collection class.
        throw new UnsupportedOperationException();
    }

}