package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * AnvilPlugin
 * Created by OhCreative on 8/2/2014.
 */
public class SuperPermsCommandHandler implements CommandExecutor {
  Plugin plugin = Anvil.getPlugin(Anvil.class);
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                if (Anvil.economy == true) {
                    if (sender.hasPermission(Anvil.moneyperm) || sender.isOp() || Anvil.price == 0) {
                        AnvilGUI gui;
                        final Player player = (Player) sender;
                        if (sender.hasPermission(Anvil.permission) || (sender.isOp())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.message));
                            gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                }
                            });
                            gui.open();
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.noperms));
                            return true;
                        }
                    }
                    EconomyResponse r = EconomyManager.econ.withdrawPlayer(sender.getName(), Anvil.price);
                    if (r.transactionSuccess()) {
                        AnvilGUI gui;
                        final Player player = (Player) sender;
                        if (sender.hasPermission(Anvil.permission) || (sender.isOp())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.message) + " for " + ChatColor.GREEN + "$" + Anvil.price);
                            gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                }
                            });
                            gui.open();
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.noperms));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.moneymsg));
                    }

                } else {
                    AnvilGUI gui;
                    final Player player = (Player) sender;
                    if (sender.hasPermission(Anvil.permission) || (sender.isOp())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.message));
                        gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        });
                        gui.open();
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.noperms));
                        return true;
                    }
                }
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(Anvil.reloadperm)) {
                        Bukkit.getPluginManager().disablePlugin(Anvil.getPlugin(Anvil.class));
                        Bukkit.getPluginManager().enablePlugin(Anvil.getPlugin(Anvil.class));
                        if (Anvil.msgops) {
                            for (Player pl : PlayerManager.getPlayers()) {
                                if (pl.hasPermission(Anvil.notifyperm) || pl.isOp()) {
                                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.reload));
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.reload));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.noperms));
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (sender.hasPermission(Anvil.helpperm) || sender.isOp()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.helpmsg));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.noperms));
                    }

                } else {

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.ukargs));
                }
            }

        } else {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + "Only player's may use Anvil command."));
            } else if (args[0].equalsIgnoreCase("reload")) {
                Bukkit.getPluginManager().disablePlugin(Anvil.getPlugin(Anvil.class));
                Bukkit.getPluginManager().enablePlugin(Anvil.getPlugin(Anvil.class));
                if (Anvil.msgops) {
                    for (Player pl : PlayerManager.getPlayers()) {
                        if (sender.hasPermission(Anvil.notifyperm) || pl.isOp()) {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.reload));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.reload));
                }

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Anvil.prefix + Anvil.ukargs));
            }
        }
        return true;
    }
}
