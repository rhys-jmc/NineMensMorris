package com.rhys.ninemensmorris.Controller;

import com.rhys.ninemensmorris.Model.*;
import com.rhys.ninemensmorris.View.Display;

import java.util.Stack;

/**
 * Created by Rhys on 14/05/2015.
 */
public class Game {
    public static final int STATE_PLACE = 0;
    public static final int STATE_REMOVE = 1;
    public static final int STATE_SLIDE = 2;
    public static final int STATE_FLY = 3;
    public static final int STATE_COMPLETE = 4;

    private final Board board;
    private final Stack<Move> moveStack;

    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;

    private int gameState;

    /**
     *
     */
    public Game() {
        this.moveStack = new Stack<Move>();
        this.board = new Board();
        this.playerOne = null;
        this.playerTwo = null;
        this.currentPlayer = null;
        new Display(this, board);
    }

    /**
     *
     * @return
     */
    public int getGameState() {
        return gameState;
    }

    private void setGameState(String destStr) {
        if (board.hasSpot(destStr) && board.pieceInMill(board.getSpot(destStr))) {
            gameState = STATE_REMOVE;
        } else if (getOtherPlayer().allPiecesPlaced()) {
            if (getOtherPlayer().threePiecesLeft()) {
                gameState = STATE_FLY;
            } else if (getOtherPlayer().noLegalMove() || getOtherPlayer().twoPiecesLeft()) {
                gameState = STATE_COMPLETE;
            } else {
                gameState = STATE_SLIDE;
            }
        } else {
            gameState = STATE_PLACE;
        }
    }

    /**
     *
     */
    private void setGameState() {
        setGameState("");
    }

    /**
     *
     * @return
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     *
     * @param player
     */
    private void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /**
     *
     * @return
     */
    public Player getOtherPlayer() {
        if (currentPlayer.equals(playerOne)) {
            return playerTwo;
        } else {
            return playerOne;
        }
    }

    /**
     *
     * @param name
     */
    public void addPlayer(String name) {
        if (playerOne == null) {
            playerOne = new Human(name);
        } else if (playerTwo == null) {
            playerTwo = new Human(name);
        }
    }

    /**
     *
     */
    public void start() {
        currentPlayer = playerOne;
        setGameState();
    }

    /**
     *
     * @param destStr
     * @return
     */
    public String move(String destStr) {
        if (gameState != STATE_SLIDE && gameState != STATE_FLY) {
            return move(null, destStr);
        } else {
            return "You need to select which piece to move to that destination!";
        }
    }

    /**
     *
     * @param srcStr
     * @param destStr
     * @return
     */
    public String move(String srcStr, String destStr) {
        if (srcStr == null || board.hasSpot(srcStr)) {
            if (board.hasSpot(destStr)) {
                String output = "";

                switch (gameState) {
                    case STATE_PLACE:
                        Place place = new Place();
                        if (currentPlayer.place(place, board.getSpot(destStr))) {
                            moveStack.push(place);
                            setGameState(destStr);
                            output = "Piece placed on " + destStr;

                            if (gameState == STATE_REMOVE) {
                                output += "\nMill created!";
                            } else {
                                changeTurn();
                            }
                        } else {
                            output = "Place failed";
                        }
                        break;
                    case STATE_REMOVE:
                        Remove remove = new Remove();
                        if (currentPlayer.remove(remove, board.getSpot(destStr))) {
                            moveStack.push(remove);
                            setGameState();
                            output = "Piece removed from " + destStr;
                            changeTurn();
                        } else {
                            output = "Remove failed";
                        }
                        break;
                    case STATE_SLIDE:
                        Slide slide = new Slide();
                        if (currentPlayer.slide(slide, board.getSpot(srcStr), board.getSpot(destStr))) {
                            moveStack.push(slide);
                            setGameState(destStr);
                            output = "Piece slid from " + srcStr + " to " + destStr;

                            if (gameState == STATE_REMOVE) {
                                output += "\nMill created!";
                            } else {
                                changeTurn();
                            }
                        } else {
                            output = "Slide failed";
                        }
                        break;
                    case STATE_FLY:
                        Fly fly = new Fly();
                        if (currentPlayer.fly(fly, board.getSpot(srcStr), board.getSpot(destStr))) {
                            moveStack.push(fly);
                            setGameState(destStr);
                            output = "Piece flew from " + srcStr + " to " + destStr;

                            if (gameState == STATE_REMOVE) {
                                output += "\nMill created!";
                            } else {
                                changeTurn();
                            }
                        } else {
                            output = "Fly failed";
                        }
                        break;
                }

                return output;
            } else {
                return "Destination spot doesn't exist!";
            }
        } else {
            return "Source spot doesn't exist!";
        }
    }

    /**
     *
     * @return
     */
    public String undo() {
        if (moveStack.size() > 0) {
            setCurrentPlayer(moveStack.peek().getPlayer());
            moveStack.pop().undo();

            if (moveStack.empty()) {
                setGameState();
            } else {
                setGameState(moveStack.peek().getDest().getCoord());
            }
            return "Move undone!";
        } else {
            return "No moves to undo!";
        }
    }

    /**
     * 
     */
    private void changeTurn() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }
}
