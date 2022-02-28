package com.github.danirod12.jackal.server.game.generator;

import com.github.danirod12.jackal.server.game.item.PlayerEntity;
import com.github.danirod12.jackal.server.game.item.TeamBoat;
import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.game.tile.EmptyTile;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.game.tile.TileType;
import com.github.danirod12.jackal.server.game.tile.VoidTile;
import com.github.danirod12.jackal.server.util.GameColor;
import com.github.danirod12.jackal.server.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MapGenerator {

    /**
     * @param sizeY Height (Size Y)
     * @param sizeX Width (Size X)
     * @param players Players amount that will play on this map
     * @return GameTile[y][x]. Generated cuboid map. First index for Y, second index for X
     */
    public static GameTile[][] generateMap(int sizeY, int sizeX, int players) {

        // TODO generate map
        return new GameTile[][] {

                { new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile() },
                { new VoidTile(), new VoidTile(), new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new VoidTile(), new VoidTile(), new VoidTile() },
                { new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.SAND), new VoidTile(), new VoidTile() },
                { new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.ROCK), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.SAND), new VoidTile() },
                { new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.ROCK), new EmptyTile(TileType.ROCK), new EmptyTile(TileType.ROCK), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.SAND), new VoidTile() },
                { new VoidTile(), new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.GRASS), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new VoidTile(), new VoidTile() },
                { new VoidTile(), new VoidTile(), new VoidTile(), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new EmptyTile(TileType.SAND), new VoidTile() },
                { new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile(), new VoidTile() }

        };

    }

    /**
     * Insert team boats
     * @param map Generated cuboid map
     * @param colors Colors to be inserted
     * @return Assigned colors and created boats
     */
    public static Map<GameColor, TeamBoat> connectBoats(GameTile[][] map, List<GameColor> colors) {

        Map<GameColor, TeamBoat> boats = new HashMap<>();

        List<VoidTile> tiles = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {

                GameTile tile = map[y][x];
                if(tile instanceof VoidTile && getShores(map, true, y, x).size() != 0)
                    tiles.add((VoidTile) tile);

            }
        }

        for(GameColor color : colors) {

            TeamBoat boat = new TeamBoat(color);

            VoidTile tile = tiles.get(ThreadLocalRandom.current().nextInt(tiles.size()));
            tiles.remove(tile);
            tile.addItem(boat);

            for (int i = 0; i < 3; i++) {
                PlayerEntity player = boat.registerPlayer();
                tile.addItem(player);
            }

            boats.put(color, boat);

        }

        return boats;

    }

    /**
     * Get tile shores (List(Pair(y, x)))
     * @param map Generated cuboid map
     * @param shortest True - only up, down, right and left. False - all directions
     * @param y Y start location
     * @param x X start location
     * @return Tile shores
     */
    public static List<Pair<Integer, Integer>> getShores(GameTile[][] map, boolean shortest, int y, int x) {

        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for(MoveDirection direction : shortest ? MoveDirection.getAllShortestDirections() : MoveDirection.getAllShortDirections()) {

            int newY = y + direction.getY();
            int newX = x + direction.getX();

            if(newX < 0 || newY < 0 || newY >= map.length || newX >= map[0].length) continue;
            if(!(map[newY][newX] instanceof VoidTile))
                list.add(new Pair<>(newY, newX));

        }
        return list;

    }

    /**
     * Get tile shores (List(Pair(y, x)))
     * @param map Generated cuboid map
     * @param shortest True - only up, down, right and left. False - all directions
     * @param shores_shortest True - only up, down, right and left. False - all directions
     * @param y Y start location
     * @param x X start location
     * @return Tile shores
     */
    public static List<Pair<Integer, Integer>> getShoresRelatedArea(GameTile[][] map, boolean shortest, boolean shores_shortest, int y, int x) {

        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for(MoveDirection direction : shortest ? MoveDirection.getAllShortestDirections() : MoveDirection.getAllShortDirections()) {

            int newY = y + direction.getY();
            int newX = x + direction.getX();

            if(newX < 0 || newY < 0 || newY >= map.length || newX >= map[0].length) continue;
            if(map[newY][newX] instanceof VoidTile && getShores(map, shores_shortest, newY, newX).size() != 0)
                list.add(new Pair<>(newY, newX));

        }
        return list;

    }

}
