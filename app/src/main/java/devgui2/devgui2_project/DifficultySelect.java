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

	public void custom(View view) {
		Intent intent = new Intent(this, CustomDifficulty.class);
		startActivity(intent);
	}
}
