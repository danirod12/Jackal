package com.github.danirod12.jackal.server.game.generator;

import com.github.danirod12.jackal.server.game.tile.EmptyTile;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.game.tile.TileType;
import com.github.danirod12.jackal.server.game.tile.VoidTile;

public class MapGenerator {

    public static GameTile[][] generateMap(int sizeX, int sizeY, int players) {

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

}
