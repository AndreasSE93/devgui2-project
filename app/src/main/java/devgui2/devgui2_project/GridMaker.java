package devgui2.devgui2_project;

import java.util.LinkedList;
import java.util.List;

public class GridMaker {
	/**
	 * Generates a set of {@link Piece}s that exactly fills a grid of the given size.
	 * Each block has a maximum size measured in amount of blocks, which is a random number
	 * between minSize and maxSize. A {@link Piece} is not guaranteed to have any minimum size.
	 *
	 * @param gridWidth  The grid's width  measured in grid cells.
	 * @param gridHeight The grid's height measured in grid cells.
	 * @param minSize    The lower bound for the maximum amount of blocks in a {@link Piece}.
	 * @param maxSize    The upper bound for the maximum amount of blocks in a {@link Piece}.
	 * @return An array of {@link Piece}s that exactly fills a grid of the given size.
	 */
	public static Piece[] makeGrid(int gridWidth, int gridHeight, int minSize, int maxSize) {
		// grid[x][y] holds the index of the piece that occupies that cell, or -1 if it's unoccupied
		int grid[][] = new int[gridWidth][gridHeight];
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				grid[x][y] = -1;
			}
		}
		List<Integer> pieceSizes = new LinkedList<>();    // Current amount of blocks in each piece
		List<Integer> pieceMaxSizes = new LinkedList<>(); // Maximum amount of blocks in each piece

		// Generate a solution grid
		emptyCellFinder:
		while (true) {
			// Pick a cell at random
			int x = (int)(Math.random() * gridWidth);
			int y = (int)(Math.random() * gridHeight);

			// If a the picked cell is occupied, go to the next cell
			int origX = x, origY = y;
			while (grid[x][y] != -1) {
				x++;
				if (x == gridWidth) {
					x = 0;
					y++;
					if (y == gridHeight) {
						y = 0;
					}
				}
				if (x == origX && y == origY) {
					// No more empty cells, so the grid has been filled
					break emptyCellFinder;
				}
			}

			// Pick a direction at random
			int dir = (int)(Math.random() * 4);
			int origDir = dir;
			while (true) {
				// Get the coordinates for the neighboring cell in picked direction
				int nX = x, nY = y;
				switch (dir) {
					case 0:
						nX--;
						break;
					case 1:
						nY--;
						break;
					case 2:
						nX++;
						break;
					case 3:
						nY++;
						break;
					default:
						throw new RuntimeException("No dir " + Integer.toString(dir));
				}

				// Make sure the neighboring cell is in the grid
				if (nX >= 0 && nY >= 0 && nX < gridWidth && nY < gridHeight) {
					// Check which Piece is in the neighboring cell
					int nPiece = grid[nX][nY];
					if (nPiece == -1) {
						// Neighboring cell is empty, so create a new piece consisting
						// of the picked cell and the neighboring cell
						nPiece = pieceSizes.size();
						grid[ x][ y] = nPiece;
						grid[nX][nY] = nPiece;
						pieceSizes.add(2);
						pieceMaxSizes.add((int) (Math.random() * (maxSize - minSize + 1)) + minSize);
						continue emptyCellFinder;
					} else if (pieceSizes.get(nPiece) < pieceMaxSizes.get(nPiece)) {
						// The neighboring piece's size can be bigger, so add the picked
						// cell to the neighboring cell's piece and update its size
						grid[x][y] = nPiece;
						pieceSizes.set(nPiece, pieceSizes.get(nPiece) + 1);
						continue emptyCellFinder;
					}
				}

				// The picked the cell could not be joined with
				// the neighboring cell, so try another direction
				dir = (dir + 1) % 4;
				if (dir == origDir) {
					// All directions tried - give up
					break;
				}
			}

			// Could not join the picked cell with any of its neighbors, so create a 1x1 piece
			grid[x][y] = pieceSizes.size();
			pieceSizes.add(1);
			pieceMaxSizes.add(1);
		}

		// Construct Piece objects from the generated solution
		Piece[] pieces = new Piece[pieceSizes.size()];
		for (int i = 0; i < pieces.length; i++) {
			int color = 0xFF000000 | (int)(Math.random() * 0x1000000); // A=0xFF, RGB in set randomly
			int minX = gridWidth  - 1;
			int minY = gridHeight - 1;
			int maxX = 0;
			int maxY = 0;
			// Find piece bounds
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (grid[x][y] == i) {
						if (x < minX) minX = x;
						if (x > maxX) maxX = x;
						if (y < minY) minY = y;
						if (y > maxY) maxY = y;
					}
				}
			}
			int width  = maxX - minX + 1;
			int height = maxY - minY + 1;
			boolean[][] blockMap = new boolean[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					blockMap[x][y] = grid[minX + x][minY + y] == i;
				}
			}
			pieces[i] = new Piece(color, 0, blockMap);
		}
		return pieces;
	}
}
