package com.paulzhangcc.demo.lock;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by paul on 2017/11/16.
 */
public class ZookeeperLock implements Lock{
    @Override
    public void lock(String key) {

    }

    @Override
    public boolean tryLock(String key) {
        return false;
    }

    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean tryLock(List<String> keys) {
        return false;
    }

    @Override
    public boolean tryLock(List<String> keys, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean unLock(String key) {
        return false;
    }

    @Override
    public void unLock(List<String> keys) {

    }
}
