import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Test;

public class MainTest extends Config {
    @Test
    public void databaseConnect(){
        int a = 10;
        int b = 15;

        assertThat(a+5, equalTo(b));
    }
}