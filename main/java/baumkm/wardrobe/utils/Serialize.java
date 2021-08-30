package baumkm.wardrobe.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serialize {

    /**
     * serializes an ItemStack to a String
      * @param item
     * @return
     * @throws IOException
     */
    public static String serializeItemStack(ItemStack item) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream data = new BukkitObjectOutputStream(outputStream);
        data.writeObject(item);
        data.close();
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    /**
     * deserializes an ItemStack from a String
     * @param s
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ItemStack deserializeItemStack(String s) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
        BukkitObjectInputStream data = new BukkitObjectInputStream(inputStream);
        return (ItemStack) data.readObject();

    }
}
