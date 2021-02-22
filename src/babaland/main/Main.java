package babaland.main;

import babaland.main.commands.Gamemode;
import babaland.main.commands.clearground.ClearGroundItems;
import babaland.main.commands.tickconversion.TickConversion;
import babaland.main.events.PlayerEvents;
import babaland.main.tps.Lag;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);

        getCommand("gm").setExecutor(new Gamemode());
        getCommand("cleargrounditems").setExecutor(new ClearGroundItems());
        getCommand("online").setExecutor(new TickConversion());

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
    }
}
