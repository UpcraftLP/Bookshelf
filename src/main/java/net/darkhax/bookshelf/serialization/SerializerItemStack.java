package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerItemStack implements ISerializer<ItemStack> {
    
    public static final ISerializer<ItemStack> SERIALIZER = new SerializerItemStack();
    
    private SerializerItemStack() {
        
    }
    
    @Override
    public ItemStack read (JsonElement json) {
        
        if (json.isJsonObject()) {
            
            return ShapedRecipe.deserializeItem(json.getAsJsonObject());
        }
        
        throw new JsonParseException("Expected JSON object, got " + JSONUtils.toString(json));
    }
    
    @Override
    public JsonElement write (ItemStack toWrite) {
        
        final JsonObject json = new JsonObject();
        
        json.addProperty("item", toWrite.getItem().getRegistryName().toString());
        json.addProperty("count", toWrite.getCount());
        
        if (toWrite.hasTag()) {
            
            json.add("nbt", Serializers.NBT.write(toWrite.getTag()));
        }
        
        return json;
    }
    
    @Override
    public ItemStack read (PacketBuffer buffer) {
        
        return buffer.readItemStack();
    }
    
    @Override
    public void write (PacketBuffer buffer, ItemStack toWrite) {
        
        buffer.writeItemStack(toWrite);
    }
}