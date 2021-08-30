package baumkm.wardrobe.utils;

import com.google.gson.Gson;

public class JsonString {

    /**
     * converts a Array with Strings into a Json String in order to easily store the array in the Database
     * @param arr
     * @return
     */
    public static String ArrayToJsonString(String[] arr){
        Gson gson = new Gson();
        return gson.toJson(arr);
    }

    /**
     * converts a Json String into an Array with Strings
     * @param json
     * @return
     */
    public static String[] JsonStringToArray(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, String[].class);
    }
}
