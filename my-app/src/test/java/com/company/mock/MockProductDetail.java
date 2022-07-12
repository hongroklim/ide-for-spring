package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.dto.ProductDetailDTO;
import com.company.dto.ProductOptionDTO;
import com.company.product.detail.ProductDetailService;
import com.company.util.RandomUtil;

@Component("MockProductDetail")
public class MockProductDetail extends AbstractMockObject<ProductDetailDTO> {
    
    @Autowired private ProductDetailService pDetailService;

    @Autowired private MockProductOption mPOption;

    @Override
    public ProductDetailDTO temp() {
        ProductDetailDTO temp = new ProductDetailDTO();

        ProductOptionDTO option = mPOption.any();
        temp.setProductId(option.getProductId());
        temp.setOptionCd(option.getOptionId());

        temp.setPriceChange(RandomUtil.randomInt(4));
        temp.setStockCnt(RandomUtil.randomInt(2));
        temp.setEnabled(true);

        return temp;
    }

    @Override
    protected ProductDetailDTO createObjService(ProductDetailDTO obj) {
        return pDetailService.createDetail(obj);
    }

    @Override
    protected ProductDetailDTO getObjService(ProductDetailDTO obj) {
        return pDetailService.getDetail(obj);
    }

    @Override
    protected ProductDetailDTO tempNth(int i){
        ProductDetailDTO pDetail = this.temp();
        ProductOptionDTO pOption = mPOption.anyList(i+1).get(i);

        pDetail.setOptionCd(pOption.getOptionId());

        return pDetail;
    }
}