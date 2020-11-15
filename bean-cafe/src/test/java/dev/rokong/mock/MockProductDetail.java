package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.detail.ProductDetailService;

@Component("MockProductDetail")
public class MockProductDetail extends MockObjects {
    
    private List<ProductDetailDTO> pDetailList = new ArrayList<ProductDetailDTO>();

    @Autowired private ProductDetailService pDetailService;

    @Autowired private MockProductOption pOption;

    public ProductDetailDTO tempDetail(){
        ProductOptionDTO option = pOption.anyPOption();
        ProductDetailDTO temp = new ProductDetailDTO();
        temp.setProductId(option.getProductId());
        temp.setOptionCd(option.getOptionId());
        temp.setPriceChange(this.randomInt(4));
        temp.setStockCnt(this.randomInt(2));
        temp.setEnabled(true);
        return temp;
    }

    public ProductDetailDTO createDetail(){
        return pDetailService.createDetail(this.tempDetail());
    }

    private boolean isValidList(){
        if(this.pDetailList.size() == 0){
            return true;
        }else{
            return pDetailService.getDetail(this.pDetailList.get(0)) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.pDetailList.clear();
        }
    }

    public ProductDetailDTO anyPDetail(){
        this.validatingList();

        if(this.pDetailList.size() == 0){
            pDetailList.add(this.createDetail());
        }
        return pDetailList.get(0);
    }

    private void appendPDetailListUntil(int count){
        List<ProductOptionDTO> optionList = pOption.anyPOptionList(count);
        ProductDetailDTO pDetail = null;

        for(int i=this.pDetailList.size(); i<count; i++){
            pDetail = this.tempDetail();
            pDetail.setOptionCd(optionList.get(i).getOptionId());
            this.pDetailList.add(pDetailService.createDetail(pDetail));
        }
    }

    public List<ProductDetailDTO> anyPDetailList(int count){
        this.validatingList();
        this.appendPDetailListUntil(count);
        return this.pDetailList.subList(0, count);
    }
}