import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
}