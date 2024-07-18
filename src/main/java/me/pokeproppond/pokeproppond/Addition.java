package me.pokeproppond.pokeproppond;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

@Getter
public class Addition {
    public static void init(Main pl) {
        AdType.init(pl);
    }

    private final String permission;
    private final CalculationType calculationType;
    private final Integer value;

    public Addition(String permission, CalculationType calculationType, Integer value) {
        this.permission = permission;
        this.calculationType = calculationType;
        this.value = value;
    }

    public int calculateBonusResults(Player player, int oldValue) {
        if (!player.hasPermission(this.getPermission())) {
            return oldValue;
        }
        return this.calculationType.calculationResults(oldValue, this.value);
    }

    public enum CalculationType {
        PLUS, MULTIPLY, REPLACE;

        public int calculationResults(int oldV, int addV) {
            if (this == PLUS) return oldV + addV;
            if (this == MULTIPLY) return oldV * addV;
            if (this == REPLACE) return addV;
            return 0;
        }
    }

    @Getter
    public enum AdType {
        EVS, EXP;

        private Addition[] additions;

        private static void init(Main pl) {
            for (AdType value : AdType.values()) {
                value.additions = new Addition[0];
            }
            ConfigurationSection section = pl.getConfig().getConfigurationSection("addition.evPoints");
            ConfigurationSection section1 = pl.getConfig().getConfigurationSection("addition.expPoints");
            if (section != null) {
                Set<String> keys = section.getKeys(true);
                keys.removeAll(section.getKeys(false));
                keys.removeIf(s-> !s.endsWith(".type"));
                if (!keys.isEmpty()) {
                    ArrayList<Addition> list = new ArrayList<>();
                    for (String key : keys) {
                        list.add(new Addition(
                                key, CalculationType.valueOf(section.getString(key))
                                , section.getInt(key.substring(0,key.lastIndexOf('.')) + ".value")
                        ));
                    }
                    EVS.additions = list.toArray(new Addition[0]);
                }
            }
            if (section1 != null) {
                Set<String> keys = section1.getKeys(true);
                keys.removeAll(section1.getKeys(false));
                keys.removeIf(s-> !s.endsWith(".type"));
                if (!keys.isEmpty()) {
                    ArrayList<Addition> list = new ArrayList<>();
                    for (String key : keys) {
                        list.add(new Addition(
                                key, CalculationType.valueOf(section1.getString(key))
                                , section1.getInt(key.substring(0,key.lastIndexOf('.')) + ".value")
                        ));
                    }
                    EXP.additions = list.toArray(new Addition[0]);
                }
            }
        }
    }
}
