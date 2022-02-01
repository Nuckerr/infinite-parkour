package wtf.nucker.infiniteparkour.manager.impl;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import wtf.nucker.infiniteparkour.InfiniteParkour;
import wtf.nucker.infiniteparkour.command.ParkourCommand;
import wtf.nucker.infiniteparkour.listener.PlayerListener;
import wtf.nucker.infiniteparkour.manager.BaseManager;
import wtf.nucker.infiniteparkour.object.ParkourSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nucker
 * @project infinite-parkour
 * @date 01/02/2022
 */
public class ParkourManager extends BaseManager {

  private InfiniteParkour core;
  private Map<UUID, ParkourSession> currentSessions;
  private Yaml config;

  private int yLevelDifference;

  @Override
  public void enable(InfiniteParkour core) {
    this.core = core;
    this.currentSessions = new HashMap<>();
    this.config = new Yaml("config.yml", this.core.getDataFolder().getAbsolutePath(), this.core.getResource("config.yml"))
      .addDefaultsFromInputStream();
    config.setReloadSettings(ReloadSettings.MANUALLY);
    config.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);

    this.yLevelDifference = config.getInt("highest-y-level") - config.getInt("lowest-y-level");
    if(yLevelDifference == 0) this.yLevelDifference = 1;

    core.getCommandManager().registerCommand(new ParkourCommand(this));
    core.getServer().getPluginManager().registerEvents(new Listener() {

      @EventHandler
      public void onLogout(PlayerQuitEvent event) {
        if(currentSessions.containsKey(event.getPlayer().getUniqueId())) {
          getPlayerSession(event.getPlayer()).end();
        }
      }
    }, core);
    core.getServer().getPluginManager().registerEvents(new PlayerListener(this), core);
  }

  @Override
  public void disable() {
    currentSessions.clear();
  }

  public int getYLevelDifference() {
    return yLevelDifference;
  }

  public Yaml getConfig() {
    return config;
  }

  public boolean isPlayerPlaying(Player player) {
    return this.getCurrentSessions().containsKey(player.getUniqueId());
  }

  @Nullable
  public ParkourSession getPlayerSession(Player player) {
    return this.getCurrentSessions().getOrDefault(player.getUniqueId(), null);
  }

  public ParkourSession startSession(Player player) {
    if(this.isPlayerPlaying(player)) throw new IllegalArgumentException("Player is already playing infinite parkour");
    ParkourSession session = new ParkourSession(player, this);
    this.currentSessions.put(player.getUniqueId(), session);
    session.start();
    return session;
  }

  /**
   * @deprecated only designed to be used by ParkourSession to remove itself
   */
  public void forceRemoveSession(ParkourSession session) {
    this.currentSessions.remove(session.getPlayer().getUniqueId(), session);
  }

  public Map<UUID, ParkourSession> getCurrentSessions() {
    return currentSessions;
  }
}
