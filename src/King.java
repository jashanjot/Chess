import java.awt.Point;

import javax.swing.ImageIcon;

/** The "King" class.
 * This class extends the Piece class for Kings.
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public class King extends Piece {
	public King(int x, int y,int color) {
		super (x,y);
		colorOfPiece = color;
		type = "King";
		if (color == WHITE)
			image = new ImageIcon("Game Resources/king.png").getImage();
		else
			image = new ImageIcon("Game Resources/kingb.png").getImage();
	}

	
	public boolean isLegalMove(Point previousPoint, Point droppedPoint, Piece [][] grid, boolean castleCheck) {

		int xPosInitial = locationToPoint(previousPoint.x);
		int yPosInitial = locationToPoint(previousPoint.y);
		int xPosDropped = locationToPoint(droppedPoint.x);
		int yPosDropped = locationToPoint(droppedPoint.y);
		
		Point toCheck = new Point(previousPoint.x,
				previousPoint.y);
		
		if (castleCheck) {
			if (yPosDropped == 0 && xPosDropped == 2) {
				if (numOfMoves == 0 && grid[0][0] instanceof Rook && grid[0][0].numOfMoves == 0 && grid[1][0] == null&& grid[2][0] == null&& grid[3][0] == null) {
					for (int column = xPosInitial; column > 1; column--) {
						if (pointInAttack(grid, Chess.turn, toCheck)){
							return false;
						}
						toCheck.x -= Chess.PIXELS_OF_BOX;
					}
					castle=1;
					return true;
				}
			}
			
			if (yPosDropped == 0 && xPosDropped == 6) {
				if (numOfMoves == 0 && grid[7][0] instanceof Rook && grid[7][0].numOfMoves == 0 && grid[6][0] == null&& grid[5][0] == null) {
					for (int column = xPosInitial; column < 7; column++) {
						if (pointInAttack(grid, Chess.turn, toCheck)){
							return false;
						}
						toCheck.x += Chess.PIXELS_OF_BOX;
					}
					castle=2;
					return true;
				}
			}
			
			if (yPosDropped == 7 && xPosDropped == 6) {
				if (numOfMoves == 0 && grid[7][7] instanceof Rook && grid[7][7].numOfMoves == 0 && grid[6][7] == null&& grid[5][7] == null) {
					for (int column = xPosInitial; column < 7; column++) {
						if (pointInAttack(grid, Chess.turn, toCheck)){
							return false;
						}
						toCheck.x += Chess.PIXELS_OF_BOX;
					}
					castle=3;
					return true;
				}
			}
			
			if (yPosDropped == 7 && xPosDropped == 2) {
				if (numOfMoves == 0 && grid[0][7] instanceof Rook && grid[0][7].numOfMoves == 0 && grid[1][7] == null&& grid[2][7] == null&& grid[3][7] == null) {
					for (int column = xPosInitial; column > 1; column--) {
						if (pointInAttack(grid, Chess.turn, toCheck)){
							return false;
						}
						toCheck.x -= Chess.PIXELS_OF_BOX;
					}
					castle=4;
					return true;
				}
			}
		}
		
		if (grid[xPosDropped][yPosDropped] != null) {
			if (grid[xPosDropped][yPosDropped].colorOfPiece == colorOfPiece) {
				return false;
			}
		}
		
		int rowIncrement=Math.abs(xPosDropped-xPosInitial);
		int columnIncrement=Math.abs(yPosDropped-yPosInitial);
		
		
		if (rowIncrement >1 || columnIncrement>1){
			return false;
		}
		
		if (castleCheck){
			if (!kingInCheck(grid,xPosInitial,yPosInitial, xPosDropped,yPosDropped)){
				return true;
			}
		}
		if (castleCheck){
			return false;
		}
		
		return true;
	}

	public boolean hasMoves (Piece [][]grid){
		int row = locationToPoint(this.y);
		int column = locationToPoint(this.x);
		
		Point previousPoint = new Point (this.x,this.y);
		
		for (int rowCheck = row - 1; rowCheck <=row+1; rowCheck++){
			for (int columnCheck = column - 1; columnCheck <=column+1; columnCheck++){
				if (columnCheck >=0 && columnCheck <=7 && rowCheck >=0 && rowCheck <=7){
				
				Point newPoint = new Point (PointTolocation(columnCheck), PointTolocation(rowCheck));
				
					if (grid [column][row].isLegalMove(previousPoint, newPoint, grid, true)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
}