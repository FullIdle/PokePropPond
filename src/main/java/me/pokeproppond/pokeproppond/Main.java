package me.pokeproppond.pokeproppond;

import com.google.common.collect.Lists;
import com.mc9y.nyeconomy.api.NyEconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements TabCompleter {
    public static Main plugin;
    public static NyEconomyAPI nyeApi;
    public static String evPoints;

    @Override
    public void onEnable() {
        plugin = this;
        nyeApi = NyEconomyAPI.getInstance();
        reloadConfig();
        getCommand("pokeproppond").setExecutor(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        evPoints = this.getConfig().getString("evPoints");
    }

    List<String> sub = Lists.newArrayList(
            "evs","reload"
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String arg = args[0].toLowerCase();
            if (arg.equals("evs")) {
                if (!sender.hasPermission("pokeproppond.cmd.evs")){
                    sender.sendMessage("§c你没有权限!");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§c该指令只能由玩家执行!");
                    return false;
                }
                Player player = (Player) sender;
                Inventory inv = new Gui(player).getInventory();
                player.openInventory(inv);
                return false;
            }
            if (arg.equals("reload")) {
                if (!sender.hasPermission("pokeproppond.cmd.reload")){
                    sender.sendMessage("§c你没有权限!");
                    return false;
                }
                sender.sendMessage("§aReloaded!");
                this.reloadConfig();
                return false;
            }
        }
        sender.sendMessage(new String[]{
                "§7§lHELP",
                "§7/pokeproppond evs",
                "§7/pokeproppond reload"
        });
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 1) return this.sub;
        if (args.length == 1) return this.sub.stream().filter(s->s.startsWith(args[0])).collect(Collectors.toList());
        return null;
    }
}