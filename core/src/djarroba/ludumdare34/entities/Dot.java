package djarroba.ludumdare34.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

import java.util.ArrayList;
import java.util.Random;

public class Dot implements Entity {

	public Body body;
	GameScreen screen;
	PointLight light;
	Color color;
	boolean found;

	float radius = 0.2f;

	ArrayList<Sound> sounds;

	public Dot(GameScreen screen, Vector2 position) {
		this.screen = screen;
		this.color = Colors.randomColor(0.8f);

		sounds = new ArrayList<Sound>();
		sounds.add(screen.game.assets.get("sounds/explosion1.wav", Sound.class));
		sounds.add(screen.game.assets.get("sounds/explosion2.wav", Sound.class));
		sounds.add(screen.game.assets.get("sounds/explosion3.wav", Sound.class));
		sounds.add(screen.game.assets.get("sounds/explosion4.wav", Sound.class));

		position = new Vector2(((int) position.x) + 0.5f, ((int) position.y) + 0.5f);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(position.x, position.y);

		body = screen.world.createBody(bodyDef);

	}

	@Override
	public void onAdded(EntityManager entityManager) {

	}

	@Override
	public void onRemove(EntityManager entityManager) {
		if(!screen.world.isLocked())
			screen.world.destroyBody(body);
		if (light != null)
			light.remove();
		screen.level.dots.remove(this);
	}

	@Override
	public void update(float delta) {
		if(isLightTouching() && light == null) {
			found = true;

			light = new PointLight(screen.rayHandler, 128, color, 0.25f, body.getPosition().x, body.getPosition().y);
			FixtureDef fixtureDef = new FixtureDef();

			CircleShape shape = new CircleShape();
			shape.setRadius(radius);

			fixtureDef.shape = shape;

			fixtureDef.filter.categoryBits = PhysicsCategories.DOT;
			fixtureDef.filter.maskBits = PhysicsCategories.PROJECTILE;

			body.createFixture(fixtureDef);

			shape.dispose();
		}
	}

	@Override
	public void drawUpdate(float delta, SpriteBatch batch) {

	}

	@Override
	public void lateDrawUpdate(float delta, SpriteBatch batch) {

	}

	@Override
	public void lateUpdate(float delta) {
		if(light != null) {
			light.setDistance(light.getDistance()+delta/2);
			if(light.getDistance() > 7.5f) {

				/*
				Explosion here
				 */
				for (int i = 0; i < 50; i++) {
					Particle particle = new Particle(screen, body, 5f, screen.game.assets.get("particle_dot.png", Texture.class), Colors.particleDot, 1.2f, 1);
					screen.entityManager.add(particle);
				}
				getRandomSound().play();

				screen.entityManager.remove(this);
				screen.lose();
			}
		}
	}

	private boolean isLightTouching() {
		return (screen.rayHandler.pointAtLight(body.getPosition().x+radius, body.getPosition().y) ||
				screen.rayHandler.pointAtLight(body.getPosition().x-radius, body.getPosition().y) ||
				screen.rayHandler.pointAtLight(body.getPosition().x, body.getPosition().y+radius) ||
				screen.rayHandler.pointAtLight(body.getPosition().x, body.getPosition().y-radius));
	}

	public boolean isFound() {
		return found;
	}

	private Sound getRandomSound() {
		Random random = new Random();
		return sounds.get(random.nextInt(sounds.size()));
	}

}
