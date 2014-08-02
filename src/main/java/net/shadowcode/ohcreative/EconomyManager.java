package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * AnvilPlugin
 * Created by OhCreative on 8/2/2014.
 */
public class EconomyManager {
    public static Economy econ = null;

    public static boolean setupEconomy() {
        Plugin p = Anvil.getPlugin(Anvil.class);
        if(p.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
