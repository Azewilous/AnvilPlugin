package net.shadowcode.ohcreative;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * AnvilPlugin
 * Created by OhCreative on 8/2/2014.
 */
public class PlayerManager implements Listener {

    public static List<Player> players = new ArrayList<Player>();

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

    public static List<Player> getPlayers()
    {
        return players;
    }

}
