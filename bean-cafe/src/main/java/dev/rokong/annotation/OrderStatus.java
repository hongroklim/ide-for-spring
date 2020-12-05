package dev.rokong.annotation;

public enum OrderStatus {
    //normal process
    WRITING(100), //주문 작성 중
    //작성완료
    PAYMENT_READY(200), //결재 중
    //결재
    CHECKING(300), //주문 확인 중
    //확인완료
    PRODUCT_READY(400), //상품 준비 중
    //준비완료
    DELIVERING(500), //배송중
    //배송완료
    COMPLETE(600),
    
    //exception status
    CUSTOMER_CANCEL(-100), //고객요청에 의해 취소
    
    CANCELED_WRITE(-110),
    CALCELED_PAYMENT(-120),
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
     * check canceled status has cause process
     * CUSTOMER_CANCEL and SELLER_CANCEL can return true
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

        for(OrderStatus o : OrderStatus.values()){
            if(o.isCanceled() && o.isCustomerCancel()){
                if(o.hasCauseProcess() && o.causeProcess() == this){
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

        for(OrderStatus o : OrderStatus.values()){
            if(o.isCanceled() && o.isSellerCancel()){
                if(o.hasCauseProcess() && o.causeProcess() == this){
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