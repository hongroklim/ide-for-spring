package dev.rokong.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("MockObjects")
public class MockObjects {
    
    @Autowired public MockCart cart;
    
    @Autowired public MockCategory category;

    @Autowired public MockDelivery delivery;

    @Autowired public MockOrder order;
    @Autowired public MockOrderProduct oProduct;

    @Autowired public MockPayType payType;
    
    @Autowired public MockProduct product;
    @Autowired public MockProductOption pOption;
    @Autowired public MockProductDetail pDetail;
    @Autowired public MockProductDelivery pDelivery;
    
    @Autowired public MockUser user;

    @Autowired public MockReview review;

}