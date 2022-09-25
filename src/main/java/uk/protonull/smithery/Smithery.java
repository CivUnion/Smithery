package uk.protonull.smithery;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.protonull.smithery.alloys.AlloyCombinations;
import uk.protonull.smithery.alloys.AlloyListener;
import uk.protonull.smithery.commands.CommandRegistrar;
import uk.protonull.smithery.config.Config;
import uk.protonull.smithery.forge.ForgeListener;
import uk.protonull.smithery.forge.ForgeManager;
import vg.civcraft.mc.civmodcore.ACivMod;

public final class Smithery extends ACivMod {

    private CommandRegistrar commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            Config.forceParseAll();
        }
        catch (final Throwable thrown) {
            getLogger().log(Level.WARNING, "Could not parse config!", thrown);
            disable();
            return;
        }

        commandManager = new CommandRegistrar(this);
        commandManager.init();

        registerListener(new ForgeListener());
        registerListener(new AlloyListener());
        ForgeManager.loadAllForges();
        AlloyCombinations.generateCombinations();
    }

    @Override
    public void onDisable() {
        AlloyCombinations.clearCombinations();
        ForgeManager.saveAllForges();
        Config.reset();
        commandManager.reset();
    }

    public static @NotNull Smithery getInstance() {
        return JavaPlugin.getPlugin(Smithery.class);
    }

}
