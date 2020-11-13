package dev.rokong.util;

import java.util.List;

/**
 * util of List
 * @see java.util.List
 */
public class ListUtil {
    /**
     * return random index of list
     * 
     * @param size list.size()
     * @return random index
     */
    public static int randomIndex(int size){
        double d = Math.random();
        int i = (int) d*100;

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
     * @return if return true, it is not empty
     */
    public static boolean isEmpty(List<?> list){
        return (list==null || list.size()==0) ? true : false;
    }

    /**
     * check List<?> is empty
     * @param list to be verified List
     * @return if return true, it is not empty
     * @see {@link #isEmpty(List)}
     */
    public static boolean isNotEmpty(List<?> list){
        return !isEmpty(list);
    }
}