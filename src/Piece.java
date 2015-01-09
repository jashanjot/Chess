import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** The "Piece" class.
 * This class creates a new instance of Pieces depending on what type they are. It also contains methods which all of its subclasses use
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public abstract class Piece extends Rectangle {
	// Constants for the black and white piece variable
	protected final int WHITE = -1;
	protected final int BLACK = 1;
	
	//Image of the piece
	protected Image image;
	
	protected boolean EnPassant;
	
	protected String type;
	
	protected int colorOfPiece;
	protected int numOfMoves = 0;
	 
	protected int castle=0;
	 
	public boolean promoted;
	
	/** Constructs a rectangle for the piece
	 */
	public Piece(int row, int column) {
		super(row * Chess.PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD, column * Chess.PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD,
				Chess.PIXELS_OF_BOX, Chess.PIXELS_OF_BOX);
	}

	
	public abstract boolean isLegalMove (Point previousPoint, Point droppedPoint, Piece [][] grid,boolean justNotCheck);
	
	public abstract boolean hasMoves(Piece[][] grid);
	
	public int locationToPoint (int point){
		return (point-Chess.TOP_LEFT_BOARD)/Chess.PIXELS_OF_BOX;
	}
	
	public int PointTolocation (int Location){
		return (Location* Chess.PIXELS_OF_BOX+Chess.TOP_LEFT_BOARD);
	}
	
	protected boolean pointInAttack(Piece [][] grid, int turn, Point toCheck){ 
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row]!=null && grid[column][row].colorOfPiece != turn) {
					Point checkPiecePiont = new Point(grid[column][row].x,grid[column][row].y);
					if (grid[column][row].isLegalMove(checkPiecePiont, toCheck,grid,false)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean isPinned(Piece [][] grid, int turn, Point toCheck){
		
		return false;
	}
	
	protected boolean kingInCheck(Piece [][] grid, Point intial, Point dropped) {
		Piece savedIntialLocation = grid [locationToPoint(intial.x)][locationToPoint (intial.y)];
		Piece savedDroppedLocation = grid [locationToPoint(dropped.x)][locationToPoint (dropped.y)];
		grid [locationToPoint(intial.x)][locationToPoint (intial.y)] = null;
		grid [locationToPoint(dropped.x)][locationToPoint (dropped.y)] = savedIntialLocation;
		
		Point king = new Point ();
		if (colorOfPiece == WHITE){
			king = new Point (Chess.whiteKing.x,Chess.whiteKing.y);
		}
		else{
			king = new Point (Chess.blackKing.x,Chess.blackKing.y);
		}
		
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row]!=null && grid[column][row].colorOfPiece != colorOfPiece) {
					Point checkPiecePiont = new Point(grid[column][row].x,grid[column][row].y);

					if (grid[column][row].isLegalMove(checkPiecePiont, king ,grid,false)) {
						grid [locationToPoint(intial.x)][locationToPoint (intial.y)]  = savedIntialLocation;
						grid [locationToPoint(dropped.x)][locationToPoint (dropped.y)] = savedDroppedLocation;
						return true;
					}
				}
			}
		}
		grid [locationToPoint(intial.x)][locationToPoint (intial.y)]  = savedIntialLocation;
		grid [locationToPoint(dropped.x)][locationToPoint (dropped.y)] = savedDroppedLocation;
		return false;
	}
	
	protected boolean kingInCheck(Piece [][] grid,int kingXIntial,int kingYIntial, int kingX, int kingY) {

		Point king = new Point (PointTolocation(kingX),PointTolocation(kingY));
		
			Piece savePiece = grid [kingX][kingY];
			Piece kingSave = grid[kingXIntial][kingYIntial];
			grid [kingX][kingY]=kingSave;
			grid[kingXIntial][kingYIntial]=null;
		
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row]!=null && grid[column][row].colorOfPiece != colorOfPiece) {
					Point checkPiecePiont = new Point(grid[column][row].x,grid[column][row].y);

					
					if (grid[column][row].isLegalMove(checkPiecePiont, king ,grid,false)) {
						grid [kingX][kingY] = savePiece;
						grid [kingXIntial][kingYIntial]=kingSave;
						return true;
					}
				}
			}
		}
		grid [kingX][kingY] = savePiece;
		grid [kingXIntial][kingYIntial]=kingSave;
		return false;
	}
	
	
    public void move (Point initialPos, Point finalPos)
    {
    	int y = finalPos.y - initialPos.y;
    	int x = finalPos.x - initialPos.x;

        translate (x,y);
    }

	public void draw(Graphics g) {
		g.drawImage(image, this.x, this.y, Chess.PIXELS_OF_BOX,
				Chess.PIXELS_OF_BOX, null);
	}
}