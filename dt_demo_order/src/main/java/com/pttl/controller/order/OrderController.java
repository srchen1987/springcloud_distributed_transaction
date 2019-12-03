package com.pttl.controller.order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pttl.service.order.OrderService;
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
    

    @PostMapping(value = "/buyProduct")
    public String buyProduct(@RequestParam String product,@RequestParam String num){
        boolean result = orderService.buyProduct(product,num);
        if(result) {
            return "购买成功";
        }
        else {
            return "购买失败";
        }
    }
    
}
