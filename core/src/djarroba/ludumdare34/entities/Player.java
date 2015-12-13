package djarroba.ludumdare34.entities;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.units.Units;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

import java.util.ArrayList;
import java.util.Random;

public class Player implements Entity {

	ConeLight flashlight;
	PointLight light;
	Body body;
	GameScreen screen;
	float speed = 1f;
	float timeUntilCanShoot = 0;
	public ArrayList<Projectile> projectiles;
	Sprite sprite;

	ArrayList<Sound> shootingSounds;

	public Player(GameScreen screen) {
		this.screen = screen;
		projectiles = new ArrayList<Projectile>();

		shootingSounds = new ArrayList<Sound>();
		shootingSounds.add(screen.game.assets.get("sounds/shoot1.wav", Sound.class));
		shootingSounds.add(screen.game.assets.get("sounds/shoot2.wav", Sound.class));
		shootingSounds.add(screen.game.assets.get("sounds/shoot3.wav", Sound.class));
		shootingSounds.add(screen.game.assets.get("sounds/shoot4.wav", Sound.class));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(screen.level.getSpawnPosition().x, screen.level.getSpawnPosition().y);

		body = screen.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();

		CircleShape shape = new CircleShape();
		shape.setRadius(0.3f);

		fixtureDef.shape = shape;
		fixtureDef.restitution = 0.2f;
		fixtureDef.friction = 0.8f;
		fixtureDef.density = 0.6f;

		fixtureDef.filter.categoryBits = PhysicsCategories.PLAYER;
		fixtureDef.filter.maskBits = PhysicsCategories.WORLD;

		body.createFixture(fixtureDef);
		body.setLinearDamping(0.8f);

		shape.dispose();

		Filter filter = new Filter();
		filter.categoryBits = PhysicsCategories.PLAYER_LIGHT;
		filter.maskBits = PhysicsCategories.WORLD | PhysicsCategories.DOT;
		light = new PointLight(screen.rayHandler, 128, Colors.player, 1.5f, body.getPosition().x, body.getPosition().y);
		light.setContactFilter(filter);
		flashlight = new ConeLight(screen.rayHandler, 512, Colors.player, 24, body.getPosition().x, body.getPosition().y, (float) Math.toDegrees(body.getAngle()), 38);
		flashlight.setContactFilter(filter);

		Texture tex = screen.game.assets.get("player.png", Texture.class);
		sprite = new Sprite(tex, 16, 16);
		sprite.setSize(sprite.getWidth()/Units.PPU, sprite.getHeight()/Units.PPU);
		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setScale(0.64f);

		generateParticle();
	}

	@Override
	public void onAdded(EntityManager entityManager) {

	}

	@Override
	public void onRemove(EntityManager entityManager) {

	}

	@Override
	public void update(float delta) {
		if(timeUntilCanShoot > 0) {
			timeUntilCanShoot -= delta;
		}

		/*
		Rotate player towards mouse cursor
		 */
		Vector3 mousePos = screen.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector3 playerPos = new Vector3(body.getPosition().x, body.getPosition().y, 0);

		Vector3 vectorToTarget = new Vector3(mousePos.x - playerPos.x, mousePos.y - playerPos.y, 0);

		double angle = Math.toDegrees(Math.atan2(vectorToTarget.y, vectorToTarget.x));

		body.setTransform(body.getPosition().x, body.getPosition().y, (float) Math.toRadians(angle));


		/*
		Move the player towards the cursor
		 */
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Vector2 finalVelocity = new Vector2();

			finalVelocity.x = (float) (Math.cos(body.getAngle()) * speed);
			finalVelocity.y = (float) (Math.sin(body.getAngle()) * speed);

			body.applyForceToCenter(finalVelocity, true);
		}

		/*
		Update the lights
		 */
		light.setPosition(body.getPosition().x, body.getPosition().y);
		flashlight.setPosition(body.getPosition().x, body.getPosition().y);
		flashlight.setDirection((float) angle);

		/*
		Shoot projectiles
		 */
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (canShoot()) {
				shoot();
			}
		}

	}

	@Override
	public void drawUpdate(float delta, SpriteBatch batch) {

	}

	@Override
	public void lateDrawUpdate(float delta, SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.draw(batch);
	}

	@Override
	public void lateUpdate(float delta) {

	}

	private void shoot() {
		getRandomShootingSound().play();
		Projectile projectile = new Projectile(this);
		screen.entityManager.add(projectile);
		projectiles.add(projectile);
		timeUntilCanShoot = 0.33f;
	}

	private boolean canShoot() {
		return timeUntilCanShoot <= 0;
	}

	public Projectile getProjectileByBody(Body body) {
		for(Projectile projectile : projectiles) {
			if(body == projectile.body) {
				return projectile;
			}
		}

		return null;
	}

	private void generateParticle() {
		Particle particle = new Particle(screen, body, 1f, screen.game.assets.get("particle.png", Texture.class), Colors.particlePlayer, 1, 0.1f);
		screen.entityManager.add(particle);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				generateParticle();
			}
		}, 0.03f);
	}

	private Sound getRandomShootingSound() {
		Random random = new Random();
		return shootingSounds.get(random.nextInt(shootingSounds.size()));
	}

}
