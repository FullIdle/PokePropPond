package me.pokeproppond.pokeproppond.guihub;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
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
public class ExpGui extends GuiHelper {
    private boolean isSelectMenu = true;
    private boolean isLevelingUpGui = false;

    public ExpGui(Player player) {
        super(player, "§f§l经验值分点");
    }

    @Override
    public void selectPoke(Pokemon pokemon) {
        if (pokemon.isEgg()) {
            return;
        }
        this.isSelectMenu = false;
        this.removeMenu();
        pokemon.getLevelContainer().updateExpToNextLevel();
        //background
        {
            ItemStack aBg = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/evcheckergui.png")
                            .setSize(65, 112)
                            .setTextureSize(170, 112)
                            .setPosOverride(25, 16)
                            .setZLevel(1)
                            .build());
            ItemMeta itemMeta = aBg.getItemMeta();
            itemMeta.setDisplayName(" ");
            aBg.setItemMeta(itemMeta);
            this.getInventory().setItem(45, aBg);
        }
        {
            ItemStack bBg = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("minecraft:textures/gui/container/creative_inventory/tab_items.png")
                            .setColor(146, 146, 146, 155)
                            .setSize(65, 211)
                            .setTextureSize(212, 211)
                            .setPosOverride(88, 16)
                            .setUV(139, 0)
                            .setZLevel(1)
                            .build());
            ItemMeta itemMeta = bBg.getItemMeta();
            itemMeta.setDisplayName(" ");
            bBg.setItemMeta(itemMeta);
            this.getInventory().setItem(8, bBg);
        }

        //data
        this.setProgressItem(pokemon, pokemon.getLevelContainer().getExp());
        {
            String text = "§l总点:" + Main.nyeApi.getBalance(Main.expPoints, this.getPlayer().getName());
            ItemStack schedule = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setTextScale(16)
                            .setText(text)
                            .setPosOffset(0, 10)
                            .setZLevel(2)
                            .build()
            );
            ItemMeta itemMeta = schedule.getItemMeta();
            itemMeta.setDisplayName(text);
            schedule.setItemMeta(itemMeta);
            this.getInventory().setItem(46, schedule);
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
            this.getInventory().setItem(37, cancel);
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
            this.getInventory().setItem(39, save);
        }
        //↑
        {
            ItemStack up = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/shopkeeper.png")
                            .setSize(30, 23)
                            .setTextureSize(400, 400)
                            .setPosOverride(114, 35)
                            .setUV(232, 352)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = up.getItemMeta();
            itemMeta.setDisplayName("§l+");
            itemMeta.setLore(Collections.singletonList("§r§l1000倍率(shift+click)"));
            up.setItemMeta(itemMeta);
            this.getInventory().setItem(15, up);
        }
        //↓
        {
            ItemStack down = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setImage("pixelmon:textures/gui/shopkeeper.png")
                            .setSize(30, 23)
                            .setTextureSize(400, 400)
                            .setPosOverride(114, 90)
                            .setUV(232, 372)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = down.getItemMeta();
            itemMeta.setDisplayName("§l-");
            itemMeta.setLore(Collections.singletonList("§r§l1000倍率(shift+click)"));
            down.setItemMeta(itemMeta);
            this.getInventory().setItem(42, down);
        }
        //poke
        this.setTargetPoke(pokemon);
        {
            ItemStack photo = CraftItemStack.asBukkitCopy(
                    GuiHelper.getPokePhotoBuilder(pokemon)
                            .setSize(55, 55)
                            .setTextureSize(55, 55)
                            .setPosOverride(29, 38)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = photo.getItemMeta();
            itemMeta.setDisplayName(" ");
            photo.setItemMeta(itemMeta);
            this.getInventory().setItem(29, photo);
        }
        //pokeName
        {
            String displayName = "§l" + ((pokemon.getNickname() == null || pokemon.getNickname().replace(" ", "").isEmpty())
                    ? pokemon.getLocalizedName() : pokemon.getNickname());
            ItemStack name = CraftItemStack.asBukkitCopy(
                    ItemUIElement.builder()
                            .setTextJustification(TextJustification.CENTER)
                            .setPosOverride(57, 25)
                            .setText(displayName)
                            .setTextScale(16)
                            .setZLevel(2)
                            .build());
            ItemMeta itemMeta = name.getItemMeta();
            itemMeta.setDisplayName(displayName);
            name.setItemMeta(itemMeta);
            this.getInventory().setItem(2, name);
        }
        //event handler
        this.onOpen(e -> {
            if (this.isLevelingUpGui) {
                this.selectPoke(this.getTargetPoke());
            }
        });
        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory) return;
            int slot = e.getSlot();
            if (slot == 37) {
                //cancel
                this.isSelectMenu = true;
                this.setSelectMenu();
                return;
            }
            if (slot == 39) {
                //save
                int et = this.getExpTotalPoints();
                int ne = this.getItemNowSetExp();
                Level lc = this.getTargetPoke().getLevelContainer();
                int oe = lc.getExp();
                int pay = ne - oe;
                Main.nyeApi.set(Main.expPoints, this.getPlayer().getName(), et);
                if (pay == lc.expToNextLevel - oe) {
                    this.isLevelingUpGui = true;
                } else {
                    Bukkit.getScheduler().runTask(Main.plugin, () ->
                            this.selectPoke(this.getTargetPoke()));
                }
                lc.awardEXP(pay, ExperienceGainType.RARE_CANDY);
                return;
            }
            if (slot == 15) {
                //up
                if (!this.getTargetPoke().getLevelContainer().canLevelUp()) {
                    e.getWhoClicked().sendMessage("§c你的宝可梦已无法再升级了!");
                    return;
                }
                int et = this.getExpTotalPoints();
                if (et == 0) return;
                int ne = this.getItemNowSetExp();
                int nle = this.getTargetPoke().getLevelContainer().expToNextLevel;
                if (ne == nle) {
                    e.getWhoClicked().sendMessage("§c该等级所需经验已满,保存后进入下一阶段(保存后无法经验无法退还!)");
                    return;
                }
                if (e.isShiftClick()) {
                    int canA = Math.min(Math.min(et, 1000), nle - ne);
                    ne += canA;
                    et -= canA;
                } else {
                    ne++;
                    et--;
                }
                this.setProgressItem(this.getTargetPoke(), ne);
                this.setExpTotal(et);
                return;
            }
            if (slot == 32) {
                //all in
                int et = this.getExpTotalPoints();
                int ne = this.getItemNowSetExp();
                Level levelContainer = this.getTargetPoke().getLevelContainer();
                int oe = levelContainer.getExp();
                int nle = levelContainer.expToNextLevel;
                if (e.isShiftClick()) {
                    //out
                    if (ne == oe) return;
                    int canM = ne - oe;
                    ne -= canM;
                    et += canM;
                } else {
                    if (et == 0) return;
                    int canA = Math.min(nle - ne, et);
                    ne += canA;
                    et -= canA;
                }
                this.setProgressItem(this.getTargetPoke(), ne);
                this.setExpTotal(et);
            }
            if (slot == 42) {
                //down
                int oe = this.getTargetPoke().getLevelContainer().getExp();
                int ne = this.getItemNowSetExp();
                if (oe == ne) return;
                int et = this.getExpTotalPoints();
                if (e.isShiftClick()) {
                    int canM = Math.min(ne - oe, 1000);
                    ne -= canM;
                    et += canM;
                } else {
                    ne--;
                    et++;
                }
                this.setProgressItem(this.getTargetPoke(), ne);
                this.setExpTotal(et);
            }
        });

        this.onClose(e -> {
            if (this.isSelectMenu) return;
            Bukkit.getScheduler().runTask(Main.plugin, () -> {
                if (e.getPlayer() != null) {
                    if (!this.isLevelingUpGui) {
                        this.isSelectMenu = true;
                        this.setSelectMenu();
                    }
                    e.getPlayer().openInventory(e.getInventory());
                }
            });
        });
    }

    public int getItemNowSetExp() {
        ItemStack item = this.getInventory().getItem(32);
        return Integer.parseInt(item.getItemMeta().getDisplayName().replace("§l", "").split("/")[0]);
    }

    public void setProgressItem(Pokemon poke, int nowExp) {
        int h = calculateProgress(poke, nowExp);
        ItemStack progress = CraftItemStack.asBukkitCopy(
                ItemUIElement.builder()
                        .setImage("minecraft:textures/blocks/water_flow.png")
                        .setColor(198, 198, 198, 255)
                        .setSize(10, h)
                        .setTextureSize(10, 180)
                        .setPosOverride(94, 121 - h)
                        .setUV(0, 0)
                        .setZLevel(2)
                        .build()
        );
        ItemMeta itemMeta = progress.getItemMeta();
        itemMeta.setDisplayName("§l" + nowExp + "/" + poke.getLevelContainer().expToNextLevel);
        itemMeta.setLore(Collections.singletonList("§rAll IN(click me)"));
        progress.setItemMeta(itemMeta);
        this.getInventory().setItem(32, progress);
    }

    public int calculateProgress(Pokemon pokemon, int nowExp) {
        Level lc = pokemon.getLevelContainer();
        float fraction = lc.getExp() == nowExp ? lc.getExpFraction() : (lc.expToNextLevel == 0 ? 0.0F : (float) nowExp / (float) lc.expToNextLevel);
        return (int) (fraction * 90);
    }

    public int getExpTotalPoints() {
        return Integer.parseInt(this.getInventory().getItem(46).getItemMeta().getDisplayName().replace("§l总点:", ""));
    }

    private void setExpTotal(int value) {
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
}
