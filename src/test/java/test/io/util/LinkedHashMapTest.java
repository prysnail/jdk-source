package test.io.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {

    public static void main(String[]args){
        LinkedHashMapTest test = new LinkedHashMapTest();

        //测试1：插入顺序遍历
        Map map = test.init(16 , 0.75f , false);
        test.traverse(map);

        //测试2：访问顺序遍历
        Map accessMap = test.init(16 , 0.75f , true);
        accessMap.get("星期三");
        accessMap.get("星期一");
        test.traverse(accessMap);
    }

    public Map init(int initialCapacity,
                    float loadFactor,
                    boolean accessOrder){
        Map map = new LinkedHashMap(initialCapacity , loadFactor , accessOrder);
        map.put("星期一","1");
        map.put("星期二","2");
        map.put("星期三","3");
        map.put("星期四","4");
        map.put("星期五","5");
        map.put("星期六","6");
        map.put("星期天","7");

        return map;
    }

    /**
     * 遍历
     * @param map
     */
    public void traverse(Map<String,String> map){

        for(Map.Entry<String, String> entry : map.entrySet()){
            if (entry.getKey().equals("星期四")) break;
            System.out.println(entry.getKey());
        }
    }
}
