package config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.main.MainDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/context/root-context.xml"})
@Transactional @Rollback
public class SpringConfig {
    
    @Autowired protected ApplicationContext ac;

    @Autowired MainDAO mDAO;

    @Test
    public void initialization(){
        assertThat(1, is(equalTo(1)));
    }

    /**
     * set auto increment sequences
     * to max values of each table
     * <p>it will perform stored function
     * <code>SELECT reset_serial();</code>
     */
    @After
    public void resetSerial(){
        try{
            mDAO.resetSerial();
        }catch(UncategorizedSQLException e){
            //exception because a test failed
        }
    }

}