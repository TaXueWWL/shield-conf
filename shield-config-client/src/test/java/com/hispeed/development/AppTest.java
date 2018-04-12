package com.hispeed.development;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    @org.junit.Test
    public void testMap() {
        Map<String, String> configMap = new ConcurrentHashMap<>();
        configMap.put("key1", "value1");
        configMap.put("key2", "value2");
        configMap.put("key3", "value3");
        configMap.put("key4", "value4");
        configMap.put("key5", "value5");

        List<Demo> configList = new CopyOnWriteArrayList<>();
        configList.add(new Demo("key1", "value1"));
        configList.add(new Demo("key2", "value2"));
        configList.add(new Demo("key3", "value3"));
        configList.add(new Demo("key4", "value4"));

        List<String> keyList = new CopyOnWriteArrayList<>();
        for (Demo demo : configList) {
            keyList.add(demo.getKey());
        }
        for (String key : configMap.keySet()) {
            if (keyList.contains(key)) {
                System.out.println(key + " 包含在list中，跳过本次");
            }
        }
    }

    class Demo {
        String key;
        String value;

        public Demo(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
