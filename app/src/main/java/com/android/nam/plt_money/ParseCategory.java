package com.android.nam.plt_money;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseCategory {
    public static String[] category_id;
    public static String[] user_id;
    public static String[] category_name;
    public static String[] category_status;

    public static final String JSON_ARRAY = "result";
    public static final String CATEGORY_ID = "category_id";
    public static final String USER_ID = "user_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_STATUS = "category_status";

    private JSONArray categories = null;

    private String json;

    public ParseCategory(String json){
        this.json = json;
    }

    protected void parseCategory(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            categories = jsonObject.getJSONArray(JSON_ARRAY);

            category_id = new String[categories.length()];
            user_id = new String[categories.length()];
            category_name = new String[categories.length()];
            category_status = new String[categories.length()];

            for(int i=0;i<categories.length();i++){
                JSONObject jo = categories.getJSONObject(i);
                category_id[i] = jo.getString(CATEGORY_ID);
                user_id[i] = jo.getString(USER_ID);
                category_name[i] = jo.getString(CATEGORY_NAME);
                category_status[i] = jo.getString(CATEGORY_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}