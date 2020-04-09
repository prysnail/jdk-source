/*
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

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;
import java.util.Map;

/**
 * 相较于 Map，提供原子的putIfAbsent、remove、replace方法
 *
 * 内存一致性：memory visibility、happen-before
 *
 * @since 1.5
 * @author Doug Lea
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface ConcurrentMap<K, V> extends Map<K, V> {
    /**
     * 如果给定的key没有value关联，则添加键值对
     *
     * 等价于
     * <pre>
     *   if (!map.containsKey(key))
     *       return map.put(key, value);
     *   else
     *       return map.get(key);</pre>
     * 的原子实现.
     *
     */
    V putIfAbsent(K key, V value);

    /**
     * <pre>
     *   if (map.containsKey(key) &amp;&amp; map.get(key).equals(value)) {
     *       map.remove(key);
     *       return true;
     *   } else return false;</pre>
     * 的原子实现
     *
     */
    boolean remove(Object key, Object value);

    /**
     * <pre>
     *   if (map.containsKey(key) &amp;&amp; map.get(key).equals(oldValue)) {
     *       map.put(key, newValue);
     *       return true;
     *   } else return false;</pre>
     * 的原子实现
     */
    boolean replace(K key, V oldValue, V newValue);

    /**
     * <pre>
     *   if (map.containsKey(key)) {
     *       return map.put(key, value);
     *   } else return null;</pre>
     * 的原子实现
     */
    V replace(K key, V value);
}
