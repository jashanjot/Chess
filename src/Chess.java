import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.applet.*;
import java.net.*;



/** The "Chess" class.
 * This class is the main initiator for the game with all the GUI and major methods such as checkmate
 * @author Jashanjit Sohanpal and Evan Cao
 * @version June 13, 2013
*/

public class Chess extends JFrame {
	// Point where the mouse dropped the piece, and picked up the piece.
	private Point lastPoint;
	private Point previousPoint;
	
	//Point that is stored when a piece is able to be promoted
	private Point promotePoint;
	
	//Variable for the piece selected when the mouse clicks on the board. Reason for it being public is because only Pawn uses it for promotion.
	public static Piece selectedPiece;

	//Variables for audio bgm and sounds
	private AudioClip titleBGM;
    private AudioClip gameOverSound;
    private AudioClip buttonPress;
    private AudioClip moveSound;
	
	//Keeps track of if the game if over and if someone wins or draws
	private int gameOver = 0;
	
	//The Piece that checked the King
	private Piece checkedBy;
	
	//Last move made. Reason for it being public is because only Pawn uses it for En Passant.
	public static Piece lastMove;
	
	
	// Variables for checking which game screen is displayed
    private int helpScreen = 0; 
    private int menu = 1;
    private int title = 0;

    // Variables for checking whether a mouse is over a menu button, allowing for the highlight to appear
    private int drawGamePaint = 0;
    private int promoteMenu = 0;
    private int promoHighlight = 0;
    private int highlight;
    private int titleHighlight;
    private int pointHighlight = 0;
    private int creditShow = 0;
    private int creditHighCheck = 0;
    private int drawType = 0;
    
    // Variable to check whether to play music or not
    private int musicPlay = 1;

    // Declare images
    private Image mON= new ImageIcon("Game Resources/musicOFF.png")
    .getImage();
    private Image mOFF = new ImageIcon("Game Resources/musicON.png")
    .getImage();
    private Image help1 = new ImageIcon("Game Resources/instructions1NEW.png")
    .getImage();
    private Image help2 = new ImageIcon("Game Resources/instructions2NEW.png")
    .getImage();
    private Image help3 = new ImageIcon("Game Resources/instructions3NEW.png")
    .getImage();
    private Image iconW = new ImageIcon("Game Resources/w.png")
    .getImage();
    private Image iconB = new ImageIcon("Game Resources/b.png")
    .getImage();
    private Image board = new ImageIcon("Game Resources/chessScreen.png")
    .getImage();
    private Image menu1 = new ImageIcon("Game Resources/menu1.png")
    .getImage();
    private Image menu2 = new ImageIcon("Game Resources/menu2.png")
    .getImage();
    private Image menu3W = new ImageIcon("Game Resources/menu3w.png")
    .getImage();
    private Image menu3B = new ImageIcon("Game Resources/menu3b.png")
    .getImage();
    private Image menu3D = new ImageIcon("Game Resources/menu3D.png")
    .getImage();
    private Image highLighting = new ImageIcon("Game Resources/highlight.png")
    .getImage();
    private Image titleScreen = new ImageIcon("Game Resources/BG.png")
    .getImage();
    private Image titleHigh = new ImageIcon("Game Resources/titlehighlight.png")
    .getImage();
    private Image boardHigh = new ImageIcon("Game Resources/boardhighlight.png")
    .getImage();
    private Image promoWindow = new ImageIcon("Game Resources/promotionWindow.png")
    .getImage();
    private Image promoKnight = new ImageIcon("Game Resources/promoKnight.png")
    .getImage();
    private Image promoKnightS = new ImageIcon("Game Resources/promoKnightS.png")
    .getImage();
    private Image promoQueen = new ImageIcon("Game Resources/promoQueen.png")
    .getImage();
    private Image promoQueenS = new ImageIcon("Game Resources/promoQueenS.png")
    .getImage();
    private Image promoRook = new ImageIcon("Game Resources/promoRook.png")
    .getImage();
    private Image promoRookS = new ImageIcon("Game Resources/promoRookS.png")
    .getImage();
    private Image promoBishop = new ImageIcon("Game Resources/promoBishop.png")
    .getImage();
    private Image promoBishopS = new ImageIcon("Game Resources/promoBishopS.png")
    .getImage();
    private Image credit = new ImageIcon("Game Resources/credits.png")
    .getImage();
    private Image creditHigh = new ImageIcon("Game Resources/creditHighlight.png")
    .getImage();
    
    
    //Arrays of the white and black captured pieces
	private ArrayList<String> whiteCapturedPieces = new ArrayList<String>();
	private ArrayList<String> blackCapturedPieces = new ArrayList<String>();
	
	//Number of Pieces on the board
	private int newNumOfPieces = 0;
	private int oldNumOfPieces = 32;
	
	// Number of moves without a kill
	private int numOfMovesWithoutKill = 0;
	
	//Copy of the white and black king which are on the board. Reason for public is because it is only used in the king class
	public static Piece whiteKing;
	public static Piece blackKing;
	
	//constant to represent the white pieces
	private final int WHITE = -1;
	
	//Constants to represent whose turn it is
	private static final int whiteTurn = -1;
	private static final int blackTurn = 1;
	
	//Variable for whose turn it is
	public static int turn = whiteTurn;
	
	//Constant for distance of the top left of the board from the edge of the jframe
	public static final int TOP_LEFT_BOARD = 44;
	
	//Variable to make the piece snap onto the square where the piece is dropped
	private int xChange,yChange;
	
	//Constant for the pixels long and wide each square on the board is.
	public static final int PIXELS_OF_BOX = 64;
	
	private final Font TITLE_FONT = new Font ("Verdana", Font.BOLD, 12);

	// 2D array of the board of the type PieceType
	private Piece[][] grid = new Piece[8][8];

	//Array storing previous moves made
	private ArrayList<Board [][]> gridList = new ArrayList<Board[][]>();
	
	/** Constructs the JFrame
	 */
	public Chess() {
		// Create Main Frame
		super("Chess");
		this.getContentPane().setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
		
		// Declare Audio and start BGM loop
		gameOverSound = Applet.newAudioClip (getCompleteURL ("gameOver.wav"));  
        buttonPress = Applet.newAudioClip (getCompleteURL ("buttonPress.wav"));
        moveSound = Applet.newAudioClip (getCompleteURL ("pieceSound.wav"));
        titleBGM = Applet.newAudioClip (getCompleteURL ("titleBGMlonger.wav"));
        titleBGM.loop();
		
		gameInitialize(grid);
		BoardArea board = new BoardArea();
		this.add(board);
	}

