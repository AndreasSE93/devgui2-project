package devgui2.devgui2_project;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andreas on 2015-05-29.
 */
public class GridMaker {
	public static Piece[] makeGrid(int gridWidth, int gridHeight, int minSize, int maxSize) {
		int grid[][] = new int[gridWidth][gridHeight];
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				grid[x][y] = -1;
			}
		}
		List<Integer> pieceSizes = new LinkedList<Integer>();
		List<Integer> pieceMaxSizes = new LinkedList<Integer>();
		emptyCellFinder:
		while (true) {
			int x = (int)(Math.random() * gridWidth);
			int y = (int)(Math.random() * gridHeight);
			//android.util.Log.d("GridMaker", "x=" + Integer.toString(x) + ", y=" + Integer.toString(y));
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
				//android.util.Log.d("GridMaker", "Occupied... x=" + Integer.toString(x) + ", y=" + Integer.toString(y));
				if (x == origX && y == origY) {
					//android.util.Log.d("GridMaker", "No more empty spaces");
					break emptyCellFinder;
				}
			}
			int dir = (int)(Math.random() * 4);
			int origDir = dir;
			while (true) {
				int nX = x, nY = y;  // Neighboring piece coordinates
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
				//android.util.Log.d("GridMaker", "x=" + Integer.toString(x) + ", y=" + Integer.toString(y) + ", nX=" + Integer.toString(nX) + ", nY=" + Integer.toString(nY) + ", dir=" + Integer.toString(dir) + ", origDir=" + Integer.toString(origDir));
				if (nX >= 0 && nY >= 0 && nX < gridWidth && nY < gridHeight) {
					int nPiece = grid[nX][nY];
					if (nPiece == -1) {
						nPiece = pieceSizes.size();
						grid[ x][ y] = nPiece;
						grid[nX][nY] = nPiece;
						pieceSizes.add(2);
						pieceMaxSizes.add((int) (Math.random() * (maxSize - minSize + 1)) + minSize);
						//android.util.Log.d("GridMaker", "New block: maxSize=" + pieceMaxSizes.get(pieceMaxSizes.size()-1).toString());
						continue emptyCellFinder;
					} else if (pieceSizes.get(nPiece) < pieceMaxSizes.get(nPiece)) {
						grid[x][y] = nPiece;
						pieceSizes.set(nPiece, pieceSizes.get(nPiece) + 1);
						continue emptyCellFinder;
					}
				} else {
					//android.util.Log.d("GridMaker", "Neighbor outside grid: nX=" + Integer.toString(nX) + ", xY=" + Integer.toString(nY));
				}
				dir = (dir + 1) % 4;
				if (dir == origDir) {
					break;
				}
			}
			android.util.Log.d("GridMaker", "Block can't join with any neighbor: (" + Integer.toString(x) + ", " + Integer.toString(y) + ")");
			grid[x][y] = pieceSizes.size();
			pieceSizes.add(1);
			pieceMaxSizes.add(1);
		}
		Piece[] pieces = new Piece[pieceSizes.size()];
		for (int i = 0; i < pieces.length; i++) {
			int color = 0xFF000000 | (int)(Math.random() * 0x1000000); // A=0xFF, RGB in set randomly
			int minX = gridWidth  - 1;
			int minY = gridHeight - 1;
			int maxX = 0;
			int maxY = 0;
			for (int x = 0; x < gridWidth; x++) {
				// Find piece bounds
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
			//android.util.Log.d("GridMaker", Integer.toString(minX) + " " + Integer.toString(maxX) + " " + Integer.toString(minY) + " " + Integer.toString(maxY));
			android.util.Log.d("GridMaker", "Block " + Integer.toString(i) + ": " + Arrays.deepToString(blockMap));
			pieces[i] = new Piece(color, 0, blockMap);
		}
		android.util.Log.i("GridMaker", "Grid done (" + Integer.toString(pieces.length) + " Pieces)");
		android.util.Log.i("GridMaker", "Grid: " + Arrays.deepToString(grid));
		android.util.Log.d("GridMaker", "Block sizes:     " + Arrays.toString(pieceSizes.toArray()));
		android.util.Log.d("GridMaker", "Block max sizes: " + Arrays.toString(pieceMaxSizes.toArray()));
		return pieces;
	}
}
