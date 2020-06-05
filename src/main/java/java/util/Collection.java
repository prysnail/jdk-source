/*
 * Copyright (c) 1997, 2010, Oracle and/or its affiliates. All rights reserved.
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
 *
 * 集合层次中的根接口，
 * This interface is typically used to pass collections around and manipulate them where
 * maximum generality is desired.
 *
 * 实现该接口的类应当提供两个标准构造器 ：
 * 1. 无参构造器
 * 2. 以Collection为参的构造器
 *
 * 添加非法元素时，根据具体实现，成功或
 * 抛出unchecked exception，typically <tt>NullPointerException</tt> or <tt>ClassCastException</tt>.
 *
 * 每个集合决定自己的同步策略
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Set
 * @see     List
 * @see     Map
 * @see     SortedSet
 * @see     SortedMap
 * @see     HashSet
 * @see     TreeSet
 * @see     ArrayList
 * @see     LinkedList
 * @see     Vector
 * @see     Collections
 * @see     Arrays
 * @see     AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     *
     */
    int size();


    boolean isEmpty();

    /**
     * @throws ClassCastException
     * @throws NullPointerException
     */
    boolean contains(Object o);

    /**
     * 不保证返回的顺序
     */
    Iterator<E> iterator();

    /**
     * 如果迭代器保证顺序，则该方法必须以相同顺序返回元素
     *
     * 返回的数组是安全的，该集合不维护该数组的引用
     *
     * 该方法是数组和集合的桥梁
     *
     */
    Object[] toArray();

    /**
     *
     * 数组空间 > 集合元素空间：多余空间补null
     *
     * 具备和<tt>toArray()</tt>一样的特性
     *
     * <p>Suppose <tt>x</tt> is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of <tt>String</tt>:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of this collection are to be
     *        stored, if it is big enough; otherwise, a new array of the same
     *        runtime type is allocated for this purpose.
     *
     * @throws ArrayStoreException 数组不是集合的超类型时
     * @throws NullPointerException 数组null时
     */
    <T> T[] toArray(T[] a);

    // Modification Operations

    /**
     *
     * <tt>true</tt>：集合改变
     * <tt>false</tt>：已经存在并且不允许重复
     *
     * 如果不是因已经存在元素而拒绝添加元素，必须抛出异常
     *
     * @throws UnsupportedOperationException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws IllegalArgumentException if some property of the element
     *         prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to insertion restrictions
     */
    boolean add(E e);

    /**
     * <tt>true</tt>：集合改变
     *
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws UnsupportedOperationException
     */
    boolean remove(Object o);


    // Bulk Operations


    boolean containsAll(Collection<?> c);


    boolean addAll(Collection<? extends E> c);


    boolean removeAll(Collection<?> c);


    boolean retainAll(Collection<?> c);


    void clear();


    // Comparison and hashing

    /**
     *
     *  如果需要值比较，则覆盖 <tt>Object.equals</tt>
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see List#equals(Object)
     */
    boolean equals(Object o);

    /**
     * 覆盖equals方法必须覆盖hashcode方法
     *
     * <tt>c1.equals(c2)</tt> implies that <tt>c1.hashCode()==c2.hashCode()</tt>.
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    int hashCode();
}
