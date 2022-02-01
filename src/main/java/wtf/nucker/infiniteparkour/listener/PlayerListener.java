package wtf.nucker.infiniteparkour.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wtf.nucker.infiniteparkour.manager.impl.ParkourManager;
import wtf.nucker.infiniteparkour.object.ParkourSession;

/**
 * @author Nucker
 * @project infinite-parkour
 * @date 01/02/2022
 */
public class PlayerListener implements Listener {

  private final ParkourManager manager;

  public PlayerListener(ParkourManager manager) {
    this.manager = manager;
  }

  @EventHandler
  public void onPlayerJump(PlayerJumpEvent event) {
    if(!manager.isPlayerPlaying(event.getPlayer())) return;
    Block hitBlock = event.getTo().add(0, -1, 0).getBlock();
    if(!hitBlock.getType().equals(Material.valueOf(manager.getConfig().getString("blocks.next")))) return;
    manager.getPlayerSession(event.getPlayer()).prepareForNext();
  }

  @EventHandler
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    if(!manager.isPlayerPlaying(event.getPlayer())) return;
    ParkourSession session = manager.getPlayerSession(event.getPlayer());
    if(event.getPlayer().getLocation().getY() < session.getLowestY()) {
      session.end();
      event.getPlayer().sendMessage(Component.text("You failed the parkour").color(NamedTextColor.RED));
    }
  }
}
