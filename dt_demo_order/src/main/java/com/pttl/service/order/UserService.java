package com.pttl.service.order;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pttl.distributed.transaction.annotation.DistributedTransaction;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;

@FeignClient(value = "user")
public interface UserService {
   /* @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @DistributedTransaction(action = "myorder2")
    String sayHiFromClientOne(DistributedTransactionContext context,@RequestParam(value = "name") String name);*/

    @RequestMapping("/payment")
    @DistributedTransaction(action = "userPayment")
    public boolean payment(@RequestBody DistributedTransactionContext distributedTransactionContext,@RequestParam("userId") int userId, @RequestParam("payment") double payment);

}
