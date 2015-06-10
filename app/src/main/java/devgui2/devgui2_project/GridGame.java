package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import java.util.Arrays;

public class GridGame extends Activity {
	private Piece[] pieces;
	private int gridWidth, gridHeight;
	private boolean[][] grid;  // grid[x][y] keeps track of what cells are occupied

	// Tracked for win screen and, maybe one day, high-score keeping
	private long startTime;
	private int moves = 0;

	// For onSaveInstanceState
	private Integer canvasWidth, canvasHeight;

	// For moving and rotation of Pieces
	private boolean touchPointDown[] = new boolean[10]; // Keeps track of which touch points are being held down
	private float touchPointStartX[] = new float[10];   // Initial screen coordinates for touch point
	private float touchPointStartY[] = new float[10];
	private float touchPointX[] = new float[10];        // Current screen coordinates for touch point
	private float touchPointY[] = new float[10];
	private float touchPieceStartX;                     // Initial screen coordinates for the moving Piece's origin (top-left corner)
	private float touchPieceStartY;
	private double touchPointStartRot;                  // Initial direction from first touch point to second touch point
	private double touchPieceStartRot;                  // Initial rotation of the moving Piece
	private int touchMovingPiece;                       // Index in pieces[] for Piece that is currently being moved

	private float gridX1, gridY1;  // Coordinates for the grid's top-left     corner
    private float gridX2, gridY2;  // Coordinates for the grid's bottom-right corner
    private float blockLength;

	private boolean musicEnabled;
	private MediaPlayer musicPlayer;

	@Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		musicEnabled = sharedPrefs.getBoolean("pref_sound_music", true);
		if (musicEnabled) {
			musicPlayer = MediaPlayer.create(this, R.raw.bgmloop);
			musicPlayer.setLooping(true);
			musicPlayer.start();
		}

        // Create a DrawView and display it
        final DrawView drawView = new DrawView(this);
		/*
		drawView.pointColor[0] = 0xDDFFFFFF;
		drawView.pointColor[1] = 0xDDFF0000;
		drawView.pointColor[2] = 0xDD00FF00;
		drawView.pointColor[3] = 0xDD0000FF;
		*/
        drawView.setBackgroundColor(0xA0000000); // 75% opaque, black background - the system's default background gradient will shine through
        setContentView(drawView);

	    this.gridWidth         = getIntent().getIntExtra("gridWidth",    5);
	    this.gridHeight        = getIntent().getIntExtra("gridHeight",   5);
	    final int blockMinSize = getIntent().getIntExtra("blockMinSize", 3);
	    final int blockMaxSize = getIntent().getIntExtra("blockMaxSize", 3);

        // If there is a saved state, restore it - create a new game otherwise
	    if (savedInstanceState != null) {
            // Arrays are saved as Serializable Object arrays, but Object arrays can't be type
            // casted, but casting can be done while using Arrays.copyOf instead.
            Object[] pieces = (Object[])savedInstanceState.getSerializable("pieces");
            this.pieces = Arrays.copyOf(pieces, pieces.length, Piece[].class);
            Object[] grid = (Object[])savedInstanceState.getSerializable("grid");
            this.grid   = Arrays.copyOf(grid,   grid.length,   boolean[][].class);
	    } else {
		    this.pieces = GridMaker.makeGrid(gridWidth, gridHeight, blockMinSize, blockMaxSize);
		    this.grid = new boolean[gridWidth][gridHeight];
	    }

	    // Wait for the layout to be ready, so we can get its size
	    drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
			    drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			    canvasWidth  = drawView.getWidth();
			    canvasHeight = drawView.getHeight();
			    final float aspectRatio = (float)canvasWidth / (float)canvasHeight;
			    float xMargin, yMargin;  // Pixels between edges of the screen and the grid
			    if (aspectRatio > 0.5 && aspectRatio < 2.0) {
				    if (aspectRatio < 1) {
					    xMargin = canvasWidth / 4.0f * aspectRatio;
					    yMargin = (canvasHeight - (canvasWidth - xMargin * 2.0f)) / 2.0f;
				    } else if(aspectRatio > 1.0f) {
					    float aspect = 1.0f / aspectRatio;
					    yMargin = canvasHeight / 4.0f * aspect;
					    xMargin = (canvasWidth - (canvasHeight - yMargin * 2.0f)) / 2.0f;
				    } else {
					    xMargin = canvasHeight - canvasHeight / 4.0f;
					    yMargin = xMargin;
				    }
			    } else if (aspectRatio <= 0.5) {
				    xMargin = 0;
				    yMargin = (canvasHeight - canvasWidth) >> 1;
			    } else {
				    xMargin = (canvasWidth - canvasHeight) >> 1;
				    yMargin = 0;
			    }

