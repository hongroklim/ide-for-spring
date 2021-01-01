package dev.rokong.pay.api;

import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value="pay/api", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Pay API"})
public class PayApiController {

    @Autowired
    private OrderService orderService;

    @Autowired @Qualifier("tossService")
    private PayApiService tossService;

    @Autowired @Qualifier("kakaoPayService")
    private PayApiService kakaoPayService;

    @RequestMapping(value="/{orderId}", method= RequestMethod.POST)
    public String requestPayment(@PathVariable int orderId){
        return (String) this.channelingApi("requestPay", orderId);
    }

    @RequestMapping(value="/{orderId}/status", method=RequestMethod.GET)
    public PayStatusDTO paymentStatus(@PathVariable int orderId){
        return (PayStatusDTO) this.channelingApi("getPayStatus", orderId);
    }

    @RequestMapping(value="/{orderId}/approve", method={RequestMethod.GET, RequestMethod.POST})
    public void approvePayment(@PathVariable int orderId,
                               @RequestParam(required=false, defaultValue="") String pg_token){
        Map<String, Object> map = new HashMap<>();
        if(ObjUtil.isNotEmpty(pg_token)){
            map.put("pg_token", pg_token);
        }
        this.channelingApi("approvePay", orderId, map);
    }

    /**
     * make Class array from Object array
     * this method is used by class.getMethod(String, Class ...)
     *
     * @param objects object array
     * @return class array
     */
    private Class[] objectToClass(Object[] objects){
        if(objects == null || objects.length==0){
            //if parameter is empty
            return null;
        }else{
            //Object.getClass() into Class
            Class[] classes = new Class[objects.length];
            for(int i=1; i<objects.length; i++){
                //TODO can not detect primitive class
                classes[i] = objects[i].getClass();
            }
            return classes;
        }
    }

    /**
     * find appropriate api service by pay type id
     *
     * @param payTypeId to find pay type id
     * @return payApiService field
     */
    private PayApiService findApiService(int payTypeId){
        //extract method
        Method getPayTypeId = null;
        String methodName = "getPayTypeId";

        try {
            getPayTypeId = PayApiService.class.getMethod(methodName, null);
        } catch (NoSuchMethodException e) {
            log.debug("method name : {}", methodName, e);
            throw new RuntimeException("can not get method");
        }

        PayApiService payApiService = null;
        //from controller's fields
        for(Field f : this.getClass().getDeclaredFields()){
            f.setAccessible(true);

            //get only PayApiService
            if(f.getType().equals(PayApiService.class)){
                //get field from this controller
                try {
                    payApiService = (PayApiService) f.get(this);
                } catch (IllegalAccessException e) {
                    log.debug("field : {}", f.toString(), e);
                    continue;
                }

                //invoke method
                int servicePayTypeId = 0;
                try {
                    servicePayTypeId = (int) getPayTypeId.invoke(payApiService);
                } catch (ReflectiveOperationException e) {
                    log.debug("field : {}", f.toString(), e);
                    continue;
                }

                //if payTypeId from service equals to parameter
                if(payTypeId == servicePayTypeId){
                    return payApiService;
                }
            }
        }

        log.debug("payTypeId : {}", payTypeId);
        throw new RuntimeException("no appropriate PayApiService exists");
    }

    /**
     * get appropriate api service field by order id
     *
     * @param orderId order id to find
     * @return payApiService field
     */
    private PayApiService getApiService(int orderId){
        //get order
        OrderDTO order = orderService.getOrderNotNull(orderId);
        int payTypeId = order.getPayId();

        //find api service by payTypeId
        return this.findApiService(payTypeId);
    }

    private Method getMethod(String methodName, Object ... parameters){
        Method method = null;

        //first, find method by name
        for(Method m : PayApiService.class.getDeclaredMethods()){
            //get method which name is equals
            if(m.getName().equals(methodName)){
                if(method == null){
                    method = m;
                } else {
                    //if method name is duplicate
                    method = null;
                    break;
                }
            }
        }

        //if method name is duplicate
        if(method == null){
            try {
                method = PayApiService.class.getMethod(methodName, this.objectToClass(parameters));
            } catch (NoSuchMethodException e) {
                log.debug("method name : {}, parameters : {}", methodName, parameters);
            }
        }

        if(method == null){
            throw new RuntimeException("can not find method :" + methodName);
        }

        return method;
    }

    /**
     * invoke appropriate api service per order.
     * only {@link PayApiService} type fields in this class detected.
     * first parameter should be orderId
     *
     * @param methodName method in {@link PayApiService}
     * @param parameters parameters passed to method
     * @return return Object from method
     */
    private Object channelingApi(String methodName, Object ... parameters){
        //verify parameters
        if(parameters == null || parameters.length < 1){
            if(parameters != null){
                log.debug("parameters : {}", parameters.toString());
            }
            throw new RuntimeException("parameters is not enough");
        }

        int orderId = (int) parameters[0];

        //get method
        Method method = this.getMethod(methodName, parameters);

        //get field
        PayApiService payApiService = this.getApiService(orderId);

        //invoke
        Object result = null;
        try {
            result = method.invoke(payApiService, parameters);
        } catch (ReflectiveOperationException e) {
            log.debug("method : {}, field : {}",
                    method.toString(), payApiService.toString());
            throw new RuntimeException("can not invoke method in field", e);
        }

        return result;
    }
}