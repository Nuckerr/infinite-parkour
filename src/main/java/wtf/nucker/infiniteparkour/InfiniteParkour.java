package wtf.nucker.infiniteparkour;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.nucker.infiniteparkour.manager.impl.ParkourManager;

public final class InfiniteParkour extends JavaPlugin {

  private ParkourManager parkourManager;
  private PaperCommandManager commandManager;

  @Override
  public void onEnable() {
    this.getLogger().info("Infinite Parkour is loading up.");
    this.commandManager = new PaperCommandManager(this);
    this.parkourManager = new ParkourManager();
    parkourManager.enable(this);
  }

  @Override
  public void onDisable() {
    parkourManager.disable();
  }

  public ParkourManager getParkourManager() {
    return parkourManager;
  }

  public PaperCommandManager getCommandManager() {
    return commandManager;
  }
}
