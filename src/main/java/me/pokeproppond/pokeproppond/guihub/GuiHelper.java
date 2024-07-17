package me.pokeproppond.pokeproppond.guihub;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.Getter;
import lombok.Setter;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public abstract class GuiHelper extends ListenerInvHolder {
    private static final List<Integer> emptyList = Lists.newArrayList(
            10, 13, 16, 37, 40, 43
    );

    private final Player player;
    private final Inventory inventory;
    @Setter
    private Pokemon targetPoke;

    public GuiHelper(Player player, String title) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 6 * 9, title);
        this.setSelectMenu();
    }

    public void setSelectMenu() {
        removeMenu();
        //clear data
        this.targetPoke = null;

        //set
        ItemStack itemStack = CraftItemStack.asBukkitCopy(
                ItemUIElement.builder()
                        .setImage("minecraft:textures/gui/demo_background.png")
                        .setSize(163, 112)
                        .setTextureSize(230, 190)
                        .setPosOffset(-1, -1)
                        .setUV(5, 5)
                        .setZLevel(0)
                        .build());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        this.getInventory().setItem(0, itemStack);
        ItemStack bg = CraftItemStack.asBukkitCopy(
                ItemUIElement.builder()
                        .setImage("pixelmon:textures/gui/starter/background.png")
                        .setSize(162, 110)
                        .setTextureSize(162, 110)
                        .setPosOffset(-18, 0)
                        .setZLevel(0)
                        .build());
        ItemMeta bgM = bg.getItemMeta();
        bgM.setDisplayName(" ");
        bg.setItemMeta(bgM);
        this.getInventory().setItem(1, bg);

        PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.player.getUniqueId());
        Pokemon[] all = party.getAll();
        for (int i = 0; i < all.length; i++) {
            this.getInventory().setItem(emptyList.get(i), getPokeItemStack(all[i]));
        }
        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory || !emptyList.contains(e.getSlot())) return;
            int slot = e.getSlot();
            int pos = emptyList.indexOf(slot);
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(this.player.getUniqueId());
            Pokemon pokemon = pps.get(pos);
            if (pokemon == null) return;
            this.selectPoke(pokemon);
        });
    }

    public void removeMenu() {
        for (int i = 1; i < 54; i++) {
            this.getInventory().setItem(i, null);
        }
    }

    public abstract void selectPoke(Pokemon pokemon);

    public static ItemUIElement.Builder getPokePhotoBuilder(Pokemon pokemon) {
        StringBuilder builder = new StringBuilder("pixelmon:textures/sprites/");
        builder.append((pokemon.isShiny() ? "shinypokemon/" : "pokemon/"));
        int i = pokemon.getSpecies().getNationalPokedexInteger();
        builder.append(String.format("%03d", i));
        if (pokemon.getFormEnum().isDefaultForm()) {
            builder.append(pokemon.getFormEnum().getFormSuffix());
        }
        builder.append(".png");
        String path = builder.toString();
        return ItemUIElement.builder().setImage(path);
    }

    public static ItemStack getPokeItemStack(Pokemon pokemon) {
        ItemStack itemStack;
        if (pokemon == null) {
            itemStack = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/wikiicon.png")
                            .setSize(16, 16)
                            .setTextureSize(16, 16)
                            .setZLevel(1)
                            .build());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c空槽");
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        itemStack = CraftItemStack.asBukkitCopy(
                GuiHelper.getPokePhotoBuilder(pokemon)
                        .setSize(16, 16)
                        .setTextureSize(16, 16)
                        .setHoverImage("pixelmon:textures/gui/acceptdeny/buttonover.png")
                        .setZLevel(1)
                        .build());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§3" + pokemon.getLocalizedName());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
