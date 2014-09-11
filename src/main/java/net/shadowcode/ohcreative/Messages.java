package net.shadowcode.ohcreative;


public enum Messages {

PREFIX(Anvil.getProvidingPlugin(Anvil.class).getConfig().getString("anvil.prefix") + " "),
DENIED(ChatColor.translateAlternateColorCodes('&', "&4You don't have enough permissions.")),
ARG_ERROR(ChatColor.translateAlternateColorCodes('&', "&cUnknown Arguments.")),
RELOAD(ChatColor.translateAlternateColorCodes('&', "&aYou have reloaded configuration.")),
HELP("Anvil Help Menu:"),
NO_MONEY(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough money!")),
OPEN_INV(ChatColor.translateAlternateColorCodes('&', "&9You have opened the anvil inventory";)),
CLOSE_INV(ChatColor.translateAlternateColorCodes('&', "&9You have closed the anvil inventory"));



}
