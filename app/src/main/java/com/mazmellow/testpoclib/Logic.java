package com.mazmellow.testpoclib;

import android.util.Log;

public class Logic {

    private final String TAG = Logic.class.getSimpleName();


    public String method1()  {
        Log.d(TAG, "Call method1");
        return "method1 output";
    }

    public void method2()  {
        Log.d(TAG, "Call method2: ");
    }

    public Object method3()  {
        Log.d(TAG, "Call method3: ");
        return "method3 output";
    }

}
