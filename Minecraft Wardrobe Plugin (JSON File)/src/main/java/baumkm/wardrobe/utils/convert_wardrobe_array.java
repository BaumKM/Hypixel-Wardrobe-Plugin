package baumkm.wardrobe.utils;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class convert_wardrobe_array {

    /**
     * converts the Object[] array into a String <br>
     * --> <font color = "red"> ItemStacks </font> are serialized to a <font color = "red"> String </font> <br>
     * -->  <font color = "red"> Integers </font> are converted into a  <font color = "red"> String</font> <br>
     * --> new Array only contains Strings <br>
     * --> Array is converted into a Json Array (String) <br>
     * --> Json Array is can be stored in a database <br>
     * <br>
     * -->  <font color = "yellow"> in order to be able to keep the information <br>    about the  initial datatype a letter is added at the end of each String <br>
     * --> String s | Integer i | ItemStack t
      * @param arr Array
     * @return String
     */

    public static String convertArrayInString(Object[] arr){
        String[] s = new String[55];

        for(int x=0; x< arr.length; x++ ){
            String f = "";
            Object o = arr[x];
            if(o instanceof String){
                f = (String) o;
                f = f + "s";
            }
            else if(o instanceof Integer){
                f = String.valueOf(o);
                f = f + "i";
            }
            else if(o instanceof ItemStack){
                try {
                    f= Serialize.serializeItemStack((ItemStack) o);
                    f = f+"t";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            s[x] = f;
        }
        System.out.println(JsonString.ArrayToJson(s).length());
        return JsonString.ArrayToJson(s);
    }

    /**
     * converts a String into the Object[] array
     * @param b
     * @return Object[]
     */
    public static Object[] convertStringInArray(String b){
        String[] s = JsonString.JsonToArray(b);

        Object[] o = new Object[55];

        for (int x = 0; x < s.length; x++){
            String f = s[x];
            String type = String.valueOf(f.charAt(f.length()-1));
            Object result = null;

            switch (type) {
                case "s" -> result = f.substring(0, f.length() - 1);
                case "i" -> result = Integer.valueOf(f.substring(0, f.length() - 1));
                case "t" -> {
                    String t = f.substring(0, f.length() - 1);
                    try {
                        result = Serialize.deserializeItemStack(t);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            o[x] = result;
        }

        return o;

    }
}
