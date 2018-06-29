package edu.dtcc.ja;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.dtcc.ja.maze.model.Cell;
import edu.dtcc.ja.maze.model.CellTypeEnum;
import edu.dtcc.ja.maze.model.Maze;
import edu.dtcc.ja.maze.model.MazeManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;

public class MazeDrawer extends Application  {

	public static String TheMazeDataFileName;
	MazeManager mazeManager;

	public void load() {
		mazeManager = new MazeManager();
		if (TheMazeDataFileName == null) {
			throw new RuntimeException("You must provide a maze data file name!");
		}
		mazeManager.populate(TheMazeDataFileName);
		
	}

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		load();
		MazePane pane = new MazePane();
		mazeManager.addObserver(pane);
		mazeManager.setDelays(1);
		
		pane.setMazeManager(mazeManager);
		
		Button btn = new Button();
        btn.setText("Start Solving Maze");
        btn.setOnAction(e -> { mazeManager.solveMaze(); System.out.println("Fired button event!"); } );
        
        HBox root = new HBox();
        root.getChildren().add(btn);
        root.getChildren().add(pane);
		
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(root, 800, 800);
		primaryStage.setTitle("Maze"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support.
	 * Not needed for running from the command line.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			TheMazeDataFileName = args[0];
		}
		else {
			throw new RuntimeException("You must provide a maze data file name as the first argument!");
		}
		
		MazeDrawer.launch(MazeDrawer.class, args);
	} 
}



class MazePane extends Pane implements Observer {
	private MazeManager mazeManager;
	public void setMazeManager(MazeManager aMgr) { 
		mazeManager = aMgr;
	}
	
	Point originOffset = new Point(200, 400);
	int pixelsPerCell = 50; 
	
	class MazeRunnable implements Runnable {

		@Override
		public void run() {
			paint();			
		} 
	}
	public MazePane() {
		theRunnable = new MazeRunnable();
	}
	
	
	private void paint() {
	    getChildren().clear();
	    Maze theMaze = mazeManager.getMaze();
	    int rows = theMaze.getNumberOfRows();
	    int cols = theMaze.getNumberOfColumns();
	    
	    CellTypeEnum cellType;
	    for (int i = 0; i < rows; i++) {
	    	for (int j = 0; j < cols; j++) {
	    		drawCell(i, j, theMaze.getCellTypeAt(i,  j));
	    	}
	    }
	    // draw all the cell elements
	    if (pathCellList != null) {
		    for (Cell eachCell : pathCellList) {
		    	drawCircle(eachCell.getY(), eachCell.getX()); 
		    }
	    }
	  }
	
	private void drawCircle(int row, int col) {
		int cellX, cellY;   
		
		cellX = originOffset.x + (col * pixelsPerCell) + (pixelsPerCell/2);
		cellY = originOffset.y + (row * pixelsPerCell) + (pixelsPerCell/2);
		
		Ellipse e1 = new Ellipse(cellX, cellY, 
				pixelsPerCell / 4, pixelsPerCell / 4);
		      e1.setStroke(Color.color(0, 0, 0));
		      e1.setFill(Color.RED); 
		      getChildren().add(e1);
	}
	
	private void drawCell(int row, int col, CellTypeEnum cellType) {
		int cellX, cellY;   
		
		cellX = originOffset.x + (col * pixelsPerCell);
		cellY = originOffset.y + (row * pixelsPerCell);
		
		Rectangle r = new Rectangle(cellX, cellY, pixelsPerCell, pixelsPerCell); 
	      r.setStroke(Color.color(0.9, 0.9, 0.9));
	      r.setFill(null);
	      getChildren().add(r); 
		switch (cellType) { 
			case uninitialized:
				break;
			case outOfBound:
				break;
			case topLeftCorner:
				drawTopLeftCorner(cellX, cellY);
				break;
			case topRightCorner:
				drawTopRightCorner(cellX, cellY);
				break;
			case bottomLeftCorner:
				break;
			case bottomRightCorner:
				break;
			case topWall:
			case bottomWall:
			case horizontalLine:
				drawHoriziontalWall(cellX, cellY);
				break;
			case leftWall:
			case rightWall:
				drawVerticallWall(cellX, cellY);
				break; 
			case empty:
				drawSpace(cellX, cellY);
				break;
			case undefined:
				break;
			default:
				break;
		} 
	}
	
	/***
	 * Draw the cell at the origin specified by x and y.
	 * 
	 * @param x
	 * @param y
	 */
	private void drawTopLeftCorner(int x, int y) {
		int startX, startY, endX, endY;  
		startX = x + (pixelsPerCell / 2);
		startY = y + (pixelsPerCell / 2);
		endX = startX + (pixelsPerCell / 2); 
		drawLine(startX, startY, endX, startY);
		endY = startY + (pixelsPerCell / 2);
		drawLine(startX, startY, startX, endY); 
	}
	
	private void drawLine(int startX, int startY, int endX, int endY) {
		Line line = new Line(startX, startY, endX, endY);
		line.setStrokeWidth(2.0);
		getChildren().add(line);
		Text text2 = new Text(startX, startY, "1");
	    getChildren().add(text2);     
	}
	
	private void drawSpace(int startX, int startY) {
		Text text2 = new Text(startX + (pixelsPerCell / 2), startY + (pixelsPerCell / 2), "0");
	    getChildren().add(text2);     
	}
	
	/***
	 * Draw the cell at the origin specified by x and y.
	 * 
	 * @param x
	 * @param y
	 */
	private void drawTopRightCorner(int x, int y) {
		int startX, startY, endX, endY;  
		startX = x;
		startY = y + (pixelsPerCell / 2);
		endX = startX + (pixelsPerCell / 2);
		drawLine(startX, startY, endX, startY); 
		startX = endX;
		endY = startY + (pixelsPerCell / 2); 
		drawLine(startX, startY, endX, endY);
	} 
	
	/***
	 * Draw the cell at the origin specified by x and y.
	 * 
	 * @param x
	 * @param y
	 */
	private void drawHoriziontalWall(int x, int y) {
		int startX, startY, endX, endY;  
		startX = x;
		startY = y + (pixelsPerCell / 2);
		endX = startX + pixelsPerCell;
		drawLine(startX, startY, endX, startY); 
	}
	
	/***
	 * Draw the cell at the origin specified by x and y.
	 * 
	 * @param x
	 * @param y
	 */
	private void drawVerticallWall(int x, int y) {
		int startX, startY, endX, endY;  
		startX = x + (pixelsPerCell / 2);
		startY = y;
		endY = startY + pixelsPerCell;
		drawLine(startX, startY, startX, endY); 
	}
	
	@Override
	  public void setWidth(double width) {
	    super.setWidth(width);
	    paint();
	  }
	  
	  @Override
	  public void setHeight(double height) {
	    super.setHeight(height);
	    paint();
	  }

	List<Cell> pathCellList; 
	Runnable theRunnable;
	  
	public void update(Observable o, Object arg) {
		if (o instanceof MazeManager) {
			MazeManager theManager = (MazeManager) o; 
			pathCellList = new ArrayList<Cell>();
			pathCellList.addAll(theManager.getTraversedList());
			Platform.runLater(theRunnable);
		}
	}
}
