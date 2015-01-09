import java.awt.Point;

import javax.swing.ImageIcon;

/** The "Knight" class.
 * This class extends the Piece class for Knights.
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public class Knight extends Piece {
	public Knight(int x, int y, int color) {
		super (x,y);
		colorOfPiece = color;
		type = "Knight";
		if (color == WHITE)
			image = new ImageIcon("Game Resources/knight.png").getImage();
		else
			image = new ImageIcon("Game Resources/knightb.png").getImage();
	}

	public boolean isLegalMove(Point previousPoint, Point droppedPoint, Piece [][] grid,boolean check) {

		int xPosInitial = locationToPoint(previousPoint.x);
		int yPosInitial = locationToPoint(previousPoint.y);
		int xPosDropped = locationToPoint(droppedPoint.x);
		int yPosDropped = locationToPoint(droppedPoint.y);
		
		if (check){
		if (kingInCheck(grid,previousPoint,droppedPoint)){
			return false;
		}
		}
		
		if (grid[xPosDropped][yPosDropped] != null) {
			if (grid[xPosDropped][yPosDropped].colorOfPiece == colorOfPiece) {
				return false;
			}
		} 
		
		if (xPosInitial + 2 == xPosDropped && (yPosInitial + 1 == yPosDropped || yPosInitial - 1 == yPosDropped)){
			return true;
		}
		
		if (xPosInitial - 2 == xPosDropped && (yPosInitial + 1 == yPosDropped || yPosInitial - 1 == yPosDropped)){
			return true;
		}
		
		if (yPosInitial + 2 == yPosDropped && (xPosInitial + 1 == xPosDropped || xPosInitial - 1 == xPosDropped)){
			return true;
		}
		
		if (yPosInitial - 2 == yPosDropped && (xPosInitial + 1 == xPosDropped || xPosInitial - 1 == xPosDropped)){
			return true;
		}
		
		return false;
	}

	public boolean hasMoves (Piece [][]grid){
		int row = locationToPoint(this.y);
		int column = locationToPoint(this.x);
		
		Point previousPoint = new Point (this.x,this.y);
		
		for (int rowCheck = row - 2; rowCheck <=row+2; rowCheck++){
			for (int columnCheck = column - 2; columnCheck <=column+2; columnCheck++){
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