	 // Gets the URL needed for newAudioClip
    public URL getCompleteURL (String fileName)
    {
        try
        {
            return new URL ("file:" + System.getProperty ("user.dir") + "\\" + fileName);
        }
        catch (MalformedURLException e)
        {
            System.err.println (e.getMessage ());
        }
        return null;
    }
	
	/** Main to initialize the value of each piece and add some frame settings
	 */
	public static void main(String[] args) {
		Chess myGame = new Chess();
		myGame.pack();
		myGame.setLocationRelativeTo(null);
		myGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myGame.setVisible(true);
	}

    /** Initializes instances of each piece for black and white onto the grid
     * @param grid the grid of the board
     */
	private void gameInitialize(Piece[][] grid) {
		// White Pieces
		grid[0][7] = new Rook(0, 7, -1);
		grid[7][7] = new Rook(7, 7, -1);

		grid[1][7] = new Knight(1, 7, -1);
		grid[6][7] = new Knight(6, 7, -1);

		grid[2][7] = new Bishop(2, 7, -1);
		grid[5][7] = new Bishop(5, 7, -1);

		grid[3][7] = new Queen(3, 7, -1);
		grid[4][7] = new King(4, 7, -1);
		whiteKing = grid[4][7];

		for (int colum = 0; colum < 8; colum++)
			grid[colum][6] = new Pawn(colum, 6, -1);

		// Black Pieces
		grid[0][0] = new Rook(0, 0, 1);
		grid[7][0] = new Rook(7, 0, 1);

		grid[1][0] = new Knight(1, 0, 1);
		grid[6][0] = new Knight(6, 0, 1);

		grid[2][0] = new Bishop(2, 0, 1);
		grid[5][0] = new Bishop(5, 0, 1);

		grid[3][0] = new Queen(3, 0, 1);
		grid[4][0] = new King(4, 0, 1);
		blackKing = grid[4][0];
		
		for (int colum = 0; colum < 8; colum++)
			grid[colum][1] = new Pawn(colum, 1, 1);
	}
	
