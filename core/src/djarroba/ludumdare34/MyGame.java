package djarroba.ludumdare34;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import djarroba.ludumdare34.level.Level;
import djarroba.ludumdare34.level.MapManager;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.screens.MenuScreen;

public class MyGame extends Game {

	public AssetManager assets;
	public MapManager mapManager;

	@Override
	public void create () {
		assets = new AssetManager();

		assets.load("player.png", Texture.class);
		assets.load("particle.png", Texture.class);
		assets.load("particle_shot.png", Texture.class);
		assets.load("particle_dot.png", Texture.class);

		assets.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));

		assets.load("sounds/explosion1.wav", Sound.class);
		assets.load("sounds/explosion2.wav", Sound.class);
		assets.load("sounds/explosion3.wav", Sound.class);
		assets.load("sounds/explosion4.wav", Sound.class);
		assets.load("sounds/shoot1.wav", Sound.class);
		assets.load("sounds/shoot2.wav", Sound.class);
		assets.load("sounds/shoot3.wav", Sound.class);
		assets.load("sounds/shoot4.wav", Sound.class);
		assets.load("sounds/win1.wav", Sound.class);
		assets.load("sounds/lose1.wav", Sound.class);

		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		assets.load("levels/level1.tmx", TiledMap.class);
		assets.load("levels/level2.tmx", TiledMap.class);
		assets.load("levels/level3.tmx", TiledMap.class);
		assets.load("levels/level4.tmx", TiledMap.class);
		assets.load("levels/level5.tmx", TiledMap.class);
		assets.load("levels/level10.tmx", TiledMap.class);

		assets.finishLoading();

		mapManager = new MapManager(this);
		// Load the actual levels into the MapManager
		mapManager.add(assets.get("levels/level1.tmx", TiledMap.class));
		mapManager.add(assets.get("levels/level2.tmx", TiledMap.class));
		mapManager.add(assets.get("levels/level3.tmx", TiledMap.class));
		mapManager.add(assets.get("levels/level4.tmx", TiledMap.class));
		mapManager.add(assets.get("levels/level5.tmx", TiledMap.class));
		mapManager.add(assets.get("levels/level10.tmx", TiledMap.class));


		Box2D.init();

		setScreen(new GameScreen(this, mapManager.getMaps().get(0)));
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		assets.dispose();
	}

}
