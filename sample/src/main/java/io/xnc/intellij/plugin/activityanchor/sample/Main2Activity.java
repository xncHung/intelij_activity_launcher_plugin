package io.xnc.intellij.plugin.activityanchor.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import io.xnc.intellij.plugin.activityanchor.sample.bean.TestBean;
import io.xnc.intellij.plugin.activityanchor.sample.bean.TestSeri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "TestLauncher";
    private ListView lv;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lv = (ListView) findViewById(R.id.lv);

        final Map<String, Object> map = new HashMap<>();
        String testString = getIntent().getStringExtra("testString");
        Log.d(TAG, "testString: " + testString);
        map.put("testString", testString);

        int testInt = getIntent().getIntExtra("testInt", -10000);
        Log.d(TAG, "testInt: " + testInt);
        map.put("testInt", testInt);

        TestBean testParc = getIntent().getParcelableExtra("testParc");
        Log.d(TAG, "testParc: " + testParc);
        map.put("testParc", testParc);

        String[] testStrArray = getIntent().getStringArrayExtra("testStrArray");
        Log.d(TAG, "testStrArray: " + Arrays.toString(testStrArray));
        map.put("testStrArray", Arrays.toString(testStrArray));

        TestSeri testSeri = (TestSeri) getIntent().getSerializableExtra("testSeri");
        Log.d(TAG, "testSeri: " + testSeri);
        map.put("testSeri", testSeri);
        final ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(map.entrySet());
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return entries.size();
            }

            @Override
            public Map.Entry<String, Object> getItem(int position) {
                return entries.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View inflate = View.inflate(Main2Activity.this, R.layout.item, null);
                TextView key = inflate.findViewById(R.id.key);
                TextView value = inflate.findViewById(R.id.value);
                Map.Entry<String, Object> item = getItem(position);
                key.setText(item.getKey());
                value.setText(item.getValue().toString());
                return inflate;
            }
        };
        lv.setAdapter(adapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }
}
