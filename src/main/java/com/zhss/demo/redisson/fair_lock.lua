while true do
    local firstThreadId2 = redis.call('lindex', KEYS[2], 0);
    if firstThreadId2 == false then
        break ;
    end ;
    local timeout = tonumber(redis.call('zscore', KEYS[3], firstThreadId2));
    if timeout <= tonumber(ARGV[4]) then
        redis.call('zrem', KEYS[3], firstThreadId2);
        redis.call('lpop', KEYS[2]);
    else
        break ;
    end ;
end ;
if (redis.call('exists', KEYS[1]) == 0) then
    local nextThreadId = redis.call('lindex', KEYS[2], 0);
    if nextThreadId ~= false then
        redis.call('publish', KEYS[4] .. ':' .. nextThreadId, ARGV[1]);
    end ;
    return 1;
end ;
if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then
    return nil;
end ;
local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1);
if (counter > 0) then
    redis.call('pexpire', KEYS[1], ARGV[2]);
    return 0;
end ;
redis.call('del', KEYS[1]);
local nextThreadId = redis.call('lindex', KEYS[2], 0);
if nextThreadId ~= false then
    redis.call('publish', KEYS[4] .. ':' .. nextThreadId, ARGV[1]);
end ;
return 1; 
