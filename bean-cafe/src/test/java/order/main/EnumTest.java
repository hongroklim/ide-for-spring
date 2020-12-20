package order.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import dev.rokong.annotation.OrderStatus;

public class EnumTest {
    
    @Test
    public void normalProcess(){
        assertThat(OrderStatus.WRITING.isProcess(), is(equalTo(true)));
        assertThat(OrderStatus.PAYMENT.isProcess(), is(equalTo(true)));
        assertThat(OrderStatus.CHECKING.isProcess(), is(equalTo(true)));
        assertThat(OrderStatus.PRODUCT_READY.isProcess(), is(equalTo(true)));
        assertThat(OrderStatus.DELIVERING.isProcess(), is(equalTo(true)));
        assertThat(OrderStatus.COMPLETE.isProcess(), is(equalTo(true)));
    }

    @Test
    public void canceledStatus(){
        assertThat(OrderStatus.CUSTOMER_CANCEL.isCanceled(), is(equalTo(true)));
        assertThat(OrderStatus.SELLER_CANCEL.isCanceled(), is(equalTo(true)));
    }

    @Test
    public void compareProcess(){
        //former
        assertThat(OrderStatus.WRITING.isFormerThan(OrderStatus.CHECKING), is(equalTo(true)));
        assertThat(OrderStatus.CHECKING.isFormerThan(OrderStatus.DELIVERING), is(equalTo(true)));

        //same process is false
        assertThat(OrderStatus.DELIVERING.isFormerThan(OrderStatus.DELIVERING), is(equalTo(false)));
        assertThat(OrderStatus.DELIVERING.isLatterThan(OrderStatus.DELIVERING), is(equalTo(false)));

        //latter
        assertThat(OrderStatus.DELIVERING.isLatterThan(OrderStatus.CHECKING), is(equalTo(true)));
        assertThat(OrderStatus.PRODUCT_READY.isLatterThan(OrderStatus.PAYMENT), is(equalTo(true)));
    }

    @Test
    public void getCauseProcess(){
        assertThat(OrderStatus.CANCELED_WRITE.causeProcess(), is(equalTo(OrderStatus.WRITING)));
        assertThat(OrderStatus.CANCELED_PRODUCT.causeProcess(), is(equalTo(OrderStatus.PRODUCT_READY)));
        assertThat(OrderStatus.CANCEL_DELIVER.causeProcess(), is(equalTo(OrderStatus.DELIVERING)));
    }

    @Test
    public void getCauseSubject(){
        assertThat(OrderStatus.CANCELED_WRITE.isCustomerCancel(), is(equalTo(true)));
        assertThat(OrderStatus.CANCELED_PAYMENT.isCustomerCancel(), is(equalTo(true)));

        assertThat(OrderStatus.CANCEL_DELIVER.isSellerCancel(), is(equalTo(true)));
        assertThat(OrderStatus.CANCEL_CHECK.isSellerCancel(), is(equalTo(true)));
    }

    @Test
    public void checkCancelAvailable(){
        assertThat(OrderStatus.PRODUCT_READY.isCustomerCanCancel(), is(equalTo(true)));
        assertThat(OrderStatus.PAYMENT.isSellerCanCancel(), is(equalTo(false)));
    }

    public void getMainProcess(){
        assertThat(OrderStatus.PAYMENT_STANDBY.getMainProcess(), is(equalTo(OrderStatus.PAYMENT)));
    }

    @Test
    public void subProcessInPayment(){
        assertThat(OrderStatus.PAYMENT_STANDBY.isCustomerCanCancel(), is(equalTo(true)));
    }
}