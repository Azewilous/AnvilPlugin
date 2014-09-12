package net.shadowcode.ohcreative;


public enum Permissions {

 PERMISSION("anvil.anvil"), 
 VERSION("anvil.version"), 
 NOTIFY("anvil.reload.notify"), 
 RELOAD("anvil.reload"), 
 HELP("anvil.help"), 
 MONEY("anvil.money.exempt");

    private String perm;

    private Permissions(String perm) {
        this.perm = perm;
    }

    public String getNode() {
        return this.perm;
    }


}
