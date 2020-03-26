/*
 * Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.util;
import java.io.*;

/**
 * LinkedHashMap提供有序的迭代（插入的顺序），他与HashMap的区别在于 通过双向链表维护元素。
 *
 * 与HashMap、HashTable相比，有序，避免排序
 * 与TreeMap相比，减少了额外成本
 *
 * 特别的构造函数  {@link #LinkedHashMap(int,float,boolean) 创建的LinkedHashMap
 * 的迭代顺序就是最好访问顺序，从最少访问到最大访问的顺序。这样的map很适合做LRU缓存。
 *
 * LinkedHashMap迭代时间与<i>size<i>成正比
 * HashMap迭代时间与<i>capacity<i>成正比
 *
 * 所以与HashMap不同的是，初始化容量较大的，capacity影响较小
 *
 * 和HashMap相同的是，都是非同步、快速失败
 *
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author  Josh Bloch
 * @see     Object#hashCode()
 * @see     Collection
 * @see     Map
 * @see     HashMap
 * @see     TreeMap
 * @see     Hashtable
 * @since   1.4
 *
 * @date 2020/01/21
 */

public class LinkedHashMap<K,V>
        extends HashMap<K,V>
        implements Map<K,V>
{

    private static final long serialVersionUID = 3801124242820219131L;

    /**
     *  双向链表头
     */
    private transient Entry<K,V> header;

    /**
     * 迭代顺序：
     *  <tt>true</tt> 访问顺序
     *  <tt>false</tt> 插入顺序
     */
    private final boolean accessOrder;


    public LinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }

    public LinkedHashMap(int initialCapacity) {
        super(initialCapacity);
        accessOrder = false;
    }

    public LinkedHashMap() {
        super();
        accessOrder = false;
    }

    public LinkedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
        accessOrder = false;
    }

    public LinkedHashMap(int initialCapacity,
                         float loadFactor,
                         boolean accessOrder) {
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;
    }

    /**
     * HashMap中的子类初始化钩子
     */
    @Override
    void init() {
        header = new Entry<>(-1, null, null, null);
        header.before = header.after = header;
    }

    /**
     * 因为性能问题而覆盖
     * 遍历双向链表，具有更高的性能
     */
    @Override
    void transfer(HashMap.Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e = header.after; e != header; e = e.after) {
            if (rehash)
                e.hash = (e.key == null) ? 0 : hash(e.key);
            int index = indexFor(e.hash, newCapacity);
            e.next = newTable[index];
            newTable[index] = e;
        }
    }


    /**
     * 遍历双链表来查找value
     */
    public boolean containsValue(Object value) {
        // Overridden to take advantage of faster iterator
        if (value==null) {
            for (Entry e = header.after; e != header; e = e.after)
                if (e.value==null)
                    return true;
        } else {
            for (Entry e = header.after; e != header; e = e.after)
                if (value.equals(e.value))
                    return true;
        }
        return false;
    }


    public V get(Object key) {
        Entry<K,V> e = (Entry<K,V>)getEntry(key);
        if (e == null)
            return null;
        //记录访问顺序
        e.recordAccess(this);
        return e.value;
    }

    /**
     * 删除所有元素
     */
    public void clear() {
        super.clear();
        header.before = header.after = header;
    }

    /**
     * LinkedHashMap entry.
     */
    private static class Entry<K,V> extends HashMap.Entry<K,V> {
        //双向链表
        Entry<K,V> before, after;

        Entry(int hash, K key, V value, HashMap.Entry<K,V> next) {
            super(hash, key, value, next);
        }

        private void remove() {
            before.after = after;
            after.before = before;
        }

        /**
         * 插入前驱节点
         */
        private void addBefore(Entry<K,V> existingEntry) {
            after  = existingEntry;
            before = existingEntry.before;
            before.after = this;
            after.before = this;
        }

        /**
         * Map.get、Map.set调用此方法
         * 如果按访问顺序（即：accessOrder为true),则将元素移动到list尾部
         * 按插入顺序，则不做任何
         */
        void recordAccess(HashMap<K,V> m) {
            LinkedHashMap<K,V> lm = (LinkedHashMap<K,V>)m;
            if (lm.accessOrder) {
                lm.modCount++;
                remove();
                addBefore(lm.header);
            }
        }

        void recordRemoval(HashMap<K,V> m) {
            remove();
        }
    }

    private abstract class LinkedHashIterator<T> implements Iterator<T> {
        Entry<K,V> nextEntry    = header.after;
        Entry<K,V> lastReturned = null;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return nextEntry != header;
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            LinkedHashMap.this.remove(lastReturned.key);
            lastReturned = null;
            expectedModCount = modCount;
        }

        Entry<K,V> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (nextEntry == header)
                throw new NoSuchElementException();

            Entry<K,V> e = lastReturned = nextEntry;
            nextEntry = e.after;
            return e;
        }
    }

    private class KeyIterator extends LinkedHashIterator<K> {
        public K next() { return nextEntry().getKey(); }
    }

    private class ValueIterator extends LinkedHashIterator<V> {
        public V next() { return nextEntry().value; }
    }

    private class EntryIterator extends LinkedHashIterator<Map.Entry<K,V>> {
        public Map.Entry<K,V> next() { return nextEntry(); }
    }

    // These Overrides alter the behavior of superclass view iterator() methods
    Iterator<K> newKeyIterator()   { return new KeyIterator();   }
    Iterator<V> newValueIterator() { return new ValueIterator(); }
    Iterator<Map.Entry<K,V>> newEntryIterator() { return new EntryIterator(); }

    /**
     * 覆盖addEntry，在此基础上删除最久的元素（如果允许）
     */
    void addEntry(int hash, K key, V value, int bucketIndex) {
        super.addEntry(hash, key, value, bucketIndex);

        // Remove eldest entry if instructed
        Entry<K,V> eldest = header.after;
        if (removeEldestEntry(eldest)) {
            removeEntryForKey(eldest.key);
        }
    }

    /**
     * 覆盖crateEntry，添加将新建元素维护到双向链表中
     */
    void createEntry(int hash, K key, V value, int bucketIndex) {
        HashMap.Entry<K,V> old = table[bucketIndex];
        Entry<K,V> e = new Entry<>(hash, key, value, old);
        table[bucketIndex] = e;
        //新建元素维护到双向链表中
        e.addBefore(header);
        size++;
    }

    /**
     *
     * true：删除最久的元素，被 put/putAll方法调用。当用于缓存时，非常有用：删除旧元素控制内存消耗
     * 如：
     * <pre>
     *     private static final int MAX_ENTRIES = 100;
     *
     *     protected boolean removeEldestEntry(Map.Entry eldest) {
     *        return size() > MAX_ENTRIES;
     *     }
     * </pre>
     */
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return false;
    }
}
