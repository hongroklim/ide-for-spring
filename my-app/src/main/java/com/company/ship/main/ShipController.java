package com.company.ship.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ship/{oId}")
@ResponseStatus(HttpStatus.OK)
public class ShipController {
    
    @Autowired ShipService shipService;

    

}