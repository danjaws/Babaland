package babaland.main.events;

import babaland.main.tps.Lag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PlayerEvents implements Listener {
    private final Plugin plugin;
    public PlayerEvents(final Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for(Player online : Bukkit.getOnlinePlayers()) {
                    if(online == player) {
                        DecimalFormat df = new DecimalFormat("#.##");

                        online.setPlayerListHeader(ChatColor.GRAY + "TPS: " + ChatColor.GREEN + "" + df.format(Lag.getTPS() * 10) + ChatColor.GRAY + "" + "(" + Math.round((1.0D - Lag.getTPS() / 20.0D) * 100.0D) + "%)");
                        online.setPlayerListName(ChatColor.GREEN + "" + getPlayerPing(player) + " " + ChatColor.WHITE + "" + player.getName() + " " + returnWorld(player.getWorld()));
                    }
                }
            }
        }, 20L, 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onWorldMovement(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(online == player) online.setPlayerListName(ChatColor.GREEN + "" + getPlayerPing(player) + " " + ChatColor.WHITE + "" + player.getName() + " " + returnWorld(player.getWorld()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerSleepEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        event.setCancelled(true);

        if (world.getTime() >= 12500 || world.hasStorm()) {
            player.setBedSpawnLocation(event.getBed().getLocation());
            world.setTime(1000);
            world.setStorm(false);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You have baba slept!"));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You can only sleep at night or during thunderstorms."));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerBedInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if(hand.getType() == Material.AIR) return;

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if(hand.getType() == Material.BLACK_BED
                    || hand.getType() == Material.BLUE_BED
                    || hand.getType() == Material.BROWN_BED
                    || hand.getType() == Material.CYAN_BED
                    || hand.getType() == Material.GRAY_BED
                    || hand.getType() == Material.GREEN_BED
                    || hand.getType() == Material.LIGHT_BLUE_BED
                    || hand.getType() == Material.LIGHT_GRAY_BED
                    || hand.getType() == Material.LIME_BED
                    || hand.getType() == Material.MAGENTA_BED
                    || hand.getType() == Material.ORANGE_BED
                    || hand.getType() == Material.PINK_BED
                    || hand.getType() == Material.PURPLE_BED
                    || hand.getType() == Material.RED_BED
                    || hand.getType() == Material.WHITE_BED
                    || hand.getType() == Material.YELLOW_BED) {
                World world = player.getWorld();

                if(world.getTime() >= 12500 || world.hasStorm()) {
                    world.setTime(500);
                    world.setStorm(false);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You have baba slept!"));

                    event.setCancelled(true);
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You can only sleep at night or during thunderstorms."));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set " + event.getEntity().getName() + " Alive 0");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onDisablePhantom(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType() == EntityType.PHANTOM) {
            for(Player player : Bukkit.getOnlinePlayers()) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("A slut phantom has been killed."));
            event.setCancelled(true);
        }
    }

    private String returnWorld(World world) {
        if(world.getEnvironment() == World.Environment.NETHER)
            return ChatColor.RED + "Nether";
        if(world.getEnvironment() == World.Environment.THE_END)
            return ChatColor.BLACK + "End";
        if(world.getEnvironment() == World.Environment.NORMAL)
            return ChatColor.GREEN + "Overworld";
        return ChatColor.WHITE + "?";
    }
    private int getPlayerPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
}