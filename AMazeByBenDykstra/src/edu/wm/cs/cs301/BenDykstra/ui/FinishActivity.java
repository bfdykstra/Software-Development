package edu.wm.cs.cs301.BenDykstra.ui;

import android.support.v7.app.ActionBarActivity;

import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import edu.wm.cs.cs301.BenDykstra.falstad.Maze;
import edu.wm.cs.cs301.BenDykstra.falstad.MazeFileWriter;

public class FinishActivity extends ActionBarActivity {
	
	MazeFileWriter mfr;
	Maze maze;

	@Override
	/**
	 * Displays whether the user won or lost as well as the path length and battery level.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		
		Intent intent = getIntent();
		
		maze = (Maze) getApplicationContext();
		
		TextView finish = (TextView) findViewById(R.id.finish_text);
		TextView lost = (TextView) findViewById(R.id.lost_text);
		TextView batteryText = (TextView) findViewById(R.id.textView2);
		TextView pathText = (TextView) findViewById(R.id.textView3);
		TextView stoppedText = (TextView) findViewById(R.id.textView4);
		
		int pathLength = intent.getExtras().getInt(PlayActivity.PATH_LENGTH);
		int battery = intent.getExtras().getInt(PlayActivity.BATTERY, 2500);
		boolean stopped = intent.getExtras().getBoolean(PlayActivity.STOPPED);
		
		if(!stopped)	{
			finish.setVisibility(View.VISIBLE);
		}else{
			lost.setVisibility(View.VISIBLE);
		}
		
		batteryText.setText("Battery Level: " + battery);
		pathText.setText("Steps Taken: " + pathLength);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * restarts the application so that a user can play again. If the user wants to save the file, writes the maze to a file using
	 * MazeFileWriter
	 * @param view
	 */
	public void restart(View view){
		Intent intent = new Intent(this, AMazeActivity.class);
		CheckBox save_file = (CheckBox) this.findViewById(R.id.checkBox1);
		
		if(save_file.isChecked()){
			String filename = "level" + AMazeActivity.difficulty;
			String storedMaze = MazeFileWriter.store(filename, maze.getWidth(), maze.getHeight(), maze.mazebuilder.rooms, maze.mazebuilder.expectedPartiters, maze.mazebuilder.root, maze.mazecells, maze.mazedists.dists, maze.mazebuilder.startx, maze.mazebuilder.starty);
			//save file with file saving method
			FileOutputStream outputStream;
			int mode = Context.MODE_PRIVATE ;
			try {
				outputStream = openFileOutput(filename, mode);
				outputStream.write(storedMaze.getBytes());
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.startActivity(intent);
	}
}
