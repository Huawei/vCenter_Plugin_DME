package com.huawei.vmware.util;

import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * @author lianq
 * @className TernaryTest
 * @description TODO
 * @date 2020/12/2 10:56
 */
public class TernaryTest {

    @InjectMocks
    Ternary ternary = new Ternary("21",21,true);


    @Test
    public void first() {
        ternary.first();
    }

    @Test
    public void testFirst() {
        ternary.first("21");
    }

    @Test
    public void second() {
        ternary.second();
    }

    @Test
    public void testSecond() {
        ternary.second(21);
    }

    @Test
    public void third() {
        ternary.third();
    }

    @Test
    public void testThird() {
        ternary.third(true);
    }

    @Test
    public void testHashCode() {
        ternary.hashCode();
    }

    @Test
    public void testEquals() {
        ternary.equals(ternary);
    }

    @Test
    public void testToString() {
        ternary.toString();
    }
}