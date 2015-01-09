import java.awt.Point;

import javax.swing.ImageIcon;

/** The "Pawn" class.
 * This class extends the Piece class for pawns.
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public class Pawn extends Piece {
        public Pawn(int x, int y, int color) {
                super(x, y);
                colorOfPiece = color;
                type = "Pawn";
                if (color == WHITE)
                        image = new ImageIcon("Game Resources/pawn.png").getImage();
                else
                        image = new ImageIcon("Game Resources/pawnb.png")
                                        .getImage();
        }
        

        public boolean isLegalMove(Point previousPoint, Point droppedPoint,
                        Piece[][] grid, boolean promotionAllowed) {

                int xPosInitial = locationToPoint(previousPoint.x);
                int yPosInitial = locationToPoint(previousPoint.y);
                int xPosDropped = locationToPoint(droppedPoint.x);
                int yPosDropped = locationToPoint(droppedPoint.y);
                
                if (grid[xPosDropped][yPosDropped] == null){
                        if (xPosDropped == xPosInitial + 1 && yPosDropped == yPosInitial + (1 * colorOfPiece))
                                if (grid[xPosInitial + 1][yPosInitial] instanceof Pawn){
                                        if (Chess.lastMove instanceof Pawn && locationToPoint(Chess.lastMove.x) == xPosInitial + 1 && locationToPoint(Chess.lastMove.y) == yPosInitial)
                                        if (grid[xPosInitial + 1][yPosInitial].numOfMoves == 1)
                                                if (grid[xPosInitial + 1][yPosInitial].colorOfPiece == BLACK) {
                                                        if (yPosInitial == 3){

                                                                return EnPassantCheck (grid, xPosInitial, yPosInitial, previousPoint, droppedPoint);
                                                                
                                                        }
                                                } else if (grid[xPosInitial + 1][yPosInitial].colorOfPiece == WHITE) {
                                                        if (yPosInitial == 4){

                                                                return EnPassantCheck (grid, xPosInitial, yPosInitial, previousPoint, droppedPoint);
                                                                
                                                        }
                                                }
                                        }


                        if (xPosDropped == xPosInitial - 1 && yPosDropped == yPosInitial + (1 * colorOfPiece))
                                if (grid[xPosInitial - 1][yPosInitial] instanceof Pawn)
                                        if (Chess.lastMove instanceof Pawn && locationToPoint(Chess.lastMove.x) == xPosInitial - 1 && locationToPoint(Chess.lastMove.y) == yPosInitial)
                                        if (grid[xPosInitial - 1][yPosInitial].numOfMoves == 1)
                                                if (grid[xPosInitial - 1][yPosInitial].colorOfPiece == BLACK) {
                                                        if (yPosInitial == 3){

                                                                return EnPassantCheck (grid, xPosInitial, yPosInitial, previousPoint, droppedPoint);
                                                                
                                                        }
                                                } else if (grid[xPosInitial - 1][yPosInitial].colorOfPiece == WHITE) {
                                                        if (yPosInitial == 4){

                                                                return EnPassantCheck (grid, xPosInitial, yPosInitial, previousPoint, droppedPoint);
                                                                
                                                        }
                                                }
                }
                
                if (promotionAllowed){
                        if (kingInCheck(grid,previousPoint,droppedPoint)){
                                return false;
                        }
                }
                
                if (grid[xPosDropped][yPosDropped] == null) {
                        if (numOfMoves == 0)
                                if (yPosDropped == yPosInitial + (2 * colorOfPiece)
                                                && xPosInitial == xPosDropped && grid [xPosInitial][yPosInitial+1* colorOfPiece]==null) {
                                        return true;
                                }

                        if (yPosDropped == yPosInitial + (1 * colorOfPiece)
                                        && xPosInitial == xPosDropped) {
                                if ((yPosDropped == 0 || yPosDropped == 7) && promotionAllowed) {
                                        Chess.selectedPiece.promoted = true;
                                }
                                return true;
                        }
                }
                
                
                if ((xPosDropped == xPosInitial + 1 || xPosDropped == xPosInitial - 1)
                                && yPosDropped == (yPosInitial + 1 * colorOfPiece)){
                                if (grid[xPosDropped][yPosDropped] != null && colorOfPiece != grid[xPosDropped][yPosDropped].colorOfPiece) {
                                        if ((yPosDropped == 0 || yPosDropped == 7) && promotionAllowed) {
                                                System.out.println("2");
                                                Chess.selectedPiece.promoted = true;
                                        }
                                        return true;
                                }
                         else if (grid[xPosDropped][yPosDropped] != null && colorOfPiece != grid[xPosDropped][yPosDropped].colorOfPiece) {
                                if ((yPosDropped == 0 || yPosDropped == 7) && promotionAllowed) {
                                        Chess.selectedPiece.promoted = true;
                                }
                                return true;
                        }
                }
                return false;
        }

        
        public boolean hasMoves (Piece [][]grid){
                int row = locationToPoint(this.y);
                int rowCheck = locationToPoint(this.y) + colorOfPiece;
                int column = locationToPoint(this.x);
                
                if (rowCheck == 0 || rowCheck == 7){
                        return false;
                }
                
                Point previousPoint = new Point (this.x,this.y);
                
                for (int columnCheck = column - 1; columnCheck <=column+1; columnCheck++){
                        if (columnCheck >=0 && columnCheck <=7){
                                Point newPoint = new Point (PointTolocation(columnCheck), PointTolocation(rowCheck));
                                if (grid [column][row].isLegalMove(previousPoint, newPoint, grid, true)){
                                return true;
                                }
                        }
                }
                return false;
        }
        
        private boolean EnPassantCheck (Piece [][] grid, int xPosInitial, int yPosInitial, Point previousPoint, Point droppedPoint){
                EnPassant = true;
                int direction =0;
                if (droppedPoint.x > previousPoint.x){
                        direction = 1;
                }
                else if (droppedPoint.x > previousPoint.x){
                        direction = -1;
                }
                
                Piece enPassantPiece = grid[xPosInitial + direction][yPosInitial];
                grid[xPosInitial + direction][yPosInitial]= null;
                
                if (!kingInCheck(grid,previousPoint,droppedPoint)){
                        grid[xPosInitial + direction][yPosInitial] =enPassantPiece;
                        return true;
                }
                else{
                        grid[xPosInitial + 1][yPosInitial] =enPassantPiece;
                        return false;
                }
        }
}
