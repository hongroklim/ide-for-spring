package pay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Slf4j
public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void deserialize(){
        String string = "{\n" +
                "  \"tid\": \"T1234567890123456789\",\n" +
                "  \"cid\": \"TC0ONETIME\",\n" +
                "  \"status\": \"SUCCESS_PAYMENT\",\n" +
                "  \"partner_order_id\": \"partner_order_id\",\n" +
                "  \"partner_user_id\": \"partner_user_id\",\n" +
                "  \"payment_method_type\": \"MONEY\",\n" +
                "  \"item_name\": \"초코파이\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"amount\": {\n" +
                "    \"total\": 2200,\n" +
                "    \"tax_free\": 0,\n" +
                "    \"vat\": 200,\n" +
                "    \"point\": 0,\n" +
                "    \"discount\": 0\n" +
                "  },\n" +
                "  \"canceled_amount\": {\n" +
                "    \"total\": 0,\n" +
                "    \"tax_free\": 0,\n" +
                "    \"vat\": 0,\n" +
                "    \"point\": 0,\n" +
                "    \"discount\": 0\n" +
                "  },\n" +
                "  \"cancel_available_amount\": {\n" +
                "    \"total\": 2200,\n" +
                "    \"tax_free\": 0,\n" +
                "    \"vat\": 200,\n" +
                "    \"point\": 0,\n" +
                "    \"discount\": 0\n" +
                "  },\n" +
                "  \"created_at\": \"2016-11-15T21:18:22\",\n" +
                "  \"approved_at\": \"2016-11-15T21:20:48\",\n" +
                "  \"payment_action_details\": [\n" +
                "    {\n" +
                "      \"aid\": \"A5678901234567890123\",\n" +
                "      \"payment_action_type\": \"PAYMENT\",\n" +
                "      \"payment_method_type\": \"MONEY\",\n" +
                "      \"amount\": 2200,\n" +
                "      \"point_amount\": 0,\n" +
                "      \"discount_amount\": 0,\n" +
                "      \"approved_at\": \"2016-11-15T21:20:48\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ObjectNode json = null;

        try {
            json = this.mapper.readValue(string, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertThat(json, is(notNullValue()));
        log.debug(json.toPrettyString());

        assertThat(json.get("amount").get("total").asInt(),
                is(equalTo(2200)));
    }

}
