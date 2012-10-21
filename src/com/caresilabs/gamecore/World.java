package com.caresilabs.gamecore;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author Simon
 * extend @GameCallback
 */

public class World {
	public static Vector2 Gravity;
	private ArrayList<GameObject> mObjects;
	private GameCallback mCallback;
	private Renderer renderer;
	private SpatialHashGrid mSpatialHashGrid;
	private boolean mDebug = true;
	private final float width;
	private final float height;
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param gravity
	 * @param callback
	 */
	public World(int width, int height, Vector2 gravity, GameCallback callback) {
		this.width = width;
		this.height = height;
		this.mObjects = new ArrayList<GameObject>();
		this.mCallback = callback;
		this.renderer = new Renderer(this, width, height);
		this.mSpatialHashGrid = new SpatialHashGrid(width, height, Math.max(width, height) * .3f);
		World.Gravity = gravity;
	}
	
	public void setCameraSize(float width, float height) {
		renderer.getCam().viewportWidth= width;
		renderer.getCam().viewportHeight = height;
	}
	
	/**
	 * @update should be called at every frame
	 * @param delta
	 */
	public void update(float delta) {
		updateObjects(delta);
		renderObjects(delta);
	}
	
	/**
	 * Adds a @GameObject to the World
	 * @param object
	 */
	public void addObject(GameObject object) { 
		mObjects.add(object);
		mSpatialHashGrid.insertDynamicObject(object);
	}
	
	/**
	 * Removes a @GameObject to the World
	 * @param object
	 */
	public void removeObject(GameObject object) {
		mObjects.remove(object);
		mSpatialHashGrid.removeObject(object);
	}
	
	/**
	 * Finds all @GameObject with the id
	 * @param object
	 */
	public GameObject getObject(String id) {
		for(GameObject object: mObjects) {
			if(object.getName() == id) {
				return object;
			}
		}
		return null;
	}

	private void renderObjects(float delta) {
		renderer.render();
	}

	private List<GameObject> tempObjects = new ArrayList<GameObject>();
	private void updateObjects(float delta) {
		tempObjects.clear();
		for(GameObject object: mObjects) {
			object.update(delta);
			mCallback.update(object);
			object.updatePhysics(delta); 
			object.stateTime += delta;
			
			if (object.bounds.x + object.bounds.width/2 < 0 || object.bounds.x > width) {
				if (object.bounds.y - object.bounds.height/2 < 0 || object.bounds.y - object.bounds.height/2 > height) {
					removeObject(object); 
					return;
				}
			}
			/*if(object.getSprite() != object.getAnimation().getKeyFrame(object.getStateTime(), object.getAnimationLoop()))
				object.getSprite().setRegion(object.getAnimation().getKeyFrame(object.getStateTime(), object.getAnimationLoop()));
			*/
			
			for(GameObject obj : mSpatialHashGrid.getPotentialColliders(object)) {
				for(GameObject tmp : tempObjects) {
					if(tmp == obj)
						continue;
				}
				mCallback.colliding(object, obj);
				tempObjects.add(obj);
			}
		}
	}
	
	private Vector3 touch = new Vector3();
	/**
	 * get a list with all object which were touched
	 * @param x
	 * @param y
	 * @return
	 */
	public List<GameObject> touchedObjects(float x, float y) {
		renderer.getCam().unproject(touch.set(x, y, 0));
		List<GameObject> temp = new ArrayList<GameObject>();
		for(GameObject object: mObjects) {
			if(object.bounds.contains(touch.x, touch.y))
				temp.add(object);
		}
		return temp;
	}
	
	/**
	 * implement GameCallback
	 * @author Simon
	 *
	 */
	public interface GameCallback {
		public void colliding(GameObject g1, GameObject g2);
		public void update(GameObject g1);
	}
	
	public ArrayList<GameObject> getmObjects() {
		return mObjects;
	}
	
	public void setmDebug(boolean mDebug) {
		this.mDebug = mDebug;
	}
	public boolean isDebug() {
		return mDebug;
	}
	public float getHeight() {
		return height;
	}
	public float getWidth() {
		return width;
	}
	public OrthographicCamera getCamera() {
		return renderer.getCam();
	}
}
