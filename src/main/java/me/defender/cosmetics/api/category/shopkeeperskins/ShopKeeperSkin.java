package me.defender.cosmetics.api.category.shopkeeperskins;

import com.cryptomorin.xseries.XMaterial;
import me.defender.cosmetics.api.category.Cosmetics;
import me.defender.cosmetics.api.enums.ConfigType;
import me.defender.cosmetics.api.enums.FieldsType;
import me.defender.cosmetics.api.enums.RarityType;
import me.defender.cosmetics.api.util.StartupUtils;
import me.defender.cosmetics.api.configuration.ConfigManager;
import me.defender.cosmetics.api.configuration.ConfigUtils;
import me.defender.cosmetics.api.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.defender.cosmetics.api.configuration.ConfigUtils.get;
import static me.defender.cosmetics.api.configuration.ConfigUtils.saveIfNotFound;
import static me.defender.cosmetics.api.util.Utility.saveIfNotExistsLang;

public abstract class ShopKeeperSkin extends Cosmetics {


    private final String category = "shopkeeper-skins";
    ConfigManager config = ConfigUtils.getShopKeeperSkins();
    ConfigType type = ConfigType.SHOP_KEEPER_SKINS;

    /**
     * Register the shopkeeper skin
     * This method should be called when the plugin is enabled.
     */
    @Override
    public void register(){
        // save to config
        String configPath = category + "." + getIdentifier() + ".";
        saveIfNotFound(type, configPath + "price", getPrice());
         saveIfNotFound(type, configPath + "rarity", getRarity().toString());
        if(!XMaterial.matchXMaterial(getItem()).isSupported()) {
            Bukkit.getLogger().severe("The item is not supported! (Information: Category name is " + category + " and item name is " + getIdentifier());
            return;
        }
        if(XMaterial.matchXMaterial(getItem()).isSimilar(XMaterial.PLAYER_HEAD.parseItem())){
            get(type).setItemStack(configPath + "item", getItem(), base64());
        }else{
            get(type).setItemStack(configPath + "item", getItem());
        }

        // save to language file
        saveIfNotExistsLang("cosmetics." + configPath + "name", getDisplayName());
        // Format the lore
        List<String> finalLore = new ArrayList<>();
        finalLore.addAll(Arrays.asList("&8ShopKeeper Skins", ""));
        finalLore.addAll(getLore());
        if(getRarity() != RarityType.NONE){
            finalLore.addAll(Arrays.asList("", "&eRight-Click to preview!", "" ,"&7Rarity: {rarity}","&7Cost: &6{cost}", "", "{status}"));
        }else{
            finalLore.addAll(Arrays.asList("", "&7Rarity: {rarity}","&7Cost: &6{cost}", "", "{status}"));
        }

        saveIfNotExistsLang("cosmetics." + configPath + "lore", finalLore);
        StartupUtils.shopKeeperSkinList.add(this);
    }

    /**
     * Get the topper's field
     * @param fields the field to get
     * @param p the player to get the field
     * @return the field
     */
    public Object getField(FieldsType fields, Player p){
        String configPath = category + "." + getIdentifier() + ".";

        switch (fields){
            case NAME:
                return Utility.getMSGLang(p, "cosmetics." + configPath + "name");
            case PRICE:
                return config.getInt(configPath + "price");
            case LORE:
                return Utility.getListLang(p, "cosmetics." + configPath + "lore");
            case RARITY:
                return RarityType.valueOf(config.getString(configPath + "rarity"));
            case ITEM_STACK:
                return config.getItemStack(configPath + "item");
            default:
                return null;
        }
    }

    /**
     * Display the shopkeeper skin to the player
     *
     * @param player the player to display the shopkeeper skin
     * @param shopLocation the location of the shopkeeper
     * @param upgradeLocation the location of the upgrade shopkeeper
     */
    public abstract void execute(Player player, Location shopLocation, Location upgradeLocation);

    /**
     * Get the default shopkeeper skin
     * @param player the player to get the default shopkeeper skin
     * @return the default shopkeeper skin
     */
    public static @NotNull ShopKeeperSkin getDefault(Player player){
        for(ShopKeeperSkin shopKeeperSkin : StartupUtils.shopKeeperSkinList){
            if(shopKeeperSkin.getField(FieldsType.RARITY, player) == RarityType.NONE){
                return shopKeeperSkin;
            }
        }

        // This will never return null!
        return null;
    }
}

