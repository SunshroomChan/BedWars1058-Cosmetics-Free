package me.defender.cosmetics.support.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.defender.cosmetics.Cosmetics;
import me.defender.cosmetics.api.BwcAPI;
import me.defender.cosmetics.api.enums.CosmeticsType;
import me.defender.cosmetics.api.util.StartupUtils;
import me.defender.cosmetics.api.util.Utility;
import me.defender.cosmetics.database.PlayerOwnedData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    private final Cosmetics plugin;

    public Placeholders(Cosmetics plugin) {
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "bwc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "IamTheDefender";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }

    @Override
    public boolean persist() {
        return true;
    }



    public String onPlaceholderRequest(Player player, String placeholder) {
        BwcAPI api = new BwcAPI();
        PlayerOwnedData ownedData = Utility.playerOwnedDataList.get(player.getUniqueId());
        switch (placeholder.toLowerCase()) {
            case "selected_dc":
                return api.getSelectedCosmetic(player, CosmeticsType.DeathCries);
            case "selected_vd":
                return api.getSelectedCosmetic(player, CosmeticsType.VictoryDances);
            case "selected_spray":
                return api.getSelectedCosmetic(player, CosmeticsType.Sprays);
            case "selected_km":
                return api.getSelectedCosmetic(player, CosmeticsType.KillMessage);
            case "selected_skin":
                return api.getSelectedCosmetic(player, CosmeticsType.ShopKeeperSkin);
            case "selected_ws":
                return api.getSelectedCosmetic(player, CosmeticsType.WoodSkins);
            case "selected_glyph":
                return api.getSelectedCosmetic(player, CosmeticsType.Glyphs);
            case "selected_bbe":
                return api.getSelectedCosmetic(player, CosmeticsType.BedBreakEffects);
            case "selected_pt":
                return api.getSelectedCosmetic(player, CosmeticsType.ProjectileTrails);
            case "selected_finalkill":
                return api.getSelectedCosmetic(player, CosmeticsType.FinalKillEffects);
            case "selected_islandtopper":
                return api.getSelectedCosmetic(player, CosmeticsType.IslandTopper);
            case "owned_vd":
                return String.valueOf(ownedData.getVictoryDance());
            case "owned_ws":
                return String.valueOf(ownedData.getWoodSkin());
            case "owned_shopkeeper":
                return String.valueOf(ownedData.getShopkeeperSkin());
            case "owned_pt":
                return String.valueOf(ownedData.getProjectileTrail());
            case "owned_km":
                return String.valueOf(ownedData.getKillMessage());
            case "owned_gly":
                return String.valueOf(ownedData.getGlyph());
            case "owned_finalkill":
                return String.valueOf(ownedData.getFinalKillEffect());
            case "owned_dc":
                return String.valueOf(ownedData.getDeathCry());
            case "owned_bbe":
                return String.valueOf(ownedData.getBedDestroy());
            case "owned_spray":
                return String.valueOf(ownedData.getSpray());
            case "owned_island":
                return String.valueOf(ownedData.getIslandTopper());
            case "total_vd":
                return StartupUtils.victoryDancesList.size() + "";
            case "total_bbe":
                return StartupUtils.bedDestroyList.size() + "";
            case "total_dc":
                return StartupUtils.deathCryList.size() + "";
            case "total_finalkill":
                return StartupUtils.finalKillList.size() + "";
            case "total_shopkeeper":
                return StartupUtils.shopKeeperSkinList.size() + "";
            case "total_ws":
                return StartupUtils.woodSkinsList.size() + "";
            case "total_km":
                return StartupUtils.killMessageList.size() + "";
            case "total_pt":
                return StartupUtils.projectileTrailList.size() + "";
            case "total_spray":
                return StartupUtils.sprayList.size() + "";
            case "total_island":
                return StartupUtils.islandTopperList.size() + "";
            default:
                return null;
        }
    }
}
