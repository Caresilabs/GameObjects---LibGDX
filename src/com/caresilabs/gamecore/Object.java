package com.caresilabs.gamecore;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Object {
	public final Vector2 position;
	public final Vector2 velocity;
	public final Vector2 acceleration;
	public final Rectangle bounds;
	protected float stateTime;
	private Sprite mSprite;
	private String mName;
	private int state;
	private Animation mAnimation;
	private boolean animationLoop;
	private ObjectType type;
	
	public static enum ObjectType {
		STATIC, DYNAMIC
	}
	/**
	 * Do not use ^^
	 * @param sprite
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Object (Texture sprite, String name, float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
		this.mSprite = new Sprite(sprite);
		this.mSprite.setSize(width, height);
		this.mName = name;
		this.mAnimation = new Animation(1f, mSprite);
		this.type = ObjectType.DYNAMIC;
	}
	
	protected void updatePhysics(float delta) {
		velocity.add(acceleration.mul(delta));
		if(type == ObjectType.DYNAMIC) {
			velocity.add(World.Gravity.x * delta, World.Gravity.y * delta);
		}
		position.add(velocity.x * delta, velocity.y * delta);
        bounds.x = position.x - bounds.width / 2;
 		bounds.y = position.y - bounds.height / 2;
	}
	
	//TODO Animations 
	public void setAnimation(float dur, boolean loop, TextureRegion ... s) {
		mAnimation = new Animation(dur, s);
		animationLoop = loop;
	}
	
	public Animation getAnimation() {
		return mAnimation;
	}
	
	public abstract void update(float delta);
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public Sprite getSprite() {
		return mSprite;
	}
	
	public String getName() {
		return mName;
	}
	public float getStateTime() {
		return stateTime;
	}
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}
	
	public void setSize(float width, float height) {
		mSprite.setSize(width, height);
	}
	
	public float getWidth() {
		return mSprite.getWidth();
	}
	
	public float getHeight() {
		return mSprite.getHeight();
	}
	
	public void setBoundsSize(float width, float height) {
		bounds.setWidth(width);
		bounds.setHeight(height);
	}
	
	public void centerBounds() {
		bounds.set(position.x - bounds.width/2, position.y - bounds.height/2, bounds.width, bounds.height);
	}
	
	public boolean getAnimationLoop() {
		return animationLoop;
	}
	public void setAnimationLoop(boolean animationLoop) {
		this.animationLoop = animationLoop;
	}
	
	public void setRotation(float degrees) {
		mSprite.setRotation(degrees);
	}
	
	public void rotate(float degrees) {
		mSprite.rotate(degrees);
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setType(ObjectType type) {
		this.type = type;
	}
	public ObjectType getType() {
		return type;
	}
}
