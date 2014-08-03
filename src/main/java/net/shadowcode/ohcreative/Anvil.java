package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
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
    public Anvil plugin;
    public static String permission, prefix, noperms, reload, ukargs, vperm,
           message, notifyperm, reloadperm, helpperm, helpmsg, moneyperm, moneymsg;
    public static boolean msgops, economy;
    public static int price;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        if(getDataFolder() == null || !getDataFolder().exists()) {
            getConfig().options().copyDefaults(true);
        }
        saveConfig();
        loadConfig();

        if(economy) {
          if (!EconomyManager.setupEconomy()) {
            getLogger().severe(" has been disabled due to no Vault dependency found.");
            getServer().getPluginManager().disablePlugin(this);
          }
        }
           PermissionsManager.setupPermissions();
    }
    public void onReload() {
        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();
    }

    public Plugin getPlugin() {
        return plugin;
    }


    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getName().equalsIgnoreCase("anvil")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(args.length == 0)
                {
                    if(economy == true) {
                        if(PermissionsManager.perms.has(p, moneyperm) || sender.isOp() || price == 0) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if (PermissionsManager.perms.has(p, permission) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.RED + "[ADMIN]" + message));
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
                            return true;
                        } else {
                            EconomyResponse r = EconomyManager.econ.withdrawPlayer(sender.getName(), price);
                            if (r.transactionSuccess()) {
                                AnvilGUI gui;
                                final Player player = (Player) sender;
                                if (PermissionsManager.perms.has(p, permission) || (sender.isOp())) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message) + ChatColor.GREEN + "$" + price);
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




    public void loadConfig() {
        prefix = getConfig().getString("anvil.prefix") + " ";
        permission = "anvil.anvil";
        noperms = getConfig().getString("anvil.NoPermMSG");
        ukargs = getConfig().getString("anvil.ArgsMSG");
        reload = getConfig().getString("anvil.ReloadMSG");
        msgops = getConfig().getBoolean("anvil.msgops");
        message = getConfig().getString("anvil.message");
        notifyperm = "anvil.reload.notify";
        reloadperm = "anvil.reload";
        helpperm = "anvil.help";
        helpmsg = getConfig().getString("anvil.helpmsg");
        economy = getConfig().getBoolean("anvil.economy");
        price = getConfig().getInt("anvil.price");
        moneymsg = getConfig().getString("anvil.NoMoneyMSG");
        moneyperm = "anvil.economy.exempt";
        vperm = "anvil.version";
    }

}
