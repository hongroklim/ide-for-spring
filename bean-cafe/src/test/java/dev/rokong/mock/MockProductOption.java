package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.option.ProductOptionService;
import dev.rokong.util.RandomUtil;

@Component("MockProductOption")
public class MockProductOption {
    
    private ProductOptionDTO optionGroup = null;
    private List<ProductOptionDTO> pOptionList = new ArrayList<ProductOptionDTO>();

    @Autowired private ProductOptionService pOptionService;

    @Autowired private MockProduct product;

    public ProductOptionDTO tempPOption(){
        ProductOptionDTO pOption = new ProductOptionDTO();
        pOption.setProductId(product.anyProduct().getId());
        pOption.setOptionGroup(1);
        pOption.setOptionId("00");
        pOption.setName("grp-"+RandomUtil.randomString(4));
        pOption.setOrd(1);
        return pOption;
    }

    private void validatingOptionGroup(){
        if(this.optionGroup==null
                || pOptionService.getPOption(this.optionGroup)==null){
            ProductOptionDTO temp = this.tempPOption();
            temp.setOptionGroup(null);
            temp.setOptionId(null);
            temp.setOrd(null);
            this.optionGroup = pOptionService.createPOptionGroup(temp);
        }
    }

    public ProductOptionDTO createPOption(){
        this.validatingOptionGroup();
        
        //create product option from option group
        ProductOptionDTO pOption = new ProductOptionDTO(this.optionGroup);
        pOption.setOptionId(null);
        pOption.setName("opt-"+RandomUtil.randomString(4));
        pOption.setOrd(null);

        return pOptionService.createPOption(pOption);
    }

    private boolean isValidList(){
        if(this.pOptionList.size() == 0){
            return true;
        }else{
            return pOptionService.getPOption(this.pOptionList.get(0)) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.pOptionList.clear();
        }
    }

    public ProductOptionDTO anyPOption(){
        this.validatingList();

        if(this.pOptionList.size() == 0){
            pOptionList.add(this.createPOption());
        }
        return pOptionList.get(0);
    }

    public List<ProductOptionDTO> anyPOptionList(int count){
        this.validatingList();

        while(this.pOptionList.size() < count){
            this.pOptionList.add(this.createPOption());
        }
        return this.pOptionList.subList(0, count);
    }
}