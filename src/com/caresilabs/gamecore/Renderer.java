package com.caresilabs.gamecore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

class Renderer {
	private World world;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private FPSLogger fps;
	private ShapeRenderer debugRenderer;
	
	protected Renderer(World world, float width, float height) {
		this.world = world;
		this.batch = new SpriteBatch(); 
		this.cam = new OrthographicCamera(width, height);
		this.cam.position.set(width/2, height/2, 0);
		this.fps = new FPSLogger();
		this.debugRenderer = new ShapeRenderer();
	}
	
	public void render() {
		GLCommon gl = Gdx.gl;
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glClearColor(1, 1, 1, 1);
	    
	    fps.log();
	    
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		batch.begin();
		if(world.isDebug()) {
			debugRenderer.setProjectionMatrix(cam.combined);
			debugRenderer.begin(ShapeType.Rectangle);
		}
		renderBackground();
		
		batch.enableBlending();
		renderObjects();
		
		if(world.isDebug()) 
			debugRenderer.end();
		
		batch.end();
	}

	private void renderBackground() {
		//TODO add background support
	}

	private void renderObjects() {
		for(GameObject object: world.getmObjects()) {
			if (object.position.x < cam.position.x + cam.viewportWidth / 2 + 0 + object.bounds.width/2  && object.position.x > cam.position.x - cam.viewportWidth / 2 - object.bounds.width/2) {
				if (object.position.y < cam.position.y + cam.viewportHeight / 2 + object.bounds.height && object.position.y > cam.position.y - cam.viewportHeight / 2 - object.bounds.height) {
					batch.draw(object.getSprite(), object.position.x - object.bounds.width/2, object.position.y - object.bounds.height/2, object.bounds.width, object.bounds.height);
					if(world.isDebug()) {
						debugRenderer.setColor(new Color(1, 0, 0, 1));
						debugRenderer.rect(object.bounds.x, object.bounds.y, object.bounds.width, object.bounds.height);
					}
				}
			}
		}
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
}
