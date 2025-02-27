package me.defender.cosmetics.api.category.shopkeeperskins.utils;

import com.hakan.core.HCore;
import com.hakan.core.npc.Npc;
import com.hakan.core.skin.Skin;
import me.defender.cosmetics.Cosmetics;
import me.defender.cosmetics.api.BwcAPI;
import me.defender.cosmetics.api.enums.CosmeticsType;
import me.defender.cosmetics.api.configuration.ConfigManager;
import me.defender.cosmetics.api.configuration.ConfigUtils;
import me.defender.cosmetics.api.util.Utility;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.defender.cosmetics.api.util.Utility.plugin;

public class ShopKeeperSkinsUtils {

    /**
     * This creates an entity NPC.
     * */
    private static void createEntityNPC(final EntityType ent, final Location loc) {
        NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        NPC npc = registry.createNPC(ent, "");
        npc.setBukkitEntityType(ent);
        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, false);
        npc.spawn(loc);

    }

    /**
     * This creates an entity NPC but with a timer.
     * */
    private static void createEntityNPC(final EntityType ent, final Location loc, int ticks) {
        NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        NPC npc = registry.createNPC(ent, "");
        npc.setBukkitEntityType(ent);
        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, false);
        npc.spawn(loc);

        new BukkitRunnable() {
            int tick = ticks;
            @Override
            public void run() {
                if (tick == 0){
                    npc.despawn();
                    cancel();
                }
                tick--;
            }
        }.runTaskTimer(plugin(), 0, 20);
    }

    /**
     * This method should only be used
     * When playing in game.
     * */
    private static void createShopKeeperNPC(Player p, Location loc, Location loc1, String value, String sign, Boolean mirror) {
        if (mirror) {
            List<String> values = Arrays.asList(Objects.requireNonNull(Utility.getFromName(p.getName())));
            value = values.get(0);
            sign = values.get(1);
        }
        NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        // Shop NPC
        NPC npc = registry.createNPC(EntityType.PLAYER, "");
        npc.setName("&r");
        npc.getTrait(SkinTrait.class).setSkinPersistent(UUID.randomUUID().toString(), sign, value);
        npc.getTrait(SkinTrait.class).setTexture(value, sign);
        npc.getTrait(LookClose.class).lookClose(true);
        npc.getOrAddTrait(HologramTrait.class).clear();
        npc.spawn(loc);
        npc.getEntity().setMetadata("NPC2", new FixedMetadataValue(plugin(), ""));

        // Shop NPC
        NPC npc1 = registry.createNPC(EntityType.PLAYER, "");
        npc1.setName("&r");
        npc1.getTrait(SkinTrait.class).setSkinPersistent(UUID.randomUUID().toString(), sign, value);
        npc1.getTrait(SkinTrait.class).setTexture(value, sign);
        npc1.getTrait(LookClose.class).lookClose(true);
        npc1.getOrAddTrait(HologramTrait.class).clear();
        npc1.spawn(loc1);
        npc1.getEntity().setMetadata("NPC2", new FixedMetadataValue(plugin(), ""));

    }

    /**
     * This method should only be used
     * When sending a preview.
     * */
    private static void createShopKeeperNPC(Player p, Location loc, String value, String sign, Boolean mirror, int ticks) {
        // mirror skin
        if (mirror) {
            List<String> values = Arrays.asList(Objects.requireNonNull(Utility.getFromName(p.getName())));
            value = values.get(0);
            sign = values.get(1);
        }
        NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        // Shop NPC
        NPC npc = registry.createNPC(EntityType.PLAYER, "");
        npc.setName("&r");
        npc.getTrait(SkinTrait.class).setSkinPersistent(UUID.randomUUID().toString(), sign, value);
        npc.getTrait(SkinTrait.class).setTexture(value, sign);
       npc.getTrait(LookClose.class).lookClose(true);
        npc.getOrAddTrait(HologramTrait.class).clear();
        npc.spawn(loc);
        npc.getEntity().setMetadata("NPC2", new FixedMetadataValue(plugin(), ""));

        new BukkitRunnable() {
            int tick = ticks;
            @Override
            public void run() {
                if (tick == 0){
                    npc.despawn();
                    cancel();
                }
                tick--;
            }
        }.runTaskTimer(plugin(), 0, 20);
    }


    /**

     Spawns a NPC in the form of a shopkeeper at the provided location using the selected skin from the player's
     cosmetic selection. If the selected skin has the option to be mirrored, it will use the player's current skin.
     Also spawns another NPC at the provided location1.
     @param p The player whose selected skin will be used for the NPC.
     @param loc The location where the first NPC will be spawned.
     @param loc1 The location where the second NPC will be spawned.
     */
    public static void spawnShopKeeperNPC(Player p, Location loc, Location loc1) {
        Cosmetics plugin = plugin();
        String skin = new BwcAPI().getSelectedCosmetic(p, CosmeticsType.ShopKeeperSkin);
        ConfigManager config = ConfigUtils.getShopKeeperSkins();
        String key = CosmeticsType.ShopKeeperSkin.getSectionKey();
        String skinvalue = config.getString(key + "." + skin + ".skin-value");
        String skinsign = config.getString(key + "." + skin + ".skin-sign");
        String etype = config.getString(key + "." + skin + ".entity-type");
        boolean mirror = config.getBoolean(key + "." + skin + ".mirror");

        if(mirror){
            createShopKeeperNPC(p, loc, loc1, skinvalue, skinsign, true);
            return;
        }
        if(etype != null) {
            createEntityNPC(EntityType.valueOf(etype), loc1);
            createEntityNPC(EntityType.valueOf(etype), loc);
        }else if(skinvalue != null && skinsign != null) {
            createShopKeeperNPC(p, loc, loc1, skinvalue, skinsign, false);
        }
    }

    public static void spawnShopKeeperNPC(Player p, Location loc, Location loc1, String skin) {
        ConfigManager config = ConfigUtils.getShopKeeperSkins();
        String key = CosmeticsType.ShopKeeperSkin.getSectionKey();
        String skinvalue = config.getString(key + "." + skin + ".skin-value");
        String skinsign = config.getString(key + "." + skin + ".skin-sign");
        String etype = config.getString(key + "." + skin + ".entity-type");
        boolean mirror = config.getBoolean(key + "." + skin + ".mirror");

        if(mirror){
            createShopKeeperNPC(p, loc, loc1, skinvalue, skinsign, true);
            return;
        }
        if(etype != null) {
            createEntityNPC(EntityType.valueOf(etype), loc1);
            createEntityNPC(EntityType.valueOf(etype), loc);
        }else if(skinvalue != null && skinsign != null) {
            createShopKeeperNPC(p, loc, loc1, skinvalue, skinsign, false);
        }
    }

    public static void spawnShopKeeperNPCForPreview(Player p, Location loc, String skin) {
        Cosmetics plugin = plugin();
        ConfigManager config = ConfigUtils.getShopKeeperSkins();
        String key = CosmeticsType.ShopKeeperSkin.getSectionKey();
        String skinvalue = config.getString(key + "." + skin + ".skin-value");
        String skinsign = config.getString(key + "." + skin + ".skin-sign");
        String etype = config.getString(key + "." + skin + ".entity-type");
        boolean mirror = config.getBoolean(key + "." + skin + ".mirror");

        if(mirror){
            createShopKeeperNPC(p, loc, skinvalue, skinsign, true, 5);
            return;
        }
        if(etype != null) {
            createEntityNPC(EntityType.valueOf(etype), loc, 5);
        }else if(skinvalue != null && skinsign != null) {
            createShopKeeperNPC(p, loc, skinvalue, skinsign, false, 5);
        }
    }
}
