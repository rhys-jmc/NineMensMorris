package com.rhys.ninemensmorris.Model;

/**
 * Created by Rhys on 14/05/2015.
 */
public class Place implements Move {

    private Player player;
    private Piece piece;
    private Spot src;
    private Spot dest;

    public boolean move(Player player, Piece piece, Spot dest) {
        return move(player, piece, null, dest);
    }

    @Override
    public boolean move(Player player, Piece piece, Spot src, Spot dest) {
        if (piece.place(dest)) {
            this.player = player;
            this.piece = piece;
            this.src = src;
            this.dest = dest;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void undo() {
        piece.setSpot(src);
        dest.removePiece();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Piece getPiece() {
        return piece;
    }

    @Override
    public Spot getDest() {
        return dest;
    }

    @Override
    public Spot getSrc() {
        return src;
    }
}
