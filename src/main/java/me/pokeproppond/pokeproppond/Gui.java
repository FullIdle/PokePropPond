package me.pokeproppond.pokeproppond;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.TextJustification;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@Getter
public class Gui extends ListenerInvHolder {
    private static final List<Integer> emptyList = Lists.newArrayList(
            10, 13, 16, 37, 40, 43
    );
    private final Inventory inventory;
    private final Player player;
    private Pokemon targetPoke = null;

    public Gui(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 6 * 9, "§f§l努力值分点");
        setMainMenu();
    }

    private void removeMenu() {
        for (int i = 1; i < 54; i++) {
            this.inventory.setItem(i, null);
        }
    }

    private void setMainMenu() {
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
        this.inventory.setItem(0, itemStack);
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
        this.inventory.setItem(1, bg);

        PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.player.getUniqueId());
        Pokemon[] all = party.getAll();
        for (int i = 0; i < all.length; i++) {
            this.inventory.setItem(emptyList.get(i), getPokeItemStack(all[i]));
        }
        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory || !emptyList.contains(e.getSlot())) return;
            int slot = e.getSlot();
            int pos = emptyList.indexOf(slot);
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(this.player.getUniqueId());
            Pokemon pokemon = pps.get(pos);
            if (pokemon == null || pokemon.isEgg()) return;
            setEffortPokeMenu(pokemon);
        });
    }

    private void setEffortPokeMenu(Pokemon poke) {
        removeMenu();
        //background
        {
            ItemStack aBg = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/evcheckergui.png")
                            .setSize(161, 112)
                            .setTextureSize(161, 112)
                            .setPosOverride(7, 16)
                            .setZLevel(1)
                            .build());
            ItemMeta itemMeta = aBg.getItemMeta();
            itemMeta.setDisplayName(" ");
            aBg.setItemMeta(itemMeta);
            this.inventory.setItem(45, aBg);
        }
        //evsTag/data
        {
            {
                long data = Main.nyeApi.getBalance(Main.evPoints,this.player.getName());
                String name = "§l总点:" + data;
                ItemStack total = CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder()
                                .setText(name)
                                .setPosOffset(-19, 9)
                                .setTextScale(16)
                                .setZLevel(2)
                                .build());
                ItemMeta itemMeta = total.getItemMeta();
                itemMeta.setDisplayName(name);
                total.setItemMeta(itemMeta);
                this.inventory.setItem(46, total);
            }
            StatsType[] statValues = StatsType.getStatValues();
            for (int i = 0; i < statValues.length; i++) {
                StatsType statValue = statValues[i];
                {
                    String typeName = "§l" + statValue.getLocalizedName();
                    ItemStack stack = CraftItemStack.asBukkitCopy(
                            ItemUIElement.builder()
                                    .setText(typeName)
                                    .setPosOverride(75, 28 + (17 * i))
                                    .setTextScale(16)
                                    .setZLevel(2)
                                    .build());
                    ItemMeta itemMeta = stack.getItemMeta();
                    itemMeta.setDisplayName(typeName);
                    stack.setItemMeta(itemMeta);
                    this.inventory.setItem(4 + (i * 9), stack);
                }
                //<
                {
                    ItemStack left = CraftItemStack.asBukkitCopy(
                            ItemUIElement.builder()
                                    .setImage("pixelmon:textures/gui/uielements/tile034.png")
                                    .setSize(16, 16)
                                    .setTextureSize(16, 16)
                                    .setPosOverride(115, 23 + (17 * i))
                                    .setZLevel(2)
                                    .build());
                    ItemMeta itemMeta = left.getItemMeta();
                    itemMeta.setDisplayName("§l-");
                    itemMeta.setLore(Collections.singletonList("§r§l四倍率(shift+click)"));
                    left.setItemMeta(itemMeta);
                    this.inventory.setItem(6 + (i * 9), left);
                }
                //>
                {
                    ItemStack right = CraftItemStack.asBukkitCopy(
                            ItemUIElement.builder()
                                    .setImage("pixelmon:textures/gui/uielements/tile032.png")
                                    .setSize(16, 16)
                                    .setTextureSize(16, 16)
                                    .setPosOverride(152, 23 + (17 * i))
                                    .setZLevel(2)
                                    .build());
                    ItemMeta itemMeta = right.getItemMeta();
                    itemMeta.setDisplayName("§l+");
                    itemMeta.setLore(Collections.singletonList("§r§l四倍率(shift+click)"));
                    right.setItemMeta(itemMeta);
                    this.inventory.setItem(8 + (i * 9), right);
                }
                //data
                {
                    String text = "§l" + poke.getEVs().getStat(statValue);
                    ItemStack data = CraftItemStack.asBukkitCopy(
                            ItemUIElement.builder()
                                    .setTextJustification(TextJustification.CENTER)
                                    .setText(text)
                                    .setTextScale(16)
                                    .setPosOverride(142, 27 + (17 * i))
                                    .setZLevel(2)
                                    .build());
                    ItemMeta itemMeta = data.getItemMeta();
                    itemMeta.setDisplayName(text);
                    itemMeta.setLore(Collections.singletonList("§rAll IN(click me)"));
                    data.setItemMeta(itemMeta);
                    this.inventory.setItem(7 + (i * 9), data);
                }
            }
        }
        //cancel save
        {
            ItemStack cancel = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/inventory/disabled.png")
                            .setSize(13, 13)
                            .setTextureSize(13, 13)
                            .setPosOffset(5, 6)
                            .setZLevel(2)
                            .build()
            );
            ItemMeta itemMeta = cancel.getItemMeta();
            itemMeta.setDisplayName("§c取消");
            cancel.setItemMeta(itemMeta);
            this.inventory.setItem(36, cancel);
        }

        {
            ItemStack save = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/pc/icons/rename.png")
                            .setSize(13, 13)
                            .setTextureSize(13, 13)
                            .setPosOffset(5, 6)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = save.getItemMeta();
            itemMeta.setDisplayName("§a保存");
            save.setItemMeta(itemMeta);
            this.inventory.setItem(38, save);
        }


        //poke
        this.targetPoke = poke;
        {
            ItemStack photo = CraftItemStack.asBukkitCopy(
                    getPokePhotoBuilder(poke)
                            .setSize(55, 55)
                            .setTextureSize(55, 55)
                            .setPosOverride(9, 30)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = photo.getItemMeta();
            itemMeta.setDisplayName(" ");
            photo.setItemMeta(itemMeta);
            this.inventory.setItem(19, photo);
        }
        //pokeName
        {
            String displayName = "§l" + ((poke.getNickname() == null || poke.getNickname().replace(" ", "").isEmpty())
                    ? poke.getLocalizedName() : poke.getNickname());
            ItemStack name = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setTextJustification(TextJustification.CENTER)
                            .setPosOverride(40, 25)
                            .setText(displayName)
                            .setTextScale(16)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = name.getItemMeta();
            itemMeta.setDisplayName(displayName);
            name.setItemMeta(itemMeta);
            this.inventory.setItem(1, name);
        }

        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory) return;
            int slot = e.getSlot();
            //cancel
            if (slot == 36) {
                this.setMainMenu();
                return;
            }
            if (slot == 38) {
                //待写
                int[] array = new int[6];
                for (int i = 0; i < 6; i++) {
                    array[i] = Integer.parseInt(this.inventory.getItem(7 + (i * 9))
                            .getItemMeta().getDisplayName().replace("§l", ""));
                }
                this.targetPoke.getEVs().fillFromArray(array);
                Main.nyeApi.set(Main.evPoints,this.player.getName(),getEvPoints());
                e.getWhoClicked().sendMessage("§a分配数据已保存!");
                this.setMainMenu();
                return;
            }
            //-
            if ((slot - 6) % 9 == 0) {
                ItemStack item = this.inventory.getItem(slot + 1);
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                int evs = poke.getEVs().getArray()[(slot - 6) / 9];
                if (ev == 0 || ev == evs) return;
                int ee = ev;
                if (e.isShiftClick()) {
                    if (ev - 4 < 0) {
                        ev = 0;
                    } else {
                        ev -= 4;
                    }
                } else {
                    ev--;
                }
                if (ev < evs) ev = evs;
                String text = "§l" + ev;
                itemMeta.setDisplayName(text);
                item.setItemMeta(itemMeta);
                this.inventory.setItem(slot + 1, CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                                .setText(text)
                                .build()));
                this.setEvPoints(this.getEvPoints() + (ee - ev));
                return;
            }
            //all in
            if ((slot - 7) % 9 == 0) {
                ItemStack item = this.inventory.getItem(slot);
                int evPoints = this.getEvPoints();
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                int pos = (slot - 7) / 9;
                int rt;
                int re;
                if (e.isShiftClick()) {
                    int ov = this.targetPoke.getEVs().getArray()[pos];
                    if (ev == ov) return;
                    rt = evPoints + (ev - ov);
                    re = ov;
                } else {
                    int total = this.getEvTotal();
                    if (evPoints == 0 || total == 510 || ev == 252) return;
                    int canA = Math.min(Math.min(510 - total, 252 - ev), evPoints);
                    rt = evPoints - canA;
                    re = ev + canA;
                }
                String text = "§l" + re;
                itemMeta.setDisplayName(text);
                item.setItemMeta(itemMeta);
                this.inventory.setItem(slot, CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                                .setText(text)
                                .build()));
                this.setEvPoints(rt);
                return;
            }
            //+
            if ((slot - 8) % 9 == 0) {
                int evPoints = this.getEvPoints();
                if (evPoints == 0) return;
                int total = this.getEvTotal();
                if (total == 510) return;
                ItemStack item = this.inventory.getItem(slot - 1);
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                if (ev == 252) return;
                int ee = ev;
                if (e.isShiftClick()) {
                    if (ev + 4 > 252) {
                        ev = 252;
                    } else {
                        ev += 4;
                    }
                } else {
                    ev++;
                }
                long ec = ev - ee;
                if (ec > evPoints) {
                    ec = evPoints;
                    ev = (int) (ee + evPoints);
                }

                String text = "§l" + ev;
                itemMeta.setDisplayName(text);
                item.setItemMeta(itemMeta);
                this.inventory.setItem(slot - 1, CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                                .setText(text)
                                .build()));
                this.setEvPoints(evPoints - ec);
                return;
            }
            return;
        });
    }

    private int getEvPoints() {
        return Integer.parseInt(this.inventory.getItem(46).getItemMeta().getDisplayName().replace("§l总点:", ""));
    }

    private void setEvPoints(long value) {
        ItemStack item = this.inventory.getItem(46);
        ItemMeta itemMeta = item.getItemMeta();
        String text = "§l总点:" + value;
        itemMeta.setDisplayName(text);
        item.setItemMeta(itemMeta);
        this.inventory.setItem(46, CraftItemStack.asBukkitCopy(
                ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                        .setText(text)
                        .build()
        ));
    }

    private int getEvTotal() {
        if (this.targetPoke == null) {
            return -1;
        }
        int total = 0;
        for (int i = 0; i < 6; i++) {
            total += Integer.parseInt(
                    this.inventory.getItem(7 + (i * 9)).getItemMeta().getDisplayName().replace("§l", ""));
        }
        return total;
    }

    private ItemStack getPokeItemStack(Pokemon pokemon) {
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
        itemStack = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asBukkitCopy(
                (((net.minecraft.server.v1_12_R1.ItemStack) (Object)
                        ItemPixelmonSprite.getPhoto(pokemon))));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§3" + pokemon.getLocalizedName());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemUIElement.Builder getPokePhotoBuilder(Pokemon pokemon) {
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
}
