package net.shadowcode.ohcreative;

import net.minecraft.server.v1_7_R4.ContainerAnvil;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * AnvilPlugin
 * Created by OhCreative on 7/31/2014.
 */
public class AnvilGUI {

    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity){
            super(entity.inventory, entity.world, 0, 0, 0, entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman){
            return true;
        }
    }

    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);


        private int slot;

        private AnvilSlot(int slot){
            this.slot = slot;
        }

        public int getSlot(){
            return slot;
        }

        public static AnvilSlot bySlot(int slot){
            for(AnvilSlot anvilSlot : values()){
                if(anvilSlot.getSlot() == slot){
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    public class AnvilClickEvent {
        private AnvilSlot slot;
        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name){
            this.slot = slot;
        }

        public boolean getWillClose(){
            return close;
        }

        public void setWillClose(boolean close){
            this.close = close;
        }

        public boolean getWillDestroy(){
            return destroy;
        }

        public void setWillDestroy(boolean destroy){
            this.destroy = destroy;
        }

    }

    public interface AnvilClickEventHandler {
        public void onAnvilClick(AnvilClickEvent event);
    }

    private Player player;

    private HashMap<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();

    private Inventory inv;

    private Listener listener;

    public AnvilGUI(Player player, final AnvilClickEventHandler handler){
        this.player = player;

        this.listener = new Listener(){
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event){
                if(event.getWhoClicked() instanceof Player){

                    if(event.getInventory().equals(inv)){

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if(item != null){
                            if(item.hasItemMeta()){
                                ItemMeta meta = item.getItemMeta();

                                if(meta.hasDisplayName()){
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                        handler.onAnvilClick(clickEvent);

                        if(clickEvent.getWillClose()){
                            event.getWhoClicked().closeInventory();
                        }

                        if(clickEvent.getWillDestroy()){
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event){
                if(event.getPlayer() instanceof Player){                    Inventory inv = event.getInventory();
                    if(inv.equals(AnvilGUI.this.inv)){
                            destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event){
                if(event.getPlayer().equals(getPlayer())){
                    destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, Anvil.getPlugin(Anvil.class));
    }

    public Player getPlayer(){
        return player;
    }

    public void open(){
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        inv = container.getBukkitView().getTopInventory();

        for(AnvilSlot slot : items.keySet()){
            inv.setItem(slot.getSlot(), items.get(slot));
        }

        int c = p.nextContainerCounter();

        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, 8, "Repairing", 9, true));
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);
    }

    public void destroy(){
        player = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }
}
