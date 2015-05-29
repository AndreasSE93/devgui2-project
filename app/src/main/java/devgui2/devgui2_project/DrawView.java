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

    public DrawView(Context context)
    {
        super(context);
        paint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        int drawWidth = this.getWidth();
        int drawHeight = this.getHeight();
        float aspectRatio = (float)drawWidth/(float)drawHeight;
        float xMargin;
        float yMargin;
        if (aspectRatio > 0.5 && aspectRatio < 2.0) {
            if (aspectRatio < 1) {
                xMargin = ((float)drawWidth/(float)4 * (aspectRatio));
                yMargin = ((float)drawHeight - ((float)drawWidth - xMargin*2))/(float)2;
            } else if(aspectRatio > 1) {
                float aspect = 1/aspectRatio;
                yMargin = ((float)drawHeight/(float)4 * (aspect));
                xMargin = ((float)drawWidth - ((float)drawHeight - yMargin*2))/(float)2;
            } else {
                xMargin = (float)drawHeight - ((float)drawHeight/(float)4);
                yMargin = xMargin;
            }
        } else if (aspectRatio <= 0.5) {
            xMargin = 0;
            yMargin = (drawHeight - drawWidth) >> 1;
        } else {
            xMargin = (drawWidth - drawHeight) >> 1;;
            yMargin = 0;
        }

        float startX;
        float startY;
        float endX;
        float endY;

        float gridRatio = (float)gridWidth/(float)gridHeight;
        float gridAreaWidth = (float)drawWidth - 2*xMargin;
        float gridAreaHeight = (float)drawHeight - 2*yMargin;
        float gridX;
        float gridY;
        float gridX2;
        float gridY2;
        float blockLength;
        if (gridRatio > 1) {
            //Wide
            blockLength = gridAreaWidth/(float)gridWidth;
            gridX = xMargin;
            gridY = ((float)drawHeight/(float)2)-(blockLength*gridHeight/(float)2);
            gridX2 = drawWidth - xMargin;
            gridY2 = gridY + blockLength * gridHeight;
        } else if (gridRatio < 1) {
            //Tall
            blockLength = gridAreaHeight/(float)gridHeight;
            gridX = ((float)drawWidth/(float)2)-(blockLength*gridWidth/(float)2);
            gridY = yMargin;
            gridX2 = gridX + blockLength * gridWidth;
            gridY2 = drawHeight - yMargin;
        } else {
            blockLength = gridAreaWidth / (float) gridWidth;
            gridX = xMargin;
            gridY = yMargin;
            gridX2 = drawWidth - xMargin;
            gridY2 = drawHeight - yMargin;
        }

        for (int i = 0; i <= gridWidth; i++) { //Draw longitudinal lines
            float offset = i*blockLength;
            canvas.drawLine(gridX+offset, gridY, gridX+offset, gridY2, paint);
        }

        for (int i = 0; i <= gridHeight; i++) { //Draw latitudinal lines
            float offset = i*blockLength;
            canvas.drawLine(gridX, gridY+offset, gridX2, gridY+offset, paint);
        }

        //canvas.drawLine(startX, startY, endX, endY, paint);
        //canvas.drawRect(xMargin, yMargin, drawWidth-xMargin, drawHeight-yMargin, paint);
        /*
        android.util.Log.i("drawWidth", ((Integer) drawWidth).toString());
        android.util.Log.i("drawHeight", ((Integer)drawHeight).toString());
        android.util.Log.i("aspectRatio", ((Float)aspectRatio).toString());
        android.util.Log.i("xMargin", ((Float)xMargin).toString());
        android.util.Log.i("yMargin", ((Float)yMargin).toString());
        */
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        gridWidth = bundle.getInt("gridWidth");
        gridHeight = bundle.getInt("gridHeight");
    }
}
