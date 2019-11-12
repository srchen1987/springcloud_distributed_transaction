package com.pttl.mapper.order;

import com.pttl.service.order.entity.Order;
import org.springframework.stereotype.Repository;

/**
 * Created by 24968 on 2019/11/4.
 */
@Repository
public interface OrderMapper {
    int insertOrder(Order order);
}
