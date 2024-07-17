package me.pokeproppond.pokeproppond.guihub;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.TextJustification;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import lombok.Getter;
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack;
import me.pokeproppond.pokeproppond.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

@Getter
public class EvsGui extends GuiHelper {
    private boolean isSelectMenu = true;

    public EvsGui(Player player) {
        super(player, "§f§l努力值分点");
    }

    @Override
    public void selectPoke(Pokemon pokemon) {
        if (pokemon.isEgg()) {
            return;
        }
        this.isSelectMenu = false;
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
            this.getInventory().setItem(45, aBg);
        }
        //evsTag/data
        {
            {
                int data = Main.nyeApi.getBalance(Main.evPoints, this.getPlayer().getName());
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
                this.getInventory().setItem(46, total);
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
                    this.getInventory().setItem(4 + (i * 9), stack);
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
                    this.getInventory().setItem(6 + (i * 9), left);
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
                    this.getInventory().setItem(8 + (i * 9), right);
                }
                //data
                {
                    String text = "§l" + pokemon.getEVs().getStat(statValue);
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
                    this.getInventory().setItem(7 + (i * 9), data);
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
            this.getInventory().setItem(36, cancel);
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
            this.getInventory().setItem(38, save);
        }


        //poke
        this.setTargetPoke(pokemon);
        {
            ItemStack photo = CraftItemStack.asBukkitCopy(
                    GuiHelper.getPokePhotoBuilder(pokemon)
                            .setSize(55, 55)
                            .setTextureSize(55, 55)
                            .setPosOverride(9, 38)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = photo.getItemMeta();
            itemMeta.setDisplayName(" ");
            photo.setItemMeta(itemMeta);
            this.getInventory().setItem(19, photo);
        }
        //pokeName
        {
            String displayName = "§l" + ((pokemon.getNickname() == null || pokemon.getNickname().replace(" ", "").isEmpty())
                    ? pokemon.getLocalizedName() : pokemon.getNickname());
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
            this.getInventory().setItem(1, name);
        }

        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory) return;
            int slot = e.getSlot();
            //cancel
            if (slot == 36) {
                this.isSelectMenu = true;
                this.setSelectMenu();
                return;
            }
            if (slot == 38) {
                //待写
                int[] array = new int[6];
                for (int i = 0; i < 6; i++) {
                    array[i] = Integer.parseInt(this.getInventory().getItem(7 + (i * 9))
                            .getItemMeta().getDisplayName().replace("§l", ""));
                }
                this.getTargetPoke().getEVs().fillFromArray(array);
                Main.nyeApi.set(Main.evPoints, this.getPlayer().getName(), getEvPoints());
                e.getWhoClicked().sendMessage("§a分配数据已保存!");
                this.isSelectMenu = true;
                this.setSelectMenu();
                return;
            }
            //-
            if ((slot - 6) % 9 == 0) {
                ItemStack item = this.getInventory().getItem(slot + 1);
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                int evs = pokemon.getEVs().getArray()[(slot - 6) / 9];
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
                this.getInventory().setItem(slot + 1, CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                                .setText(text)
                                .build()));
                this.setEvPoints(this.getEvPoints() + (ee - ev));
                return;
            }
            //all in
            if ((slot - 7) % 9 == 0) {
                ItemStack item = this.getInventory().getItem(slot);
                int evPoints = this.getEvPoints();
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                int pos = (slot - 7) / 9;
                int rt;
                int re;
                if (e.isShiftClick()) {
                    int ov = this.getTargetPoke().getEVs().getArray()[pos];
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
                this.getInventory().setItem(slot, CraftItemStack.asBukkitCopy(
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
                ItemStack item = this.getInventory().getItem(slot - 1);
                ItemMeta itemMeta = item.getItemMeta();
                int ev = Integer.parseInt(itemMeta.getDisplayName().replace("§l", ""));
                if (ev == 252) return;
                int ee = ev;
                if (e.isShiftClick()) {
                    int canM = Math.min(510 - total, 4);
                    if (ev + canM > 252) {
                        ev = 252;
                    } else {
                        ev += canM;
                    }
                } else {
                    ev++;
                }
                int ec = ev - ee;
                if (ec > evPoints) {
                    ec = evPoints;
                    ev = ee + evPoints;
                }

                String text = "§l" + ev;
                itemMeta.setDisplayName(text);
                item.setItemMeta(itemMeta);
                this.getInventory().setItem(slot - 1, CraftItemStack.asBukkitCopy(
                        ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                                .setText(text)
                                .build()));
                this.setEvPoints(evPoints - ec);
            }
        });
        this.onClose(e -> {
            if (this.isSelectMenu) return;
            this.isSelectMenu = true;
            this.setSelectMenu();
            Bukkit.getScheduler().runTask(Main.plugin, () -> e.getPlayer().openInventory(e.getInventory()));
        });
    }

    private int getEvPoints() {
        return Integer.parseInt(this.getInventory().getItem(46).getItemMeta().getDisplayName().replace("§l总点:", ""));
    }

    private void setEvPoints(long value) {
        ItemStack item = this.getInventory().getItem(46);
        ItemMeta itemMeta = item.getItemMeta();
        String text = "§l总点:" + value;
        itemMeta.setDisplayName(text);
        item.setItemMeta(itemMeta);
        this.getInventory().setItem(46, CraftItemStack.asBukkitCopy(
                ItemUIElement.builder(((net.minecraft.item.ItemStack) CraftItemStack.asNMSCopy(item)))
                        .setText(text)
                        .build()
        ));
    }

    private int getEvTotal() {
        if (this.getTargetPoke() == null) {
            return -1;
        }
        int total = 0;
        for (int i = 0; i < 6; i++) {
            total += Integer.parseInt(
                    this.getInventory().getItem(7 + (i * 9)).getItemMeta().getDisplayName().replace("§l", ""));
        }
        return total;
    }
}
