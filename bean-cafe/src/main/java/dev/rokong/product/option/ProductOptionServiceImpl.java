package dev.rokong.product.option;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductOptionServiceImpl implements ProductOptionService {
    
    @Autowired ProductOptionDAO pOptionDAO;

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
}
