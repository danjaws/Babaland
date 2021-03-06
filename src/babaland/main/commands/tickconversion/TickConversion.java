package babaland.main.commands.tickconversion;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TickConversion implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(args.length <= 0) {
                player.sendMessage(ChatColor.RED + "Usage: /online <player>");
            } else {
                Score alive = player.getServer().getScoreboardManager().getMainScoreboard().getObjective("Alive").getScore(args[0]);
                int ticks = alive.getScore();
                int secondTime = ticks / 20;

                SimpleDateFormat sdf = new SimpleDateFormat("DD 'days' HH' hours 'mm' minutes and 'ss' seconds'");
                player.sendMessage(ChatColor.GREEN + "" + args[0] + " has been alive for " + sdf.format(new Date(secondTime*1000L)));
            }
        } else {
            System.out.println("Player only command.");
        }
        return true;
    }
}
