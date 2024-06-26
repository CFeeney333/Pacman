package com.cathal.pacman.level;

import com.cathal.pacman.Main;
import com.cathal.pacman.graphics.*;
import com.cathal.pacman.maths.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.cathal.pacman.graphics.Direction.*;
import static org.lwjgl.glfw.GLFW.*;

public class LevelOne implements ILevel {

    private final Pacman pacman;
    private final Wall wall;
    private final Food food;
    private final Ghost ghost;
    private final Ghost ghost2;

    private final float speed;
    private float ghostSpeed;
    private int score;

    private final Renderer renderer;
    private final Window window;

    private final ArrayList<Float> foodPositions = new ArrayList<>();
    private final float[] wallPositions = new float[] {
                 1,  0,
                 5,  0,
                 6,  0,
                 7,  0,
                13,  0,
                 1,  1,
                 3,  1,
                 7,  1,
                 9,  1,
                10,  1,
                11,  1,
                15,  1,
                17,  1,
                18,  1,
                19,  1,
                 1,  2,
                 3,  2,
                 5,  2,
                13,  2,
                15,  2,
                 1,  3,
                 3,  3,
                 4,  3,
                 5,  3,
                 7,  3,
                 8,  3,
                 9,  3,
                11,  3,
                12,  3,
                13,  3,
                15,  3,
                16,  3,
                17,  3,
                18,  3,
                 5,  4,
                 9,  4,
                11,  4,
                13,  4,
                 1,  5,
                 2,  5,
                 3,  5,
                 5,  5,
                 7,  5,
                 9,  5,
                11,  5,
                13,  5,
                14,  5,
                15,  5,
                17,  5,
                18,  5,
                 1,  6,
                17,  6,
                 3,  7,
                 4,  7,
                 5,  7,
                 7,  7,
                 8,  7,
                 9,  7,
                10,  7,
                12,  7,
                13,  7,
                14,  7,
                15,  7,
                16,  7,
                17,  7,
                18,  7,
                 1,  8,
                 9,  8,
                14,  8,
                 1,  9,
                 2,  9,
                 3,  9,
                 5,  9,
                 6,  9,
                 7,  9,
                 9,  9,
                11,  9,
                12,  9,
                14,  9,
                15,  9,
                16,  9,
                17,  9,
                18,  9,
                 9, 10,
                12, 10
    };

    public LevelOne(Renderer renderer, Window window) {
        this.window = window;
        this.renderer = renderer;

        for (int x = 0; x < Main.UNIT_WIDTH; x++) {
            for (int y = 0; y < Main.UNIT_HEIGHT; y++) {
                float[] position = new float[] {(float) x, (float) y};
                boolean positionInWalls = false;
                for (int wallInd = 0; wallInd < wallPositions.length; wallInd += 2) {
                    if (position[0] == wallPositions[wallInd] && position[1] == wallPositions[wallInd + 1]) {
                        positionInWalls = true;
                    }
                }
                if (!positionInWalls) {
                    foodPositions.add(position[0]);
                    foodPositions.add(position[1]);
                }
            }
        }

        // don't add food to gameItems mainly because it will be rendered separately
        food = new Food(new Vector3f());

        // don't add wall to gameItems mainly because it will be rendered separately
        wall = new Wall(new Vector3f());

        pacman = new Pacman(new Vector3f(0.5f, 0.5f, 0.0f));
        speed = 0.25f;

        ghost = new Ghost(new Vector3f(10.5f, 6.5f, 0.0f), EAST, 0.125f);
        ghost2 = new Ghost(new Vector3f(16.5f, 2.5f, 0.0f), EAST, 0.125f);
    }

