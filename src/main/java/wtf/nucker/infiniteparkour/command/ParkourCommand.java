package wtf.nucker.infiniteparkour.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import wtf.nucker.infiniteparkour.manager.impl.ParkourManager;

/**
 * @author Nucker
 * @project infinite-parkour
 * @date 01/02/2022
 */
@CommandAlias("parkour")
public class ParkourCommand extends BaseCommand {

  private final ParkourManager manager;

  public ParkourCommand(ParkourManager manager) {
    this.manager = manager;
  }

  @Subcommand("start")
  @Description("Starts the parkour")
  public void onStart(Player player) {
    try {
      manager.startSession(player);
    }catch (IllegalArgumentException e) {
      player.sendMessage(Component.text("You have already started the parkour").color(NamedTextColor.RED));
    }
  }

  @Subcommand("end")
  @Description("End a parkour")
  public void onEnd(Player player) {
    if(!manager.isPlayerPlaying(player)) {
      player.sendMessage(Component.text("You are not currently playing").color(NamedTextColor.RED));
      return;
    }
    manager.getPlayerSession(player).end();
    player.sendMessage(Component.text("You have ended your parkour session").color(NamedTextColor.RED));
  }
}
