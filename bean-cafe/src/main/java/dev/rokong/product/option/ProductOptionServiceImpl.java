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
        this.getPOptionNotNull(pOption);

        //if pOption is option group's title
        if("00".equals(pOption.getOptionId())){
            //get product option list in group
            ProductOptionDTO param = new ProductOptionDTO(pOption.getProductId(), pOption.getOptionGroup()+1);
            List<ProductOptionDTO> optionsInGrp = pOptionDAO.selectProductOptionList(param);

            if(optionsInGrp != null && optionsInGrp.size() > 1){
                //if there is the other option, throw exception
                log.debug("product option paramerter : "+pOption.toString());
                log.debug("the size of options in group : "+optionsInGrp.size()+" (this size should be 1)");
                throw new BusinessException("other option in same group exists");
            }
        }

        //get product option list whose option group is pOption's option group +1
        ProductOptionDTO param = new ProductOptionDTO(pOption.getProductId(), pOption.getOptionGroup()+1);
        List<ProductOptionDTO> lowerOptions = pOptionDAO.selectProductOptionList(param);
        
        if(lowerOptions != null && lowerOptions.size() > 0){
            //if lower group exists, throw exception
            log.debug("product option paramerter : "+pOption.toString());
            log.debug("lower option's option group : "+lowerOptions.get(0).getOptionGroup());
            throw new BusinessException("lower options exist");
        }

        log.debug("associated option cd in order product is set null");

        log.debug("associated product details are deleted");
        //TODO delete product detail associated with product option

        log.debug("associated option cd in cart is deleted");
        //TODO delete cart associated with product option

        pOptionDAO.deleteProductOption(pOption);
    }

    public ProductOptionDTO updatePOption(ProductOptionDTO asisPOption, ProductOptionDTO tobePOption){
        // update name or option_id
        ProductOptionDTO asis = this.getPOptionNotNull(asisPOption);

        boolean updateName = !asis.getName().equals(tobePOption.getName());
        boolean updateId = !asis.getOptionId().equals(tobePOption.getOptionId());

        if(!updateName && !updateId){
            //if nothing to be changed, return asis one
            return asis;
        }

        if(!"00".equals(asis.getOptionId()) && "00".equals(tobePOption.getOptionId())){
            //if tobe option id tries to be option group's title, throw exception
            log.debug("asis product option paramerter : "+asis.toString());
            log.debug("tobe product option paramerter : "+tobePOption.toString());
            throw new BusinessException("option id can not be changed to title's id (00)");
        }

        if(updateId){
            //get option list in same group
            ProductOptionDTO param = new ProductOptionDTO(asis.getProductId(), asis.getOptionGroup());
            List<ProductOptionDTO> optionsInGrp = this.getPOptionList(param);

            int availableRange = (optionsInGrp == null) ? 1 : optionsInGrp.size()-1; //except title(00)

            if(Integer.parseInt(tobePOption.getOptionId()) > availableRange){
                //if tobe option id exceed existing count of options, throw exception
                log.debug("asis product option paramerter : "+asis.toString());
                log.debug("tobe product option paramerter : "+tobePOption.toString());
                throw new BusinessException("tobe option id can not exceed "+availableRange);
            }
        }

        //if option's name (except title) is going to be changed
        if(updateName && !"00".equals(tobePOption.getOptionId())){
            //get option list in same group
            ProductOptionDTO param = new ProductOptionDTO(asis.getProductId(), asis.getOptionGroup());
            List<ProductOptionDTO> optionsInGrp = this.getPOptionList(param);

            for(ProductOptionDTO pOption : optionsInGrp){
                if(!"00".equals(pOption.getOptionId()) &&
                        pOption.getName().equals(tobePOption.getName())){
                    //avoid duplicate option name (except option group's title)
                    log.debug("asis product option paramerter : "+asis.toString());
                    log.debug("tobe product option paramerter : "+tobePOption.toString());
                    throw new BusinessException("duplicate name in same option group");
                }
            }
        }

        //resolve dependency issue (product detail, order product, cart)
        /*
            1. change only name
                product detail : update name
                order product : set null option cd (asis)
                cart : no action
            
            2. change only option id
                product detail : if exists(asis ~ tobe), throw exception
                order product : set null option cd (asis ~ tobe)
                cart : cascade
        */
        if(updateId){
            //TODO if product detail exists(asis ~ tobe), throw exception

            if(updateName){
                //TODO product detail : update name
            }

            //TODO order_product : set null option cd (asis ~ tobe)

            //TODO cart : cascade

        }else if(updateName){
            //TODO product detail : update name

            //TODO order_product : set null option cd (asis)

        }

        if(updateId){
            this.rearrangeOptionIdOrder(asis, tobePOption.getOptionId());
            //after update id, set updated option id in asis
            asis.setOptionId(tobePOption.getOptionId());
        }

        if(updateName){
            pOptionDAO.updateProductOption(asis, asis.getOptionId(), tobePOption.getName());
        }

        return this.getPOptionNotNull(asis);
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

    private void rearrangeOptionIdOrder(ProductOptionDTO asisPOption, String tobeOptionId){
        //TODO push and pull, update last
    }
}
