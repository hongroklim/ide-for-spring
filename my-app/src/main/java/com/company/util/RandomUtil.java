package com.company.util;

import java.util.List;
import java.util.UUID;

public class RandomUtil {
    /**
     * return random string
     * 
     * @param length string's length
     * @return random string
     */
    public static String randomString(int length){
        UUID u = UUID.randomUUID();
        String s = u.toString();
        s = s.replaceAll("[^A-Za-z]+", "");

        while(s.length() < length){
            u = UUID.randomUUID();
            s += u.toString().replaceAll("[^A-Za-z]+", "");
        }
        
        return s.substring(0, length);
    }

    /**
     * return random string which length is 4
     * 
     * @return {@link #randomString(int)}
     */
    public static String randomString(){
        return randomString(4);
    }

    /**
     * return random integer
     * 
     * @param length integer's length
     * @return random integer
     */
    public static int randomInt(int length){
        double result = Math.random();
        int i = (int) (result * (Math.pow(10, length+1)));
        return (int) (i % (Math.pow(10, length)));
    }

    /**
     * return random integer which length is 4
     * 
     * @return {@link #randomInt(int)}
     */
    public static int randomInt(){
        return randomInt(4);
    }
    
    /**
     * return random index of list
     * 
     * @param size list.size()
     * @return random index
     */
    public static int randomIndex(int size){
        double d = Math.random();
        int i = (int) d*10000;

        return i % size;
    }

    /**
     * return random index of list
     * 
     * @param list search target
     * @return {@link #randomIndex(int)}
     */
    public static int randomIndex(List<?> list){
        return randomIndex(list.size());
    }

    /**
     * return random item in list
     * 
     * @param <T> type of list items
     * @param list search list
     * @return random item
     * 
     * @see {@link #randomIndex(List)}
     */
    public static <T> T randomItem(List<T> list) {
        if(ObjUtil.isEmpty(list)){
            throw new IllegalArgumentException("list is empty");
        }

        return (T) list.get(randomIndex(list.size()));
    }

    /**
     * return random item in array
     * 
     * @param <T> type of array items
     * @param array search array
     * @return random item
     */
    public static <T> T randomItem(T[] array){
        if(ObjUtil.isEmpty(array)){
            throw new IllegalArgumentException("array is empty");
        }

        return (T) array[randomIndex(array.length)];
    }
}