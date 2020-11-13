package dev.rokong.product.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.product.option.ProductOptionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@SuppressWarnings("unused")
public class ProductDetailServiceImpl implements ProductDetailService {
    
    @Autowired ProductDetailDAO pDetailDAO;
    
    @Autowired ProductOptionService pOptionService;

    public List<ProductDetailDTO> getDetails(ProductDetailDTO pDetail){
        return pDetailDAO.selectDetailList(pDetail);
    }

    public ProductDetailDTO getDetail(ProductDetailDTO pDetail){
        if(pDetail.getProductId() == 0){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail's product id is not exists");
        }else if(pDetail.getOptionCd() == null || "".equals(pDetail.getOptionCd())){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail's option code is not exists");
        }

        return pDetailDAO.selectDetail(pDetail);
    }

    public ProductDetailDTO getDetailNotNull(ProductDetailDTO pDetail){
        ProductDetailDTO result = this.getDetail(pDetail);

        if(result == null){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is not exists");
        }

        return result;
    }

    public ProductDetailDTO createDetail(ProductDetailDTO pDetail){
        ProductDetailDTO exists = this.getDetail(pDetail);
        if(exists != null){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is already exists");
        }

        this.verifyOptionCd(pDetail);

        String name = this.createFullName(pDetail);
        pDetail.setFullNm(name);

        return this.getDetailNotNull(pDetail);
    }

    public void deleteDetail(ProductDetailDTO pDetail){
        this.getDetailNotNull(pDetail);
        pDetailDAO.deleteDetail(pDetail);
    }

    public ProductDetailDTO updateDetail(ProductDetailDTO pDetail){
        //update price change, stock cnt, enabled

        this.getDetailNotNull(pDetail);

        pDetailDAO.updateDetail(pDetail);

        return this.getDetailNotNull(pDetail);
    }

    private void verifyOptionCd(ProductDetailDTO pDetail){
        //check option cd is not empty
        if(pDetail.getOptionCd() == null || "".equals(pDetail.getOptionCd())){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is empty");
        }

        //get option list of product
        ProductOptionDTO optionParam = new ProductOptionDTO(pDetail.getProductId());
        List<ProductOptionDTO> optionList = pOptionService.getPOptionList(optionParam);

        if(optionList == null || optionList.size() <= 1){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("option list is empty");
        }

        int maxGroupInList = optionList.get(optionList.size()-1).getOptionGroup();
        int maxGroupInDetail = pDetail.getOptionCd().length()/2;

        if(maxGroupInList > maxGroupInDetail){
            log.debug("product detail parameter : "+pDetail.toString());
            log.debug("max option group : "+maxGroupInList);
            throw new BusinessException("option cd exceeds max option group");
        }

        int i = 1;
        for(ProductOptionDTO o : optionList){
            if(i > o.getOptionGroup()){
                continue;

            }else if(i == o.getOptionGroup() && pDetail.idOfGroup(i).equals(o.getOptionId())){
                if(++i > maxGroupInDetail){
                    break;
                }

            }else if("00".equals(o.getOptionId()) && i < o.getOptionGroup()){
                log.debug("product detail parameter : "+pDetail.toString());
                throw new BusinessException("the "+i+"th option group not exists");

            }
        }

        if(i == maxGroupInDetail){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("the "+i+"th option group not exists");
        }

    }

    private String createFullName(ProductDetailDTO pDetail){
        ProductOptionDTO optionParam = new ProductOptionDTO(pDetail.getProductId());
        List<ProductOptionDTO> optionList = pOptionService.getPOptionList(optionParam);

        int optionGroup = 1;
        int maxOptionGroup = pDetail.getOptionCd().length()/2;
        maxOptionGroup++;

        StringBuffer sBuffer = new StringBuffer();

        for(ProductOptionDTO pOption : optionList){
            if(pOption.getOptionGroup() > optionGroup){
                continue;

            }else if("00".equals(pOption.getOptionId())){
                sBuffer.append(pOption.getName()).append(" : ");
                
            }else if(optionGroup == pOption.getOptionGroup()
                        && pOption.getOptionId().equals(pDetail.idOfGroup(optionGroup))){
                sBuffer.append(pOption.getName());
                if(++optionGroup == maxOptionGroup){
                    break;
                }else{
                    sBuffer.append(" / ");
                }

            }
        }

        return sBuffer.toString();
    }
}