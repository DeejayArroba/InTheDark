package djarroba.ludumdare34.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import djarroba.ludumdare34.entities.Dot;
import djarroba.ludumdare34.entities.Projectile;
import djarroba.ludumdare34.screens.GameScreen;

public class CollisionListener implements ContactListener {

	GameScreen screen;

	public CollisionListener(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void beginContact(Contact contact) {
		Projectile projectileA = screen.player.getProjectileByBody(contact.getFixtureA().getBody());
		Projectile projectileB = screen.player.getProjectileByBody(contact.getFixtureB().getBody());
		Dot dotA = screen.level.getDotByBody(contact.getFixtureA().getBody());
		Dot dotB = screen.level.getDotByBody(contact.getFixtureB().getBody());

		if(projectileA != null && dotB != null) {
			screen.startTimer();
			screen.entityManager.remove(projectileA);
			screen.entityManager.remove(dotB);

			for(Dot dot : screen.level.dots) {
				if(!dot.isFound()) {
					screen.lose();
					return;
				}
			}

			screen.dotsLeft--;
		}
		if(projectileB != null && dotA != null) {
			screen.startTimer();
			screen.entityManager.remove(projectileB);
			screen.entityManager.remove(dotA);

			for(Dot dot : screen.level.dots) {
				if(!dot.isFound()) {
					screen.lose();
					return;
				}
			}

			screen.dotsLeft--;
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
