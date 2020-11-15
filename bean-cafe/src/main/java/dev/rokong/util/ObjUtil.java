package dev.rokong.util;

import java.util.List;

/**
 * util of List
 * @see java.util.List
 */
public class ObjUtil {
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
        return (T) list.get(randomIndex(list.size()));
    }

    /**
     * check List<?> is empty
     * @param list to be verified List
     * @return if return true, parameter is empty
     */
    public static boolean isEmpty(List<?> list){
        return (list==null || list.size()==0) ? true : false;
    }

    /**
     * check List<?> is empty
     * @param list to be verified List
     * @return if return true, parameter is not empty
     * @see {@link #isEmpty(List)}
     */
    public static boolean isNotEmpty(List<?> list){
        return !isEmpty(list);
    }

    /**
     * check String is empty
     * 
     * @param string to be verified string
     * @return if return true, parameter is empty
     */
    public static boolean isEmpty(String string){
        return string == null || "".equals(string);
    }

    /**
     * check String is not empty
     * 
     * @param string to be verified string
     * @return if return true, parameter is not empty
     * @see {@link #isEmpty(String)}
     */
    public static boolean isNotEmpty(String string){
        return !isEmpty(string);
    }

    /**
     * check Integer is defined (not null and not 0)
     * @param integer to be verified Integer
     * @return if return true, parameter is defined
     */
    public static boolean isDefined(Integer integer){
        return integer != null && integer != 0;
    }

    /**
     * check Integer is defined (is null and is 0)
     * @param integer to be verified Integer
     * @return if return true, parameter is not defined
     */
    public static boolean isNotDefined(Integer integer){
        return !isDefined(integer);
    }
}