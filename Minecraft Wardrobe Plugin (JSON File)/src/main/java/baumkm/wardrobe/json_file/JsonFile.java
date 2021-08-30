package baumkm.wardrobe.json_file;

import baumkm.wardrobe.utils.convert_wardrobe_array;
import com.google.gson.*;
import org.bukkit.entity.Player;

import java.io.*;

public class JsonFile {
    private static File file = null;

    /**
     *
     * @param p Player
     * @param array JsonArray
     */
    public static void savePlayerArray(Player p, String array, int page) {
         /* structure of the json file
        {players:{
            uuid1:{
                "1": [array],
                "2": [array]
            },
            uuid2: {
                "1": [array],
                "2": [array]
            },
            uuid3:{
                "1": [array],
                "2": [array]
            }
        }}

         */
        JsonParser jsonParser = new JsonParser();
        JsonObject mainJson = new JsonObject();
        JsonObject secondJson;
        JsonObject thirdJson = null;

        readOldFile: try {
            FileReader reader = new FileReader(file);

            //outer bracket
            JsonElement element = jsonParser.parse(reader);
            reader.close();


            if (element.isJsonNull()) {
                secondJson = new JsonObject();
                thirdJson = new JsonObject();
                break readOldFile;
            }
            JsonObject playerList = element.getAsJsonObject().get("players").getAsJsonObject();
            secondJson = playerList; //keep all other player
            JsonObject playerArrays = playerList.getAsJsonObject(p.getUniqueId().toString()); //keep  all other pages

            if(playerArrays == null){
                thirdJson = new JsonObject();
                break readOldFile;
            }
            thirdJson = playerArrays;




        }
        catch (IOException | IllegalStateException | NullPointerException e) {
            secondJson = new JsonObject();
            thirdJson = new JsonObject();
            e.printStackTrace();
        }
        thirdJson.add(String.valueOf(page),new Gson().fromJson(array, JsonElement.class));
        secondJson.add(p.getUniqueId().toString(), thirdJson);

        mainJson.add("players", secondJson);


        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(mainJson.toString());
            fileWriter.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * gets the saved array from a player
     * @param p Player
     * @return array, null if no entry was found
     */
    public static Object[] get_player(Player p, int page){
        /* structure of the json file
        {players:{
            uuid1:{
                "1": [array],
                "2": [array]
            },
            uuid2: {
                "1": [array],
                "2": [array]
            },
            uuid3:{
                "1": [array],
                "2": [array]
            }
        }}

         */
        JsonParser jsonParser = new JsonParser();

        readOldFile: try {

            FileReader reader = new FileReader(file);
            JsonElement element = jsonParser.parse(reader);
            reader.close();

            if (element.isJsonNull()) break readOldFile;

            JsonObject playersList = element.getAsJsonObject().get("players").getAsJsonObject();

            if(playersList.getAsJsonObject(p.getUniqueId().toString()) == null) break readOldFile;

            JsonElement playerArray = playersList.getAsJsonObject(p.getUniqueId().toString()).get(String.valueOf(page));

            if(playerArray == null) break readOldFile;
            return convert_wardrobe_array.convertStringInArray(playerArray.getAsJsonArray().toString());




        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }

    public static void create_file(){
        if (file == null) {
            file = new File("plugins/player_array.json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
