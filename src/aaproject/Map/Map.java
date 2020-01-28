/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaproject.Map;

import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 * @author Darren
 */
public class Map {

    private MapUnit[][] map;

    public Map() {
        map = new MapUnit[31][31];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (i == 0) {
                    map[i][j] = new MapUnit("North Border", i, j);
                } else if (i == 31 - 1) {
                    map[i][j] = new MapUnit("South Border", i, j);
                } else if (j == 0) {
                    map[i][j] = new MapUnit("West Border", i, j);
                } else if (j == 31 - 1) {
                    map[i][j] = new MapUnit("East Border", i, j);
                } else {
                    map[i][j] = new MapUnit("Empty", i, j);
                }
            }

        }
    }

    @Override
    public String toString() {
        return Arrays.deepToString(map).replace("], ", "]\n").replace(",", "").replace("[", "").replace("]", "").trim();
    }

    public synchronized MapUnit[][] getMap() {
        return map;
    }

    public int getSize() {
        return map.length;
    }

}
