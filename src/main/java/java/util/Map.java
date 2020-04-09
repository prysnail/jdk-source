/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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

/**
 * 键值映射对象，map不能包含重复keys，每个key最多包含一个值。
 *
 * <tt>map</tt> 接口提供三种集合试图：
 *  1. set of keys
 *  2. collection of values
 *  3. set of key-value mappings
 *
 * 有些map有序，如<tt>TreeMap</tt>;有的无序，如<tt>HashMap</tt>
 *
 * 注：当心可变对象作为key，禁止把map自身作为key，但作为value可以。
 *
 * hashCode()不等则两个对象一定不等

 * @author  Josh Bloch
 * @see HashMap
 * @see TreeMap
 * @see Hashtable
 * @see SortedMap
 * @see Collection
 * @see Set
 * @since 1.2
 *
 * @date 2020/4/9 22:05
 */
public interface Map<K,V> {

    // Query Operations

    int size();

    boolean isEmpty();

    /**
     * key==null ? k==null : key.equals(k)
     */
    boolean containsKey(Object key);

    /**
     * value==null ? v==null : value.equals(v)
     */
    boolean containsValue(Object value);

    V get(Object key);

    // Modification Operations

    V put(K key, V value);

    V remove(Object key);


    // Bulk Operations

    void putAll(Map<? extends K, ? extends V> m);

    void clear();


    // Views

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();


    interface Entry<K,V> {

        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }

    // Comparison and hashing


    boolean equals(Object o);

    /**
     *the sum of the hash codes of each entry in the map's
     * <tt>entrySet()</tt> view
     */
    int hashCode();

}
