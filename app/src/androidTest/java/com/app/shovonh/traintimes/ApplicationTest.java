package com.app.shovonh.traintimes;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testNames() {
        String stationName = "Inman Park/Reynoldstown";
        assertEquals("Inman Park Station", stationName.substring(0, stationName.indexOf("/")) + " Station");
    }


}