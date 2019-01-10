package xyz.wildseries.wildstacker.hooks;

import net.novucs.ftop.FactionsTopPlugin;
import net.novucs.ftop.hook.SpawnerStackerHook;
import net.novucs.ftop.hook.event.SpawnerMultiplierChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wildseries.wildstacker.WildStackerPlugin;
import xyz.wildseries.wildstacker.api.events.SpawnerPlaceEvent;
import xyz.wildseries.wildstacker.api.events.SpawnerStackEvent;
import xyz.wildseries.wildstacker.api.events.SpawnerUnstackEvent;
import xyz.wildseries.wildstacker.api.objects.StackedSpawner;
import xyz.wildseries.wildstacker.objects.WStackedSpawner;
import xyz.wildseries.wildstacker.utils.ItemUtil;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class PluginHook_Novucs implements SpawnerStackerHook, Listener {

    private PluginHook_Novucs(WildStackerPlugin instance){
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    @Override
    public void initialize() {
    }

    @Override
    public EntityType getSpawnedType(ItemStack itemStack) {
        return EntityType.PIG;
    }

    @Override
    public int getStackSize(ItemStack itemStack) {
        return ItemUtil.getSpawnerItemAmount(itemStack);
    }

    @Override
    public int getStackSize(CreatureSpawner creatureSpawner) {
        return WStackedSpawner.of(creatureSpawner).getStackAmount();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void updateWorth(SpawnerPlaceEvent e){
        StackedSpawner spawner = e.getSpawner();

        int oldMultiplier = 0;
        int newMultiplier = spawner.getStackAmount();

        SpawnerMultiplierChangeEvent spawnerMultiplierChangeEvent = new SpawnerMultiplierChangeEvent(spawner.getSpawner(), oldMultiplier, newMultiplier);
        Bukkit.getPluginManager().callEvent(spawnerMultiplierChangeEvent);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void updateWorth(SpawnerStackEvent e) {
        StackedSpawner spawner = e.getSpawner();

        int oldMultiplier = spawner.getStackAmount();
        int newMultiplier = spawner.getStackAmount() + 1;

        SpawnerMultiplierChangeEvent spawnerMultiplierChangeEvent = new SpawnerMultiplierChangeEvent(spawner.getSpawner(), oldMultiplier, newMultiplier);
        Bukkit.getPluginManager().callEvent(spawnerMultiplierChangeEvent);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void updateWorth(SpawnerUnstackEvent e) {
        StackedSpawner spawner = e.getSpawner();

        int oldMultiplier = spawner.getStackAmount();
        int newMultiplier = spawner.getStackAmount() - 1;

        SpawnerMultiplierChangeEvent spawnerMultiplierChangeEvent = new SpawnerMultiplierChangeEvent(spawner.getSpawner(), oldMultiplier, newMultiplier);
        Bukkit.getPluginManager().callEvent(spawnerMultiplierChangeEvent);
    }

    public static void register(WildStackerPlugin instance){
        try{
            Field field = FactionsTopPlugin.class.getDeclaredField("spawnerStackerHook");
            field.setAccessible(true);
            field.set(JavaPlugin.getPlugin(FactionsTopPlugin.class), new PluginHook_Novucs(instance));
        }catch(NoClassDefFoundError | Exception ignored){}
    }

}
