package djarroba.ludumdare34.level;

import djarroba.ludumdare34.MyGame;

import java.util.ArrayList;

public class LevelManager {

	ArrayList<Level> levels;
	MyGame game;

	public LevelManager(MyGame game) {
		this.game = game;
		levels = new ArrayList<Level>();
	}

	public void add(Level level) {
		levels.add(level);
	}

	public ArrayList<Level> getLevels() {
		return levels;
	}

}
