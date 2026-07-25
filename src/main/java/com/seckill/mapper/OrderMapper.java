package com.seckill.mapper;

import com.seckill.entity.Order;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (user_id, product_id, order_no, amount, status, create_time, expire_time) " +
            "VALUES (#{userId}, #{productId}, #{orderNo}, #{amount}, #{status}, #{createTime}, #{expireTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Update("UPDATE orders SET status = #{status}, pay_time = NOW() WHERE order_no = #{orderNo}")
    int updateStatus(@Param("orderNo") String orderNo, @Param("status") int status);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    @Update("UPDATE orders SET status = 2 WHERE order_no = #{orderNo} AND status = 0")
    int cancelExpiredOrder(String orderNo);
}
