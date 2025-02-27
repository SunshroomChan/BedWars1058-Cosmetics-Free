package me.defender.cosmetics.api.category.finalkilleffects.items;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import me.defender.cosmetics.api.category.finalkilleffects.FinalKillEffect;
import me.defender.cosmetics.api.enums.RarityType;
import me.defender.cosmetics.api.util.Utility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class TornadoEffect extends FinalKillEffect {
    @Override
    public ItemStack getItem() {
        return XMaterial.GLASS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "tornado";
    }

    @Override
    public String getDisplayName() {
        return "Tornado";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns a tornado at the", "&7location of victim!");
    }

    @Override
    public int getPrice() {
        return 5000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    @Override
    public void execute(Player killer, Player victim) {
        Location loc = victim.getLocation();
        XSound.ENTITY_GENERIC_EXPLODE.play(loc, 1.0f, 1.0f);
        new BukkitRunnable() {
            int angle = 0;

            public void run() {
                final int max_height = 7;
                final double max_radius = 3.0;
                final int lines = 4;
                final double height_increasement = 0.25;
                final double radius_increasement = max_radius / max_height;
                for (int l = 0; l < lines; ++l) {
                    for (double y = 0.0; y < max_height; y += height_increasement) {
                        final double radius = y * radius_increasement;
                        final double x = Math.cos(Math.toRadians(360 / lines * l + y * 30.0 - this.angle)) * radius;
                        final double z = Math.sin(Math.toRadians(360 / lines * l + y * 30.0 - this.angle)) * radius;
                        Particle particle = new Particle(ParticleType.CLOUD, 1, 0.01f, new Vector(0.0f, 0.0f, 0.0f));
                        HCore.playParticle(loc.clone().add(x, y, z), particle);

                    }
                }
                ++this.angle;
                if (this.angle == 70) {
                    this.cancel();
                }
            }
        }.runTaskTimer(Utility.plugin(), 2L, 0L);
    }
}
