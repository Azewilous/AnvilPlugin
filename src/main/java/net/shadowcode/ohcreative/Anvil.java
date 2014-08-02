package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * AnvilPlugin
 * Created by OhCreative on 7/18/2014.
 */
public class Anvil extends JavaPlugin implements Listener {
    protected String permission;
    protected String prefix;
    protected String noperms;
    protected String reload;
    protected String ukargs;
    protected String message;
    protected String notifyperm;
    protected String reloadperm;
    protected String helpperm;
    protected String helpmsg;
    protected String moneyperm;
    protected String moneymsg;
    protected boolean msgops;
    protected boolean economy;
    protected int price;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();

    if(getConfig().getBoolean("economy")) {
        if (!EconomyManager.setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
    }


    protected void loadConfig() {
        this.prefix = this.getConfig().getString("anvil.prefix") + " ";
        this.permission = this.getConfig().getString("anvil.permission");
        this.noperms = this.getConfig().getString("anvil.NoPermMSG");
        this.ukargs = this.getConfig().getString("anvil.ArgsMSG");
        this.reload = this.getConfig().getString("anvil.ReloadMSG");
        this.msgops = this.getConfig().getBoolean("anvil.msgops");
        this.message = this.getConfig().getString("anvil.message");
        this.notifyperm = this.getConfig().getString("anvil.notifyperm");
        this.reloadperm = this.getConfig().getString("anvil.reloadperm");
        this.helpperm = this.getConfig().getString("anvil.helpperm");
        this.helpmsg = this.getConfig().getString("anvil.helpmsg");
        this.economy = this.getConfig().getBoolean("anvil.economy");
        this.price = this.getConfig().getInt("anvil.price");
        this.moneymsg = this.getConfig().getString("anvil.NoMoneyMSG");
        this.moneyperm = this.getConfig().getString("anvil.moneyperm");

    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getName().equalsIgnoreCase("anvil")) {
            if(sender instanceof Player) {
                if(args.length == 0)
                {
                    if(economy) {
                        if(sender.hasPermission(this.moneyperm) || sender.isOp()) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if ((sender.hasPermission(this.permission)) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.message));
                                gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                        event.setWillClose(false);
                                        event.setWillDestroy(false);
                                    }
                                });
                                gui.open();
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.noperms));
                                return true;
                            }
                        }
                      EconomyResponse r = EconomyManager.econ.withdrawPlayer(sender.getName(), this.price);
                        if (r.transactionSuccess()) {
                            AnvilGUI gui;
                            final Player player = (Player) sender;
                            if ((sender.hasPermission(this.permission)) || (sender.isOp())) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.message) + " for " + ChatColor.GREEN + "$" + this.price);
                                gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                        event.setWillClose(false);
                                        event.setWillDestroy(false);
                                    }
                                });
                                gui.open();
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.noperms));
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.moneymsg));
                        }

                    } else {
                        AnvilGUI gui;
                        final Player player = (Player) sender;
                        if ((sender.hasPermission(this.permission)) || (sender.isOp())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.message));
                            gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                }
                            });
                            gui.open();
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.noperms));
                            return true;
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission(this.reloadperm)) {
                            saveConfig();
                            reloadConfig();
                            loadConfig();
                            if (this.msgops) {
                                for (Player p : PlayerManager.getPlayers()) {
                                    if (p.hasPermission(this.notifyperm) || p.isOp()) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.noperms));
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        if (sender.hasPermission(this.helpperm) || sender.isOp()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.helpmsg));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.noperms));
                        }

                    } else {

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.ukargs));
                    }
                }

            } else {
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + "Only player's may use this command."));
                } else
                if(args[0].equalsIgnoreCase("reload")) {
                    saveConfig();
                    reloadConfig();
                    loadConfig();
                    if (this.msgops) {
                        for(Player p : PlayerManager.getPlayers()) {
                            if (p.hasPermission(this.notifyperm) || p.isOp()) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.ukargs));
                }
            }
            }

        return true;
    }







}
