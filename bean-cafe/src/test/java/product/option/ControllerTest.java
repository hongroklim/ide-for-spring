package product.option;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.main.ProductService;
import dev.rokong.product.option.ProductOptionController;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;

public class ControllerTest extends MvcUnitConfig {

    @Autowired ProductOptionController pOptionController;
    @Autowired ProductOptionService pOptionService;
    @Autowired ProductOptionDAO pOptionDAO;

    @Autowired ProductService pService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(pOptionController).build();
    }
    
    private int randomIdx(int size){
        double d = Math.random();
        int i = (int) d*100;

        return i % size;
    }

    private ProductDTO getAnyProduct(){
        List<ProductDTO> pList = pService.getProductList();
        assertThat(pList, is(notNullValue()));
        assertThat(pList.size(), is(greaterThan(0)));

        int index = this.randomIdx(pList.size());
        ProductDTO result = pList.get(index);

        assertThat(result, is(notNullValue()));
        return result;
    }

    private ProductOptionDTO getAnyProductOption(){
        List<ProductDTO> pList = pService.getProductList();
        List<ProductOptionDTO> list = null;
        ProductOptionDTO param = null;
        ProductOptionDTO result = null;

        for(ProductDTO p : pList){
            param = new ProductOptionDTO(p.getId());
            list = pOptionService.getPOptionList(param);
            if(list != null && list.size() > 0){
                result = list.get(this.randomIdx(list.size()));
                break;
            }
        }

        if(result == null){
            throw new NullPointerException("there are no product option");
        }

        return result;
    }

    private ProductOptionDTO anyPOption;

    @Before
    public void initAnyProductOption(){
        anyPOption = this.getAnyProductOption();
        assertThat(this.anyPOption, is(notNullValue()));
        assertThat(this.anyPOption.getProductId(), is(not(equalTo(0))));
    }

    @Test
    public void requestMapping() throws Exception {
        String url = "/product/"+anyPOption.getProductId()+"/option";
        List<ProductOptionDTO> res = this.reqAndResBody(url, RequestMethod.GET, null);
        assertThat(res, is(notNullValue()));

        url = "/product/"+anyPOption.getProductId()+"/option";
        url +="/group/"+anyPOption.getOptionGroup();
        res = this.reqAndResBody(url, RequestMethod.GET, null);

        url = "/product/"+anyPOption.getProductId()+"/option";
        url += "/group/"+anyPOption.getOptionGroup();
        url += "/id/"+"01";
        ProductOptionDTO res2 = this.reqAndResBody(url, RequestMethod.GET, null, ProductOptionDTO.class);
        assertThat(res2, is(notNullValue()));
    }

    private String pOptionURL(ProductOptionDTO pOption){
        assertThat(pOption.getProductId(), is(not(equalTo(0))));
        String url = "/product/"+anyPOption.getProductId()+"/option";
        if(pOption.getOptionGroup() == null){
            return url;
        }

        url += "/group/"+pOption.getOptionGroup();
        if(pOption.getOptionId() == null || "".equals(pOption.getOptionId())){
            return url;
        }

        url += "/id/"+pOption.getOptionId();
        return url;
    }

    @Test
    public void getPOptionsInProduct() throws Exception {
        String url = this.pOptionURL(new ProductOptionDTO(anyPOption.getProductId()));
        List<ProductOptionDTO> res = this.reqAndResBody(url, RequestMethod.GET, null);
        assertThat(res, is(notNullValue()));
    }

    @Test
    public void getPOptionsInGroup() throws Exception {
        String url = this.pOptionURL(new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup()));

        List<ProductOptionDTO> res = this.reqAndResBody(url, RequestMethod.GET, null);
        assertThat(res, is(notNullValue()));
    }

    @Test
    public void getPOption() throws Exception {
        String url = this.pOptionURL(anyPOption);

        ProductOptionDTO res = this.reqAndResBody(url, RequestMethod.GET, null, ProductOptionDTO.class);
        assertThat(res, is(notNullValue()));
    }

    @Test
    public void createProductOptionGroup() throws Exception {
        String url = this.pOptionURL(new ProductOptionDTO(anyPOption.getProductId()));
        url += "/group";
        ProductOptionDTO newGroup = new ProductOptionDTO(anyPOption.getProductId());
        newGroup.setName("new pOption group");

        ProductOptionDTO res = this.reqAndResBody(url, RequestMethod.POST, newGroup, ProductOptionDTO.class);
        assertThat(res, is(notNullValue()));
        assertThat(res.getProductId(), equalTo(newGroup.getProductId()));
        assertThat(res.getOptionId(), equalTo("00"));
    }

    @Test
    public void createProductOption() throws Exception {
        String url = this.pOptionURL(new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup()));
        ProductOptionDTO newOption = new ProductOptionDTO(anyPOption);
        newOption.setOptionId(null);
        newOption.setName("new option in list");

        ProductOptionDTO res = this.reqAndResBody(url, RequestMethod.POST, newOption, ProductOptionDTO.class);
        assertThat(res, is(notNullValue()));
        assertThat(res.getProductId(), equalTo(newOption.getProductId()));
        assertThat(res.getOptionGroup(), equalTo(newOption.getOptionGroup()));
        assertThat(res.getOptionId(), is(not(equalTo("00"))));

        //get list of group
        ProductOptionDTO param = new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup());
        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);

        //new option should be exists in that list
        boolean isExists = false;
        for(ProductOptionDTO pOption : list){
            if(pOption.getOptionId().equals(res.getOptionId())){
                isExists = true;
                break;
            }
        }
        assertThat(isExists, is(equalTo(true)));
    }

    @Test
    public void deleteProductOption() throws Exception {
        //get last option in option group
        ProductOptionDTO param = new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup()
        );
        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(greaterThan(0)));
        ProductOptionDTO tobeDeleted = list.get(list.size()-1);

        String url = this.pOptionURL(tobeDeleted);
        this.reqAndResBody(url, RequestMethod.DELETE, null, null);

        int asisSize = list.size();
        list = pOptionService.getPOptionList(param);
        assertThat(list.size(), is(equalTo(asisSize-1)));
    }

    @Test
    public void updateProductOption() throws Exception {
        //get max order in group
        ProductOptionDTO param = new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup()
        );
        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(greaterThan(0)));
        int maxOrd = list.get(list.size()-1).getOrd();

        //set tobe ord
        int tobeOrd = (anyPOption.getOrd() == maxOrd) ? 1 : maxOrd;

        ProductOptionDTO pOption = new ProductOptionDTO(anyPOption);
        pOption.setName("new option name");
        pOption.setOrd(tobeOrd);

        String url = this.pOptionURL(pOption);
        ProductOptionDTO res = this.reqAndResBody(url, RequestMethod.PUT,
            pOption, ProductOptionDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getName(), is(equalTo("new option name")));
        assertThat(res.getOrd(), is(equalTo(tobeOrd)));
    }

    public void deleteProductOptionGroup() throws Exception {

    }

    public void updateProductOptionGroupOrder() throws Exception {

    }
}
