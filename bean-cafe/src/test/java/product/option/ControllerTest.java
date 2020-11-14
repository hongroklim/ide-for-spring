package product.option;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.CategoryDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.dto.UserDTO;
import dev.rokong.product.main.ProductService;
import dev.rokong.product.option.ProductOptionController;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;
import dev.rokong.user.UserService;
import dev.rokong.util.ListUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired ProductOptionController pOptionController;
    @Autowired ProductOptionService pOptionService;
    @Autowired ProductOptionDAO pOptionDAO;

    @Autowired ProductService pService;

    @Autowired UserService uService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(pOptionController).build();
    }
    
    private int randomIdx(int size){
        double d = Math.random();
        int i = (int) d*100;

        return i % size;
    }

    @SuppressWarnings("unused")
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
        List<ProductOptionDTO> res = this.reqAndResBodyList(url, RequestMethod.GET, null, ProductOptionDTO.class);
        assertThat(res, is(notNullValue()));

        url = "/product/"+anyPOption.getProductId()+"/option";
        url +="/group/"+anyPOption.getOptionGroup();
        res = this.reqAndResBodyList(url, RequestMethod.GET, null, ProductOptionDTO.class);

        url = "/product/"+anyPOption.getProductId()+"/option";
        url += "/group/"+anyPOption.getOptionGroup();
        url += "/id/"+"01";
        ProductOptionDTO res2 = this.reqAndResBody(url, RequestMethod.GET, null, ProductOptionDTO.class);
        assertThat(res2, is(notNullValue()));
    }

    private String pOptionURL(ProductOptionDTO pOption){
        assertThat(pOption.getProductId(), is(not(equalTo(0))));
        String url = "/product/"+pOption.getProductId()+"/option";
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
        List<ProductOptionDTO> res = this.reqAndResBodyList(url, RequestMethod.GET, null, ProductOptionDTO.class);
        assertThat(res, is(notNullValue()));
        assertThat(res.get(0).getProductId(), is(equalTo(anyPOption.getProductId())));
    }

    @Test
    public void getPOptionsInGroup() throws Exception {
        String url = this.pOptionURL(new ProductOptionDTO(
            anyPOption.getProductId(), anyPOption.getOptionGroup()));

        List<ProductOptionDTO> res = this.reqAndResBodyList(url, RequestMethod.GET, null, ProductOptionDTO.class);
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
        assertThat(res.getOptionId(), equalTo(ProductOptionDTO.TITLE_ID));
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
        assertThat(res.getOptionId(), is(not(equalTo(ProductOptionDTO.TITLE_ID))));

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

        this.verifyOptionIdOrder(param.getProductId(), param.getOptionGroup());
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

        this.verifyOptionIdOrder(param.getProductId(), param.getOptionGroup());
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

        this.verifyOptionIdOrder(param.getProductId(), param.getOptionGroup());
    }

    @Test
    public void deleteProductOptionGroup() throws Exception {
        ProductOptionDTO param = new ProductOptionDTO(this.anyPOption.getProductId());
        String url = this.pOptionURL(param);
        url += "/group";

        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);
        int maxGroupBefore = list.get(list.size()-1).getOptionGroup();

        this.reqAndResBody(url, RequestMethod.DELETE, null, null);

        list = pOptionService.getPOptionList(param);
        int maxGroupAfter = (list == null || list.size() == 0)
                ? 0 : list.get(list.size()-1).getOptionGroup();
        
        if(maxGroupAfter != 0){
            this.verifyOptionGroupOrder(param.getProductId());
        }

        assertThat(maxGroupAfter, is(lessThan(maxGroupBefore)));
    }

    private UserDTO getAnyUser(){
        return ListUtil.randomItem(uService.getUsers());
    }

    /**
     * create sample data of product option.
     * 
     * @return new product's id
     */
    private int initializeProductOption() {
        //create new product
        ProductDTO product = new ProductDTO();
        product.setCategoryId(CategoryDTO.ETC_ID);
        product.setSellerNm(this.getAnyUser().getUserNm());
        product.setName("new product test");
        product.setPrice(1000);
        product = pService.createProduct(product);
        assertThat(product, is(notNullValue()));
        assertThat(product.getId(), is(greaterThan(0)));

        //create new product's option
        ProductOptionDTO pOption = new ProductOptionDTO(product.getId());
        pOption.setName("first group");
        pOptionService.createPOptionGroup(pOption);

        pOption.setName("second group");
        pOptionService.createPOptionGroup(pOption);

        pOption.setName("third group");
        pOptionService.createPOptionGroup(pOption);
        
        List<ProductOptionDTO> optionList = pOptionService.getPOptionList(pOption);
        assertThat(ListUtil.isNotEmpty(optionList), is(equalTo(true)));

        //append option's detail
        pOption.setOptionGroup(1);
        pOption.setName("1-1 element");
        pOptionService.createPOption(pOption);

        pOption.setName("1-2 element");
        pOptionService.createPOption(pOption);

        pOption.setOptionGroup(2);
        pOption.setName("2-1 element");
        pOptionService.createPOption(pOption);

        pOption.setOptionGroup(3);
        pOption.setName("3-1 element");
        pOptionService.createPOption(pOption);

        pOption.setName("3-2 element");
        pOptionService.createPOption(pOption);

        pOption.setOptionGroup(null);
        pOption.setOptionId(null);
        optionList = pOptionService.getPOptionList(pOption);
        assertThat(ListUtil.isNotEmpty(optionList), is(equalTo(true)));
        assertThat(optionList.size(), is(equalTo(8)));

        return product.getId();
    }

    @Test
    public void updateProductOptionGroupOrder() throws Exception {
        int productId = this.initializeProductOption();

        List<ProductOptionDTO> list = pOptionService.getPOptionList(
            new ProductOptionDTO(productId)
        );
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(greaterThan(1)));
        
        ProductOptionDTO param = ListUtil.randomItem(list);

        int maxGroup = list.get(list.size()-1).getOptionGroup();

        int asisGroup = param.getOptionGroup();
        int tobeGroup = (param.getOptionGroup()==maxGroup) ? 1 : maxGroup;

        ProductOptionDTO urlParam = new ProductOptionDTO(
            param.getProductId(), asisGroup
        );
        String url = this.pOptionURL(urlParam);

        param.setOptionGroup(tobeGroup);
        
        ProductOptionDTO res = this.reqAndResBody(url, RequestMethod.PUT,
                param, ProductOptionDTO.class);
        
        this.verifyOptionGroupOrder(param.getProductId());

        assertThat(res, is(notNullValue()));
        assertThat(res.getProductId(), is(equalTo(param.getProductId())));
        assertThat(res.getOptionGroup(), is(equalTo(tobeGroup)));
        assertThat(res.getOptionId(), is(equalTo(ProductOptionDTO.TITLE_ID)));
    }

    /**
     * verify Option Group's sequence (1, 2, 3 ...)<p/>
     * if option group is empty, it will not throw any Exceptions.
     * 
     * @param productId
     * @exception AssertionError if serialized option group doesn't match sequence
     */
    private void verifyOptionGroupOrder(int productId) {
        ProductOptionDTO pOption = new ProductOptionDTO(productId);
        List<ProductOptionDTO> list = pOptionService.getPOptionList(pOption);

        if(list == null || list.size() == 0){
            return;
        }else{
            assertThat(list, is(notNullValue()));
            assertThat(list.size(), is(greaterThan(0)));
        }

        int seq = 0;

        for(int i=0; i<list.size(); i++){
            if(ProductOptionDTO.TITLE_ID.equals(list.get(i).getOptionId())){
                assertThat(list.get(i).getOptionGroup(), is(equalTo(++seq)));
            }
        }
    }

    /**
     * verify Option Ids following order<p/>
     * if list of option id is empty, it will not throw any Exceptions.
     * 
     * @param productId
     * @param optionGroup
     * @exception IllegalArgumentException if invalid option id exists in list
     * @exception AssertionError if serialized option id doesn't follow order
     */
    private void verifyOptionIdOrder(int productId, int optionGroup){
        ProductOptionDTO pOption = new ProductOptionDTO(productId, optionGroup);
        List<ProductOptionDTO> list = pOptionService.getPOptionList(pOption);

        if(list == null || list.size() > 0){
            return;
        }else{
            assertThat(list, is(notNullValue()));
            assertThat(list.size(), is(greaterThan(1)));
        }

        String beforeId = list.get(0).getOptionId();
        String afterId = null;
        ProductOptionDTO.verifyId(beforeId);

        for(int i=1; i<list.size(); i++){
            afterId = list.get(i).getOptionId();
            ProductOptionDTO.verifyId(afterId);
            assertThat(this.secondIsLater(beforeId, afterId), is(equalTo(true)));
            beforeId = afterId;
        }
    }

    private boolean secondIsLater(String firstId, String secondId){
        String firstPrefix = firstId.substring(0, 1);
        String firstSuffix = firstId.substring(1, 2);

        String secondPrefix = secondId.substring(0 ,1);
        String secondSuffix = secondId.substring(1, 2);

        if(firstPrefix.equals(secondPrefix)){
            return secondCharIsLater(firstSuffix, secondSuffix);
        }
        
        if(firstPrefix.matches("^[0-9]$")){
            return (!secondPrefix.matches("^[0-9]$"))
                    ? true : secondCharIsLater(firstPrefix, secondPrefix);

        }else if(firstPrefix.matches("^[A-Z]$")){
            return (secondPrefix.matches("^[0-9]$"))
                    ? false : secondCharIsLater(firstPrefix, secondPrefix);

        }else if(firstPrefix.matches("^[a-z]$")){
            return (secondPrefix.matches("^[a-z]$"))
                    ? secondCharIsLater(firstPrefix, secondPrefix) : true;
            
        }

        //if no conditions are true, this method will throw Exception
        ProductOptionDTO.verifyId(firstPrefix);

        return false;
    }

    private boolean secondCharIsLater(String firstChar, String secondChar){
        int firstAscii = (int) firstChar.charAt(0);
        int secondAscii = (int) secondChar.charAt(0);
        
        if(firstChar.matches("^[0-9]$")){
            return (!secondChar.matches("^[0-9]$"))
                    ? true : firstAscii < secondAscii;

        }else if(firstChar.matches("^[A-Z]$")){
            return (secondChar.matches("^[0-9]$"))
                    ? false : firstAscii < secondAscii;

        }else if(firstChar.matches("^[a-z]$")){
            return (secondChar.matches("^[a-z]$"))
                    ? firstAscii < secondAscii : true;
        }

        //if no conditions are true, this method will throw Exception
        ProductOptionDTO.verifyId(firstChar);

        return false;
    }

    @Test
    public void verifyCharTest(){
        assertThat(this.secondCharIsLater("0", "9"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("9", "A"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("A", "Z"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("Z", "a"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("a", "z"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("0", "A"), is(equalTo(true)));
        assertThat(this.secondCharIsLater("1", "z"), is(equalTo(true)));
    }

    @Test
    public void verifyIdTest(){
        assertThat(this.secondIsLater("01", "09"), is(equalTo(true)));
        assertThat(this.secondIsLater("08", "0A"), is(equalTo(true)));
        assertThat(this.secondIsLater("0A", "A0"), is(equalTo(true)));
        assertThat(this.secondIsLater("0A", "0B"), is(equalTo(true)));
        assertThat(this.secondIsLater("AZ", "Aa"), is(equalTo(true)));
        assertThat(this.secondIsLater("A1", "z9"), is(equalTo(true)));
        assertThat(this.secondIsLater("b1", "ba"), is(equalTo(true)));
    }
}