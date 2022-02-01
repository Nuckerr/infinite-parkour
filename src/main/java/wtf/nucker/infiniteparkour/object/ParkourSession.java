package wtf.nucker.infiniteparkour.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import wtf.nucker.infiniteparkour.manager.impl.ParkourManager;

import java.util.Random;

/**
 * @author Nucker
 * @project infinite-parkour
 * @date 01/02/2022
 */
public class ParkourSession {

  private static final Integer MAX_DISTANCE = 4;

  private final Player player;
  private final ParkourManager manager;
  private final Random random;
  private final Location spawnLocation;

  private Block currentBlock, previousBlock, nextBlock;

  public ParkourSession(Player player, ParkourManager manager) {
    this.player = player;
    this.spawnLocation = player.getLocation();
    this.manager = manager;
    this.random = new Random();
  }

  public void start() {
    final int y = manager.getConfig().getInt("lowest-y-level") + random.nextInt(manager.getYLevelDifference());
    boolean addX = random.nextBoolean(), addZ = random.nextBoolean();
    int x = random.nextInt(manager.getConfig().getInt("range")), z = random.nextInt(manager.getConfig().getInt("range"));
    if(addX) x = (int) player.getLocation().getX() + x; else x = (int) player.getLocation().getX() - x;
    if(addZ) z = (int) player.getLocation().getX() + z; else z = (int) player.getLocation().getZ() - z;
    Location startingLoc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
    startingLoc.getBlock().setType(Material.valueOf(manager.getConfig().getString("blocks.current")));
    this.currentBlock = startingLoc.getBlock();
    this.genNextBlock();
    player.teleport(startingLoc.clone().add(0, 1, 0));
  }

  public void prepareForNext() {
    if(this.previousBlock != null) this.previousBlock.setType(Material.AIR);
    this.previousBlock = this.currentBlock;
    this.currentBlock = this.nextBlock;
    this.currentBlock.setType(Material.valueOf(manager.getConfig().getString("blocks.current")));
    this.previousBlock.setType(Material.valueOf(manager.getConfig().getString("blocks.previous")));
    this.genNextBlock();
  }

  private void genNextBlock() {
    if(currentBlock == null) {
      player.teleport(spawnLocation);
      player.sendMessage(Component.text("An unexpected error occured").color(NamedTextColor.RED));
      throw new NullPointerException("Unable to locate the current block for " + player.getName() + "'s parkour.");
    }

    int y = 0, maxDistance = MAX_DISTANCE;
    if(random.nextBoolean() && !(currentBlock.getY() >= manager.getConfig().getInt("highest-y-level"))) {
      ++y;
      maxDistance = 3;
    }else if(random.nextBoolean() && !(currentBlock.getY() <= manager.getConfig().getInt("lowest-y-level"))) {
      --y;
      maxDistance = 5;
    }
    final boolean changeX = random.nextBoolean();
    int distance = random.nextInt(maxDistance);
    while (distance <= 1) { // Make sure its not right next to the current block
      distance = random.nextInt(maxDistance);
    }
    int x = changeX ? distance : 0, z = !changeX ? distance : 0;
    Location newLoc = currentBlock.getLocation().clone().add(x, y, z);
    newLoc.getBlock().setType(Material.valueOf(manager.getConfig().getString("blocks.next")));
    this.nextBlock = newLoc.getBlock();
  }


  public void end() {
    player.teleport(spawnLocation);
    if(this.previousBlock != null) this.previousBlock.setType(Material.AIR);
    this.currentBlock.setType(Material.AIR);
    this.nextBlock.setType(Material.AIR);
    manager.forceRemoveSession(this);
  }

  public int getLowestY() {
    return this.currentBlock.getY() - 2; // Bit of leg room
  }

  public Player getPlayer() {
    return player;
  }
}
