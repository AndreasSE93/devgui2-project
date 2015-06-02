package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DifficultySelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_difficulty_select);
	}

	public void play(View view) {
		int gridWidth, gridHeight, blockMinSize, blockMaxSize;
		switch (view.getId()) {
			case R.id.easyButton:
				gridWidth = 4;
				gridHeight = 4;
				blockMinSize = 2;
				blockMaxSize = 3;
				break;
			case R.id.mediumButton:
				gridWidth = 6;
				gridHeight = 6;
				blockMinSize = 3;
				blockMaxSize = 4;
				break;
			case R.id.hardButton:
				gridWidth = 8;
				gridHeight = 8;
				blockMinSize = 4;
				blockMaxSize = 6;
				break;
			default:
				throw new RuntimeException("Unknown play button pressed");
		}
		Intent intent = new Intent(this, GridGame.class);
		intent.putExtra("gridWidth",    gridWidth);
		intent.putExtra("gridHeight",   gridHeight);
		intent.putExtra("blockMinSize", blockMinSize);
		intent.putExtra("blockMaxSize", blockMaxSize);
		startActivity(intent);
	}

	public void custom(View view) {
		Intent intent = new Intent(this, CustomDifficulty.class);
		startActivity(intent);
	}
}
