package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

public class GridGame extends Activity {

	int canvasWidth;
	int canvasHeight;
	Piece[] pieces;
	Integer canvasSavedWidth, canvasSavedHeight;
	boolean touchPointDown[] = new boolean[10];
	float touchPointStartX[] = new float[10];
	float touchPointStartY[] = new float[10];
	float touchPointX[] = new float[10];
	float touchPointY[] = new float[10];
	float touchPieceStartX;
	float touchPieceStartY;
	double touchPointStartRot;
	double touchPieceStartRot;
	int touchMovingPiece;
	float gridX1, gridY1, gridX2, gridY2, blockLength;
	DrawView drawView;

	boolean sfx;
	boolean bgm;
	MediaPlayer musicPlayer;
	MediaPlayer winPlayer;

	@Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		sfx = sharedPrefs.getBoolean("pref_sound_fx", true);
		bgm = sharedPrefs.getBoolean("pref_sound_music", true);
		if (bgm == true) {
			musicPlayer = MediaPlayer.create(this, R.raw.bgmloop);
			musicPlayer.setLooping(true);
			musicPlayer.start();
		}
		if (sfx == true) {
			winPlayer = MediaPlayer.create(this, R.raw.sfxwin);
		}
        setContentView(R.layout.activity_gridgame);
        final DrawView drawView = new DrawView(this, getIntent().getExtras());
		this.drawView = drawView;
		drawView.pointColor[0] = 0xDDFFFFFF;
		drawView.pointColor[1] = 0xDDFF0000;
		drawView.pointColor[2] = 0xDD00FF00;
		drawView.pointColor[3] = 0xDD0000FF;
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
	    final int gridWidth    = getIntent().getIntExtra("gridWidth",    5);
	    final int gridHeight   = getIntent().getIntExtra("gridHeight",   5);
	    final int blockMinSize = getIntent().getIntExtra("blockMinSize", 3);
	    final int blockMaxSize = getIntent().getIntExtra("blockMaxSize", 3);
	    if (savedInstanceState != null) {
		    this.pieces = (Piece[])savedInstanceState.getSerializable("pieces");
	    } else {
		    this.pieces = GridMaker.makeGrid(gridWidth, gridHeight, blockMinSize, blockMaxSize);
	    }
	    final Piece[] pieces = this.pieces;
	    drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
			    drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			    final int canvasWidth  = drawView.getWidth();
			    final int canvasHeight = drawView.getHeight();
			    canvasSavedWidth  = canvasWidth;
			    canvasSavedHeight = canvasHeight;
			    final float aspectRatio = (float)canvasWidth / (float)canvasHeight;
			    float xMargin;
			    float yMargin;
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

			    for (int i = 0; i < pieces.length; i++) {
				    pieces[i].initBitmap(blockLength);
				    if (savedInstanceState != null) {
					    final int oldWidth  = savedInstanceState.getInt("canvasWidth");
					    final int oldHeight = savedInstanceState.getInt("canvasHeight");
					    pieces[i].setX((int)(((float)(pieces[i].getX())) / oldWidth  * canvasWidth));
					    pieces[i].setY((int)(((float)(pieces[i].getY())) / oldHeight * canvasHeight));
				    } else {
					    pieces[i].setX((int) (Math.random() * (canvasWidth - pieces[i].getBitmap().getWidth())));
					    pieces[i].setY((int) (Math.random() * (canvasHeight - pieces[i].getBitmap().getHeight())));
				    }
			    }
			    drawView.init(gridX1, gridY1, gridX2, gridY2, gridWidth, gridHeight, blockLength);
			    drawView.setPieces(pieces);
		    }
	    });
    }

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		bundle.putSerializable("pieces", this.pieces);
		bundle.putInt("canvasWidth", canvasSavedWidth);
		bundle.putInt("canvasHeight", canvasSavedHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int point = event.getActionIndex();
		float x = event.getX(point);
		float y = event.getY(point);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchPointDown[point] = true;
				touchPointStartX[point] = touchPointX[point] = x;
				touchPointStartY[point] = touchPointY[point] = y;
				Log.d("GridGame", "Touch down: " + Integer.toString(point));
				if (point == 0) {
					touchMovingPiece = -1;
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
						float newpx = px + (bitmap.getWidth()/2);
						float newpy = py + (bitmap.getHeight()/2);
						float relx = newpx - touchPointX[point];
						float rely = newpy - touchPointY[point];
						double r = Math.sqrt(relx*relx + rely*rely);
						double theta = Math.atan(((rely) / 2.0) / ((relx) / 2.0));
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
				} else if (point == 1 && touchMovingPiece !=-1) {
					touchPieceStartRot = pieces[touchMovingPiece].getRot();
					touchPointStartRot = Math.atan2(touchPointX[0] - x, y - touchPointY[0]);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				for (int i = 0; i < event.getPointerCount(); i++) {
					touchPointX[i] = event.getX(i);
					touchPointY[i] = event.getY(i);
				}
				if (touchPointDown[0] && touchMovingPiece != -1) {
					pieces[touchMovingPiece].setX((int) (touchPieceStartX + x - touchPointStartX[point]));
					pieces[touchMovingPiece].setY((int) (touchPieceStartY + y - touchPointStartY[point]));
				}
				if (touchPointDown[1] && touchMovingPiece != -1) {
					double rot = (Math.atan2(touchPointX[0] - touchPointX[1], touchPointY[1] - touchPointY[0]));
					double newRot = Math.toDegrees(touchPieceStartRot + rot - touchPointStartRot) % 360.0;
					pieces[touchMovingPiece].setRot((float)newRot);
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				Log.d("GridGame", "Touch up: " + Integer.toString(point));
				touchPointDown[point] = false;
				//touchPieceStartRot = pieces[touchMovingPiece].getRot();
				break;
			case MotionEvent.ACTION_UP:
				for (int i = 0; i < touchPointDown.length; i++) {
					touchPointDown[i] = false;
				}
				break;
		}
		for (int i = 0; i < event.getPointerCount(); i++) {
			drawView.pointX[i] = (int)touchPointX[i];
			drawView.pointY[i] = (int)touchPointY[i];
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (bgm == true) {
			musicPlayer.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (bgm == true) {
			musicPlayer.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onStop();
		if (bgm == true) {
			musicPlayer.stop();
		}
	}
}