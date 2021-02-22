package babaland.main.commands.clearground;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ClearGroundItems implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(args.length <= 0) {
                player.sendMessage(ChatColor.RED + "Usage: /cleargrounditems <radius>");
            } else {
                try {
                    int radius = Integer.parseInt(args[0]);
                    int destroyed = 0;

                    for(Entity entity : player.getNearbyEntities((radius * 2), (radius * 2), 70)) {
                        if(entity.getType() == EntityType.DROPPED_ITEM) {
                            destroyed += 1;
                            entity.remove();
                            player.sendMessage(ChatColor.GRAY + "" + destroyed + " ground items removed.");
                        }
                    }
                } catch(IllegalArgumentException ex) {
                    player.sendMessage(ChatColor.RED + "Unrecognisable number '" + args[0] + "'");
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("Player only command!");
        }
        return true;
    }
}
