package com.github.danirod12.jackal.server.game.move;

import com.github.danirod12.jackal.server.game.generator.MapGenerator;
import com.github.danirod12.jackal.server.game.item.TeamBoat;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.game.tile.VoidTile;
import com.github.danirod12.jackal.server.util.GameColor;
import com.github.danirod12.jackal.server.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DirectionManager {

    /**
     *
     * @param map Generated map [y][x]
     * @param y Start location y
     * @param x Start location x
     * @return Available movements and ways. location.location.(...).target
     */
    public static List<String> getAvailableMovements(GameTile[][] map, int y, int x, GameColor color) {
        if(color == null || color == GameColor.UNKNOWN) throw new IllegalArgumentException("Color cannot be null or unknown");
        return getAvailableMovements(map, y, x, color, null, new ArrayList<>());
    }

    private static List<String> getAvailableMovements(GameTile[][] map, int y, int x, GameColor color, String prefix, List<GameTile> skip) {

        List<String> actions = new ArrayList<>();

        if(y < 0 || x < 0 || y >= map.length || x >= map[0].length) return actions;

        GameTile tile = map[y][x];
        if(skip.contains(tile)) return actions;
        skip.add(tile);

        if(tile instanceof VoidTile && prefix == null) {
            if(((VoidTile) tile).getBoat() != null) {
                for (Pair<Integer, Integer> shore : MapGenerator.getShores(map, true, y, x)) {
                    actions.addAll(getAvailableMovements(map, shore.getKey(), shore.getValue(), color, y + ":" + x, skip));
                }
            } else {
                for(Pair<Integer, Integer> shore : MapGenerator.getShoresRelatedArea(map, false, true, y, x))
                    actions.add(y + ":" + x + "." + shore.getA() + ":" + shore.getB());
            }
            return actions;
        }

        if(!tile.isAccessible(color)) return actions;
        // TODO check if tile closed

        final String new_prefix = (prefix == null ? "" : prefix + ".") + y + ":" + x;

        final MoveDirection[] directions = prefix == null ? MoveDirection.getAllShortDirections() : tile.getRedirections();
        for(MoveDirection direction : directions) {
            int new_y = y + direction.getY();
            int new_x = x + direction.getX();
            if(map[new_y][new_x] instanceof VoidTile && prefix == null) {
                TeamBoat boat = ((VoidTile) map[new_y][new_x]).getBoat();
                if(boat != null && boat.getColor() == color)
                    actions.add(new_prefix + "." + new_y + ":" + new_x);
                continue;
            }
            actions.addAll(getAvailableMovements(map, new_y, new_x, color, new_prefix, skip));
        }

        if(directions.length == 0)
            actions.add(new_prefix);
        return actions;

    }

}
