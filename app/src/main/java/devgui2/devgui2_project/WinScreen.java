package devgui2.devgui2_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class WinScreen extends Activity {
    private MediaPlayer winPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_screen);

		// Get the stats passed on by GridGame
		final int moveCount  = getIntent().getIntExtra ("moveCount",  -1);
		final int pieceCount = getIntent().getIntExtra ("pieceCount", -1);
		final long solveTime = getIntent().getLongExtra("solveTime",  -1);

		// Replace placeholders in the win message
		TextView textView = (TextView)findViewById(R.id.statsText);
		String text = textView.getText().toString();
		text = text.replace("{1}", Integer.toString(moveCount));
		text = text.replace("{2}", Integer.toString(pieceCount));
		text = text.replace("{3}", Long.toString(solveTime / 1000));
		textView.setText(text);

		// Play win sound if sound effects are enabled
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean soundEnabled = sharedPrefs.getBoolean("pref_sound_fx", true);
		if (soundEnabled) {
			winPlayer = MediaPlayer.create(this, R.raw.sfxwin);
			winPlayer.start();
		}
	}

	// onClick from activity_win_screen.xml
	public void restart(View view) {
        // Tell GridGame to restart itself
        setResult(R.integer.RESTART_GAME);
        finish();
	}

	//onClick from activity_win_screen.xml
	public void menu(View view) {
        // Bring the main menu screen to the top of the history stack
        Intent intent = new Intent(this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        winPlayer.release();
    }
}
