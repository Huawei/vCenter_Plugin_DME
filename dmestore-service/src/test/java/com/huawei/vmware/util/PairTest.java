package com.huawei.vmware.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className PairTest
 * @description TODO
 * @date 2020/12/2 11:02
 */
public class PairTest {

    @InjectMocks
    Pair pair = new Pair("21", 21);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void first() {
        pair.first();
    }

    @Test
    public void second() {
        pair.second();
    }

    @Test
    public void testSecond() {
        pair.second(21);
    }

    @Test
    public void testFirst() {
        pair.first("21");
    }

    @Test
    public void set() {
        pair.set("21", 21);

    }

    @Test
    public void testHashCode() {
        pair.hashCode();
    }

    @Test
    public void testEquals() {
        pair.equals(pair);
    }

    @Test
    public void testToString() {
        pair.toString();
    }
}