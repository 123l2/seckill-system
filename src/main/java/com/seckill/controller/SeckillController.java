package com.seckill.controller;

import com.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/seckill")
public class SeckillController {

    private final SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @PostMapping("/{productId}")
    public Map<String, Object> doSeckill(@PathVariable Long productId,
                                         @RequestParam Long userId) {
        long result = seckillService.seckill(userId, productId);
        String msg;
        if (result == 1)       msg = "success";
        else if (result == 2)  msg = "duplicate request";
        else                   msg = "sold out";
        return Map.of("code", result, "message", msg);
    }

    @GetMapping("/stock/{productId}")
    public Map<String, Object> stock(@PathVariable Long productId) {
        int remaining = seckillService.getRemainingStock(productId);
        return Map.of("productId", productId, "remaining", remaining);
    }
}
