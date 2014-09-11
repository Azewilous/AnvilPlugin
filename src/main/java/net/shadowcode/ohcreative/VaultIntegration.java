package net.shadowcode.ohcreative;

public final class VaultIntegration {


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