    /** Checks the number of checks on the king
     * @return the number of checks on the king
     */
	private int kingInCheck() {
		Point kingPoint = new Point();
		if (turn == -1){
		    kingPoint = new Point(whiteKing.x, whiteKing.y);
		}else if (turn == 1){
			kingPoint = new Point(blackKing.x, blackKing.y);
		}
		
		int numOfChecks=0;
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row]!=null && grid[column][row].colorOfPiece != turn) {
					Point checkPiecePiont = new Point(grid[column][row].x,grid[column][row].y);

					if (grid[column][row].isLegalMove(checkPiecePiont, kingPoint,grid,false)) {
						checkedBy = grid[column][row];
						numOfChecks++;
					}
				}
			}
		}
		System.out.println(numOfChecks);
		return numOfChecks;
	}
	
    /** Checks if it is possible to block the path of a check to the king
     * @param checkPoint the point of the piece checking the king
     * @return true or false depending on if the path can be blocked
     */
	private boolean canBlockPathOfCheck (Point checkPoint){
		
		Piece targetKing = targetKing();
		
		int columnPosOfPieceGivingCheck= checkedBy.locationToPoint(checkPoint.x);
		int rowPosOfPieceGivingCheck= checkedBy.locationToPoint(checkPoint.y);
		int xPosOnGridOfKing= targetKing.locationToPoint(targetKing.x);
		int yPosOnGridOfKing=targetKing.locationToPoint(targetKing.y);
		
		int rowIncrement=0;
		int columnIncrement=0;
		
		if (columnPosOfPieceGivingCheck < xPosOnGridOfKing){
			columnIncrement=1;
		}
		else if (columnPosOfPieceGivingCheck > xPosOnGridOfKing){
			columnIncrement = -1;
		}
		
		if (rowPosOfPieceGivingCheck<yPosOnGridOfKing){
			rowIncrement = 1;
		}
		else if (rowPosOfPieceGivingCheck>yPosOnGridOfKing){
			rowIncrement = -1;
		}
		
	//	System.out.println(columnPosOfPieceGivingCheck + " " + rowPosOfPieceGivingCheck);
		System.out.println(xPosOnGridOfKing + " " + yPosOnGridOfKing);
		System.out.println (columnIncrement + " " +rowIncrement);
		
		xPosOnGridOfKing-=columnIncrement;
		yPosOnGridOfKing-=rowIncrement;
		
		System.out.println (xPosOnGridOfKing + " " +yPosOnGridOfKing);
		
		while (columnPosOfPieceGivingCheck != xPosOnGridOfKing || rowPosOfPieceGivingCheck !=yPosOnGridOfKing){
			
			columnPosOfPieceGivingCheck+=columnIncrement;
			rowPosOfPieceGivingCheck+=rowIncrement;
			
			Point movePoint = new Point (columnPosOfPieceGivingCheck*PIXELS_OF_BOX+TOP_LEFT_BOARD,rowPosOfPieceGivingCheck*PIXELS_OF_BOX+TOP_LEFT_BOARD);
			
			if (canMoveHere(movePoint, movePoint,true, turn*-1)){
				System.out.println ("Block possible");
				return false;
			}
		}
		
		return true;
	}
	
    /** Finds which king can be in check
     * @return the king that is the one that could be in check
     */
	private Piece targetKing (){
		Piece targetKing = null;
		if (turn==whiteTurn){
			targetKing = whiteKing;
		}
		else if (turn==blackTurn){
			targetKing = blackKing;
		}
		
		return targetKing;
	}
	
    /** Checks if it is possible to block the path of a check to the king
     * @param checkPoint the point of the piece checking the king
     * @return true or false depending on if the path can be blocked
     */
	private void mouseDropOffBoard (Point currentPosOfClick){
        	selectedPiece.move (currentPosOfClick, previousPoint);
        	selectedPiece = null;
    		repaint();
	}
	
    /** Moves the piece on the grid
     * @param droppedPoint Point where the piece was dropped
     * @param currentPosOfClick place where the user dropped the piece
     * @param pointToMoveTo the point of the top left of the square to move to
     */
	private void pieceMove (Point droppedPoint,Point currentPosOfClick, Point pointToMoveTo){
		
		Piece droppedPlace = grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX];
		if (droppedPlace!=null){
			if (droppedPlace.colorOfPiece == WHITE){
				whiteCapturedPieces.add(droppedPlace.type);
			}
			else{
				blackCapturedPieces.add(droppedPlace.type);
			}
		}
		
		selectedPiece.move (currentPosOfClick, pointToMoveTo);
		
		grid [(previousPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX]=null;
		grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX] = selectedPiece;
		grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX].numOfMoves++;
	
		lastMove = selectedPiece;    
	}
	
    /** Castles the king depending on which type of castle
     */
	private void castleKing (){
		if (selectedPiece.castle ==1){
			grid[3][0] = grid[0][0];
			grid[0][0] = null;
			grid[3][0].setLocation(3 * PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD, TOP_LEFT_BOARD);
		}
		
		if (selectedPiece.castle ==2){
			grid[5][0] = grid[7][0];
			grid[7][0] = null;
			grid[5][0].setLocation(5 * PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD, TOP_LEFT_BOARD);
		}
		
		if (selectedPiece.castle ==3){
			grid[5][7] = grid[7][7];
			grid[7][7] = null;
			grid[5][7].setLocation(5 * PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD, TOP_LEFT_BOARD+7*PIXELS_OF_BOX);
		}
		
		if (selectedPiece.castle ==4){
			grid[3][7] = grid[0][7];
			grid[0][7] = null;
			grid[3][7].setLocation(3 * PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD, TOP_LEFT_BOARD+7*PIXELS_OF_BOX);
		}

		selectedPiece.castle=0;
	}
	
    /** Switches whose turn it is
     */
	private void switchTurn (){
		 if (turn == whiteTurn){
			 turn = blackTurn;
		 }else if (turn == blackTurn){
			 turn = whiteTurn;
		 }
	}
	
    /** Promotes piece to queen, rook, knight or bishop
     * @param droppedPoint Point where the piece was dropped
     * @param type the type of piece the pawn will be promoted to
     */
	private void promoting (Point droppedPoint, String type){
		
		grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX] = new Queen((droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX,(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX, selectedPiece.colorOfPiece);
		if ( type.equals("Bishop")){
			grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX] = new Bishop((droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX,(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX, selectedPiece.colorOfPiece);
		}
		else if ( type.equals("Rook")){
			grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX] = new Rook((droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX,(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX, selectedPiece.colorOfPiece);
		}
		else if ( type.equals("Knight")){
			grid [(droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX][(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX] = new Knight((droppedPoint.x- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX,(droppedPoint.y- Chess.TOP_LEFT_BOARD)/PIXELS_OF_BOX, selectedPiece.colorOfPiece);
		}
	}
	
    /** Checks if all of a either white or black pieces can move to a certain point
     * @param previousPoint previous point of where the piece currently moved is
     * @param checkPoint The place where the previous move piece was dropped
     * @param allMoves depending on which version of the method that is needed, tells method to make piece checking king null
     * @param checkingTurn tells method what color of pieces to use for method
     * @return true or false depending on if there is a piece that can be moved to the check point
     */
	private boolean canMoveHere (Point previousPoint, Point checkPoint, boolean allMoves, int checkingTurn){
		
		Piece previousPieceLocation =grid [(previousPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX];
		Piece newLocation =grid [(checkPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX];
		grid [(previousPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = null;
		grid [(checkPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = previousPieceLocation;
		
		if (!allMoves){
			grid [(checkedBy.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkedBy.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = null;
		}
		
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row]!=null && grid[column][row].colorOfPiece != checkingTurn) {
					Point checkPiecePiont = new Point(grid[column][row].x,grid[column][row].y);

				
					if (grid[column][row] instanceof King){

						System.out.println("kingInCheck");
						System.out.println(checkPiecePiont);
						System.out.println(checkPoint);
						
					}
					if (grid[column][row].isLegalMove(checkPiecePiont, checkPoint,grid,true)) {
						grid [(checkedBy.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkedBy.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = checkedBy ;
						
						grid [(previousPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = previousPieceLocation;
						grid [(checkPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = newLocation;
						
						return true;
					}
				}
			}
		}
		grid [(checkedBy.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkedBy.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = checkedBy ;
		
		grid [(previousPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = previousPieceLocation;
		grid [(checkPoint.x-TOP_LEFT_BOARD)/PIXELS_OF_BOX][(checkPoint.y-TOP_LEFT_BOARD)/PIXELS_OF_BOX] = newLocation;
		
		return false;
}

    /** Checks if the king has legal moves
     * @return true or false depending on if the king can move
     */	
private boolean canKingMove (){
	Piece targetKing = targetKing();
	
	int kingRow = targetKing.locationToPoint(targetKing.x);
	int kingColumn = targetKing.locationToPoint(targetKing.y);
	Point pointOfKing = new Point (targetKing.x,targetKing.y);
	
	for (int column = kingRow - 1; column <= kingRow + 1; column++) {
		for (int row = kingColumn - 1; row <= kingColumn + 1; row++) {
			if (column >-1 && column<8 && row > -1 && row <8){

				Point checkPoint = new Point (column * PIXELS_OF_BOX + TOP_LEFT_BOARD,row * PIXELS_OF_BOX + TOP_LEFT_BOARD);
				
				
				if (grid[column][row] == null){
					if (!canMoveHere(pointOfKing, checkPoint,true, turn)){
						System.out.println("null move");
						return true;
					}
				}

				
				if (targetKing.isLegalMove(pointOfKing, checkPoint, grid,false)) {
					System.out.println(checkPoint);
					if (!canMoveHere(pointOfKing,checkPoint,true, turn)){
						System.out.println("taking move move");
						return true;
					}
				}
			}
		}
	}
	
	System.out.println("no legal move");
	return false;
}
	
/** Checks if there is a stalemate
 * @return if there is a stalemate or not
 */
private boolean isStalemate (){
	for (int row = 0; row < 8; row++) {
		for (int column = 0; column < 8; column++) {
			if (grid[column][row]!=null && grid[column][row].colorOfPiece == turn) {
				if (grid[column][row].hasMoves(grid)) {
					return false;
				}
			}
		}
	}
	return true;
}

/** Creates a new game by resetting all variables
 */
private void newGame (){
	for (int row = 0; row < 8; row++) {
		for (int column = 0; column < 8; column++) {
			grid[column][row] =null;
		}
	}
	
	gameOver = 0;
	drawGamePaint =0;
	promoteMenu = 0;
	drawType = 0;
	oldNumOfPieces = 32;
	numOfMovesWithoutKill = 0;
	
	whiteCapturedPieces.clear();
	blackCapturedPieces.clear();
	
	turn = WHITE;
	
	gridList.clear();
	
	gameInitialize (grid);
	repaint();
}

/** Undoes the previous move
 */
private void undoMove (){

	if (gridList.size() ==1){
		newGame();
		repaint();
		return;
	}
	
	if (gridList.size() > 1){
		Board[][] undoList = gridList.get(gridList.size()  - 2);

		whiteCapturedPieces.clear();

		blackCapturedPieces.clear();
		
		undoList[0][0].movesWithoutCapture = numOfMovesWithoutKill;
		
		for (int numOfPawns = 0; numOfPawns < 8; numOfPawns++){
			whiteCapturedPieces.add("Pawn");
		}
		
		whiteCapturedPieces.add("Rook");
		whiteCapturedPieces.add("Rook");
		
		whiteCapturedPieces.add("Knight");
		whiteCapturedPieces.add("Knight");
		
		whiteCapturedPieces.add("Bishop");
		whiteCapturedPieces.add("Bishop");
		
		whiteCapturedPieces.add("Queen");
		whiteCapturedPieces.add("King");
		
		for (int numOfPawns = 0; numOfPawns < 8; numOfPawns++){
			blackCapturedPieces.add("Pawn");
		}
		
		blackCapturedPieces.add("Rook");
		blackCapturedPieces.add("Rook");
		
		blackCapturedPieces.add("Knight");
		blackCapturedPieces.add("Knight");
		
		blackCapturedPieces.add("Bishop");
		blackCapturedPieces.add("Bishop");
		
		blackCapturedPieces.add("Queen");
		blackCapturedPieces.add("King");
		
		for (int row = 0; row < 8; row++){
			for (int column = 0; column < 8; column++) {
				grid[column][row] = null;
				if (undoList[column][row].Piece =="Pawn"){
					grid[column][row] = new Pawn(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("Pawn");
					}
					else{
						blackCapturedPieces.remove("Pawn");
					}
				}
				if (undoList[column][row].Piece =="Rook"){
					grid[column][row] = new Rook(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("Rook");
					}
					else{
						blackCapturedPieces.remove("Rook");
					}
				}
				if (undoList[column][row].Piece =="Knight"){
					grid[column][row] = new Knight(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("Knight");
					}
					else{
						blackCapturedPieces.remove("Knight");
					}
				}
				if (undoList[column][row].Piece =="Bishop"){
					grid[column][row] = new Bishop(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("Bishop");
					}
					else{
						blackCapturedPieces.remove("Bishop");
					}
				}
				if (undoList[column][row].Piece =="Queen"){
					grid[column][row] = new Queen(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("Queen");
					}
					else{
						blackCapturedPieces.remove("Queen");
					}
				}
				if (undoList[column][row].Piece =="King"){
					grid[column][row] = new King(column, row,undoList[column][row].color);
					grid[column][row].numOfMoves =undoList[column][row].numOfMoves;
					
					if (undoList[column][row].color == WHITE){
						whiteCapturedPieces.remove("King");
						whiteKing = grid[column][row];
					}
					else{
						blackCapturedPieces.remove("King");
						blackKing = grid[column][row];
					}
				}
			}
		}
		gridList.remove (gridList.size()  - 1);
		switchTurn();
	}
}

/** En Passant captures on the grid
 * @param droppedPoint point where the piece was dropped
 */
private void EnPassantCapture (Point droppedPoint){
		Piece enPassantPiece = grid [(droppedPoint.x - TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y - TOP_LEFT_BOARD)/PIXELS_OF_BOX];
		
		if (enPassantPiece instanceof Pawn){
			if (enPassantPiece.colorOfPiece == WHITE){
				whiteCapturedPieces.add(enPassantPiece.type);
			}
			else{
				blackCapturedPieces.add(enPassantPiece.type);
			}
			
		}
		
		selectedPiece.EnPassant = false;
		
		grid [(droppedPoint.x - TOP_LEFT_BOARD)/PIXELS_OF_BOX][(previousPoint.y - TOP_LEFT_BOARD)/PIXELS_OF_BOX] = null;
		
}

/** Records the game in the arraylist gridList
 */
	private void recordOfGame (){
		Board[][] instanceOfGrid = new Board[8][8];
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (grid[column][row] != null){
					Board instance = new Board ();
					instance.Piece = grid [column][row].type;
					instance.color = grid [column][row].colorOfPiece;
					instance.numOfMoves = grid [column][row].numOfMoves;
					instance.movesWithoutCapture = numOfMovesWithoutKill;
					instance.lastMove = lastMove;
					instanceOfGrid [column][row] = instance;
				}
				else {
					Board instance = new Board ();
					instanceOfGrid [column][row] = instance;
				}
			}
		}

		gridList.add (instanceOfGrid);
	}
	
 	
private int numCaptured (int BlackOrWhite, String piece){
	int numPiece = 0;
	if (BlackOrWhite == whiteTurn)
	{
	for (int i = 0	; i < whiteCapturedPieces.size(); i++)
	{		
		if (whiteCapturedPieces.get(i).equals(piece))
				numPiece ++;
		}
	}
	else if (BlackOrWhite == blackTurn)
	{
		for (int i = 0	; i < blackCapturedPieces.size(); i++)
		{		
			if (blackCapturedPieces.get(i).equals(piece))
					numPiece ++;
			}
		}
	return numPiece;
}

/** Checks for draw by repeated moves
 * @return true or false depending on if it is a draw or not
 */
private boolean drawByRepeatedMoves (){
		if (gridList.size() > 8) {
			int differentMoves = 0;

			for (int compare = gridList.size(); compare >= gridList.size() - 1; compare--) {
				Board[][] firstList = gridList.get(compare - 1);
				Board[][] secondList = gridList.get(compare - 5);

				for (int row = 0; row < 8; row++) {
					for (int column = 0; column < 8; column++) {
						if (!(firstList[column][row].Piece.equals(secondList[column][row].Piece) && firstList[column][row].color == secondList[column][row].color)) {
							differentMoves++;
						} else if ((firstList == null && secondList != null)
								|| firstList != null && secondList == null) {
							differentMoves++;
						}
					}
				}

			}

			if (differentMoves == 0) {
				gameOver = 1;
				System.out.println("DRAW BY REPEATED MOVE");
				drawGamePaint = 1;
				menu = 3;
				return true;
			}
		}
		return false;
}

/** Checks for different types of game finishes such as draws, stalemates, and checkmates. Also records the game
 */
/** Checks for different types of game finishes such as draws, stalemates, and checkmates. Also records the game
 */
private void CheckingForGameFinishes (){
 
	System.out.println(kingInCheck());
	System.out.println(blackKing);
 
	if (kingInCheck()>1){
		if (!canKingMove()){
			gameOver=1;
			menu = 3;
			System.out.println(turn + " Lost by double check");
			repaint();
			gameOverSound.play();
			return;
		}
	}
	else if (kingInCheck()==1){
		System.out.println (checkedBy);
		if (!canKingMove()){
			System.out.println ("King Cant Move");
			Point checkPoint = (checkedBy.getLocation());
			if (!canMoveHere (checkPoint,checkPoint,true, turn*-1)){
				System.out.println ("Check Piece Cannot Be Taken");
				if (checkedBy instanceof Knight){
					gameOver=1;
					System.out.println(turn + " Lost by knight ");
					menu = 3;
					repaint();
					gameOverSound.play();
					return;
				}
				else if (canBlockPathOfCheck(checkPoint)){
					gameOver=1;
					menu = 3;
					System.out.println(turn + " Lost"); 
					repaint();
					gameOverSound.play();
					return;
				}
			}
		}
	}

	if (kingInCheck()==0){
		if (isStalemate()) {
			gameOver = 1;
			drawGamePaint = 1;
			drawType = 1;
			menu = 3;
			System.out.println("STALEMATE");
			repaint();
			gameOverSound.play();
			return;
		}
	}
 
	for (int row = 0; row < 8; row++) {
		for (int column = 0; column < 8; column++) {
			if (grid[column][row] != null){
				newNumOfPieces++;
			}
		}
	}
 
	if (newNumOfPieces != oldNumOfPieces){
		numOfMovesWithoutKill = 0;
	}
	else{
		numOfMovesWithoutKill++;
		System.out.println(numOfMovesWithoutKill);
	}
 
	oldNumOfPieces = newNumOfPieces;	
	newNumOfPieces=0;
	
	if (numOfMovesWithoutKill == 50){
		gameOver=1;
		System.out.println ("DRAW BY 50 MOVES WITHOUT KILL");
		drawGamePaint = 1;
		drawType = 2;
		menu = 3;
		repaint();
		gameOverSound.play();
		return;
	}
 
	recordOfGame();
 
	if (drawByRepeatedMoves ()){
		gameOver=1;
		System.out.println ("DRAW BY REPEATED MOVE");
		drawGamePaint = 1;
		drawType = 3;
		menu = 3;
		repaint();
		gameOverSound.play();
		return;
	}
}


	private class BoardArea extends JPanel {
		/** Constructs the JPanel on the jframe
		 */
		public BoardArea() {
			this.setLocation(0, 0);
			setBackground(new Color(0, 125, 0));
			
			this.addMouseListener(new MouseHandler());
			this.addMouseMotionListener(new MouseMotionHandler());
			this.addMouseMotionListener(new MouseHandler());
		}

		public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            
            // Display the title screen and title menu buttons
            if (title == 0)
            {
                        g.drawImage(titleScreen, 0, 0, 800,600, null);
            
                        // Highlight title buttons
                    if  ( titleHighlight == 1 )                
                     g.drawImage (titleHigh, 200, 393, 400, 40, this);  
                    else if ( titleHighlight == 2 )  
                            g.drawImage (titleHigh, 200, 455, 400, 40, this); 
                    else if ( titleHighlight == 3 )         
                            g.drawImage (titleHigh, 200, 515, 400, 40, this);
                    
                    // Display credit screen and highlight
                    if (creditShow == 1)
                        g.drawImage(credit,0,0,800,600,this);  
                    
                    if (creditHighCheck == 1)
                        g.drawImage(creditHigh,0,0,800,600,this);
            }       
            else
            {
            	// Display chess main screen
                g.drawImage(board, 0, 0, 800,600, null);
                                    
                // Highlight menu buttons
                    if  ( highlight == 1 )                
                                g.drawImage (highLighting, 600, 375, 150, 50, this);  
                    else if ( highlight == 2 )  
                            g.drawImage (highLighting, 600, 437, 150, 50, this); 
                    else if ( highlight == 3 )      
                            g.drawImage (highLighting, 600, 500, 150, 50, this); 
                    
                    
                   // Draw pieces on board
                    for (int row = 0; row < 8; row++)
                        for (int column = 0; column < 8; column++) {
                                if (grid[row][column] != null)
                                        grid[row][column].draw(g);
                        }
            // Highlight the original spot of the piece when moving with mouse
            if (selectedPiece != null)      
            {
                g.drawImage(boardHigh, previousPoint.x,previousPoint.y,64,64,this);
                selectedPiece.draw(g);
            }
            
            // Display the Draw/Win message
            if (drawGamePaint == 1){
            	g.setColor (Color.white);
                g.setFont (TITLE_FONT);
                // Display what caused the Draw
                g.drawImage(menu3D, 0, 0, 800,600, this);
                	if (drawType == 1)
                		g.drawString ("STALEMATE", 639, 253);
                	else if (drawType == 2)
                		g.drawString ("50 MOVES W/O KILL", 605, 253);
                	else if (drawType == 3)
                		g.drawString ("REPEATED MOVES", 617, 253);            		                    
            }      
            else if (gameOver == 1){
                if (turn == whiteTurn)
                        g.drawImage(menu3B, 0, 0, 800,600, this);
                else if (turn == blackTurn)
                        g.drawImage(menu3W, 0, 0, 800,600, this);
            }
            // Display the menu screen
            else if (menu == 1)
                    g.drawImage(menu1, 0, 0, 800,600, this);
            else if (menu == 2)
                    g.drawImage(menu2, 0, 0, 800,600, this);
            else if (menu == 3 && turn == whiteTurn)
                        g.drawImage(menu3B, 0, 0, 800,600, this);
            else if (menu == 3 && turn == blackTurn)
                        g.drawImage(menu3W, 0, 0, 800,600, this);
            
            
           // Display player turn icons
            if (turn == whiteTurn)
                 g.drawImage(iconW, 643, 75, 65,50, this);
            else if (turn == blackTurn)
                 g.drawImage(iconB, 640, 75, 75,50, this);
            
            
            // Display the number of captured pieces         
            if (gameOver == 0)
            {
            g.setColor (Color.white);
            g.setFont (TITLE_FONT);
            g.drawString (("" + numCaptured(1,"Pawn")), 650, 165); // B PAWN
            g.drawString (("" + numCaptured(-1,"Pawn")), 650, 288); // W PAWN
            g.drawString (("" + numCaptured(1,"Bishop")), 730, 165); // B BISHOP            
            g.drawString (("" + numCaptured(-1,"Bishop")), 730, 288); // W BISHOP
            g.drawString (("" + numCaptured(1,"Rook")), 650, 192); // B ROOK            
            g.drawString (("" + numCaptured(-1,"Rook")), 650, 316); // W ROOK
            g.drawString (("" + numCaptured(1,"Queen")), 730, 192); // B QUEEN            
            g.drawString (("" + numCaptured(-1,"Queen")), 730, 316); // W QUEEN
            g.drawString (("" + numCaptured(1,"Knight")), 690, 218); // B KNIGHT         
            g.drawString (("" + numCaptured(-1,"Knight")), 690, 341); // W KNIGHT
            }
            
            // Display the promote menu and highlight the choices when mousing over
            if (promoteMenu == 1){
                g.drawImage(promoWindow, 46,171,512,256,this); 
                
                if (promoHighlight == 0)
                {
                        g.drawImage(promoKnight, 46,171,512,256,this);
                        g.drawImage(promoBishop, 46,171,512,256,this);  
                        g.drawImage(promoRook, 46,171,512,256,this);    
                        g.drawImage(promoQueen, 46,171,512,256,this);                           
                }
                else if (promoHighlight == 1)
                {
                        g.drawImage(promoKnightS, 46,171,512,256,this);
                        g.drawImage(promoBishop, 46,171,512,256,this);  
                        g.drawImage(promoRook, 46,171,512,256,this);    
                        g.drawImage(promoQueen, 46,171,512,256,this);                           
                }
                else if (promoHighlight == 2)
                {
                        g.drawImage(promoKnight, 46,171,512,256,this);
                        g.drawImage(promoBishop, 46,171,512,256,this);  
                        g.drawImage(promoRook, 46,171,512,256,this);    
                        g.drawImage(promoQueenS, 46,171,512,256,this);                          
                }
                else if (promoHighlight == 3)
                {
                        g.drawImage(promoKnight, 46,171,512,256,this);
                        g.drawImage(promoBishopS, 46,171,512,256,this);         
                        g.drawImage(promoRook, 46,171,512,256,this);    
                        g.drawImage(promoQueen, 46,171,512,256,this);                           
                }
                else if (promoHighlight == 4)
                {
                        g.drawImage(promoKnight, 46,171,512,256,this);
                        g.drawImage(promoBishop, 46,171,512,256,this);  
                        g.drawImage(promoRookS, 46,171,512,256,this);   
                        g.drawImage(promoQueen, 46,171,512,256,this);                           
                }
            }                   
            }
            
            // Display the help screen
            if (helpScreen == 1)
                g.drawImage (help1, 0,0, 800,600,this);
        else if (helpScreen == 2)
                g.drawImage (help2,0,0,800,600,this);
        else if (helpScreen == 3)
                g.drawImage (help3,0,0,800,600,this);
            
            // Display music button
            if (musicPlay == 0)
                g.drawImage (mON,0,0,800,600,this);
            else if (musicPlay ==1 )
                g.drawImage (mOFF,0,0,800,600,this);
                }
		
		private class MouseHandler extends MouseAdapter {
			
			public void mousePressed(MouseEvent event) {
                
                Point selectedPoint = event.getPoint ();
                 
                 int x;
                 int y;
                 
                 // Respond if mouse clicks over music button
                 if (selectedPoint.x > 768 && selectedPoint.x < 795 && selectedPoint.y > 565 && selectedPoint.y < 593 && musicPlay == 1){
                         musicPlay = 0;
                         titleBGM.stop();
                 }
                 else if (selectedPoint.x > 768 && selectedPoint.x < 795 && selectedPoint.y > 565 && selectedPoint.y < 593 && musicPlay == 0){
                         musicPlay = 1;
                         titleBGM.loop();
                 }    

                // Respond if mouse clicks over promotion menu icons
                 if (promoteMenu == 1){
                         if (selectedPoint.x >= 61 && selectedPoint.x < 150 && selectedPoint.y >= 285 && selectedPoint.y < 400)
                         {
                                 buttonPress.play();
                         promoting (promotePoint,"Knight"); 
                        promoteMenu = 0; 
                        setCursor (Cursor.getDefaultCursor ());
                        switchTurn ();
                         selectedPiece = null;
                         CheckingForGameFinishes();
                         repaint();
                         return;
                         }
                         else if (selectedPoint.x >= 195 && selectedPoint.x < 270 && selectedPoint.y >= 260 && selectedPoint.y < 400)
                         {
                                 buttonPress.play();
                         promoting (promotePoint,"Queen"); 
                        promoteMenu = 0; 
                        setCursor (Cursor.getDefaultCursor ());
                        switchTurn ();
                         selectedPiece = null;
                         CheckingForGameFinishes();
                         repaint();
                         return;
                         }
                         else if (selectedPoint.x >= 335 && selectedPoint.x < 400 && selectedPoint.y >= 260 && selectedPoint.y < 400)
                         {
                                 buttonPress.play();
                         promoting (promotePoint,"Bishop"); 
                        promoteMenu = 0; 
                        setCursor (Cursor.getDefaultCursor ());
                        switchTurn ();
                         selectedPiece = null;
                         CheckingForGameFinishes();
                         repaint();
                         return;
                         }
                         else if (selectedPoint.x >= 460 && selectedPoint.x < 530 && selectedPoint.y >= 280 && selectedPoint.y < 400)
                         {
                                 buttonPress.play();
                         promoting (promotePoint,"Rook");
                         promoteMenu = 0; 
                        setCursor (Cursor.getDefaultCursor ());
                        switchTurn ();
                         selectedPiece = null;
                         CheckingForGameFinishes();
                         repaint();
                         return;
                         } 
                 }

        
                 else
                 {
                        // Respond if a mouse button was pressed over the give up image & quit button
                	 if (selectedPoint.x >= 600 && selectedPoint.x < 750 && selectedPoint.y >= 500 && selectedPoint.y < 550 && title == 1 && helpScreen == 0)
                	 	{
                		 // Resign
                		 	if ( menu == 1 ) 
                		 	{
                		 		buttonPress.play();  
                		 		menu = 2;
                		 		highlight = 0;
                		 		repaint();
                		 		setCursor (Cursor.getDefaultCursor ());
                     
                		 	} 
                		 	else if ( menu == 3 ) 
                		 	{            
                		 		// Quit
                		 		buttonPress.play();
                		 		System.exit(0);
                		 	}
                	 	}
             
                	// Respond if a mouse button was pressed over the undo button or resign or new game
                	 if (selectedPoint.x >= 600 && selectedPoint.x < 750 && selectedPoint.y >= 375 && selectedPoint.y < 425 && title == 1 && helpScreen == 0)
                	 	{
                		 // Undo
                		 if ( menu == 1) 
                		 {                  
                			 buttonPress.play();
                			 undoMove();
                			 promoteMenu = 0;
                			 repaint();                     
                		 } 
                		 // Resign
                		 else if ( menu == 2 ) 
                		 {               
                			 buttonPress.play();
                			 menu = 3;
                			 gameOver = 1;
                			 gameOverSound.play();
                			 repaint();                       
                		 }
                		 // New Game
                		 else if ( menu == 3 ) 
                		 {               
                			 buttonPress.play();
                			 menu = 1;
                			 newGame();                      
                			 repaint();             			 
                		 }
                	 }
             
                         
                	 // Respond if a mouse button was pressed over title start game
                	 if (selectedPoint.x >= 275 && selectedPoint.x < 537 && selectedPoint.y >= 390 && selectedPoint.y < 430 && creditShow == 0 && helpScreen == 0 && title == 0)
                	 	{
	                		 buttonPress.play();
	                		 title = 1;
	                		 titleHighlight = 0;
	                		 newGame();
	                		 repaint();
	                		 setCursor (Cursor.getDefaultCursor ()); // change mouse cursor from a hand to a regular pointer 
                	 	}
          
                        // Respond if a mouse button was pressed over the Help image
                	 if (selectedPoint.x >= 600 && selectedPoint.x < 750 && selectedPoint.y >= 437 && selectedPoint.y < 487 && title == 1)
                	 	{
                		 	if ( helpScreen == 0 && menu == 1)  // if a help screen isn't already displayed
                		 	{
                		 		// Repaint the panel with help screen #1 showing
                		 		buttonPress.play();
                		 		helpScreen = 1;     
                		 		repaint();
                		 		setCursor (Cursor.getDefaultCursor ()); // change mouse cursor from a hand to a regular pointer 
                		 	}  
                		 	else if (helpScreen == 0 && menu == 2)    
                		 	{
                		 		// Go Back
                		 		buttonPress.play();
                		 		menu = 1;
                		 		repaint();                               
                		 	}
                		 	// Respond if a mouse button was pressed over the return to main menu button
                		 	else if (helpScreen == 0 && menu == 3)    
                		 	{
		                         buttonPress.play();
		                         title = 0;
		                         menu = 1;
		                         repaint();
		                         setCursor (Cursor.getDefaultCursor ()); // change mouse cursor from a hand to a regular pointer 
                		 	}
                	 	}
             
                	 // respond to title help
                	 if (selectedPoint.x >= 275 && selectedPoint.x < 537 && selectedPoint.y >= 455 && selectedPoint.y < 495 && title == 0 )
                	 {
                		 if ( helpScreen == 0 && creditShow == 0)  // if a help screen isn't already displayed
                		 {
                			 // Repaint the panel with help screen #1 showing
                			 buttonPress.play();
                			 helpScreen = 1;     
                			 repaint();
                			 setCursor (Cursor.getDefaultCursor ()); // change mouse cursor from a hand to a regular pointer 
                		 }       
                	 }
                	 
                	 // If credit is showing, return to title
                	 if (creditShow == 1 && title == 0 && helpScreen == 0 && selectedPoint.x >= 280 && selectedPoint.x < 513 && selectedPoint.y >= 490 && selectedPoint.y < 570 )
                	 {
                		 buttonPress.play();
                		 creditShow = 0;
                		 creditHighCheck = 0;
                		 repaint();
                	 }
                	 // display credit screen
                	 	else if (selectedPoint.x >= 275 && selectedPoint.x < 537 && selectedPoint.y >= 515 && selectedPoint.y < 555 && title == 0 && helpScreen == 0 && creditShow == 0)
                	 	{              	 		              
                	 		// Repaint the panel with credits showing
                	 		buttonPress.play();
                	 		creditShow = 1;    
                	 		creditHighCheck = 1;
                	 		repaint();      
                	 	}
                 
                	 	// Respond if a mouse button was pressed while mouse was located within the Y coordinates of the buttons on the help screens
                	 if (helpScreen != 0)
                	 {
                		 if (selectedPoint.y >= 35 && selectedPoint.y < 86 && menu == 1  || selectedPoint.y >= 35 && selectedPoint.y < 86 && title == 0 )
                		 {            
                			 // Check for button 1 press
                			 if (helpScreen != 1 && selectedPoint.x >= 517 && selectedPoint.x < 568){
                				 buttonPress.play();
                				 helpScreen = 1;
                			 }
                			 // Check for button 2 press
                			 else if (helpScreen != 2 && selectedPoint.x > 578 && selectedPoint.x <629){
                				 helpScreen = 2;
                				 buttonPress.play();
                			 }
                			 // Check for button 3 press
                			 else if (helpScreen != 3 && selectedPoint.x > 639 && selectedPoint.x <690){
                				 helpScreen = 3;
                				 buttonPress.play();
                			 }
                			 // Check if exit image was pressed in either help screen
                			 else if (helpScreen > 0 && selectedPoint.x >= 700 && selectedPoint.x < 751)
                			 {
                				 buttonPress.play();
                				 helpScreen = 0;  // zero means don't show any help screen     
                				 titleHighlight = 0;
                				 highlight = 0;
                			 }
                		 }
                	 } 
                	 repaint ();
                         
          
                // If the game isn't over
                if (gameOver ==0)
                {                                                      
                	if ( title == 1 && selectedPoint.x>=TOP_LEFT_BOARD && selectedPoint.y>=TOP_LEFT_BOARD &&selectedPoint.x<=TOP_LEFT_BOARD+8*PIXELS_OF_BOX && selectedPoint.y<=TOP_LEFT_BOARD+8*PIXELS_OF_BOX){
                        x = (selectedPoint.x- TOP_LEFT_BOARD) / PIXELS_OF_BOX;
                        y = (selectedPoint.y- TOP_LEFT_BOARD) / PIXELS_OF_BOX;        
                        
                        if (grid [x][y] == null){
                        	return;
                        }
                        
                        xChange =(grid[x][y].x-selectedPoint.x);
                        yChange=(grid[x][y].y-selectedPoint.y);
                 }
                    else
                        return;
    
                 if (grid[x][y].colorOfPiece != turn){
                         return;
                 }
                 
                 if (event.getButton () == MouseEvent.BUTTON1)
                 	{
                	 	selectedPiece = grid [x][y];
                	 	lastPoint = selectedPoint;
                	 	previousPoint = new Point(selectedPiece.x,selectedPiece.y);
                 	}
                 	repaint ();
                 	return;
                	}
                 }
			}

			public void mouseReleased (MouseEvent event)
            {
                    if (promoteMenu == 0){
                    	Point droppedPoint = event.getPoint ();

                    	int xCordToMoveTo = ((droppedPoint.x-Chess.TOP_LEFT_BOARD) / PIXELS_OF_BOX)*PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD;
                    	int yCordToMoveTo = ((droppedPoint.y-Chess.TOP_LEFT_BOARD) / PIXELS_OF_BOX)*PIXELS_OF_BOX+ Chess.TOP_LEFT_BOARD;

                    	Point pointToMoveTo = new Point(xCordToMoveTo,yCordToMoveTo);

                    	if (selectedPiece != null) {
                    		Point currentPosOfClick = new Point(droppedPoint.x+ xChange, droppedPoint.y + yChange);

                    		if(droppedPoint.x < TOP_LEFT_BOARD || droppedPoint.x > TOP_LEFT_BOARD+8*PIXELS_OF_BOX ||droppedPoint.y < TOP_LEFT_BOARD || droppedPoint.y > TOP_LEFT_BOARD+8*PIXELS_OF_BOX){
                    			mouseDropOffBoard(currentPosOfClick);
                    			moveSound.play();
                    			return;
                    		}
                    		
                    		if (selectedPiece.isLegalMove(previousPoint, droppedPoint, grid,true)){
                    			pieceMove (droppedPoint,currentPosOfClick,pointToMoveTo);
                   
                    			if (selectedPiece.EnPassant == true) {
                    				EnPassantCapture (droppedPoint);                   
                    			}

                    			if (selectedPiece.castle > 0) {
                    				castleKing();                   				
                    			}
                    			
                    			if (selectedPiece.promoted == true){                 
                    				promoteMenu = 1; 
                    				promotePoint = droppedPoint; 
                    				selectedPiece.promoted = false;
                    				repaint();
                    				moveSound.play();
                    				return;
                    			}

                    			switchTurn ();
                    			selectedPiece = null;
                    			CheckingForGameFinishes();                   
                    			repaint();
                    			moveSound.play();
                    			return;
                    		}
                    		else
                    		{
                    			selectedPiece.move (currentPosOfClick, previousPoint);
                    			selectedPiece = null;
                    			repaint();
                    			moveSound.play();
                    			return;
                    		}
                    	}
                    } 
            	}
			}
		
		
		private class MouseMotionHandler implements MouseMotionListener {
            public void mouseDragged(MouseEvent event) {
            	Point currentPoint = event.getPoint ();
            	if (promoteMenu == 0){
            		// While dragging a piece, highlight the original position
            		if (selectedPiece != null)
            		{
            			selectedPiece.move (lastPoint, currentPoint);
            			lastPoint = currentPoint;
            			pointHighlight = 1;
            			repaint ();
            		}
            		else 
            			pointHighlight = 0;
            	}
            }

            public void mouseMoved (MouseEvent event)
            	{
                Point pos = event.getPoint (); 
                                                                        
                if (helpScreen == 0)
                {                           
                	if (title == 1 && promoteMenu == 1)
                	{
                		// Highlight the promote options
                			if (pos.x >= 61 && pos.x < 150 && pos.y >= 285 && pos.y < 400)
                			{
                				promoHighlight = 1;
                				setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                			}
                			else if (pos.x >= 195 && pos.x < 270 && pos.y >= 260 && pos.y < 400){
                				promoHighlight = 2;
                				setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                			}
                			else if (pos.x >= 335 && pos.x < 400 && pos.y >= 260 && pos.y < 400){
                				promoHighlight = 3;
                				setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                			}
                			else if (pos.x >= 460 && pos.x < 530 && pos.y >= 280 && pos.y < 400){
                				promoHighlight = 4;
                				setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                			}
                			else
                			{
                				promoHighlight = 0;
                				setCursor (Cursor.getDefaultCursor ());
                			}	
                		
                	}
                	// In-game Menu Highlighting & mouse cursor changing
                	else if (title == 1 && promoteMenu == 0) 
                	{
                		if (pos.x > 600 && pos.x < 750 && pos.y > 375 && pos.y < 425)
                		{
                			highlight = 1;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                		}    
                		else if (pos.x > 600 && pos.x < 750 && pos.y > 437 && pos.y < 487)
                		{    	
                			highlight = 2;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                		}
                		else if (pos.x > 600 && pos.x < 750 && pos.y > 500 && pos.y < 550 && menu != 2)
                		{    
                			highlight = 3;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                		}
                		else
                		{ 
                			highlight = 0;
                			setCursor (Cursor.getDefaultCursor ());  // change mouse cursor to its normal image 
                		}                                                                                    		
                                       
                		// Change the mouse into a hand cursor when mousing over pieces
                		if (gameOver == 0);
                		{
                			if (pos.x>=TOP_LEFT_BOARD && pos.y>=TOP_LEFT_BOARD && pos.x<=TOP_LEFT_BOARD+8*PIXELS_OF_BOX && pos.y<=TOP_LEFT_BOARD+8*PIXELS_OF_BOX)
                			{
                				int x = (pos.x- TOP_LEFT_BOARD) / PIXELS_OF_BOX;
                				int y = (pos.y- TOP_LEFT_BOARD) / PIXELS_OF_BOX;
                				
                				if (x <8 && x > -1 &&y <8 && y > -1){
                					if (grid [x][y] != null && promoteMenu == 0)
                					{
                						if (grid [x][y].colorOfPiece == turn && gameOver != 1)                                                                                                                                                                                                                  
                							setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));                                                                                                                                     
                						else                                            
                							setCursor (Cursor.getDefaultCursor ());
                					}
                				}
                			}
                		}                                           		
                	}                                                                                                                                                                                                                                                  
                	else if (title == 0 && creditShow == 0)
                	{
                		// If mouse is over the "Help" image of the title screen, then highlight/change the "help" image            
                		if (pos.x >= 275 && pos.x < 537 && pos.y >= 390 && pos.y < 430 )
                		{
                			titleHighlight = 1;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
                		}    
                		else if (pos.x >= 275 && pos.x < 537 && pos.y >= 455 && pos.y < 495)
                		{    
                			titleHighlight = 2;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                		}
                		else if (pos.x >= 275 && pos.x < 537 && pos.y >= 515 && pos.y < 555)
                		{    
                			titleHighlight = 3;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                		}
                		else
                		{    
                			titleHighlight = 0;
                			setCursor (Cursor.getDefaultCursor ());  // change mouse cursor to its normal image 
                		}
                	}
                	else if (title == 0 && creditShow == 1){
                		// If mouse is over the "back" image of the title screen, then highlight/change the "back" image            
                		if (pos.x >= 285 && pos.x < 507 && pos.y >= 490 && pos.y < 562 )
                		{
                			creditHighCheck = 1;
                			setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
                		} 
                		else {
                			creditHighCheck = 0;
                			setCursor (Cursor.getDefaultCursor ());
                		}
                	}
                                                     
                }
                else if (helpScreen != 0){
                	                                           
                	// Respond if a mouse button hovers over help screen buttons
                              
                	// Check for the buttons in help screen
                	if (pos.x > 517 && pos.x < 568 && pos.y >= 35 && pos.y < 86 || pos.x > 578 && pos.x < 629 && pos.y >= 35 && pos.y < 86 || pos.x >= 639 && pos.x < 690 && pos.y >= 35 && pos.y < 86 || pos.x >= 700 && pos.x < 751 && pos.y >= 35 && pos.y < 86)
                		setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));                             
                	else                             
                		setCursor (Cursor.getDefaultCursor ());  // change mouse cursor to its normal image                      
                }
                
                if (pos.x > 768 && pos.x < 795 && pos.y > 565 && pos.y < 593)
                        setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));                      
                repaint();
                            
            	}
		}
	}
}
       
	


               

     
                      
                 
                 
                 
                   
             
		
		
	

	
	
        
	

