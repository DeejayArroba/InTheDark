package djarroba.ludumdare34.level;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import djarroba.ludumdare34.entities.Dot;
import djarroba.ludumdare34.entities.Projectile;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.units.Units;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

import java.util.ArrayList;

public class Level {

	Body body;
	TiledMap map;
	Vector2 spawnPosition;
	String name;
	public ArrayList<Dot> dots;

	public Level(TiledMap map) {
		this.name = (String) map.getProperties().get("name");
		this.map = map;
	}

	public void load(GameScreen screen) {
		this.dots = new ArrayList<Dot>();
		float spawnX = map.getLayers().get("Spawn").getObjects().get(0).getProperties().get("x", Float.class) / Units.PPU;
		float spawnY = map.getLayers().get("Spawn").getObjects().get(0).getProperties().get("y", Float.class) / Units.PPU;
		this.spawnPosition = new Vector2(spawnX, spawnY);

		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		int tileWidth = map.getProperties().get("tilewidth", Integer.class);
		int tileHeight = map.getProperties().get("tileheight", Integer.class);

		BodyDef worldBodyDef = new BodyDef();
		worldBodyDef.type = BodyDef.BodyType.StaticBody;
		worldBodyDef.position.set(0, 0);

		body = screen.world.createBody(worldBodyDef);

		for (MapObject mapObject : map.getLayers().get("Collision").getObjects()) {
			if(mapObject instanceof RectangleMapObject) {
				// Everything here is in world units
				float objectX = mapObject.getProperties().get("x", Float.class) / tileWidth;
				float objectY = mapObject.getProperties().get("y", Float.class) / tileWidth;
				float objectWidth = mapObject.getProperties().get("width", Float.class) / tileWidth;
				float objectHeight = mapObject.getProperties().get("height", Float.class) / tileWidth;

				FixtureDef fixtureDef = new FixtureDef();

				PolygonShape shape = new PolygonShape();
				shape.setAsBox(objectWidth/2, objectHeight/2, new Vector2(objectX + objectWidth/2, objectY + objectHeight/2), 0);

				fixtureDef.shape = shape;

				fixtureDef.filter.categoryBits = PhysicsCategories.WORLD;

				body.createFixture(fixtureDef);

				shape.dispose();
			}
		}

		for(MapObject mapObject : map.getLayers().get("Dots").getObjects()) {
			float objectX = mapObject.getProperties().get("x", Float.class) / tileWidth;
			float objectY = mapObject.getProperties().get("y", Float.class) / tileWidth;

			Dot dot = new Dot(screen, new Vector2(objectX, objectY));
			screen.entityManager.add(dot);
			dots.add(dot);
		}

	}

	public TiledMap getMap() {
		return map;
	}

	public Vector2 getSpawnPosition() {
		return spawnPosition;
	}

	@Override
	public String toString() {
		return name;
	}

	public Dot getDotByBody(Body body) {
		for(Dot dot : dots) {
			if(body == dot.body) {
				return dot;
			}
		}
		return null;
	}


}