    @Override
    public int update() {
        boolean[] wallCollisions = wallCollide();

        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            if (!isScreenCollide(Main.Bounds.RIGHT) && !wallCollisions[0]) {
                pacman.translate(new Vector3f(speed, 0.0f, 0.0f));
            }
        }
        wallCollisions = wallCollide();
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            if (!isScreenCollide(Main.Bounds.LEFT) && !wallCollisions[2]) {
                pacman.translate(new Vector3f(-speed, 0.0f, 0.0f));
            }
        }
        wallCollisions = wallCollide();
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            if (!isScreenCollide(Main.Bounds.TOP) && !wallCollisions[1]) {
                pacman.translate(new Vector3f(0.0f, speed, 0.0f));
            }
        }
        wallCollisions = wallCollide();
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            if (!isScreenCollide(Main.Bounds.BOTTOM) && !wallCollisions[3]) {
                pacman.translate(new Vector3f(0.0f, -speed, 0.0f));
            }
        }

        if (foodCollision()) {
            score++;
        }

        pacman.update();
        updateGhosts();

        if (isPacmanGhostCollision(ghost) || isPacmanGhostCollision(ghost2) || foodPositions.size() == 0) {
            return 0;
        }

        return 1;
    }

    @Override
    public int render() {
        renderer.render(new IGameItem[]{pacman});
        renderWalls();
        renderFood();
        ghost.render();
        ghost2.render();
        return 0;
    }

    private void updateGhosts() {
        randomizeDirection(ghost);
        randomizeDirection(ghost2);

        ghost.move();
        ghost2.move();
        ghost.update();
        ghost2.update();
    }

    private boolean isPacmanGhostCollision(Ghost ghost) {
        if (pacman.getRightMesh() >= ghost.getLeftMesh() && pacman.getLeftMesh() <= ghost.getRightMesh()) {
            return pacman.getTopMesh() >= ghost.getBottomMesh() && pacman.getBottomMesh() <= ghost.getTopMesh();
        }
        return false;
    }

    private Direction getRandomDirection(ArrayList<Direction> directionsToTurn) {
        Random random = new Random();
        int index = random.nextInt(directionsToTurn.size());
        return directionsToTurn.get(index);
    }

    /**
     * Similar to wallCollide
     * Will extract the walls from the game items array
     * Returns an arraylist of all the sides that there are walls or a side of the window
     * @param ghost the ghost object to test for touching walls
     * @return an arraylist of Directions specifying the sides that there are or a side of the window
     */
    private Direction[] ghostWallOrScreenCollide(Ghost ghost) {
        Direction[] result;
        ArrayList<Direction> tempResult = new ArrayList<>();
        ArrayList<Direction> forRemoval = new ArrayList<>();
        for (int i = 0; i < wallPositions.length; i += 2) {
            wall.setPosition(new Vector3f(wallPositions[i] + Wall.WIDTH / 2.0f, wallPositions[i + 1] + Wall.HEIGHT / 2.0f, 0.0f));

            // check the right side of ghost
//            if (ghost.getFacing() == Direction.EAST) {
                if (((ghost.getRightBound() == wall.getLeftBound()) &&
                        (ghost.getBottomBound() < wall.getTopBound()) &&
                        (ghost.getTopBound() > wall.getBottomBound())) ||
                isGhostScreenCollide(ghost, Main.Bounds.RIGHT)) {
                    tempResult.add(EAST);
                } else {
                    if (ghost.getFacing() == EAST) {
                        tempResult.add(WEST);
                    }
                }
//            }

            // check the top side of ghost
//            if (ghost.getFacing() == Direction.NORTH) {
            if (((ghost.getTopBound() == wall.getBottomBound()) &&
                    (ghost.getLeftBound() < wall.getRightBound()) &&
                    (ghost.getRightBound() > wall.getLeftBound())) ||
                    isGhostScreenCollide(ghost, Main.Bounds.TOP)) {
                tempResult.add(NORTH);
            } else {
                if (ghost.getFacing() == NORTH) {
                    tempResult.add(SOUTH);
                }
            }
//            }

                // check the left side of ghost
//            if (ghost.getFacing() == Direction.WEST) {
            if (((ghost.getLeftBound() == wall.getRightBound()) &&
                    (ghost.getBottomBound() < wall.getTopBound()) &&
                    (ghost.getTopBound() > wall.getBottomBound())) ||
                    isGhostScreenCollide(ghost, Main.Bounds.LEFT)) {
                tempResult.add(Direction.WEST);
            } else {
                if (ghost.getFacing() == WEST) {
                    tempResult.add(EAST);  // there cannot be a wall on the east and west at the same time if it is travelling in either of those directions
                }
            }
//            }

                // check the bottom side of ghost
//            if (ghost.getFacing() == Direction.SOUTH) {
            if (((ghost.getBottomBound() == wall.getTopBound()) &&
                    (ghost.getLeftBound() < wall.getRightBound()) &&
                    (ghost.getRightBound() > wall.getLeftBound())) ||
                    isGhostScreenCollide(ghost, Main.Bounds.BOTTOM)) {
                tempResult.add(SOUTH);
            } else {
                if (ghost.getFacing() == SOUTH) {
                    tempResult.add(NORTH);
                }
            }
//            }
//            if (ghost.getFacing() == EAST) {
//                tempResult.remove(WEST);
//            } else if (ghost.getFacing() == WEST) {
//                tempResult.remove(EAST);
//            } else if (ghost.getFacing() == NORTH) {
//                tempResult.remove(SOUTH);
//            } else if (ghost.getFacing() == SOUTH) {
//                tempResult.remove(NORTH);
//            }
        }

        result = new Direction[tempResult.size()];
        tempResult.toArray(result);
        return result;
    }

    /**
     * This checks for wall and screen collisions
     * @param ghost the ghost whose direction to randomize
     */
    private void randomizeDirection(Ghost ghost) {
        Direction[] wallDirections = ghostWallOrScreenCollide(ghost);
        Direction[] allDirections = new Direction[]{NORTH, SOUTH, EAST, WEST};
        ArrayList<Direction> inverse = new ArrayList<>(4);
        inverse.addAll(Arrays.asList(allDirections));
        for (int i = 0; i < wallDirections.length; i++) {
            inverse.remove(wallDirections[i]);
        }
        Random random = new Random();
        if (inverse.size() > 0) {
            int index = random.nextInt(inverse.size());
            ghost.setFacing(inverse.get(index));
        } else {
            System.out.println("It hath happened!");
        }
//                switch (random.nextInt(4)) {
//            case 0:
//                ghost.setFacing(NORTH);
//                break;
//            case 1:
//                ghost.setFacing(EAST);
//                break;
//            case 2:
//                ghost.setFacing(SOUTH);
//                break;
//            default:
//                ghost.setFacing(Direction.WEST);
//                break;
//        }
    }

    private void renderFood() {
        for (int i = 0; i < foodPositions.size(); i += 2) {
            food.setPosition(new Vector3f(foodPositions.get(i) + 0.5f, foodPositions.get(i + 1) + 0.5f, 0.0f));
            food.render();
        }
    }

    private void renderWalls() {
        for (int i = 0; i < wallPositions.length; i += 2) {
            wall.setPosition(new Vector3f(wallPositions[i] + Wall.WIDTH / 2.0f, wallPositions[i + 1] + Wall.HEIGHT / 2.0f, 0.0f));
            wall.render();
        }
    }

    public boolean isScreenCollide(Main.Bounds side) {
        switch (side) {
            case LEFT:
                return (pacman.getLeftBound() <= Main.LEFT);
            case RIGHT:
                return (pacman.getRightBound() >= Main.RIGHT);
            case TOP:
                return (pacman.getTopBound() >= Main.TOP);
            case BOTTOM:
                return (pacman.getBottomBound() <= Main.BOTTOM);
            default:
                return false;
        }
    }

    public boolean isGhostScreenCollide(Ghost ghost, Main.Bounds side) {
        switch (side) {
            case LEFT:
                return (ghost.getLeftBound() <= Main.LEFT);
            case RIGHT:
                return (ghost.getRightBound() >= Main.RIGHT);
            case TOP:
                return (ghost.getTopBound() >= Main.TOP);
            case BOTTOM:
                return (ghost.getBottomBound() <= Main.BOTTOM);
            default:
                return false;
        }
    }

    private boolean foodCollision() {
        int xIndex = 0;
        int yIndex = 0;
        boolean hit = false;
        for (int i = 0; i < foodPositions.size(); i += 2) {
            xIndex = i;
            yIndex = i + 1;
            food.setPosition(new Vector3f(foodPositions.get(xIndex) + 0.5f, foodPositions.get(yIndex) + 0.5f, 0.0f));
            if (pacman.getTopMesh() > food.getBottomBound() && pacman.getBottomMesh() < food.getTopBound()) {
                if (pacman.getRightMesh() > food.getLeftBound() && pacman.getLeftMesh() < food.getRightBound()) {
                    hit = true;
                    break;
                }
            }
        }
        if (hit) {
            foodPositions.remove(xIndex);
            foodPositions.remove(yIndex - 1);  // the remaining elements have shifted to the left. Could have just used xIndex again
            return true;
        }
        return false;
    }

    /**
     * Will extract the walls from the game items array
     * Returns an array of four booleans:
     * index 0: is the right side of pacman touching a wall?
     * index 1: is the top side of pacman touching a wall?
     * index 2: is the left side of pacman touching a wall?
     * index 3: is the bottom side of pacman touching a wall?
     * @return an array of 4 booleans
     */
    public boolean[] wallCollide() {
        boolean[] result = new boolean[4];
        for (int i = 0; i < wallPositions.length; i += 2) {
            wall.setPosition(new Vector3f(wallPositions[i] + Wall.WIDTH / 2.0f, wallPositions[i + 1] + Wall.HEIGHT / 2.0f, 0.0f));

            // check the right side of pacman
            if ((pacman.getRightBound() == wall.getLeftBound()) &&
                    (pacman.getBottomBound() < wall.getTopBound()) &&
                    (pacman.getTopBound() > wall.getBottomBound())) {
                result[0] = true;
            }

            // check the top side of pacman
            if ((pacman.getTopBound() == wall.getBottomBound()) &&
                    (pacman.getLeftBound() < wall.getRightBound()) &&
                    (pacman.getRightBound() > wall.getLeftBound())) {
                result[1] = true;
            }

            // check the left side of pacman
            if ((pacman.getLeftBound() == wall.getRightBound()) &&
                    (pacman.getBottomBound() < wall.getTopBound()) &&
                    (pacman.getTopBound() > wall.getBottomBound())) {
                result[2] = true;
            }

            // check the bottom side of pacman
            if ((pacman.getBottomBound() == wall.getTopBound()) &&
                    (pacman.getLeftBound() < wall.getRightBound()) &&
                    (pacman.getRightBound() > wall.getLeftBound())) {
                result[3] = true;
            }
        }
        return result;
    }
}
