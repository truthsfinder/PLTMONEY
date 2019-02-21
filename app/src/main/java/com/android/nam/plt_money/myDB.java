package com.android.nam.plt_money;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class myDB {
    private SharedPreferences preferences;

    public myDB(Context baseContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(baseContext);
    }

    public void putDouble(String key, double val) {
        checkForNullKey(key);
        putString(key, String.valueOf(val));
    }

    private void putString(String key, String val) {
        checkForNullKey(key);
        checkForNullValue(val);
        preferences.edit().putString(key,val).apply();
    
    }

    public void putListString(String key, ArrayList<String> addCategory) {
        checkForNullKey(key);
        String[] myStringList = addCategory.toArray(new String[addCategory.size()]);
        preferences.edit().putString(key, TextUtils.join(",=,", myStringList)).apply();
    }

    public void putListObject(String key, ArrayList<Object> newExpensesListObject) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objString = new ArrayList<>();

        for (Object obj: newExpensesListObject){
            objString.add(gson.toJson(obj));
        }
        putListString(key, objString);
    }

    public Object getObject(String key, Class<Date> dateClass) {
        String json = getString(key);
        Object val = new Gson().fromJson(json, dateClass);
        if(val==null)
            throw new NullPointerException();
        return val;
    }

    public double getDouble(String key, double i) {
        String num = getString(key);
        try{
            return Double.parseDouble(num);
        }catch (NumberFormatException e){
            return i;
        }
    }

    private String getString(String key) {
        return preferences.getString(key,"");
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key,""),",=,")));
    }

    public ArrayList<Object> getListObject(String key, Class<Expenses> expensesClass) {
        Gson gson = new Gson();

        ArrayList<String> objString = getListString(key);
        ArrayList<Object> objects = new ArrayList<>();
        
        for(String jObjString: objString){
            Object val = gson.fromJson(jObjString, expensesClass);
            objects.add(val);
        }
        return objects;
    }
    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<Integer>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }

    private void checkForNullKey(String key) {
        if(key==null){
            throw new NullPointerException();
        }
    }

    private void checkForNullValue(String val) {
        if(val==null){
            throw new NullPointerException();
        }
    }

    public void putObject(String key, Date date) {
    }
}
