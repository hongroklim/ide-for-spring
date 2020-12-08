package dev.rokong.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.annotation.DeliveryType;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.product.delivery.ProductDeliveryService;
import dev.rokong.util.RandomUtil;

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