package dev.rokong.annotation;

/**
 * Order Status in order process.
 * <p>if <code>code > 0</code>, it is normal process.
 * else <code>code < 0</code>, it is canceled status.
 * <p>especially, if code like <code>-100 ~</code>, it is canceled by customer.
 * if code like <code>-200 ~</code>, it is canceled by seller.
 * 
 */
public enum OrderStatus {
    //normal process
    WRITING(100), //주문 작성 중
    //작성완료
    PAYMENT(200), //결재 중
        PAYMENT_STANDBY(210),   //결재 대기 중
        //결재생성요청 완료
        PAYMENT_PROGRESS(220),  //결재 진행 중
        //사용자 결재 완료
    //결재
    CHECKING(300), //주문 확인 중
        CHECK_COMPLETE(310),    //확인 완료
    //확인완료
    PRODUCT_READY(400), //상품 준비 중
    //준비완료
    DELIVERING(500), //배송중
    //배송완료
    COMPLETE(600),
    
    //canceled status
    CUSTOMER_CANCEL(-100), //고객요청에 의해 취소
    
    CANCELED_WRITE(-110),
    CANCELED_PAYMENT(-120),
    CANCELED_CHECK(-130),
    CANCELED_PRODUCT(-140),

    SELLER_CANCEL(-200), //판매자에 의해 취소

    CANCEL_CHECK(-230),
    CANCEL_PRODUCT(-240),
    CANCEL_DELIVER(-250);

    private int code;

    OrderStatus(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public boolean isProcess(){
        return this.code > 0;
    }

    public boolean isCanceled(){
        return !isProcess();
    }

    /**
     * compare sequence between process
     * 
     * @param orderStatus to compare one
     * @return return true, this precedes before compare one
     * @throws UnsupportedOperationException canceled status tries this method
     * @see {@link #isLatterThan(OrderStatus)}
     */
    public boolean isFormerThan(OrderStatus orderStatus){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }else if(!orderStatus.isProcess()){
            throw new IllegalArgumentException("only normal process parameter supported");
        }

        if(this == orderStatus){
            return false;
        }else{
            return this.getCode() < orderStatus.getCode();
        }
    }

    /**
     * compare sequence between process
     * 
     * @param orderStatus to compare one
     * @return return true, this follows after compare one
     * @throws UnsupportedOperationException canceled status tries this method
     * @see {@link #isFormerThan(OrderStatus)}
     */
    public boolean isLatterThan(OrderStatus orderStatus){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }else if(!orderStatus.isProcess()){
            throw new IllegalArgumentException("only normal process parameter supported");
        }
        
