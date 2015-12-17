package djarroba.ludumdare34.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import djarroba.ludumdare34.MyGame;

import java.util.ArrayList;

public class MapManager {

	ArrayList<TiledMap> maps;
	MyGame game;

	public MapManager(MyGame game) {
		this.game = game;
		maps = new ArrayList<TiledMap>();
	}

	public void add(TiledMap map) {
		maps.add(map);
	}

	public ArrayList<TiledMap> getLevels() {
		return maps;
	}

	public int getLevelIndex(TiledMap map) {
		for (int i = 0; i < maps.size(); i++) {
			if(maps.get(i) == map)
				return i;
		}
		return -1;
	}

}
