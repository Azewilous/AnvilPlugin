package net.shadowcode.ohcreative;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * AnvilPlugin
 * Created by OhCreative on 8/2/2014.
 */
public class PermissionsManager {
    static Plugin p = Anvil.getPlugin(Anvil.class);
    public static Permission perms = null;

    protected static boolean setupPermissions() {
        if(p.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> permissionProvider = p.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }
}
