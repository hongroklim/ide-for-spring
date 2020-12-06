package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract Mock Object Template
 * <p>override only three method;
 * {@link #temp()}, {@link #createObjService(Object)},
 * {@link #getObjService(Object)}
 * 
 * <p>also can override {@link #createNthObj(int)} if needed
 * 
 * @param <T> DTO's Class to make Mock Object
 */
public abstract class AbstractMockObject<T> {
    
    //TODO extend the other Mock objects

    //list contains created mock object(S)
    private List<T> objList = new ArrayList<T>();

    /**
     * temporary created object (not inserted in database)
     * 
     * @return temp object
     */
    public abstract T temp();

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
        T obj = this.temp();
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
     * clear Mock Object list
     * 
     */
    protected void clear(){
        this.objList.clear();
    }

    /**
     * verify list
     * 
     */
    private void validatingList(){
        if(!this.isValidList()){
            this.clear();
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
     * create template of nth object
     * 
     * @param i nth index
     * @return
     */
    protected T tempNth(int i){
        return this.temp();
    }

    /**
     * create nth object
     * <p>this method can be overrided if needed like
     * <pre>
     * <code>
     * OrderProductDTO oProduct = new OrderProductDTO();
     *return this.createObjService(oProduct);
     * </code>
     * </pre>
     * 
     * @param index nth index
     * @return
     */
    private T createNthObj(int index){
        T temp = this.tempNth(index);
        return this.createObjService(temp);
    }

    /**
     * expand object list size until parameter
     * 
     * @param count tobe .size()
     */
    private void appendListUntil(int count){
        for(int i=this.objList.size(); i<count; i++){
            this.objList.add(this.createNthObj(i));
        }
    }

    /**
     * return any mock objects
     * 
     * @param count the number of items to be returned
     * @return mock objects
     */
    public List<T> anyList(int count){
        this.validatingList();
        this.appendListUntil(count);
        return this.objList.subList(0, count);
    }

}