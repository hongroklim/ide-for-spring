package dev.rokong.product.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.cart.CartService;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.product.main.ProductService;
import dev.rokong.product.option.ProductOptionService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    
    @Autowired private ProductDetailDAO pDetailDAO;
    
    @Autowired private ProductService pService;
    @Autowired private ProductOptionService pOptionService;
    @Autowired private CartService cartService;
    @Autowired private OrderProductService oProductService;

    public List<ProductDetailDTO> getDetails(ProductDetailDTO pDetail){
        ProductDetailDTO param = new ProductDetailDTO(pDetail);
        //set parameter option_cd %
        String optionCd = param.getOptionCd();
        if(optionCd != null && !"".equals(optionCd)){
            param.setOptionCd(optionCd+"%");
        }
        return pDetailDAO.selectList(param);
    }

    private void verifyPrimaryValuesDefined(ProductDetailDTO pDetail){
        if(pDetail == null){
            throw new BusinessException("product detail is not defined");

        }else if(pDetail.getProductId() == 0){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail's product id is not exists");

        }else if(pDetail.getOptionCd() == null || "".equals(pDetail.getOptionCd())){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail's option code is not exists");
        }
    }

    public ProductDetailDTO getDetail(ProductDetailDTO pDetail){
        this.verifyPrimaryValuesDefined(pDetail);
        return pDetailDAO.select(pDetail);
    }

    public ProductDetailDTO getDetailNotNull(ProductDetailDTO pDetail){
        ProductDetailDTO result = this.getDetail(pDetail);

        if(result == null){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is not exists");
        }

        return result;
    }

    public void checkDetailExist(ProductDetailDTO pDetail){
        this.verifyPrimaryValuesDefined(pDetail);

        if(pDetailDAO.count(pDetail) == 0){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is not exists");
        }
    }

    public ProductDetailDTO createDetail(ProductDetailDTO pDetail){
        ProductDetailDTO exists = this.getDetail(pDetail);
        if(exists != null){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is already exists");
        }

        //set default value
        if(pDetail.getPriceChange() == null){
            pDetail.setPriceChange(0);
        }

        if(pDetail.getEnabled() == null){
            pDetail.setEnabled(true);
        }

        if(pDetail.getStockCnt() == null || pDetail.getStockCnt() < 0){
            log.debug("stock count is not defined or minus. it will be 0");
            pDetail.setStockCnt(0);
        }

        //is product exists
        ProductDTO product = pService.getProductNotNull(pDetail.getProductId());

        //compare price with original product
        if(product.getPrice() + pDetail.getPriceChange() < 0){
            log.debug("product : "+product.toString());
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("final price can not be under 0");
        }

        //verify option cd
        this.verifyOptionCd(pDetail);

        //create full name
        String name = this.createFullName(pDetail);
        pDetail.setFullNm(name);

        //insert
        pDetailDAO.insert(pDetail);

        return this.getDetailNotNull(pDetail);
    }

    public void deleteDetail(ProductDetailDTO pDetail){
        this.checkDetailExist(pDetail);

        //set null option cd in order product
        oProductService.updateOProductInvalid(pDetail.getProductId(), pDetail.getOptionCd());

        //delete cart
        cartService.deleteCarts(pDetail.getProductId(), pDetail.getOptionCd());

        pDetailDAO.delete(pDetail);
    }

    public ProductDetailDTO updateDetail(ProductDetailDTO pDetail){
        //update price change, stock cnt, enabled
        this.checkDetailExist(pDetail);

        //compare price with original product
        if(pDetail.getPriceChange() != null){
            ProductDTO product = pService.getProductNotNull(pDetail.getProductId());
            if(product.getPrice() + pDetail.getPriceChange() < 0){
                log.debug("product : "+product.toString());
                log.debug("product detail parameter : "+pDetail.toString());
                throw new BusinessException("final price can not be under 0");
            }
        }

        //check stock cnt
        if(pDetail.getStockCnt() != null && pDetail.getStockCnt() < 0){
            throw new BusinessException("stock count must be greater than or equal to 0");
        }

        //update
        pDetailDAO.update(pDetail);

        return this.getDetailNotNull(pDetail);
    }

    public List<ProductDetailDTO> getDetailsByOption(ProductOptionDTO pOption){
        ProductDetailDTO pDetail = this.paramByOption(pOption);
        return pDetailDAO.selectList(pDetail);
    }

    public void deleteDetailByOption(ProductOptionDTO pOption){
        ProductDetailDTO pDetail = this.paramByOption(pOption);

        //set null option cd in order product
        oProductService.updateOProductInvalid(pDetail.getProductId(), pDetail.getOptionCd());

        //delete cart
        cartService.deleteCarts(pDetail.getProductId(), pDetail.getOptionCd());

        pDetailDAO.deleteList(pDetail);
    }

    public void updateNameByOption(ProductOptionDTO pOption){
        ProductDetailDTO param = this.paramByOption(pOption);
        //set null code in order product
        //need to be changed name
        List<ProductDetailDTO> list = pDetailDAO.selectList(param);

        for(ProductDetailDTO pDetail : list){
            pDetail.setFullNm(this.createFullName(pDetail));
            //update full name
            this.updateDetail(pDetail);
        }
    }

    private void verifyOptionCd(ProductDetailDTO pDetail){
        //check option cd is not empty
        if(ObjUtil.isEmpty(pDetail.getOptionCd())){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("product detail is empty");
        }

        //get option list of product
        ProductOptionDTO optionParam = new ProductOptionDTO(pDetail.getProductId());
        List<ProductOptionDTO> optionList = pOptionService.getPOptionList(optionParam);

        //option list's first item is 00 (option name) -> this is not a available option
        if(optionList == null || optionList.size() <= 1){
            log.debug("product detail parameter : "+pDetail.toString());
            throw new BusinessException("available option list is empty");
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

            }else if(ProductOptionDTO.TITLE_ID.equals(o.getOptionId()) && i < o.getOptionGroup()){
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

            }else if(ProductOptionDTO.TITLE_ID.equals(pOption.getOptionId())){
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

    private ProductDetailDTO paramByOption(ProductOptionDTO pOption){
        ProductDetailDTO pDetail = new ProductDetailDTO();
        //set product id
        pDetail.setProductId(pOption.getProductId());

        Integer optionGroup = pOption.getOptionGroup();
        
        if(optionGroup!=null && optionGroup!=0){
            //set option cd
            StringBuffer sbuf = new StringBuffer();
            for(int i=1; i<optionGroup; i++){
                sbuf.append("__");
            }

            String optionId = pOption.getOptionId();
            if(optionId == null || "".equals(optionId)
                    || ProductOptionDTO.TITLE_ID.equals(optionId)){
                //option id is empty or refer group title -> return entire group
                sbuf.append("__");
            }else{
                //is not -> return specific option id
                sbuf.append(optionId);
            }
            
            sbuf.append("%");
            pDetail.setOptionCd(sbuf.toString());
        }
        return pDetail;
    }
}