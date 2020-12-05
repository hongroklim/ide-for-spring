package dev.rokong.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.util.RandomUtil;

@Component("MockOrderProduct")
public class MockOrderProduct extends AbstractMockObject<OrderProductDTO> {

    @Autowired OrderProductService oProductService;

    @Autowired MockOrder mOrder;

    @Autowired MockProduct mProduct;
    @Autowired MockProductDetail mPDetail;

    @Override
    public OrderProductDTO temp() {
        OrderProductDTO orderProduct = new OrderProductDTO();
        orderProduct.setOrderId(mOrder.anyOrder().getId());

        ProductDTO product = mProduct.anyProduct();
        ProductDetailDTO pDetail = mPDetail.anyPDetail();

        orderProduct.setProductId(product.getId());
        orderProduct.setOptionCd(pDetail.getOptionCd());
        orderProduct.setSellerNm(product.getSellerNm());
        orderProduct.setDiscountPrice(product.getDiscountPrice());

        orderProduct.setProductNm(product.getName());
        orderProduct.setOptionNm(pDetail.getFullNm());

        orderProduct.setCnt(RandomUtil.randomInt(1));

        return orderProduct;
    }

    @Override
    protected OrderProductDTO createObjService(OrderProductDTO obj) {
        return oProductService.addOProduct(obj);
    }

    @Override
    protected OrderProductDTO getObjService(OrderProductDTO obj) {
        return oProductService.getOProduct(obj);
    }

    @Override
    protected OrderProductDTO createNthObj(int index){
        OrderProductDTO oProduct = this.temp();

        ProductDetailDTO pDetail = mPDetail.anyPDetailList(index+1).get(index);
        oProduct.setOptionCd(pDetail.getOptionCd());
        oProduct.setOptionNm(pDetail.getFullNm());

        return this.createObjService(oProduct);
    }
}