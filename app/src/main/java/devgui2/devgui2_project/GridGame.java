package devgui2.devgui2_project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;


public class GridGame extends Activity  {
	int displayWidth;
	int displayHeight;
    float x,y;

	Piece[] pieces;
	Integer canvasSavedWidth, canvasSavedHeight;




	@Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridgame);
        final DrawView drawView = new DrawView(this, getIntent().getExtras());
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
				    xMargin = (canvasWidth - canvasHeight) >> 1;;
				    yMargin = 0;
			    }

			    final float gridRatio = (float)gridWidth / (float)gridHeight;
			    final float gridAreaWidth = (float)canvasWidth - 2.0f * xMargin;
			    final float gridAreaHeight = (float)canvasHeight - 2 * yMargin;
			    float gridX1, gridY1, gridX2, gridY2, blockLength;
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
		bundle.putInt("canvasWidth",  canvasSavedWidth);
		bundle.putInt("canvasHeight", canvasSavedHeight);
	}
}
