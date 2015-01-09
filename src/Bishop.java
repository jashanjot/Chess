import java.awt.Point;

import javax.swing.ImageIcon;

/** The "Bishop" class.
 * This class extends the Piece class for Bishops.
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public class Bishop extends Piece {
	public Bishop(int x, int y, int color) {
		super(x, y);
		colorOfPiece = color;
		type = "Bishop";
		if (color == WHITE)
			image = new ImageIcon("Game Resources/bishop.png").getImage();
		else
			image = new ImageIcon("Game Resources/bishopb.png")
					.getImage();
	}

	public boolean isLegalMove(Point previousPoint, Point droppedPoint,
			Piece[][] grid,boolean check) {

		int xPosInitial = locationToPoint(previousPoint.x);
		int yPosInitial = locationToPoint(previousPoint.y);
		int xPosDropped = locationToPoint(droppedPoint.x);
		int yPosDropped = locationToPoint(droppedPoint.y);

		if (check){
		if (kingInCheck(grid,previousPoint,droppedPoint)){
			return false;
		}
		}
		
		int rowIncrement=0;
		int columnIncrement=0;
		
		if ((xPosDropped - xPosInitial != yPosDropped - yPosInitial) && (xPosDropped + yPosDropped != xPosInitial + yPosInitial)){
			return false;
		}
		
		if (grid[xPosDropped][yPosDropped] != null) {
			if (grid[xPosDropped][yPosDropped].colorOfPiece == colorOfPiece) {
				return false;
			}
		} 
		
		if (xPosInitial < xPosDropped){
			columnIncrement=1;
		}
		else if (xPosInitial > xPosDropped){
			columnIncrement = -1;
		}
	
		if (yPosInitial<yPosDropped){
			rowIncrement = 1;
		}
		else if (yPosInitial>yPosDropped){
			rowIncrement = -1;
		}
		
		xPosDropped-=columnIncrement;
		yPosDropped-=rowIncrement;
		while (xPosInitial != xPosDropped || yPosInitial !=yPosDropped){
			
			xPosInitial+=columnIncrement;
			yPosInitial+=rowIncrement;
			
			if (grid[xPosInitial][yPosInitial] != null)
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