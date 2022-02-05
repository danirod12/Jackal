package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.objects.item.EntityPlayer;
import com.github.danirod12.jackal.client.objects.item.TeamBoat;
import com.github.danirod12.jackal.client.objects.tile.ClosedTile;
import com.github.danirod12.jackal.client.objects.tile.GameTile;
import com.github.danirod12.jackal.client.objects.tile.TileType;
import com.github.danirod12.jackal.client.objects.tile.VoidTile;
import com.github.danirod12.jackal.client.protocol.packet.ServerboundRequestActionsPacket;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameBoard implements MouseExecutor {

    private final GameLoop loop = Jackal.getGameLoop();

    // GameTile[y][x]
    private final GameTile[][] board;
    private final HashMap<GameColor, TeamBoat> boats = new HashMap<>();
    private final List<EntityPlayer> players = new ArrayList<>();

    public void onTurnChange() {
        selected_player = null;
    }

    private Pair<EntityPlayer, List<String>> selected_player = null;

    public GameBoard(int height, int width) {

        this.board = new GameTile[height][width];
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                board[y][x] = new VoidTile();
            }
        }

    }

    public void render(Graphics2D graphics) {

        int renderX = 100, renderY = 100;

        // Render map
        /*

        TODO generate a new BufferedImage of map on each tile metadata change and display this buffered image

         */
        for (GameTile[] gameTiles : board) {

            for (int x = 0; x < board[0].length; ++x) {

                GameTile tile = gameTiles[x];
                // render
                if (tile.getTileType() != TileType.EMPTY) {
                    graphics.setColor(tile.getTileType().getAssociatedColor());
                    graphics.fillRect(renderX, renderY, 64, 64);
                }

                renderX += 64;

            }
            renderX = 100;
            renderY += 64;

        }

        // Render boats
        for(TeamBoat boat : boats.values()) {

            renderX = 100 + boat.getX() * 64;
            renderY = 100 + boat.getY() * 64;

            graphics.setColor(Color.BLACK);
            graphics.fillRect(renderX, renderY, 64, 64);

            graphics.setColor(boat.getColor().asRenderColor());
            graphics.drawRect(renderX, renderY, 64, 64);

        }

        // TODO render items

        // Render players
        for(EntityPlayer player : players) {

            if(player.getSubTile() == null) continue;

            renderX = 100 + player.getX() * 64;
            renderY = 100 + player.getY() * 64;

            graphics.setColor(player.getColor().asRenderColor());
            graphics.fillOval(renderX + player.getSubTile().getOffsetX() + 4, renderY + player.getSubTile().getOffsetY() + 4, 20, 20);

        }

        // TODO render available actions
        actions: {
            if(selected_player != null) {

                if(selected_player.getValue() == null) break actions;

                graphics.setColor(ColorTheme.AVAILABLE_ACTIONS);
                graphics.setStroke(new BasicStroke(4));
                for(String target : selected_player.getValue()) {

                    String[] path = target.split("\\.");
                    renderX = -1;
                    for(int index = 0; index < path.length; ++index) {

                        String tile = path[index];

                        int temp1 = renderX;
                        int temp2 = renderY;

                        renderX = 100 + 64 * Integer.parseInt(tile.split(":")[0]);
                        renderY = 100 + 64 * Integer.parseInt(tile.split(":")[1]);

                        if(temp1 >= 0) {
                            graphics.drawLine(temp1 + 32, temp2 + 32, renderX + 32, renderY + 32);
                        }

                    }
                    graphics.drawRect(renderX, renderY, 64, 64);

                }
                graphics.setStroke(new BasicStroke(1));

            }
        }

    }

    public void onObjectUpdate(int action, String uuid, int id, int y, int x, String metadata) {

        if(action == 1) {

            players.removeIf(n -> n.getUuid().toString().equalsIgnoreCase(uuid));
            // TODO items
            // TODO for boats? Not sure
            return;

        } else if(action == 0) {

            switch (id) {

                // boat
                case 0: {

                    TeamBoat boat = null;
                    for(TeamBoat object : boats.values()) {
                        if(object.getUuid().toString().equalsIgnoreCase(uuid)) {
                            boat = object;
                            break;
                        }
                    }

                    if(boat == null) {

                        GameColor color = GameColor.parseColor(Integer.parseInt(metadata));
                        if(color == GameColor.UNKNOWN) throw new UnsupportedOperationException("Cannot create boat that not assigned to any team");
                        boats.put(color, new TeamBoat(UUID.fromString(uuid), color, y, x));
                        return;

                    }

                    boat.setY(y);
                    boat.setX(x);
                    // update metadata?
                    return;

                }

                // player
                case 1: {

                    EntityPlayer player = null;
                    for(EntityPlayer object : players) {
                        if(object.getUuid().toString().equalsIgnoreCase(uuid)) {
                            player = object;
                            break;
                        }
                    }

                    if(player == null) {
                        players.add(player = new EntityPlayer(UUID.fromString(uuid), GameColor.parseColor(Integer.parseInt(metadata))));
                    }

                    List<SubTile> subtiles = new ArrayList<>();
                    for(EntityPlayer object : players) {
                        if(object == player || player.getColor() != object.getColor()) continue;
                        subtiles.add(object.getSubTile());
                    }

                    player.updateSubTile(SubTile.findEmpty(subtiles));
                    player.setY(y);
                    player.setX(x);
                    // update metadata?
                    return;

                }

                default:
                    throw new IllegalArgumentException("Unknown game object ID - " + id);

            }

        }
        throw new IllegalArgumentException("Unknown action ID - " + action);

    }

    public GameTile getTile(int y, int x) {
        return board[y][x];
    }

    public void createTile(int y, int x, TileType type) {

        if(!type.isTerrain()) {
            if(!(board[y][x] instanceof VoidTile))
                board[y][x] = new VoidTile();
            return;
        }
        board[y][x] = new ClosedTile(type);
        
    }

    @Override
    public boolean onMouseClick(int x, int y) {

        // TODO check player selection
        Player self = loop.getConnection().getSelf();
        if(!self.isWaitingForMove()) return false;
        for(EntityPlayer player : players) {
            if(player.getColor() != self.getColor()) continue;

            int middleX = 100 + player.getX() * 64 + player.getSubTile().getOffsetX() + 14;
            int middleY = 100 + player.getY() * 64 + player.getSubTile().getOffsetY() + 14;

            if(Misc.getDistance(x, y, middleX, middleY) <= 20) {

                // clicked to player
                if(selected_player != null && selected_player.getKey() == player) return false;
                selected_player = new Pair<>(player, null);
                loop.getConnection().sendPacket(new ServerboundRequestActionsPacket(player.getUuid()));
                return true;

            }

        }

        return false;
    }

    @Override
    public boolean onMouseMove(int x, int y) {
        return false;
    }

}
