package djarroba.ludumdare34.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class EntityManager {

	ArrayList<Entity> entities;
	ArrayList<Entity> entitiesToRemove;
	ArrayList<Entity> entitiesToAdd;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		entitiesToAdd = new ArrayList<Entity>();
	}

	public void cleanUp() {
		for(Entity entity : entitiesToRemove) {
			entity.onRemove(this);
			entities.remove(entity);
		}
		entitiesToRemove.clear();
		for(Entity entity : entitiesToAdd) {
			entities.add(entity);
			entity.onAdded(this);
		}
		entitiesToAdd.clear();
	}

	public void update(float delta) {
		for(Entity entity : entities) {
			entity.update(delta);
		}
	}

	public void drawUpdate(float delta, SpriteBatch batch) {
		for(Entity entity : entities) {
			entity.drawUpdate(delta, batch);
		}

		for(Entity entity : entities) {
			entity.lateDrawUpdate(delta, batch);
		}
	}

	public void lateUpdate(float delta) {
		for(Entity entity : entities) {
			entity.lateUpdate(delta);
		}
	}

	public void add(Entity entity) {
		entitiesToAdd.add(entity);
	}

	public void remove(Entity entity) {
		entitiesToRemove.add(entity);
	}

}
