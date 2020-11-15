package dev.rokong.mock;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("MockObjects")
public class MockObjects {
    
    @Autowired public MockUser user;
    @Autowired public MockCategory category;
    @Autowired public MockProduct product;
    @Autowired public MockProductOption pOption;
    @Autowired public MockProductDetail pDetail;

    public String randomString(int length){
        UUID u = UUID.randomUUID();
        String s = u.toString();
        s = s.replaceAll("[^A-Za-z]+", "");

        while(s.length() < length){
            u = UUID.randomUUID();
            s += u.toString().replaceAll("[^A-Za-z]+", "");
        }
        
        return s.substring(0, length);
    }

    public String randomString(){
        return this.randomString(4);
    }

    public int randomInt(int length){
        double result = Math.random();
        int i = (int) (result * (Math.pow(10, length+1)));
        return (int) (i % (Math.pow(10, length)));
    }

    public int randomInt(){
        return this.randomInt(4);
    }
}