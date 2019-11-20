package com.pttl.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.service.user.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userService;
	
	@RequestMapping("/payment")
	public boolean payment(@RequestBody DistributedTransactionContext distributedTransactionContext,@RequestParam("userId") int userid,@RequestParam("payment") double payment) {
		System.out.println("#########"+distributedTransactionContext.getAttachment());
		userService.updatePaymentUser(distributedTransactionContext.getBranchTxId(),userid, payment);
		return false;
	}
	
}
