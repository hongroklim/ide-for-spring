package dev.rokong.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("restMainController")
@RequestMapping("/rest")
public class MainController {
    
    @RequestMapping(value="/index/{name}", method=RequestMethod.GET)
    public Map<String, Object> index(@PathVariable String name){
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("name", name);
        return hashMap;
    }
}