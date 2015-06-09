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

		final int moveCount  = getIntent().getIntExtra ("moveCount",  -1);
		final int pieceCount = getIntent().getIntExtra ("pieceCount", -1);
		final long solveTime = getIntent().getLongExtra("solveTime", -1);

		TextView textView = (TextView)findViewById(R.id.statsText);
		String text = textView.getText().toString();
		text = text.replace("{1}", Integer.toString(moveCount));
		text = text.replace("{2}", Integer.toString(pieceCount));
		text = text.replace("{3}", Long.toString(solveTime / 1000));
		textView.setText(text);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean sfx = sharedPrefs.getBoolean("pref_sound_fx", true);
		if (sfx) {
			winPlayer = MediaPlayer.create(this, R.raw.sfxwin);
			winPlayer.start();
		}
	}

	public void restart(View view) {
        setResult(R.integer.RESTART_GAME);
        finish();
	}

	public void menu(View view) {
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
