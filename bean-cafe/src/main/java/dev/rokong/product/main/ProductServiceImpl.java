package dev.rokong.product.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.annotation.PriceField;
import dev.rokong.category.CategoryService;
import dev.rokong.dto.ProductDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.user.UserService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired UserService uService;
    @Autowired CategoryService cService;

    @Autowired ProductDAO pDAO;

    public List<ProductDTO> getProductList(){
        return pDAO.selectProductList();
    };

    public ProductDTO createProduct(ProductDTO product){
        //check categoryId and sellerNm exists
        cService.getCategoryNotNull(product.getCategoryId());
        uService.getUserNotNull(product.getSellerNm());

        pDAO.insertProduct(product);

        return this.getProductNotNull(product);
    };

    public ProductDTO getProduct(int id){
        return pDAO.selectProduct(id);
    };

    public ProductDTO updateProduct(ProductDTO product){
        ProductDTO asisProduct = this.getProductNotNull(product);

        if(asisProduct.getCategoryId() != product.getCategoryId()){
            //check category id exists
            cService.getCategoryNotNull(product.getCategoryId());
        }

        if(!asisProduct.getSellerNm().equals(product.getSellerNm())){
            //check seller name exists
            uService.getUserNotNull(product.getSellerNm());
        }

        //if stockCnt, enabled is null, will not be updated
        pDAO.updateProduct(product);

        return this.getProductNotNull(product);
    };

    public void deleteProductStock(int id){
        this.getProductNotNull(id);
        pDAO.updateProductColumn(id, "stock_cnt", null);
    }

    public void deleteProduct(int id){
        ProductDTO getProduct = this.getProductNotNull(id);

        //TODO if there are options, throw exception

        pDAO.deleteProduct(getProduct.getId());
    };

    public ProductDTO updateProductPrice(ProductDTO product,
            PriceField field){
        ProductDTO getProduct = this.getProductNotNull(product);

        pDAO.updateProductColumn(getProduct.getId(), field.getDbName(),
            field.getValueFrom(product));
        
        return this.getProductNotNull(getProduct);
    };

    public ProductDTO getProductNotNull(int id){
        ProductDTO product = pDAO.selectProduct(id);
        if(product == null){
            throw new BusinessException(id+" product is not exists");
        }
        return product;
    }

    private ProductDTO getProductNotNull(ProductDTO product){
        if(product.getId() == 0){
            throw new IllegalArgumentException("product id is not defined");
        }
        return this.getProductNotNull(product.getId());
    }
}
