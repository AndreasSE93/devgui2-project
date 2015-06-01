package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import static android.graphics.Color.*;
import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;
import static android.view.View.getDefaultSize;

import android.content.Context;
import android.view.View;

public class GridGame extends Activity {

	int canvasWidth;
	int canvasHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridgame);
        final DrawView drawView = new DrawView(this, getIntent().getExtras());
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
        float blockLength = drawView.getBlockLength();
	    int gridWidth    = getIntent().getIntExtra("gridWidth",    5);
	    int gridHeight   = getIntent().getIntExtra("gridHeight",   5);
	    int blockMinSize = getIntent().getIntExtra("blockMinSize", 3);
	    int blockMaxSize = getIntent().getIntExtra("blockMaxSize", 3);
	    final Piece[] pieces = GridMaker.makeGrid(gridWidth, gridHeight, blockMinSize, blockMaxSize);
	    drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
			    drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			    canvasWidth  = drawView.getWidth();
			    canvasHeight = drawView.getHeight();
			    for (int i = 0; i < pieces.length; i++) {
				    pieces[i].setX((int) (Math.random() * canvasWidth));
				    pieces[i].setY((int) (Math.random() * canvasHeight));
			    }
			    drawView.setPieces(pieces);
			    drawView.invalidate();
		    }
	    });
    }
}
