package baumkm.wardrobe.utils;

import com.google.gson.Gson;


public class JsonString {

    /**
     * converts a Array with Strings into a Json String in order to easily store the array in the Database
     * @param arr Array
     * @return
     */
    public static String ArrayToJson(String[] arr){
        Gson gson = new Gson();
        return gson.toJson(arr);


    }

    /**
     * converts a Json Array into an Array with Strings
     * @param json Json Array (String)
     * @return
     */
    public static String[] JsonToArray(String json){
        Gson gson = new Gson();
        return gson.fromJson(json ,String[].class);
    }
}
