package me.pokeproppond.pokeproppond;

import com.pixelmonmod.pixelmon.api.events.pokemon.EVsGainedEvent;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof EVsGainedEvent){
            EVsGainedEvent e = (EVsGainedEvent) event.getForgeEvent();
            EntityPlayerMP ownerPlayer = e.pokemon.getOwnerPlayer();
            if (ownerPlayer == null){
                return;
            }
            Player player = Bukkit.getPlayer(ownerPlayer.func_110124_au());
            if (!player.hasPermission("pokeproppond.listener.evsgained")) return;
            e.setCanceled(true);
            String playerName = ownerPlayer.func_70005_c_();
            int add = 0;
            for (int ev : e.evs) {
                add += ev;
            }
            Main.nyeApi.deposit(Main.evPoints,playerName,add);
            player.sendMessage("§a努力点数增加:"+add +"|现总点为:"+Main.nyeApi.getBalance(Main.evPoints,playerName));
        }
    }
}
