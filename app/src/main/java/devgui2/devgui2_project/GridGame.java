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
import android.widget.LinearLayout;
import static android.graphics.Color.*;
import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;
import static android.view.View.getDefaultSize;

import android.content.Context;
import android.view.View;

public class GridGame extends Activity {

    int displayWidth;
    int displayHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridgame);
        DrawView drawView = new DrawView(this, getIntent().getExtras());
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
        float blockLength = drawView.getBlockLength();
        Boolean[][] shape = new Boolean[2][3];
        shape[0][0] = true;
        shape[0][1] = true;
        shape[0][2] = true;
        shape[1][0] = false;
        shape[1][1] = false;
        shape[1][2] = true;
        Piece piece = new Piece(Color.RED, 0, shape);
        piece.setX(50);
        piece.setY(50);
        Piece[] pieces = new Piece[1];
        pieces[0] = piece;
        drawView.setPieces(pieces);
        drawView.invalidate();
    }
}
