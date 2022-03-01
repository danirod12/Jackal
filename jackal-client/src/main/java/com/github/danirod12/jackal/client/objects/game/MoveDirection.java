package com.github.danirod12.jackal.client.objects.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MoveDirection {

    UP(0, 0, -1),
    DOWN(1, 0, 1),

    RIGHT(3, 1, 0),
    LEFT(4, -1, 0),

    UP_RIGHT(5, 1, -1),
    UP_LEFT(6, -1, -1),

    DOWN_RIGHT(7, 1, 1),
    DOWN_LEFT(8, -1, 1);

    private final int x;
    private final int y;
    private final int id;

    /**
     * @param id packet level id
     * @param x velocity x (positive right, negative left)
     * @param y velocity y (positive down, negative right)
     */
    MoveDirection(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    private static final MoveDirection[] SHORT_DIRECTIONS = { UP, DOWN, RIGHT, LEFT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT };
    private static final MoveDirection[] SHORTEST_DIRECTIONS = { UP, DOWN, RIGHT, LEFT };

    /**
     * This method allows to get a {@link MoveDirection} enum corresponding to x and y offsets
     * @param xOffset x offset ranging from -1 to 1. Negative left, positive right
     * @param yOffset y offset ranging from -1 to 1. Negative down, positive up
     * @return a list of matching {@link MoveDirection} enums
     */
    public static List<MoveDirection> parseMoveDirection(int xOffset, int yOffset) {
        return Arrays.stream(SHORT_DIRECTIONS).filter(moveDirection -> moveDirection.getX() == xOffset && moveDirection.getY() == yOffset).collect(Collectors.toList());
    }

    /**
     * all directions
     */
    public static MoveDirection[] getAllShortDirections() {
        return SHORT_DIRECTIONS;
    }

    /**
     * up, down, right, left
     */
    public static MoveDirection[] getAllShortestDirections() {
        return SHORTEST_DIRECTIONS;
    }

    /**
     * Velocity X
     */
    public int getX() {
        return x;
    }

    /**
     * velocity Y
     */
    public int getY() {
        return y;
    }

    /**
     * direction id
     */
    public int getId() {
        return id;
    }

}