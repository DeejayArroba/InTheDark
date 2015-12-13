package djarroba.ludumdare34.screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import djarroba.ludumdare34.MyGame;
import djarroba.ludumdare34.entities.EntityManager;
import djarroba.ludumdare34.entities.Player;
import djarroba.ludumdare34.hud.Hud;
import djarroba.ludumdare34.level.Level;
import djarroba.ludumdare34.listeners.CollisionListener;
import djarroba.ludumdare34.units.Units;

public class GameScreen implements Screen {

	public MyGame game;

	public static float WORLD_WIDTH = 16;
	public static float WORLD_HEIGHT = 9;

	public FitViewport viewport;
	public OrthographicCamera camera;

	public EntityManager entityManager;

	public World world;
	public Box2DDebugRenderer debugRenderer;
	public RayHandler rayHandler;

	public SpriteBatch batch;

	public Level level;

	public Player player;

	OrthogonalTiledMapRenderer mapRenderer;

	boolean isCounting = false;
	float timeLeft = 5;
	public int dotsLeft = 0;

	public boolean won = false;
	public boolean lost = false;

	Hud hud;

	public GameScreen(MyGame game, Level level) {
		this.game = game;
		this.level = level;

		Gdx.input.setInputProcessor(null);

		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);

		batch = new SpriteBatch();

		// Physics
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		rayHandler = new RayHandler(world);
		rayHandler.setShadows(true);
		rayHandler.setAmbientLight(new Color(0, 0, 0, 0.01f));
		world.setContactListener(new CollisionListener(this));

		entityManager = new EntityManager();

		// Setup the level
		level.load(this);
		dotsLeft = level.dots.size();
		mapRenderer = new OrthogonalTiledMapRenderer(level.getMap(), 1 / Units.PPU);

		player = new Player(this);
		entityManager.add(player);

		hud = new Hud(this);

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		entityManager.cleanUp();

		if(isCounting) {
			timeLeft -= delta;

			if(dotsLeft == 0) {
				win();
			}

			if(timeLeft <= 0) {
				lose();
			}
		}

		viewport.apply();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		entityManager.update(delta);

		world.step(1/60f, 6, 2);

		batch.setProjectionMatrix(camera.combined);

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.begin();

		entityManager.drawUpdate(delta, batch);

		batch.end();

		entityManager.lateUpdate(delta);

		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();

		//debugRenderer.render(world, camera.combined);
		hud.update(delta);

		if(lost) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				game.setScreen(new GameScreen(game, new Level(level.getMap())));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		hud.viewport.update(width, height);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		rayHandler.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	public void startTimer() {
		isCounting = true;
	}

	public void win() {
		if(!won && !lost) {
			won = true;
			hud.winLoseLabel.setText("You win");
			game.assets.get("sounds/win1.wav", Sound.class).play();
			end();
		}
	}

	public void lose() {
		if(!won && !lost) {
			lost = true;
			hud.winLoseLabel.setText("   Game over\n[SPACE] to retry");
			game.assets.get("sounds/lose1.wav", Sound.class).play();
			end();
		}
	}

	private void end() {

	}

}