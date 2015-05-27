package devgui2.devgui2_project;

/**
 * Created by redhotsmasher on 2015-05-27.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class DrawView extends View
{
    Bundle bundle;
    Paint paint = new Paint();
    int gridWidth;
    int gridHeight;
    int blockMinSize;
    int blockMaxSize;

    public DrawView(Context context)
    {
        super(context);
        paint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawRect(30, 30, 100, 100, paint);
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        gridWidth = bundle.getInt("gridWidth");
        gridHeight = bundle.getInt("gridHeight");
        blockMinSize = bundle.getInt("blockMinSize");
        blockMaxSize = bundle.getInt("blockMaxSize");
    }
}
