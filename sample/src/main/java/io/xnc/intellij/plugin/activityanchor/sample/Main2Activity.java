package io.xnc.intellij.plugin.activityanchor.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import io.xnc.intellij.plugin.activityanchor.sample.bean.TestBean;
import io.xnc.intellij.plugin.activityanchor.sample.bean.TestSeri;

import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "TestLauncher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        String testString = getIntent().getStringExtra("testString");
        Log.d(TAG, "testString: " + testString);

        int testInt = getIntent().getIntExtra("testInt", -10000);
        Log.d(TAG, "testInt: " + testInt);

        TestBean testParc = getIntent().getParcelableExtra("testParc");
        Log.d(TAG, "testParc: " + testParc);

        String[] testStrArray = getIntent().getStringArrayExtra("testStrArray");
        Log.d(TAG, "testStrArray: " + Arrays.toString(testStrArray));

        TestSeri testSeri = (TestSeri) getIntent().getSerializableExtra("testSeri");
        Log.d(TAG, "testSeri: " + testSeri);
    }
}
