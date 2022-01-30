package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.objects.tile.ClosedTile;
import com.github.danirod12.jackal.client.objects.tile.GameTile;
import com.github.danirod12.jackal.client.objects.tile.TileType;
import com.github.danirod12.jackal.client.objects.tile.VoidTile;

import java.awt.*;

public class GameBoard {

    private final GameTile[][] board;

    public GameBoard(int width, int height) {

        this.board = new GameTile[width][height];
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                board[x][y] = new VoidTile();
            }
        }

    }

    public void render(Graphics2D graphics) {

        int x = 100, y = 100;
        for(GameTile[] tiles : board) {
            for (GameTile tile : tiles) {

                graphics.setColor(tile.getTileType().getAssociatedColor());
                graphics.fillRect(x, y, 64, 64);

                x += 64;
            }
            x = 100;
            y += 64;
        }

    }

    public GameTile getTile(int x, int y) {
        return board[x][y];
    }

    public void createTile(int x, int y, TileType type) {

        if(!type.isTerrain()) {
            if(!(board[x][y] instanceof VoidTile))
                board[x][y] = new VoidTile();
            return;
        }
        board[x][y] = new ClosedTile(type);
        
    }

}
