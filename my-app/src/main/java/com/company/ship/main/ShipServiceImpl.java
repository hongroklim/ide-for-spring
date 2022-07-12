package com.company.ship.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.dto.ShipDTO;

@Service
public class ShipServiceImpl implements ShipService {
    
    @Autowired ShipDAO shipDAO;

    /*

    order -> ship

    When customer complete payment,
    the ord_main.status_cd will be changed
    from OrderStatus.PAYMENT_READY to OrderStatus.CHECKING.

    after that moment, 

    */

    public ShipDTO initShip(int orderId){
        
        
        return null;
    }
}