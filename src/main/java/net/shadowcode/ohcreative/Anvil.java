package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * AnvilPlugin
 * Created by OhCreative on 7/18/2014.
 */
public class Anvil extends JavaPlugin {
    public Anvil plugin;
    public static String permission, prefix, noperms, reload, ukargs, vperm,
           message, notifyperm, reloadperm, helpperm, helpmsg, moneyperm, moneymsg;
    public static boolean msgops, economy;
    public static int price;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerManager(), this);

        setupConfig();
        getConfig().options().copyDefaults(true);
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
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message) + ChatColor.RED + "-$" + price);
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

                    } else if(args[0].equalsIgnoreCase("version")) {
                        if (PermissionsManager.perms.has(p, vperm) || sender.isOp()) {
                            p.sendMessage("");
                            p.sendMessage(ChatColor.BLUE + "Anvil");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                            p.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.RED + getDescription().getVersion());
                            p.sendMessage(ChatColor.DARK_GREEN + "Developers: " + ChatColor.RED + "OhCreative");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                            p.sendMessage("");

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                        }
                    }
                    else {

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

                } else if(args[0].equalsIgnoreCase("version")) {
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.BLUE + "Anvil");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                    sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.RED + getDescription().getVersion());
                    sender.sendMessage(ChatColor.DARK_GREEN + "Developers: " + ChatColor.RED + "OhCreative");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                    sender.sendMessage("");
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

    public void setupConfig() {
        getConfig().options().header("Anvil Configuration\n " + "\n" + "Change the prefix. You may use '&' color codes.\n" +
                "    prefix: \"&8[&7Anvil&8]\"\n" + "\n" + "Enable Vault Economy Support.\n" + "    economy: false\n"  + "\n" +
                "If Economy is enabled, how much should it cost to run the /anvil command.\n" + "    price: 10" + "\n" +
                "This message is sent to the player when using the command. You may use '&' color codes.\n"
                + "    message: \"&9You have opened the anvil inventory\"\n" + "This message edits the message returned when a player does \"/anvil help\". You may use '&' color codes.\n"
                + "    helpmsg: \"&cUsage: &6/anvil [&ahelp&6/&9reload&6]\"\n" + "No Permissions Message. You may use '&' color codes.\n"
                + "    NoPermMSG: \"&4You don't have enough permissions.\"\n" + "Change the unknown arguments message. You may use '&' color codes.\n" + ""
                + "    ArgsMSG: \"&cUnknown Arguments.\"\n" + "If economy is true, This mesasge is sent if the player doesn't have enough money.\n" +
                "    NoMoneyMSG: \"&cYou do not have enough money!\"\n" + "\n" + "Message all ops on reload\n" + "    msgops: false\n" + "\n" +
                "Reload Message. You may use '&' color codes.\n" + "     ReloadMSG: \"&aYou have reloaded configuration.\"\n" + "#########################################################################################################\n");
        getConfig().addDefault("anvil.prefix", String.valueOf("&8[&7Anvil&8]"));
        getConfig().addDefault("anvil.economy", Boolean.valueOf(false));
        getConfig().addDefault("anvil.price", Integer.valueOf(10));
        getConfig().addDefault("anvil.message", String.valueOf("&9You have opened the anvil inventory."));
        getConfig().addDefault("anvil.helpmsg", String.valueOf("&cUsage: &6/anvil [&ahelp&6/&9reload&6]"));
        getConfig().addDefault("anvil.NoPermMSG", String.valueOf("&4You don't have enough permissions."));
        getConfig().addDefault("anvil.ArgsMSG", String.valueOf("&cUnknown Arguments."));
        getConfig().addDefault("anvil.NoMoneyMSG", String.valueOf("&cYou do not have enough money!"));
        getConfig().addDefault("anvil.msgops", Boolean.valueOf(false));
        getConfig().addDefault("anvil.ReloadMSG", String.valueOf("&aYou have reloaded configuration."));
    }

}
