package uk.ac.bradford.diggame;

import java.util.Random;
import uk.ac.bradford.diggame.Tile.TileType;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and moles, as well as updating information when a
 * key is pressed (processed by the InputHandler) while the game is running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 35;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 18;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to place the player and
     * moles, and to randomise movement etc. Passing an integer (e.g. 123) to
     * the constructor called here will give fixed results - the same numbers
     * will be generated every time WHICH CAN BE VERY USEFUL FOR TESTING AND
     * BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional moles and reducing patience etc.
     */
    private int levelNumber = 1;  //current level

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 0;

    /**
     * The current score in this game.
     */
    private int score = 0;

    /**
     * The current mining strength of the player, used to calculate durability
     * reductions when the player mines a tile.
     */
    private int miningStrength = 5;

    /**
     * A variable to store the X coordinate of the first BASE tile generated by
     * the generateLevel() method.
     */
    private int baseX;

    /**
     * A variable to store the Y coordinate of the first BASE tile generated by
     * the generateLevel() method
     */
    private int baseY;

    /**
     * The GUI associated with this GameEngine object. This link allows the
     * engine to pass level and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles that represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created. This is the array that is used to draw images to the
     * screen by the GUI class.
     */
    private Tile[][] level;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including energy and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player player;

    /**
     * An array of Mole objects that represents the moles in the current level
     * of the game. Elements in this array should be either an object of the
     * type Mole or should be null (which means nothing is drawn or processed
     * for movement). null values in this array are skipped during drawing and
     * movement processing. Moles that "explode" can be replaced with the value
     * null in this array which removes them from the game, using syntax such as
     * moles[i] = null
     */
    private Mole[] moles;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. This method should instantiate the level array,
     * which is an attribute of the GameEngine class and is declared above, and
     * then fill it with Tile objects that are created inside this method. It is
     * recommended that for your first version of this method you fill the 2D
     * array using for loops, and create new Tile objects inside the inner loop,
     * assigning them to an element in the array. For the first version you
     * should use just Tile objects with the type EMPTY. You will need to check
     * the constructor from the Tile class in order to create new Tile objects
     * inside your nested loops.
     *
     * Later tasks will require additions to this method to add new content, see
     * the specification document for more details.
     */
    private void generateLevel() {
        //YOUR CODE HERE
        level = new Tile[LEVEL_WIDTH][LEVEL_HEIGHT];
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                int n = rng.nextInt(100);

                if (n >= 0 && n <= 4) {
                    level[i][j] = new Tile(TileType.URANIUM); //5%
                } else if (n >= 5 && n <= 14) {
                    level[i][j] = new Tile(TileType.SILVER); //10%
                } else if (n >= 15 && n <= 29) {
                    level[i][j] = new Tile(TileType.COPPER); //15%
                } else if (n >= 30 && n <= 44) {
                    level[i][j] = new Tile(TileType.ROCK); //15%
                } else if (n >= 45 && n <= 59) {
                    level[i][j] = new Tile(TileType.HARD_DIRT); //15%
                } else if (n >= 60 && n <= 79) {
                    level[i][j] = new Tile(TileType.DIRT); //20%
                } else if (n >= 80 && n <= 94) {
                    level[i][j] = new Tile(TileType.EMPTY); //15%
                } else if (n >= 95 && n <= 99) {
                    level[i][j] = new Tile(TileType.BASE); //5%
                }
            }
        }

        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                if (level[i][j].getType() == TileType.BASE) {
                    if (baseX == 0 && baseY == 0) {
                        baseX = i;
                        baseY = j;
                    }
                }
            }
        }
    }

    /**
     * Adds moles in suitable locations in the current level. The first version
     * of this method should picked fixed positions for moles by calling the
     * constructor for the Mole class and using fixed values for the fullness, X
     * and Y positions of the Mole to be added.
     *
     * Mole objects created this way should then be added into the moles array
     * that is part of the GameEngine class. Mole objects added to the moles
     * array will then be drawn to the screen using the existing code in the
     * GameGUI class.
     *
     * The second version of this method (described in a later task) should
     * improve the placement of moles by generating random values for the X and
     * Y position of the mole before instantiating the Mole object. You may like
     * to use a loop to do this.
     */
    private void addMoles() {
        //YOUR CODE HERE
        moles = new Mole[levelNumber + 4];
        for (int i = 0; i < moles.length; i++) {
            int xPos = rng.nextInt(1, 34);
            int yPos = rng.nextInt(1, 17);
            moles[i] = new Mole(100 + levelNumber * 100, xPos, yPos);
        }
    }

    /**
     * Creates a Player object in the game. The method instantiates the Player
     * class and assigns values for the energy and position.
     *
     * The first version of this method should use a fixed position for the
     * player to start, by setting fixed X and Y values when calling the
     * constructor in the Player class. The object created should be assigned to
     * the player attribute of this class.
     *
     * The second version of this method should use a suitable method to
     * determine where the BASE in the level is and place the Player on the tile
     * representing the BASE.
     *
     */
    private void createPlayer() {
        //YOUR CODE HERE
        player = new Player(300, baseX, baseY);
    }

    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the GameInputHandler class when
     * the user has pressed one of the arrow keys on the keyboard. The method
     * should check which direction for movement is required, by checking which
     * character was passed to this method (see parameter description below).
     * Your code should alter the X and Y position of the player to place them
     * in the correct tile based on the direction of movement.
     *
     * If the target tile is not EMPTY then the player should not be moved, but
     * other effects may happen such as mining. To achieve this, the target tile
     * should be checked to determine the type of tile and appropriate methods
     * called or attribute values changed.
     *
     * @param direction A char representing the direction that the player should
     * move. N is up, S is down, W is left and E is right.
     */
    public void movePlayer(char direction) {
        //YOUR CODE HERE
        int playerX = player.getX();
        int playerY = player.getY();
        switch (direction) {
            case 'N':
                if (playerY - 1 >= 0) {
                    Tile tileN = level[playerX][playerY - 1];
                    if (tileN.getType() == TileType.EMPTY || tileN.getType() == TileType.BASE) {
                        player.setPosition(playerX, playerY - 1);
                    } else {
                        if (player.getEnergy() >= tileN.getDurability()) {
                            TileType t;
                            t = tileN.mine(miningStrength);
                            player.changeEnergy(-tileN.getDurability());
                            if (t == TileType.URANIUM) {
                                miningStrength = 25;
                            }
                        }
                    }
                }
                break;

            case 'S':
                if (playerY + 1 <= 17) {
                    Tile tileS = level[playerX][playerY + 1];
                    if (tileS.getType() == TileType.EMPTY || tileS.getType() == TileType.BASE) {
                        player.setPosition(playerX, playerY + 1);
                    } else {
                        if (player.getEnergy() >= tileS.getDurability()) {
                            TileType t;
                            t = tileS.mine(miningStrength);
                            player.changeEnergy(-tileS.getDurability());
                            if (t == TileType.URANIUM) {
                                miningStrength = 25;
                            }
                        }
                    }
                }
                break;

            case 'E':
                if (playerX + 1 <= 34) {
                    Tile tileE = level[playerX + 1][playerY];
                    if (tileE.getType() == TileType.EMPTY || tileE.getType() == TileType.BASE) {
                        player.setPosition(playerX + 1, playerY);
                    } else {
                        if (player.getEnergy() >= tileE.getDurability()) {
                            TileType t;
                            t = tileE.mine(miningStrength);
                            player.changeEnergy(-tileE.getDurability());
                            if (t == TileType.URANIUM) {
                                miningStrength = 25;
                            }
                        }
                    }
                }
                break;

            case 'W':
                if (playerX - 1 >= 0) {
                    Tile tileW = level[playerX - 1][playerY];
                    if (tileW.getType() == TileType.EMPTY || tileW.getType() == TileType.BASE) {
                        player.setPosition(playerX - 1, playerY);
                    } else {
                        if (player.getEnergy() >= tileW.getDurability()) {
                            TileType t;
                            t = tileW.mine(miningStrength);
                            player.changeEnergy(-tileW.getDurability());
                            if (t == TileType.URANIUM) {
                                miningStrength = 25;
                            }
                        }
                    }
                }
                break;

            default:
        }
        if (level[playerX][playerY].getType() == TileType.BASE) {
            player.changeEnergy(player.getMaxEnergy());
        }
    }

    /**
     * Moves all moles on the current level. This method iterates over all
     * elements of the moles array (using a for loop) and checks if each one is
     * null (using an if statement inside that for loop). For every element of
     * the array that is NOT null, this method calls the moveMole method and
     * passes it the current array element (i.e. the current mole object being
     * used in the loop).
     */
    private void moveAllMoles() {
        //YOUR CODE HERE
        for (int i = 0; i < moles.length; i++) {
            if (moles[i] != null) {
                moveMole(moles[i]);
                if (moles[i].getFullness() >= moles[i].getMaxFullness()) {
                    explode(moles[i]);
                }
            }
        }
    }

    /**
     * Moves a specific mole in the game. The method updates the X and Y
     * attributes of the Mole object passed to the method to set its new
     * position.
     *
     * @param m The Mole that needs to be moved
     */
    private void moveMole(Mole m) {
        //YOUR CODE HERE

        int moleX = m.getX();
        int moleY = m.getY();
        int playerX = player.getX();
        int playerY = player.getY();
        int n = rng.nextInt(4);

        switch (n) {
            case 0:
                if (moleY - 1 >= 1 && moleX != playerX && moleY - 1 != playerY) {
                    Tile tileN = level[moleX][moleY - 1];
                    if (tileN.getType() == TileType.EMPTY || tileN.getType() == TileType.BASE) {
                        m.setPosition(moleX, moleY - 1);
                    } else {
                        int duraN = tileN.getDurability();
                        tileN.mine(miningStrength);
                        m.changeFullness(duraN);
                    }
                }
                break;
            case 1:
                if (moleY + 1 <= 16) {
                    Tile tileS = level[moleX][moleY + 1];
                    if (tileS.getType() == TileType.EMPTY || tileS.getType() == TileType.BASE) {
                        m.setPosition(moleX, moleY + 1);
                    } else {
                        int duraS = tileS.getDurability();
                        tileS.mine(miningStrength);
                        m.changeFullness(duraS);
                    }
                }
                break;
            case 2:
                if (moleX + 1 <= 33) {
                    Tile tileE = level[moleX + 1][moleY];
                    if (tileE.getType() == TileType.EMPTY || tileE.getType() == TileType.BASE) {
                        m.setPosition(moleX + 1, moleY);
                    } else {
                        int duraE = tileE.getDurability();
                        tileE.mine(miningStrength);
                        m.changeFullness(duraE);
                    }
                }
                break;
            case 3:
                if (moleX - 1 >= 1) {
                    Tile tileW = level[moleX - 1][moleY];
                    if (tileW.getType() == TileType.EMPTY || tileW.getType() == TileType.BASE) {
                        m.setPosition(moleX - 1, moleY);
                    } else {
                        int duraW = tileW.getDurability();
                        tileW.mine(miningStrength);
                        m.changeFullness(duraW);
                    }
                }
                break;
        }
    }

    /**
     * This method is used to make a mole "explode" when its fullness value
     * reaches or exceeds its maximum fullness. This method should store the
     * mole's X and Y co-ordinates and then "mine" the tiles around this
     * position out to a fixed radius.
     *
     * @param m the mole that is exploding
     */
    private void explode(Mole m) {
        //YOUR CODE HERE
        int moleX = m.getX();
        int moleY = m.getY();
       
        Tile tileN = level[moleX][moleY - 1];
        Tile tileS = level[moleX][moleY + 1];
        Tile tileE = level[moleX + 1][moleY];
        Tile tileW = level[moleX - 1][moleY];
        Tile tileNE = level[moleX + 1][moleY - 1];
        Tile tileNW = level[moleX - 1][moleY - 1];
        Tile tileSE = level[moleX + 1][moleY + 1];
        Tile tileSW = level[moleX - 1][moleY + 1];
        Tile tileCurrent = level[moleX][moleY];
        
        tileN.mine(100);
        tileS.mine(100);
        tileE.mine(100);
        tileW.mine(100);
        tileNE.mine(100);
        tileNW.mine(100);
        tileSE.mine(100);
        tileSW.mine(100);
        tileCurrent.mine(100);
        
        //The code would generate an error if a mole explodes at the edge of the
        //level as it would try to mine a few tiles that are out of bounds. So, 
        //the moles have simply been prevented to move to the edge of the level. 
    }

    /**
     * This method should iterate over the moles array, checking each Mole
     * object to see if its current fullness os greater than or equal to its
     * maximum fullness (i.e. it "exploded" this turn). If it has, it should be
     * set to null in the moles array. You will need to check if the array
     * element currently being examined is null, before you attempt to call any
     * methods on the array element.
     */
    private void clearExplodedMoles() {
        //YOUR CODE HERE
        for (int i = 0; i < moles.length; i++) {
            if (moles[i] != null) {
                if (moles[i].getFullness() >= moles[i].getMaxFullness()) {
                    moles[i] = null;
                }
            }
        }
    }

    /**
     * This method is called when the player "mines" all ore tiles (i.e. COPPER,
     * SILVER and URANIUM tiles, and returns to the BASE "completing" the level.
     *
     * This method should increase the current level number, create a new level
     * by calling the generateLevel method and setting the level attribute using
     * the returned 2D array, add new Moles, and finally place the player in the
     * new level.
     *
     */
    private void nextLevel() {
        //YOUR CODE HERE
        levelNumber++;
        generateLevel();
        addMoles();
        placePlayer();
        miningStrength = 5;
    }

    /**
     * The first version of this method should place the player in the game
     * level by setting new, fixed X and Y values for the player object in this
     * class.
     *
     * The second version of this method in a later task should place the player
     * in a game level by choosing a position corresponding to a BASE tile.
     */
    private void placePlayer() {
        //YOUR CODE HERE
        player.setPosition(baseX, baseY);
    }

    /**
     * Checks if all "ore tiles" (copper, silver and uranium) have been mined in
     * the level, i.e. if no ore tiles remain in the level array in this class.
     * This method should iterate over the entire 2D level array and if an ore
     * tile is found it should return true. If all elements in the level array
     * have been searched and no ore tiles were found then it should return
     * false.
     *
     * @return true if no ore tiles exist in the level, false otherwise.
     */
    private boolean allOreMined() {
        //YOUR CODE HERE
        boolean oreMined = true;
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                if (level[i][j].getType() == TileType.URANIUM
                        || level[i][j].getType() == TileType.SILVER
                        || level[i][j].getType() == TileType.COPPER) {
                    oreMined = false;
                }
            }
        }
        return oreMined;//return the calculated value here instead of false
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method clears exploded moles, periodically moves any moles
     * in the level, and increments the turn number. Finally it requests the GUI
     * to redraw the game level by passing it the level, player and moles
     * objects for the current level.
     *
     */
    public void doTurn() {
        player.changeEnergy(1);
        turnNumber++;
        if (turnNumber % 4 == 0) {
            moveAllMoles();
        }
        clearExplodedMoles();
        gui.updateDisplay(level, player, moles);
        if (allOreMined() == true && level[player.getX()][player.getY()].getType() == TileType.BASE) {
            nextLevel();
        }
    }

    /**
     * Starts a game. This method generates a level, adds moles and the player
     * and then requests the GUI to update the level on screen using the
     * information on level, player and moles.
     */
    public void startGame() {
        generateLevel();
        addMoles();
        createPlayer();
        gui.updateDisplay(level, player, moles);
    }
}
