package djarroba.ludumdare34.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.units.Units;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

import java.util.Random;

public class Particle implements Entity {

	Body body;
	Sprite sprite;
	PointLight light;
	GameScreen screen;
	float scale;
	float lifeSpan;
	float timeLeft;

	public Particle(final GameScreen screen, Body parent, float lifeSpan, Texture texture, Color color, float scale, float distance) {
		this.screen = screen;
		this.scale = scale;
		this.lifeSpan = lifeSpan;
		this.timeLeft = lifeSpan;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(parent.getPosition().x, parent.getPosition().y);

		body = screen.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();

		CircleShape shape = new CircleShape();
		shape.setRadius(0.125f);

		fixtureDef.shape = shape;
		fixtureDef.restitution = 0.2f;
		fixtureDef.friction = 0.8f;
		fixtureDef.density = 0.6f;

		fixtureDef.filter.categoryBits = PhysicsCategories.PARTICLE;
		fixtureDef.filter.maskBits = PhysicsCategories.WORLD;

		body.createFixture(fixtureDef);

		shape.dispose();

		Random random = new Random();

		body.setLinearVelocity(new Vector2((0.5f-random.nextFloat())*2, (0.5f-random.nextFloat())*2));
		body.setAngularVelocity(random.nextFloat());


		sprite = new Sprite(texture, 16, 16);
		sprite.setSize(sprite.getWidth()/ Units.PPU, sprite.getHeight()/Units.PPU);
		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setScale(random.nextFloat() * scale);

		light = new PointLight(screen.rayHandler, 16, color, distance, body.getPosition().x, body.getPosition().y);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				screen.entityManager.remove(Particle.this);
			}
		}, lifeSpan);

	}

	@Override
	public void onAdded(EntityManager entityManager) {

	}

	@Override
	public void onRemove(EntityManager entityManager) {
		screen.world.destroyBody(body);
		light.remove();
	}

	@Override
	public void update(float delta) {
		timeLeft -= delta;
		light.setPosition(body.getPosition());
		Color color = light.getColor();
		color.a = timeLeft/lifeSpan/4;
		light.setColor(color);
		//light.setDistance(light.getDistance() * 50 * delta);
	}

	@Override
	public void drawUpdate(float delta, SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.setScale(timeLeft/lifeSpan);
		sprite.draw(batch);
	}

	@Override
	public void lateDrawUpdate(float delta, SpriteBatch batch) {

	}

	@Override
	public void lateUpdate(float delta) {

	}
}
