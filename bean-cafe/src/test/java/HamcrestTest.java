import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.dto.ProductOptionDTO;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HamcrestTest {
    @Test
    public void matcherTest(){
        String a = "0123456789";
        a += a + a + a + a;
        a += "0";
        System.out.println(a.length());
        assertThat(a.length(), greaterThan(50));
        assertThat(a, is(not(nullValue())));
    }

    @Test
    public void optionIdTest(){
        String optionId = "9";
        int ascii = (int) optionId.charAt(0);
        String result = Character.toString((char) ++ascii);
        assertThat(result, is(not(equalTo("A"))));
    }

    @Test
    public void optionIdMethod(){
        String optionId = "00";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("01")));
        assertThat(optionId, is(equalTo("00")));

        optionId = "09";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("0A")));

        optionId = "0Z";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("0a")));

        optionId = "0z";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("10")));

        optionId = "01";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("02")));

        optionId = "0C";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("0D")));

        optionId = "0y";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("0z")));

        optionId = "zz";
        assertThat(ProductOptionDTO.nextId(optionId), is(equalTo("00")));
    }

    @Test
    public void getIdOfGroup(){
        ProductDetailDTO pDetail = new ProductDetailDTO(2, "010201");
        assertThat(pDetail.idOfGroup(1), is(equalTo("01")));
        assertThat(pDetail.idOfGroup(2), is(equalTo("02")));
        assertThat(pDetail.idOfGroup(3), is(equalTo("01")));
    }

    @Test
    public void mediaType(){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        String typeName = mediaType.toString();

        assertThat(typeName, is(equalTo(MediaType.APPLICATION_JSON_VALUE)));
    }

    @Test
    public void standardCharset(){
        Charset charset = StandardCharsets.UTF_8;
        String charsetName = "UTF-8";

        assertThat(charset.name(), is(equalTo(charsetName)));
    }

    @Test
    public void ObjectType(){
        Object obj = null;

        //define variables with different type
        int intValue = 13;
        String stringValue = "string";
        boolean booleanValue = false;
        Double doubleValue = 1.2;

        obj = intValue;
        log.debug("int class : "+obj.getClass());
        assertThat(obj.getClass(), is(equalTo(Integer.class)));

        obj = stringValue;
        log.debug("string class : " + obj.getClass());
        assertThat(obj.getClass(), is(equalTo(String.class)));

        obj = booleanValue;
        log.debug("boolean class : " + obj.getClass());
        assertThat(obj.getClass(), is(equalTo(Boolean.class)));

        obj = doubleValue;
        log.debug("double class : "+obj.getClass());
        assertThat(obj.getClass(), is(equalTo(Double.class)));

    }
}