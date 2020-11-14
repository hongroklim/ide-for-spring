package product.detail;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.product.detail.ProductDetailController;
import dev.rokong.product.detail.ProductDetailService;
import dev.rokong.product.main.ProductService;
import dev.rokong.util.ListUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired ProductDetailController pDetailController;
    @Autowired ProductDetailService pDetailService;

    @Autowired ProductService pSerivce;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(pDetailController).build();
    }
    
    private ProductDetailDTO anyDetail;

    private String PDetailURL(ProductDetailDTO pDetail){
        assertThat("product id must be initialized",
                        pDetail.getProductId(), is(greaterThan(0)));
        
        StringBuffer sbuf = new StringBuffer();
        //create product id
        sbuf.append("/product/")
            .append(pDetail.getProductId())
            .append("/detail");

        if(pDetail.getOptionCd() != null && !"".equals(pDetail.getOptionCd())){
            //append option cd
            sbuf.append("/option/")
                .append(pDetail.getOptionCd());
        }

        return sbuf.toString();
    }

    @Before
    public void initAnyDetail() throws Exception {
        List<ProductDTO> pList = pSerivce.getProductList();
        List<ProductDetailDTO> list = null;
        for(ProductDTO product : pList){
            list = pDetailService.getDetails(new ProductDetailDTO(product.getId()));
            if(list != null && list.size() > 0){
                this.anyDetail = ListUtil.randomItem(list);
            }else{
                continue;
            }
            return;
        }
        
        throw new IllegalArgumentException("no any product detail exists");
    }

    @Test
    public void getDetailListInProduct() throws Exception {
        ProductDetailDTO param = new ProductDetailDTO(anyDetail.getProductId());

        List<ProductDetailDTO> res = this.reqAndResBodyList(
            this.PDetailURL(param), RequestMethod.GET, null, ProductDetailDTO.class);

        assertThat(ListUtil.isNotEmpty(res), is(equalTo(true)));

        assertThat(res.get(0).getProductId(), is(equalTo(param.getProductId())));
    }
    
    @Test
    public void getDetailListInGroup() throws Exception {
        ProductDetailDTO param = new ProductDetailDTO(anyDetail);

        String optionCd = param.getOptionCd();
        param.setOptionCd(optionCd.substring(0, optionCd.length()-2));

        String url = "/product/"+param.getProductId()+"/detail";
        url += "/group/"+param.getOptionCd();

        List<ProductDetailDTO> res = this.reqAndResBodyList(
            url, RequestMethod.GET, null, ProductDetailDTO.class);

        assertThat(ListUtil.isNotEmpty(res), is(equalTo(true)));
        assertThat(res.get(0).getProductId(), is(equalTo(param.getProductId())));
        assertThat(res.get(0).getOptionCd().indexOf(param.getOptionCd()),
                    is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void getDetail() throws Exception {
        ProductDetailDTO param = new ProductDetailDTO(anyDetail);

        ProductDetailDTO res = this.reqAndResBody(
            this.PDetailURL(param), RequestMethod.GET,
            null, ProductDetailDTO.class
        );

        assertThat(res.getProductId(), is(equalTo(param.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(param.getOptionCd())));

        assertThat(res.getFullNm(), is(not(equalTo(""))));
    }

    @Test
    public void createDetail() throws Exception {
        ProductDetailDTO newDetail = new ProductDetailDTO();
        newDetail.setProductId(2);
        newDetail.setOptionCd("010102");
        newDetail.setPriceChange(-1000);
        newDetail.setStockCnt(1);
        newDetail.setEnabled(false);

        ProductDetailDTO res = this.reqAndResBody(
            this.PDetailURL(new ProductDetailDTO(newDetail.getProductId())),
            RequestMethod.POST, newDetail, ProductDetailDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getProductId(), is(equalTo(newDetail.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(newDetail.getOptionCd())));
    
    }

    @Test
    public void deleteDetail() throws Exception {
        this.reqAndResBody(this.PDetailURL(anyDetail), RequestMethod.DELETE, null, null);

        ProductDetailDTO param = new ProductDetailDTO(anyDetail.getProductId());
        String optionCd = anyDetail.getOptionCd();
        param.setOptionCd(optionCd.substring(0, optionCd.length()-2));
        
        List<ProductDetailDTO> list = pDetailService.getDetails(param);

        for(ProductDetailDTO pDetail : list){
            assertThat(pDetail.getOptionCd(), is(not(equalTo(optionCd))));
        }
    }

    @Test
    public void updateDetail() throws Exception {
        ProductDetailDTO asis = anyDetail;
        
        ProductDetailDTO tobe = new ProductDetailDTO(anyDetail);

        tobe.setPriceChange(asis.getPriceChange()+1000);
        tobe.setStockCnt(asis.getStockCnt()+1000);
        tobe.setEnabled(!asis.getEnabled());

        ProductDetailDTO res = this.reqAndResBody(
            this.PDetailURL(asis), RequestMethod.PUT, tobe, ProductDetailDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getProductId(), is(equalTo(asis.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(asis.getOptionCd())));

        assertThat(res.getPriceChange(), is(equalTo(tobe.getPriceChange())));
        assertThat(res.getStockCnt(), is(equalTo(tobe.getStockCnt())));
        assertThat(res.getEnabled(), is(equalTo(tobe.getEnabled())));
    }
}