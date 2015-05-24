package com.rhys.ninemensmorris.Model;

/**
 * Created by Rhys on 20/05/2015.
 */
public class Slide implements Move {
    private Player player;
    private Piece piece;
    private Spot src;
    private Spot dest;

    /**
     *
     * @param player
     * @param src
     * @param dest
     * @return
     */
    public boolean move(Player player, Spot src, Spot dest) {
        Piece piece = src.getPiece();
        if (piece.slide(dest)) {
            this.player = player;
            this.piece = piece;
            this.src = src;
            this.dest = dest;
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    @Override
    public void undo() {
        src.setPiece(piece);
        dest.removePiece();
        piece.setSpot(src);
    }

    /**
     *
     * @return
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return
     */
    @Override
    public Spot getDest() {
        return dest;
    }
}
