package com.github.danirod12.jackal.server.test.direction;

import com.github.danirod12.jackal.server.game.generator.MapGenerator;
import com.github.danirod12.jackal.server.game.move.DirectionManager;
import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.game.tile.ArrowTile;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.game.tile.TileType;
import com.github.danirod12.jackal.server.game.tile.VoidTile;
import com.github.danirod12.jackal.server.util.GameColor;

import java.util.List;

public class DirectionManagerTest {

    public static void main(String[] args) {

        // [y][x]
        GameTile[][] map = MapGenerator.generateMap(6, 6, 4);

        // Setup arrows
        map[3][4] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.RIGHT, MoveDirection.UP, MoveDirection.DOWN });
        map[3][5] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.RIGHT });
        map[3][6] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.UP, MoveDirection.DOWN });
        map[4][6] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.DOWN_RIGHT });
        map[4][7] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.LEFT });
        map[5][7] = new ArrowTile(TileType.GRASS, new MoveDirection[] { MoveDirection.DOWN_RIGHT, MoveDirection.DOWN, MoveDirection.UP });

        // Get island tiles
        int valid = 0;
        for(GameTile[] tiles : map)
            for(GameTile tile : tiles)
                if(!(tile instanceof VoidTile))
                    valid++;

        System.out.println("Valid tiles " + valid);

        // Math movements
        final int y = 2, x = 0;

        List<String> movements = DirectionManager.getAvailableMovements(map, y, x, GameColor.WHITE);

        System.out.println("Available tiles from (y: " + y + ", x: " + x + "): " + movements.size());
        System.out.println();

        System.out.println(movements);
        System.out.println();

        printTiles(y, x, map.length, map[0].length, movements);

    }

    public static void printTiles(int defY, int defX, int height, int width, List<String> list) {

        for(int y = 0; y < height; y++) {

            System.out.print(y + ": ");
            for(int x = 0; x < width; ++x) {

                if(defX == x && defY == y) {
                    System.out.print("[Z]");
                    continue;
                }

                find_element: {
                    for(String element : list) {
                        String[] step = element.split("\\.");
                        if(step[step.length - 1].equalsIgnoreCase(y + ":" + x))
                            break find_element;
                    }
                    System.out.print("[_]");
                    continue;
                }
                System.out.print("[X]");

            }
            System.out.println();

        }

    }

}