			    final float gridRatio = (float)gridWidth / (float)gridHeight;
			    final float gridAreaWidth = (float)canvasWidth - 2.0f * xMargin;
			    final float gridAreaHeight = (float)canvasHeight - 2 * yMargin;
			    if (gridRatio > 1) {
				    //Wide
				    blockLength = gridAreaWidth / gridWidth;
				    gridX1 = xMargin;
				    gridY1 = (canvasHeight / 2.0f) - (blockLength * gridHeight / 2.0f);
				    gridX2 = canvasWidth - xMargin;
				    gridY2 = gridY1 + blockLength * gridHeight;
			    } else if (gridRatio < 1.0f) {
				    //Tall
				    blockLength = gridAreaHeight / gridHeight;
				    gridX1 = (canvasWidth / 2.0f) - (blockLength * gridWidth / 2.0f);
				    gridY1 = yMargin;
				    gridX2 = gridX1 + blockLength * gridWidth;
				    gridY2 = canvasHeight - yMargin;
			    } else {
				    blockLength = gridAreaWidth / gridWidth;
				    gridX1 = xMargin;
				    gridY1 = yMargin;
				    gridX2 = canvasWidth - xMargin;
				    gridY2 = canvasHeight - yMargin;
			    }

                for (Piece piece : pieces) {
                    piece.initBitmap(blockLength);
                    if (savedInstanceState != null) {
                        // Restore old positions and scale to new screen size
                        // (Screen size shouldn't change anymore though, as the screen orientation is locked)
                        // (If screen size does change, Pieces that are snapped to grid will not be positioned correctly)
                        final int oldWidth = savedInstanceState.getInt("canvasWidth");
                        final int oldHeight = savedInstanceState.getInt("canvasHeight");
                        piece.setX((int) (((float) (piece.getX())) / oldWidth * canvasWidth));
                        piece.setY((int) (((float) (piece.getY())) / oldHeight * canvasHeight));
                    } else {
                        // No previous state, so scatter the Pieces randomly
                        piece.setX((int) (Math.random() * (canvasWidth - piece.getBitmap().getWidth())));
                        piece.setY((int) (Math.random() * (canvasHeight - piece.getBitmap().getHeight())));
                    }
                }
			    drawView.init(gridX1, gridY1, gridX2, gridY2, gridWidth, gridHeight, blockLength);
			    drawView.setPieces(pieces);