        if(this == orderStatus){
            return false;
        }else{
            return !this.isFormerThan(orderStatus);
        }
    }

    /**
     * get next process
     * 
     * @return next process
     * @throws UnsupportedOperationException canceled status tries this method
     */
    public OrderStatus nextProcess(){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }

        int nextCode = this.getCode() + 100;

        for(OrderStatus o : OrderStatus.values()){
            if(nextCode == o.getCode()){
                return o;
            }
        }

        throw new IllegalArgumentException("can not find next process");
    }

    /**
     * check canceled status has cause process
     * <code>CUSTOMER_CANCEL</code> and <code>SELLER_CANCEL</code> can return true
     * 
     * @return if true, this has cause
     */
    public boolean hasCauseProcess(){
        if(!this.isCanceled()){
            throw new UnsupportedOperationException("only canceled status supported");
        }

        int causeCode = this.getCode()*-1;
        causeCode /= 10;
        causeCode %= 10;

        return causeCode != 0;
    }

    /**
     * get cause process from canceled status
     * 
     * @return cause process
     * @throws UnsupportedOperationException process tries this method
     */
    public OrderStatus causeProcess(){
        if(!this.isCanceled()){
            throw new UnsupportedOperationException("only canceled status supported");
        }

        int causeCode = this.getCode()*-1;
        causeCode /= 10;
        causeCode %= 10;
        causeCode *= 100;

        for(OrderStatus o : OrderStatus.values()){
            if(causeCode == o.getCode()){
                return o;
            }
        }

        throw new IllegalArgumentException("can not find cause process");
    }

    /**
     * figure out cancel code's origin
     * 
     * @return if true, this is caused by seller
     * @throws UnsupportedOperationException process tries this method
     * @see {@link #isSellerCancel()}
     */
    public boolean isCustomerCancel(){
        if(!this.isCanceled()){
            throw new UnsupportedOperationException("only canceled status supported");
        }

        int code = this.getCode();
        code /= 100;
        code *= 100;

        if(OrderStatus.CUSTOMER_CANCEL.getCode() == code){
            return true;
        }else{
            return false;
        }
    }
    

    /**
     * figure out cancel code's origin
     * 
     * @return if true, this is caused by customer
     * @throws UnsupportedOperationException process tries this method
     * @see {@link #isCustomerCancel()}
     */
    public boolean isSellerCancel(){
        return !this.isCustomerCancel();
    }

    /**
     * find main normal process
     * main process also return this
     *
     * @return main process (like _00)
     * @throws IllegalArgumentException canceled status tried this method
     */
    public OrderStatus getMainProcess(){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }

        int mainProcessCode = this.getCode() / 100;
        mainProcessCode *= 100;

        for(OrderStatus o : OrderStatus.values()){
            if(o.getCode() == mainProcessCode){
                return o;
            }
        }

        throw new IllegalArgumentException("can not find main process");
    }

    /**
     * ask customer can cancel in this process
     * 
     * @return if true, customer can cancel
     * @throws UnsupportedOperationException canceled status tries this method
     * @see {@link #isSellerCanCancel()}
     */
    public boolean isCustomerCanCancel(){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }

        OrderStatus mainProcess = this.getMainProcess();

        for(OrderStatus o : OrderStatus.values()){
            if(o.isCanceled() && o.isCustomerCancel()){
                if(o.hasCauseProcess() && o.causeProcess() == mainProcess){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * ask seller can cancel in this process
     * 
     * @return if true, seller can cancel
     * @throws UnsupportedOperationException canceled status tries this method
     * @see {@link #isCustomerCanCancel()}
     */
    public boolean isSellerCanCancel(){
        if(!this.isProcess()){
            throw new UnsupportedOperationException("only normal process supported");
        }

        OrderStatus mainProcess = this.getMainProcess();

        for(OrderStatus o : OrderStatus.values()){
            if(o.isCanceled() && o.isSellerCancel()){
                if(o.hasCauseProcess() && o.causeProcess() == mainProcess){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * get cancel status of normal process caused by customer
     * 
     * @return cancel status
     * @throws UnsupportedOperationException customer can not cancel in this process
     * @see {@link #getSellerCancel()}
     */
    public OrderStatus getCustomerCancel(){
        if(!this.isCustomerCanCancel()){
            throw new UnsupportedOperationException("only process when customer can cancel supported");
        }

        int causeCode = this.getCode();
        causeCode /= 10;
        causeCode *= -1;
        causeCode += OrderStatus.CUSTOMER_CANCEL.getCode();

        for(OrderStatus o : OrderStatus.values()){
            if(causeCode == o.getCode()){
                return o;
            }
        }

        throw new IllegalArgumentException("can not find cancel status");
    }

    /**
     * get cancel status of normal process caused by seller
     * 
     * @return cancel status
     * @throws UnsupportedOperationException seller can not cancel in this process
     * @see {@link #getCustomerCancel()}
     */
    public OrderStatus getSellerCancel(){
        if(!this.isSellerCanCancel()){
            throw new UnsupportedOperationException("only process when seller can cancel supported");
        }

        int causeCode = this.getCode();
        causeCode /= 10;
        causeCode *= -1;
        causeCode += OrderStatus.SELLER_CANCEL.getCode();

        for(OrderStatus o : OrderStatus.values()){
            if(causeCode == o.getCode()){
                return o;
            }
        }

        throw new IllegalArgumentException("can not find cancel status");
    }
}