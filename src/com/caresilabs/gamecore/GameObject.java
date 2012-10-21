package com.caresilabs.gamecore;

import com.badlogic.gdx.graphics.Texture;

public  abstract class GameObject extends Object{

	public GameObject(Texture sprite, String id, float x, float y, float width,
			float height) {
		super(sprite, id, x, y, width, height);
	}

	public abstract void update(float delta);
	
	
}
