package cn.xnc.intellij.plugin.activityanchor;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.xnc.intellij.plugin.activityanchor.ParamItem.*;

public class Anchor extends Activity {
    private static final String TAG = "ActivityLauncher";
    private static final String PARAMS_ARRAY = "params_array";
    private static final String TARGET_ACTIVITY = "target_activity";
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        String target_activity = getIntent().getStringExtra(TARGET_ACTIVITY);
        String params = getIntent().getStringExtra(PARAMS_ARRAY);
        Log.d(TAG, "onCreate: " + params);
        if (TextUtils.isEmpty(target_activity) || TextUtils.isEmpty(params)) {
            return;
        }
        try {
            byte[] decode = Base64.decode(params, Base64.DEFAULT);
            params = new String(decode);
            Type type = new TypeToken<List<ParamItem>>() {
            }.getType();
            Class<?> target = Class.forName(target_activity);
            Intent targetIntent = new Intent(this, target);
            List<ParamItem> paramItems = gson.fromJson(params, type);
            for (ParamItem param : paramItems) {
                resolveParam(targetIntent, param);
            }
            startActivity(targetIntent);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, target_activity + " is not found! ");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            finish();
        }
    }


    private void resolveParam(Intent intent, ParamItem param) throws ClassNotFoundException {
        String type = param.getType();
        Class realType;
        String realTypePath = param.getRealType();
        String value = param.getValue();
        String key = param.getKey();
        JsonElement jsonElement = new JsonParser().parse(value);
        JsonArray array;
        switch (type) {
            case STRING_OR_STRING_ARRAY:
            case STRING_ARRAY_LIST:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    String[] strArray = new String[array.size()];
                    ArrayList<String> strings = new ArrayList<>();
                    for (int index = 0; index < strArray.length; index++) {
                        String string = array.get(index).getAsString();
                        strArray[index] = string;
                        strings.add(string);
                    }
                    if (type.equals(STRING_ARRAY_LIST)) {
                        intent.putStringArrayListExtra(key, strings);
                    } else {
                        intent.putExtra(key, strArray);
                    }
                } else {
                    intent.putExtra(key, value);
                }
                break;
            case INT_OR_INT_ARRAY:
            case INTEGER_ARRAY_LIST:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    int[] intArray = new int[array.size()];
                    ArrayList<Integer> integers = new ArrayList<>();
                    for (int index = 0; index < intArray.length; index++) {
                        int asInt = array.get(index).getAsInt();
                        intArray[index] = asInt;
                        integers.add(asInt);
                    }
                    if (type.equals(INTEGER_ARRAY_LIST)) {
                        intent.putIntegerArrayListExtra(key, integers);
                    } else {
                        intent.putExtra(key, intArray);
                    }
                } else {
                    intent.putExtra(key, jsonElement.getAsInt());
                }
                break;
            case SERIALIZABLE:
                realType = Class.forName(realTypePath);
                if (realType.getTypeParameters().length > 0)
                    throw new IllegalStateException("ParameterizedType is not supported!");
                if (!Util.checkSerializable(realType))
                    throw new IllegalStateException(realType.getName() + " is not implement Serializable!");
                Serializable o = (Serializable) gson.fromJson(value, realType);
                intent.putExtra(key, o);
                break;
            case BOOLEAN_OR_BOOLEAN_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    boolean[] dataArray = new boolean[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsBoolean();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsBoolean());
                }
                break;
            case BYTE_OR_BYTE_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    byte[] dataArray = new byte[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsByte();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsByte());
                }
                break;
            case CHAR_OR_CHAR_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    char[] dataArray = new char[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsCharacter();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsCharacter());
                }
                break;
            case FLOAT_OR_FLOAT_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    float[] dataArray = new float[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsFloat();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsFloat());
                }
                break;
            case DOUBLE_OR_DOUBLE_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    double[] dataArray = new double[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsDouble();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsDouble());
                }
                break;
            case LONG_OR_LONG_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    long[] dataArray = new long[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsLong();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsLong());
                }
                break;
            case SHORT_OR_SHORT_ARRAY:
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    short[] dataArray = new short[array.size()];
                    for (int index = 0; index < dataArray.length; index++) {
                        dataArray[index] = array.get(index).getAsShort();
                    }
                    intent.putExtra(key, dataArray);
                } else {
                    intent.putExtra(key, jsonElement.getAsShort());
                }
                break;
            case PARCELABLE_ARRAY_LIST:
            case PARCELABLE_OR_PARCELABLE_ARRAY:
                realType = Class.forName(realTypePath);
                if (realType.getTypeParameters().length > 0)
                    throw new IllegalStateException("ParameterizedType is not supported!");
                if (!Util.checkParcelable(realType))
                    throw new IllegalStateException(realType.getName() + " is not implement Parcelable!");
                if (jsonElement.isJsonArray()) {
                    array = jsonElement.getAsJsonArray();
                    Parcelable[] dataArray = new Parcelable[array.size()];
                    ArrayList<Parcelable> parcelables = new ArrayList<>();
                    for (int index = 0; index < dataArray.length; index++) {
                        Parcelable item = (Parcelable) gson.fromJson(value, realType);
                        if (type.equals(PARCELABLE_ARRAY_LIST)) {
                            parcelables.add(item);
                        } else {
                            dataArray[index] = item;

                        }
                    }
                    if (type.equals(PARCELABLE_ARRAY_LIST)) {
                        intent.putParcelableArrayListExtra(key, parcelables);
                    } else {
                        intent.putExtra(key, dataArray);
                    }
                } else {
                    intent.putExtra(key, value);
                }
                break;
        }
    }

}
