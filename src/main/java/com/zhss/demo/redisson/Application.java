package com.zhss.demo.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.UUID;

public class Application {

    private static final Config config = new Config();

    static {
        config.useClusterServers()
                .addNodeAddress("redis://192.168.50.200:6409")
                .addNodeAddress("redis://192.168.50.200:6419")
                .addNodeAddress("redis://192.168.50.200:6429")
                .addNodeAddress("redis://192.168.50.200:6439")
                .addNodeAddress("redis://192.168.50.200:6449")
                .addNodeAddress("redis://192.168.50.200:6459")
                .addNodeAddress("redis://192.168.50.200:6469");
    }

    public static void main(String[] args) throws Exception {
//        reentrantLock();
        fairLock();
    }

    private static void fairLock() throws Exception {
        RedissonClient redisson = Redisson.create(config);

        System.out.println(UUID.randomUUID().toString());

        RLock lock = redisson.getFairLock("anyLock");


        lock.lock();
        lock.tryLock();

        Thread.sleep(1000);
        lock.unlock();
        lock.unlock();


    }

    public static void reentrantLock() throws Exception {
        RedissonClient redisson = Redisson.create(config);

        RLock lock = redisson.getLock("anyLock");
        lock.tryLock();


        lock.lock();

        Thread.sleep(1000);
        lock.unlock();

//		throw new Exception();
        System.exit(0);
    }

}
