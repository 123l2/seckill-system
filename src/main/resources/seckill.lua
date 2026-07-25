-- seckill.lua - atomically decrement stock and record user
local stock_key = KEYS[1]
local user_key  = KEYS[2]
local user_id   = ARGV[1]

-- check if user already bought
if redis.call("sismember", user_key, user_id) == 1 then
    return 2  -- duplicate
end

-- check and decrement stock
local stock = tonumber(redis.call("get", stock_key))
if not stock or stock <= 0 then
    return 0  -- sold out
end

redis.call("decr", stock_key)
redis.call("sadd", user_key, user_id)
return 1  -- success
