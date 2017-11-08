package com.example.uporabnik.stevec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void increment_isCorrect() throws Exception {
        for (int i = 0; i < 20; i++){
            int j = MainActivity.inc(i);
            assertEquals(i + 1, j);
        }


    }
}