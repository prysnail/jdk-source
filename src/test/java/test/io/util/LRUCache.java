package test.io.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description 基于LinkedHashMap实现LRU缓存
 * @author prysnail
 * @date 2020/3/30 21:34:00
 */
public class LRUCache {

    public static void main(String[]args){
        //缓存容量：4
        LRU<String,String> lru = new LRU<>(16,0.75f,4);
        lru.put("星期一","1");
        lru.put("星期二","2");
        lru.put("星期三","3");
        lru.put("星期四","4");
        lru.put("星期五","5");

        for(Map.Entry<String, String> entry : lru.entrySet()){
            System.out.println(entry.getKey());
        }
    }
}

class LRU<K,V> extends LinkedHashMap<K,V>{

    //缓存容量
    private int cacheSize;

    /**
     * @param cacheSize 缓存容量
     */
    public LRU(int capacity , float loadFactor , int cacheSize){
        super(capacity , loadFactor , true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        //删除超过缓存容量时，删除最老元素
        return size() > this.cacheSize;
    }
}
