package net.shadowcode.ohcreative.anvil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
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
    protected boolean msgops;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();
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
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getName().equalsIgnoreCase("anvil")) {
            if(sender instanceof Player) {
                if(args.length == 0)
                {
                    AnvilGUI gui;
                    final Player player = (Player) sender;
                    if ((sender.hasPermission(this.permission)) || (sender.isOp()))
                    {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.message));
                        gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler()
                        {
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event)
                            {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                            }
                        });
                        gui.open();
                    }  else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix +  this.noperms));
                        return true;
                    }
                } else if(args[0].equalsIgnoreCase("reload")) {
                    if(sender.hasPermission(this.reloadperm)) {
                        saveConfig();
                        reloadConfig();
                        loadConfig();
                        if (this.msgops) {
                            for(Player p : getPlayers()) {
                                if(p.hasPermission(this.notifyperm) || p.isOp()) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.reload));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix +  this.noperms));
                    }
                }  else if(args[0].equalsIgnoreCase("help")) {
                   if(sender.hasPermission(this.helpperm) || sender.isOp()) {
                           sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.helpmsg));
                   } else {
                       sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix +  this.noperms));
                   }

                } else {

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + this.ukargs));
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
                                for(Player p : getPlayers()) {
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

    protected static List<Player> players = new ArrayList<Player>();

    @EventHandler
    protected void onJoin(PlayerJoinEvent event)
    {
        players.add(event.getPlayer());
    }

    @EventHandler
    protected void onQuit(PlayerQuitEvent event)
    {
        players.remove(event.getPlayer());
    }

    public List<Player> getPlayers()
    {
        return players;
    }





}
