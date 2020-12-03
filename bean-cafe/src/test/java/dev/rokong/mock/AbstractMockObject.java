package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract Mock Object Template
 * <p>override only three method;
 * {@link #tempObj()}, {@link #createObjService(Object)},
 * {@link #getObjService(Object)}
 * <p>extends like <code>public class someting extends AbstractMockObject&lt;PayTypeDTO&gt;</code>
 * @param <T> Object Class
 */
public abstract class AbstractMockObject<T> {
    
    //list contains created mock object(S)
    private List<T> objList = new ArrayList<T>();

    /**
     * temporary created object (not inserted in database)
     * 
     * @return temp object
     */
    public abstract T tempObj();

    /**
     * service method which create object
     * 
     * @param obj to be created
     * @return created object
     */
    protected abstract T createObjService(T obj);

    /**
     * service method which get object
     * 
     * @param obj to be inquried
     * @return inquried object
     */
    protected abstract T getObjService(T obj);

    /**
     * create one object then inserted into database
     * 
     * @return
     */
    private T createObj(){
        T obj = this.tempObj();
        return this.createObjService(obj);
    }

    /**
     * confirm list is actually exist in database
     * 
     * @return if true, list is valid
     */
    private boolean isValidList(){
        if(this.objList.size() == 0){
            return true;
        }else{
            return this.getObjService(this.objList.get(0)) != null;
        }
    }

    /**
     * verify list
     * 
     */
    private void validatingList(){
        if(!this.isValidList()){
            this.objList.clear();
        }
    }

    /**
     * return any mock object
     * 
     * @return mock object
     */
    public T any(){
        this.validatingList();

        if(this.objList.size() == 0){
            this.objList.add(this.createObj());
        }

        return this.objList.get(0);
    }

    /**
     * return any mock objects
     * 
     * @param count the number of items to be returned
     * @return mock objects
     */
    public List<T> anyList(int count){
        this.validatingList();

        while(this.objList.size() < count){
            this.objList.add(this.createObj());
        }

        return this.objList.subList(0, count);
    }

}