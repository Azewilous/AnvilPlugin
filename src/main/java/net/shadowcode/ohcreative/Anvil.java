package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
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
    public static String permission, prefix, noperms, reload, ukargs, vperm,
           message, notifyperm, reloadperm, helpperm, helpmsg, moneyperm, moneymsg;
    public String bukkitVersion;
    public static boolean economy;
    public static int price;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerManager(), this);

        setupConfig();

        bukkitVersion = Bukkit.getVersion();

        getLogger().severe("Running Server version: " + bukkitVersion);

        if(economy) {
          if (!VaultIntegration.setupEconomy()) {
            getLogger().severe(" has been disabled due to no Vault dependency found.");
            getServer().getPluginManager().disablePlugin(this);
          }
        }
           VaultIntegration.setupPermissions();
           
           LicenseHandler.createLicense();
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getName().equalsIgnoreCase("anvil")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(args.length == 0)
                {
                    if(economy == true) {
                        if(VaultIntegration.perms.has(p, moneyperm) || sender.isOp() || price == 0) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if (VaultIntegration.perms.has(p, permission) || (sender.isOp())) {
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
                                if (VaultIntegration.perms.has(p, permission) || (sender.isOp())) {
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
                        if (VaultIntegration.perms.has(p, permission) || (sender.isOp())) {
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
                        if (VaultIntegration.perms.has(p, reloadperm)) {
                            reloadConfig();
                            saveConfig();
                            loadVars();
                            if (msgops) {
                                for (Player pl : PlayerManager.getPlayers()) {
                                    if (VaultIntegration.perms.has(pl, notifyperm) || p.isOp()) {
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
                            sender.sendMessage(ChatColor.GOLD + "/anvil" + ChatColor.WHITE + " - Open the anvil interface!");
                            sender.sendMessage(ChatColor.GOLD + "/anvil help" + ChatColor.WHITE + " - View help menu.");
                            sender.sendMessage(ChatColor.GOLD + "/anvil version" + ChatColor.WHITE + " - Anvil Version!");
                            if(VaultIntegration.perms.has(p, reloadperm) || sender.isOp()) {
                                sender.sendMessage(ChatColor.GOLD + "/anvil reload" + ChatColor.WHITE + " - Reload Configuration.");
                            }

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noperms));
                        }

                    } else if(args[0].equalsIgnoreCase("version")) {
                        if (VaultIntegration.perms.has(p, vperm) || sender.isOp()) {
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
                    reloadConfig();
                    saveConfig();
                    loadVars();

                    if (msgops) {
                        for(Player p : PlayerManager.getPlayers()) {
                            if (VaultIntegration.perms.has(p, notifyperm) || p.isOp()) {
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




    public void loadVars() {
        prefix = getConfig().getString("anvil.prefix") + " ";
        permission = "anvil.anvil";
        noperms = "&4You don't have enough permissions.";
        ukargs = "&cUnknown Arguments.";
        reload = "&aYou have reloaded configuration.";
        notifyperm = "anvil.reload.notify";
        reloadperm = "anvil.reload";
        helpmsg = "Anvil Help Menu:";
        helpperm = "anvil.help";
        economy = getConfig().getBoolean("anvil.economy");
        price = getConfig().getInt("anvil.price");
        moneymsg = "&cYou do not have enough money!";
        moneyperm = "anvil.economy.exempt";
        message = "&9You have opened the anvil inventory";
        vperm = "anvil.version";
    }

    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadVars();
    }

}
