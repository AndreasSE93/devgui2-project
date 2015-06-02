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
	boolean sfx;
	boolean bgm;
	MediaPlayer musicPlayer;
	MediaPlayer winPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		drawView.setBackgroundColor(Color.BLACK);
		setContentView(drawView);
		float blockLength = drawView.getBlockLength();
		int gridWidth = getIntent().getIntExtra("gridWidth", 5);
		int gridHeight = getIntent().getIntExtra("gridHeight", 5);
		int blockMinSize = getIntent().getIntExtra("blockMinSize", 3);
		int blockMaxSize = getIntent().getIntExtra("blockMaxSize", 3);
		final Piece[] pieces = GridMaker.makeGrid(gridWidth, gridHeight, blockMinSize, blockMaxSize);
		drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				canvasWidth = drawView.getWidth();
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