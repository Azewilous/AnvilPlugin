package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * AnvilPlugin
 * Created by OhCreative on 7/18/2014.
 */
public class Anvil extends JavaPlugin implements Listener {
    static Plugin plugin = Anvil.getPlugin(Anvil.class);
    public static String permission, prefix, noperms, reload, ukargs,
           message, notifyperm, reloadperm, helpperm, helpmsg, moneyperm, moneymsg;
    public static boolean msgops, economy, perms;
    public static int price;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();

        if(economy) {
          if (!EconomyManager.setupEconomy()) {
            getLogger().severe(" has been disabled due to no Vault dependency found.");
            getServer().getPluginManager().disablePlugin(this);
          }
        }
        if(perms) {
           PermissionsManager.setupPermissions();
        } else {
            getCommand("anvil").setExecutor(new SuperPermsCommandHandler());
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getName().equalsIgnoreCase("anvil")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(args.length == 0)
                {
                    if(economy) {
                        if(PermissionsManager.perms.has(p, moneyperm) || sender.isOp() || price == 0) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if (PermissionsManager.perms.has(p, permission) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
                                gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                        event.setWillClose(false);
                                        event.setWillDestroy(false);
                                    }
                                });
                                gui.open();
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                                return true;
                            }
                        }
                        EconomyResponse r = EconomyManager.econ.withdrawPlayer(sender.getName(), price);
                        if (r.transactionSuccess()) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if (PermissionsManager.perms.has(p, permission) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message) + " for " + ChatColor.GREEN + "$" + price);
                                gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                        event.setWillClose(false);
                                        event.setWillDestroy(false);
                                    }
                                });
                                gui.open();
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + moneymsg));
                        }

                    } else {
                        AnvilGUI gui;
                        final Player player = (Player) sender;
                        if (PermissionsManager.perms.has(p, permission) || (sender.isOp())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
                            gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                }
                            });
                            gui.open();
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                            return true;
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (PermissionsManager.perms.has(p, reloadperm)) {
                            saveConfig();
                            reloadConfig();
                            loadConfig();
                            if (msgops) {
                                for (Player pl : PlayerManager.getPlayers()) {
                                    if (PermissionsManager.perms.has(pl, notifyperm) || p.isOp()) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        if (PermissionsManager.perms.has(p, helpperm) || sender.isOp()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + helpmsg));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                        }

                    } else {

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + ukargs));
                    }
                }

            } else {
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "Only player's may use this command."));
                } else
                if(args[0].equalsIgnoreCase("reload")) {
                    saveConfig();
                    reloadConfig();
                    loadConfig();
                    if (msgops) {
                        for(Player p : PlayerManager.getPlayers()) {
                            if (PermissionsManager.perms.has(p, notifyperm) || p.isOp()) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + ukargs));
                }
            }
        }


        return true;
    }




    public static void loadConfig() {
        prefix = plugin.getConfig().getString("anvil.prefix") + " ";
        permission = plugin.getConfig().getString("anvil.permission");
        noperms = plugin.getConfig().getString("anvil.NoPermMSG");
        ukargs = plugin.getConfig().getString("anvil.ArgsMSG");
        reload = plugin.getConfig().getString("anvil.ReloadMSG");
        msgops = plugin.getConfig().getBoolean("anvil.msgops");
        message = plugin.getConfig().getString("anvil.message");
        notifyperm = plugin.getConfig().getString("anvil.notifyperm");
        reloadperm = plugin.getConfig().getString("anvil.reloadperm");
        helpperm = plugin.getConfig().getString("anvil.helpperm");
        helpmsg = plugin.getConfig().getString("anvil.helpmsg");
        economy = plugin.getConfig().getBoolean("anvil.economy");
        price = plugin.getConfig().getInt("anvil.economy.price");
        moneymsg = plugin.getConfig().getString("anvil.NoMoneyMSG");
        moneyperm = plugin.getConfig().getString("anvil.moneyperm");
        perms = plugin.getConfig().getBoolean("anvil.perms");
    }

}
