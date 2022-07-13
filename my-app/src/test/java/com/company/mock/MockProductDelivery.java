package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.annotation.DeliveryType;
import com.company.dto.ProductDeliveryDTO;
import com.company.product.delivery.ProductDeliveryService;
import com.company.util.RandomUtil;

@Component("MockProductDelivery")
public class MockProductDelivery extends AbstractMockObject<ProductDeliveryDTO> {

    private @Autowired ProductDeliveryService pDeliveryService;

    private @Autowired MockUser mUser;

    @Override
    public ProductDeliveryDTO temp() {
        ProductDeliveryDTO pDelivery = new ProductDeliveryDTO();

        pDelivery.setSellerNm(mUser.any().getUserNm());
        pDelivery.setDeliveryType(DeliveryType.ETC);
        pDelivery.setName("dlvr-"+RandomUtil.randomString(4));
        pDelivery.setPrice(RandomUtil.randomInt(5));

        return pDelivery;
    }

    @Override
    protected ProductDeliveryDTO createObjService(ProductDeliveryDTO obj) {
        return pDeliveryService.createPDelivery(obj);
    }

    @Override
    protected ProductDeliveryDTO getObjService(ProductDeliveryDTO obj) {
        return pDeliveryService.getPDelivery(obj.getId());
    }
    
}