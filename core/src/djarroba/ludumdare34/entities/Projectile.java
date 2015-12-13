package djarroba.ludumdare34.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

public class Projectile implements Entity {

	PointLight light;
	Body body;
	float lifeSpan;
	Player player;
	boolean alive = true;

	public Projectile(Player player) {
		this.player = player;
		Vector2 position = player.body.getPosition();

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);

		body = player.screen.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();

		CircleShape shape = new CircleShape();
		shape.setRadius(0.25f);

		fixtureDef.shape = shape;
		fixtureDef.restitution = 0.2f;
		fixtureDef.friction = 0.8f;
		fixtureDef.density = 0.6f;

		fixtureDef.filter.categoryBits = PhysicsCategories.PROJECTILE;
		fixtureDef.filter.maskBits = PhysicsCategories.WORLD | PhysicsCategories.DOT;

		body.createFixture(fixtureDef);

		shape.dispose();

		body.applyForceToCenter(new Vector2((float) Math.cos(player.body.getAngle()) * 100, (float) Math.sin(player.body.getAngle()) * 100), true);

		light = new PointLight(player.screen.rayHandler, 128, Color.YELLOW, 1, body.getPosition().x, body.getPosition().y);

		generateParticle();
	}

	@Override
	public void onAdded(EntityManager entityManager) {

	}

	@Override
	public void onRemove(EntityManager entityManager) {
		player.projectiles.remove(this);
		alive = false;
		if(!player.screen.world.isLocked())
			player.screen.world.destroyBody(body);
		if(light != null){
			light.remove();
		}
	}

	@Override
	public void update(float delta) {
		light.setPosition(body.getPosition());
		lifeSpan += delta;

		if(lifeSpan > 3) {
			player.screen.entityManager.remove(this);
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

	}

	private void generateParticle() {
		Particle particle = new Particle(player.screen,
				body,
				1f,
				player.screen.game.assets.get("particle_shot.png", Texture.class),
				Colors.particleProjectile,
				0.75f,
				0.05f);
		player.screen.entityManager.add(particle);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				if(alive){
					generateParticle();
				}
			}
		}, 0.015f);
	}

}
