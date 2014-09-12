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
    public String bukkitVersion;
    public static boolean economy;
    public static int price;
    public Plugin plugin;

    public void onEnable() {
        plugin = Anvil.getPlugin(Anvil.class);
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
                    if(economy) {
                        if(VaultIntegration.perms.has(p, Permissions.MONEY.getNode()) || sender.isOp() || price == 0) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if (VaultIntegration.perms.has(p, Permissions.PERMISSION.getNode()) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + ChatColor.RED + "[ADMIN]" + Messages.OPEN_INV.getNode()));
                                gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                        event.setWillClose(false);
                                        event.setWillDestroy(false);
                                    }
                                });
                                gui.open();
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                                return true;
                            }
                            return true;
                        } else {
                            EconomyResponse r = VaultIntegration.econ.withdrawPlayer(sender.getName(), price);
                            if (r.transactionSuccess()) {
                                AnvilGUI gui;
                                final Player player = (Player) sender;
                                if (VaultIntegration.perms.has(p, Permissions.PERMISSION.getNode()) || (sender.isOp())) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.OPEN_INV.getNode()) + ChatColor.RED + "-$" + price);
                                    gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                        public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                            event.setWillClose(false);
                                            event.setWillDestroy(false);
                                        }
                                    });
                                    gui.open();
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.NO_MONEY.getNode()));
                            }
                        }
                    } else {
                        AnvilGUI gui;
                        final Player player = (Player) sender;
                        if (VaultIntegration.perms.has(p, Permissions.PERMISSION.getNode()) || (sender.isOp())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.OPEN_INV.getNode()));
                            gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                }
                            });
                            gui.open();
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                            return true;
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (VaultIntegration.perms.has(p, Permissions.RELOAD.getNode())) {
                            reloadConfig();
                            saveConfig();

                                for (Player pl : PlayerManager.getPlayers()) {
                                    if (VaultIntegration.perms.has(pl, Permissions.NOTIFY.getNode()) || p.isOp()) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.RELOAD.getNode()));
                                    }
                                }

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        if (VaultIntegration.perms.has(p, Permissions.HELP.getNode()) || sender.isOp()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.HELP.getNode()));
                            sender.sendMessage(ChatColor.GOLD + "/anvil" + ChatColor.WHITE + " - Open the anvil interface!");
                            sender.sendMessage(ChatColor.GOLD + "/anvil help" + ChatColor.WHITE + " - View help menu.");
                            sender.sendMessage(ChatColor.GOLD + "/anvil version" + ChatColor.WHITE + " - Anvil Version!");
                            if(VaultIntegration.perms.has(p, Permissions.RELOAD.getNode()) || sender.isOp()) {
                                sender.sendMessage(ChatColor.GOLD + "/anvil reload" + ChatColor.WHITE + " - Reload Configuration.");
                            }

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                        }

                    } else if(args[0].equalsIgnoreCase("version")) {
                        if (VaultIntegration.perms.has(p, Permissions.VERSION.getNode()) || sender.isOp()) {
                            p.sendMessage("");
                            p.sendMessage(ChatColor.BLUE + "Anvil");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                            p.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.RED + getDescription().getVersion());
                            p.sendMessage(ChatColor.DARK_GREEN + "Developers: " + ChatColor.RED + "OhCreative");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-&4-&7-"));
                            p.sendMessage("");

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.DENIED.getNode()));
                        }
                    }
                    else {

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.ARG_ERROR.getNode()));
                    }
                }

            } else {
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + "Only player's may use this command."));
                } else
                if(args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    saveConfig();

                        for(Player p : PlayerManager.getPlayers()) {
                            if (VaultIntegration.perms.has(p, Permissions.NOTIFY.getNode()) || p.isOp()) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.RELOAD.getNode()));
                            }
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PREFIX.getNode() + Messages.ARG_ERROR.getNode()));
                }
            }
        }


        return true;
    }



    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