				// The game has now begun, so save the start time so we
				// can calculate the time it took to finish the game
			    startTime = System.currentTimeMillis();
		    }

	    });
    }

	// Will be called when the activity stops, in case it has to be killed later to free resources
	@Override
	public void onSaveInstanceState(@NonNull Bundle bundle) {
		bundle.putSerializable("pieces", this.pieces);
		bundle.putSerializable("grid", this.grid);
		bundle.putInt("canvasWidth",  canvasWidth);
		bundle.putInt("canvasHeight", canvasHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int point = event.getActionIndex();
		float x = event.getX(point);
		float y = event.getY(point);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:         // First touch point down
			case MotionEvent.ACTION_POINTER_DOWN: // Additional touch point down
				touchPointDown[point] = true;
				touchPointStartX[point] = touchPointX[point] = x;
				touchPointStartY[point] = touchPointY[point] = y;
				Log.d("GridGame", "Touch down: " + Integer.toString(point));
				if (point == 0) {
					touchMovingPiece = -1;
					// Loop through Pieces backwards, since the last Piece will be rendered last and therefore be on top
					findTouchedPiece:
					for (int i = pieces.length - 1; i >= 0; i--) {
						float px = pieces[i].getX();
						float py = pieces[i].getY();
						/*if (i == 0) {
							android.util.Log.i("Original x: ", ((Float)x).toString());
							android.util.Log.i("Original y: ", ((Float)y).toString());
						}*/
						boolean[][] shape = pieces[i].getShape();
						Bitmap bitmap = pieces[i].getBitmap();

						// Transform touch point according to the Piece's rotation
						float newpx = px + (bitmap.getWidth()/2);
						float newpy = py + (bitmap.getHeight()/2);
						float relx = newpx - touchPointX[point];
						float rely = newpy - touchPointY[point];
						double r = Math.sqrt(relx*relx + rely*rely);
						double theta = Math.atan((rely / 2.0) / (relx / 2.0));
						x = (float)(r*Math.cos(theta/*-Math.toRadians(pieces[i].getRot())*/));
						y = (float)(r*Math.sin(theta/*-Math.toRadians(pieces[i].getRot())*/));
						x = x+newpx;
						y = y+newpy;
						/*if(i == 0) {
							android.util.Log.i("px: ", ((Float)px).toString());
							android.util.Log.i("py: ", ((Float)py).toString());
							android.util.Log.i("newpx: ", ((Float)newpx).toString());
							android.util.Log.i("newpy: ", ((Float)newpy).toString());
							android.util.Log.i("touchPointX[point]: ", ((Float)(touchPointX[point])).toString());
							android.util.Log.i("touchPointY[point]: ", ((Float)(touchPointY[point])).toString());
							android.util.Log.i("r: ", ((Double)r).toString());
							android.util.Log.i("theta: ", ((Double)theta).toString());
							android.util.Log.i("bloclLength: ", ((Float)blockLength).toString());
							android.util.Log.i("relx: ", ((Float)relx).toString());
							android.util.Log.i("rely: ", ((Float)rely).toString());
							android.util.Log.i("x: ", ((Float)x).toString());
							android.util.Log.i("y: ", ((Float)y).toString());
							drawView.setCircle1(x, y);
							drawView.setCircle2(newpx, newpy);
						}*/

						// Loop through each block of the Piece
						for (int bx = 0; bx < shape.length; bx++) {
							for (int by = 0; by < shape[bx].length; by++) {
								if (shape[bx][by] &&
										x >= px + bx * blockLength && x <= px + (bx + 1) * blockLength &&
										y >= py + by * blockLength && y <= py + (by + 1) * blockLength) {
									touchMovingPiece = i;
									touchPieceStartX = px;
									touchPieceStartY = py;
									//Log.d("GridGame", "Piece " + Integer.toString(touchMovingPiece[point]) + " touched with point " + Integer.toString(point) +
									//		" x=" + Float.toString(x) + " px=" + Float.toString(px) + " bx=" + Integer.toString(bx) + " by=" + Integer.toString(by));
									break findTouchedPiece;
								}
							}
						}
					}
					// If a snapped Piece was touched, unsnap it
					if (touchMovingPiece != -1 && pieces[touchMovingPiece].isSnapped()) {
						pieces[touchMovingPiece].setSnapped(false);
						pieces[touchMovingPiece].setSnapping(false);
						int gridX = (int) ((pieces[touchMovingPiece].getX() - gridX1) / blockLength + 0.5f);
						int gridY = (int) ((pieces[touchMovingPiece].getY() - gridY1) / blockLength + 0.5f);
						boolean[][] shape = pieces[touchMovingPiece].getShape();
						// Free the grid cells so that blocks can snap to them again
						for (int bx = 0; bx < shape.length; bx++) {
							for (int by = 0; by < shape[bx].length; by++) {
								if (shape[bx][by]) {
									grid[gridX + bx][gridY + by] = false;
								}
							}
						}
					}
				} else if (point == 1 && touchMovingPiece != -1) {
					touchPieceStartRot = pieces[touchMovingPiece].getRot();
					touchPointStartRot = Math.atan2(touchPointX[0] - x, y - touchPointY[0]);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				// Update touch point coordinates
				for (int i = 0; i < event.getPointerCount(); i++) {
					touchPointX[i] = event.getX(i);
					touchPointY[i] = event.getY(i);
				}

				// First touch point moves the Piece
				if (touchPointDown[0] && touchMovingPiece != -1) {
					// Calculate new Piece position
					float px = (touchPieceStartX + x - touchPointStartX[point]);
					float py = (touchPieceStartY + y - touchPointStartY[point]);

					// Get Piece position in grid coordinates, rounded to the closest cell
					int gridX = (int)((px - gridX1) / blockLength + 0.5f);
					int gridY = (int)((py - gridY1) / blockLength + 0.5f);

					boolean[][] shape = pieces[touchMovingPiece].getShape();

					// Constrain grid coordinates to grid size, taking tint account the size of the Piece
					if      (gridX <  0                           ) gridX = 0;
					else if (gridX >  gridWidth -  shape   .length) gridX = gridWidth  - shape   .length;
					if      (gridY <  0                           ) gridY = 0;
					else if (gridY >= gridHeight - shape[0].length) gridY = gridHeight - shape[0].length;

					// Screen coordinates after snapping to grid
					float snappedX = gridX1 + gridX * blockLength;
					float snappedY = gridY1 + gridY * blockLength;
					//drawView.pointX[0] = snappedX;
					//drawView.pointY[0] = snappedY;

					pieces[touchMovingPiece].setSnapping(false);
					// If the new position is close to the position after snapping, try to snap
					if (Math.abs(Math.sqrt(Math.pow(snappedX - px, 2) + Math.pow(snappedY - py, 2))) < 75.0) {
						boolean overlapping = false;
						overlapFinder:
						for (int bx = 0; bx < shape.length; bx++) {
							for (int by = 0; by < shape[bx].length; by++) {
								if (shape[bx][by] && grid[gridX + bx][gridY + by]) {
									overlapping = true;
									break overlapFinder;
								}
							}
						}
						// Snap if the cells aren't already occupied
						if (!overlapping) {
							pieces[touchMovingPiece].setSnapping(true);
							px = snappedX;
							py = snappedY;
						}
					}

					pieces[touchMovingPiece].setX((int) px);
					pieces[touchMovingPiece].setY((int) py);
				}
				// Second touch point rotates the Piece
				if (touchPointDown[1] && touchMovingPiece != -1) {
					double rot = (Math.atan2(touchPointX[0] - touchPointX[1], touchPointY[1] - touchPointY[0]));
					double newRot = Math.toDegrees(touchPieceStartRot + rot - touchPointStartRot) % 360.0;
					pieces[touchMovingPiece].setRot((float)newRot);
				}
				break;
			case MotionEvent.ACTION_UP: // Last touch point released
				for (int i = 0; i < touchPointDown.length; i++) {
					touchPointDown[i] = false;
				}
				// Fall-through to finalize drop
			case MotionEvent.ACTION_POINTER_UP: // A touch point was released, but there are more remaining
				touchPointDown[point] = false;
				if (!touchPointDown[0] && touchMovingPiece != -1 && pieces[touchMovingPiece].isSnapping()) {
					// First touch point was released and the Piece can be snapped, so snap it
					pieces[touchMovingPiece].setSnapped(true);
					int gridX = (int)((pieces[touchMovingPiece].getX() - gridX1) / blockLength + 0.5f);
					int gridY = (int)((pieces[touchMovingPiece].getY() - gridY1) / blockLength + 0.5f);
					boolean[][] shape = pieces[touchMovingPiece].getShape();
					// Mark the grid cells as occupied
					for (int bx = 0; bx < shape.length; bx++) {
						for (int by = 0; by < shape[bx].length; by++) {
							if (shape[bx][by]) {
								grid[gridX + bx][gridY + by] = true;
							}
						}
					}
					moves++;
					checkWin();
				}
				break;
		}
		return false;
	}

	private void checkWin() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				if (!grid[x][y]) {
					// A cell is unoccupied, so the player hasn't won yet
					return;
				}
			}
		}
		// No unoccupied cells found, so open WinScreen
		Intent intent = new Intent(this, WinScreen.class);
		intent.putExtra("pieceCount", pieces.length);
		intent.putExtra("moveCount",  moves);
		intent.putExtra("solveTime",  System.currentTimeMillis() - startTime);
		startActivityForResult(intent, R.integer.WIN_SCREEN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R.integer.WIN_SCREEN_REQUEST) {
            if (resultCode == R.integer.RESTART_GAME) {
                Intent intent = new Intent(this, this.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(getIntent());
                startActivity(intent);
            } else {
                finish();
            }
        }
    }

    @Override
	protected void onPause() {
		super.onPause();
		if (musicEnabled) {
			musicPlayer.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (musicEnabled) {
			musicPlayer.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onStop();
		if (musicEnabled) {
			musicPlayer.stop();
			musicPlayer.release();
		}
	}
}
