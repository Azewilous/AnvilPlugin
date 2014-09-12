package net.shadowcode.ohcreative;


import org.bukkit.ChatColor;

public enum Messages {

PREFIX(Anvil.getProvidingPlugin(Anvil.class).getConfig().getString("anvil.prefix") + " "),
DENIED(ChatColor.translateAlternateColorCodes('&', "&4You don't have enough permissions.")),
ARG_ERROR(ChatColor.translateAlternateColorCodes('&', "&cUnknown Arguments.")),
RELOAD(ChatColor.translateAlternateColorCodes('&', "&aYou have reloaded configuration.")),
HELP("Anvil Help Menu:"),
NO_MONEY(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough money!")),
OPEN_INV(ChatColor.translateAlternateColorCodes('&', "&9You have opened the anvil inventory")),
CLOSE_INV(ChatColor.translateAlternateColorCodes('&', "&9You have closed the anvil inventory")),
JOIN_MSG(ChatColor.translateAlternateColorCodes('&', "&cUseful command: &6/anvil help&c!"));

    private String msg;

    private Messages(String msg) {
        this.msg = msg;
    }

    public String getNode() {
        return this.msg;
    }
}
