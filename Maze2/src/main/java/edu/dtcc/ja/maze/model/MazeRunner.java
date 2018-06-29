package edu.dtcc.ja.maze.model;

public class MazeRunner implements Runnable {
	MazeManager theMazeManager;
	public MazeRunner(MazeManager mgr) {
		theMazeManager = mgr;
	}

	public void run() {
		if (theMazeManager != null) {
			System.out.println("Running Maze GO!");
			theMazeManager.go();
		}
	}

}
