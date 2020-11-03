package dev.rokong.product.option;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.product.main.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductOptionServiceImpl implements ProductOptionService {
    
    @Autowired ProductOptionDAO pOptionDAO;

    @Autowired ProductService pService;

    public List<ProductOptionDTO> getPOptionList(ProductOptionDTO pOption){
        if(pOption.getProductId() == 0){
            log.debug("product option paramerter : "+pOption.toString());
            throw new BusinessException("product id must be declared");
        }else if(pOption.getOptionGroup() == null){
            if(pOption.getOptionId() != null && !"".equals(pOption.getOptionId())){
                log.debug("product option paramerter : "+pOption.toString());
                throw new BusinessException("option id should be empty until option group is not null");
            }
        }
        return pOptionDAO.selectProductOptionList(pOption);
    };

    public ProductOptionDTO getPOption(ProductOptionDTO pOption){
        this.verifyPrimaryKeysDefined(pOption);
        return pOptionDAO.selectProductOption(pOption);
    };

    public ProductOptionDTO getPOptionNotNull(ProductOptionDTO pOption){
        ProductOptionDTO getPOption = pOptionDAO.selectProductOption(pOption);
        if(getPOption == null){
            log.debug("product option paramerter : "+pOption.toString());
            throw new BusinessException("product option is not exists");
        }
        return getPOption;
    };

    public ProductOptionDTO createPOption(ProductOptionDTO pOption){
        //avoid primary key constraint
        ProductOptionDTO getPOption = this.getPOption(pOption);
        if(getPOption != null){
            log.debug("product option (param) : "+pOption.toString());
            log.debug("product option (get) : "+getPOption.toString());
            throw new BusinessException("product option is already exists");
        }

        //is product option name is defined
        if(pOption.getName() == null || "".equals(pOption.getName())){
            throw new BusinessException("product option's name is not defined");
        }

        //is product id exists
        pService.getProductNotNull(pOption.getProductId());

        //check option group and option id are valid
        if("00".equals(pOption.getOptionId())){
            //if the first option id in option group,
            //get last option group in product
            ProductOptionDTO param = new ProductOptionDTO(pOption.getProductId());
            List<ProductOptionDTO> optionList = this.getPOptionList(param);
            int lastGroup = 0;

            if(optionList != null && optionList.size() != 0){
                lastGroup = optionList.get(optionList.size()-1).getOptionGroup();
            }

            //option group should be max(optionGroup)+1
            if(lastGroup+1 != pOption.getOptionGroup()){
                log.debug("product option paramerter : "+pOption.toString());
                log.debug("lastGroup : "+lastGroup+", getOptionGroup() : "+pOption.getOptionGroup());
                throw new BusinessException("there is skipped option group");
            }

        }else{
            //if option is append to existing option group
            //get last option in product
            ProductOptionDTO param = new ProductOptionDTO(pOption.getProductId(), pOption.getOptionGroup());
            List<ProductOptionDTO> optionList = this.getPOptionList(param);
            String expectedId = "00";

            if(optionList != null && optionList.size() != 0){
                //if there is another option(s), get max(option)+1
                String lastId = optionList.get(optionList.size()-1).getOptionId();
                expectedId = "0"+(Integer.parseInt(lastId)+1);
                expectedId = expectedId.substring(expectedId.length()-2);
            }

            //option id should follow sequence like "00", "01", "02" ...
            if(!expectedId.equals(pOption.getOptionId())){
                log.debug("product option paramerter : "+pOption.toString());
                log.debug("expectedId : "+expectedId+", getOptionId() : "+pOption.getOptionId());
                throw new BusinessException("there is skipped option id");
            }

        }
        
        //insert
        pOptionDAO.insertProductOption(pOption);

        return this.getPOptionNotNull(pOption);
    }

    public void deletePOption(ProductOptionDTO pOption){
        //TODO product_detail, cart, order_product

        this.getPOptionNotNull(pOption);
        pOptionDAO.deleteProductOption(pOption);
    }

    private void verifyPrimaryKeysDefined(ProductOptionDTO pOption){
        if(pOption.getProductId() == 0){
            log.debug("product option paramerter : "+pOption.toString());
            throw new BusinessException("product id must be declared");

        }else if(pOption.getOptionGroup() == null || pOption.getOptionGroup() == 0){
            log.debug("product option paramerter : "+pOption.toString());
            throw new BusinessException("option group must be declared");

        }else if(pOption.getOptionId() == null && "".equals(pOption.getOptionId())){
            log.debug("product option paramerter : "+pOption.toString());
            throw new BusinessException("option id must be declared");
        }
    }
}
