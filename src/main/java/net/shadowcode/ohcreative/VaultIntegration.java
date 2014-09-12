package net.shadowcode.ohcreative;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.permission.Permission;


public final class VaultIntegration {
    
    public static Permission perms = null;
    public static Economy econ = null;

    public static boolean setupEconomy() {
        
        Plugin p = Anvil.getPlugin(Anvil.class);
        if(p.getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) { return false; }
        econ = rsp.getProvider();
        return econ != null;
        
    }

    protected static boolean setupPermissions() {
        
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) { perms = permissionProvider.getProvider(); }
        return (perms != null);
        
    }


}
