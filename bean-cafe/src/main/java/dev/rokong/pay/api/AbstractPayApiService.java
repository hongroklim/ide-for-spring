package dev.rokong.pay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.order.main.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;

public abstract class AbstractPayApiService {

    private final String PAY_NAME;  //API's name

    private final int PAY_TYPE_ID;  //API's payId in payType

    //API's endpoint (class)
    private class ApiUrl {
        private final String PREPARE;   //prepare payment
        private final String APPROVE;   //approve payment
        private final String STATUS;    //payment status

        private ApiUrl(String prepare, String approve, String status) {
            this.PREPARE = prepare;
            this.APPROVE = approve;
            this.STATUS = status;
        }
    }
    private final ApiUrl API_URL;   //API URL (instance)

    //redirect URL in beanCafe (static class)
    private static class REDIRECT_URL {
        private static final String CONTEXT_PATH = "http://rokong.dev/beanCafe";
        private static final String SUCCESS = CONTEXT_PATH+"/pay/api/complete";
        private static final String CANCEL = CONTEXT_PATH+"/pay/api/cancel";
        private static final String FAIL = CONTEXT_PATH+"/pay/api/fail";
    }

    protected AbstractPayApiService(int payTypeId, String payName,
                                    String prepareUrl, String approveUrl, String statusUrl) {
        this.PAY_TYPE_ID = payTypeId;
        this.PAY_NAME = payName;
        this.API_URL = new ApiUrl(prepareUrl, approveUrl, statusUrl);
    }

    protected PayApiDAO payApiDAO;

    @Autowired
    public final void setPayApiDAO(PayApiDAO payApiDAO){
        this.payApiDAO = payApiDAO;
    }

    //TODO make setter in final (not to be override)

    @Autowired
    protected OrderService orderService;

    @Autowired(required = false) @Qualifier("mvcMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    protected ObjectMapper objectMapper;

    /**
     * After <code>@Autowired</code>, extract objectMapper
     * into TossService from messageConverter
     */
    @PostConstruct
    public final void init() {
        if (messageConverter != null) {
            this.objectMapper = messageConverter.getObjectMapper();
        } else {
            //when dispatcher is not initialized
            this.objectMapper = new ObjectMapper();
        }
    }

    /**
     * get API's pay id
     *
     * @return pay id in pay_type
     */
    public int getPayTypeId() {
        return this.PAY_TYPE_ID;
    }

    /**
     * make request to API
     *
     * @param orderId to request order id
     * @return redirect URL
     */
    public String preparePay(int orderId) {
        return null;
    }

    public PayStatusDTO getPayStatus(int orderId) {
        String url = this.API_URL.PREPARE;
        url = REDIRECT_URL.SUCCESS;
        return null;
    }

    public void approvePay(int orderId, String ... param) {

    }
}
