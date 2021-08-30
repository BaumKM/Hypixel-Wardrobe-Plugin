# Hypixel-Wardrobe-Plugin
Wardrobe Plugin inspired by Hypixel Skyblock

This is my own implementation of an Wardrobe Plugin inspired by the Hypixel Server

There are currently two versions of my plugin. The first version (JSON File) can be used directly without any work required to get it working. It uses a JSon file to save the wardrobe if a player leaves the server. The second version uses a SQL Database to save the wardrobe. You can find a example implementation for this in the lib File, but you have to replace some of the values with your own (e.g. database name, password, ...).


Currently features:
  - two pages of wardrobe slots where you can store your armor
  - use /w or /wardrobe to open your wardrobe
  - use /w <player> to open the wardrobe of another player (requires permissons, read only)
  - use /w <player> clear to clear the wardrobe of another player (requires permissions)

Know Bugs:
  - the wardobe is only saved if the playerLeaveEvent is triggered so if the server shutsdown without kicking all players first it will not save the warobe for the affected    
    players
  
  
  
Structure of the plugin:
  - a warobe is represented by a Object[] array with 55 slots. Each inventory slot (9*6 inventory) of a wardrobe page is represented by a corresponding a slot in the wardrobe array (54 slots). The 55th slot is reserved for the currently active set ("none", 0,1,2,3,..,8)
  - inside the array ItemStacks wich belong to armor are directly stored
  - ItemSTacks that are equal for all players are represented by a String code and created once when the server starts in order to not create them over and over again (e.g. glass panes)
  - when the player opens the wardrobe a for loop iterates through the array and creates a inventory based on the array
  - String code for glass panes:
     e.g r3 --> red glasspane set 3 (lore 3)
  - when the player leaves the array is serialized:
      --> Strings stay as they are + a "s" is appended
      --> Integers are converted to a String  + an "i" is appended
      --> Itemstacks are serialized and converted to a String + a "t" is appended
   --> the output is a JSON Array with only Strings in it wich then can be stored in a JSON File or a Database
  based on the last letter when a player joins again the String array is again deserialized into an Object[] array
  
  
  
