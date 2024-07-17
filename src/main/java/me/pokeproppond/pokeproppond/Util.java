package me.pokeproppond.pokeproppond;

import org.junit.Test;

import java.util.*;

public class Util {
    @Test
    public void test() {
        List<String> list = Arrays.asList(
                "      a  ",
                "      a  ",
                "      a  ",
                "      a  ",
                "      a  ",
                "      a  "
        );
        for (int y = 0; y < list.size(); y++) {
            char[] chars = list.get(y).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == 'a') {
                    System.out.println(y * 9 + i);
                }
            }
        }
    }
}
