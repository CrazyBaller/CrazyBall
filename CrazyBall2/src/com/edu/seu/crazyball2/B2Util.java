package com.edu.seu.crazyball2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public final class B2Util {
	
	public static float PI = 3.141592653589793f;
	
	public static Body createEdge(World world, float fPosX1, float fPosY1, float fPosX2, float fPosY2,
			BodyType bodyType, float fDensity, float fRestitution, float fFriction, Object objUser, Filter filter){

		BodyDef bdEdge = new BodyDef(); 
		bdEdge.type = bodyType;
		
		Body bodyEdge = world.createBody(bdEdge);
		
		PolygonShape shapeEdge = new PolygonShape();
		shapeEdge.setAsEdge(new Vector2(fPosX1, fPosY1), new Vector2(fPosX2, fPosY2));
		
		FixtureDef fdEdge = new FixtureDef();
		fdEdge.shape = shapeEdge;
		fdEdge.density = fDensity;
		fdEdge.restitution = fRestitution;
		fdEdge.friction = fFriction;
		Fixture fixture = bodyEdge.createFixture(fdEdge);
		if(filter != null){
			fixture.setFilterData(filter);
		}
		shapeEdge.dispose();
		
		if(objUser != null) bodyEdge.setUserData(objUser);
		
		return bodyEdge;
	}
	
	public static Body createCircle(World world, float fRadius, float fPosX, float fPosY, BodyType bodyType, 
			float fAngle, float fDensity, float fRestitution, float fFriction,  Object objUser, Filter filter){
		
		BodyDef bdCircle = new BodyDef();
		bdCircle.type = bodyType;
		bdCircle.position.x = fPosX;
		bdCircle.position.y = fPosY;
		bdCircle.angle = fAngle;
		Body bodyCircle = world.createBody(bdCircle);	
		
		CircleShape shapeCircle = new CircleShape();
		shapeCircle.setRadius(fRadius);
		
		FixtureDef fxDef = new FixtureDef();
		fxDef.shape = shapeCircle;
		fxDef.density = fDensity;
		fxDef.friction = fFriction;
		fxDef.restitution = fRestitution;
		
		Fixture fixture = bodyCircle.createFixture(fxDef);
		if(filter != null){
			fixture.setFilterData(filter);
		}
		if(objUser != null) bodyCircle.setUserData(objUser);
		
		shapeCircle.dispose();
		shapeCircle = null;
		return bodyCircle;
	}
	
	public static Body createPolygon(World world, Vector2[] vectris, float fPosX, float fPosY, BodyType bodyType, 
			float fAngle, float fDensity, float fRestitution, float fFriction, Object objUser, Filter filter){
		
		BodyDef bdPoly = new BodyDef();
		bdPoly.type = bodyType;
		bdPoly.position.x = fPosX;
		bdPoly.position.y = fPosY;
		bdPoly.angle = fAngle;
		Body bodyPoly = world.createBody(bdPoly);
		
		PolygonShape shapePoly = new PolygonShape();
		shapePoly.set(vectris);
		
		FixtureDef fxDef = new FixtureDef();
		fxDef.shape = shapePoly;
		fxDef.density = fDensity;
		fxDef.friction = fFriction;
		fxDef.restitution = fRestitution;
		
		Fixture fixture = bodyPoly.createFixture(fxDef);
		if(filter != null){
			fixture.setFilterData(filter);
		}
		if(objUser != null) bodyPoly.setUserData(objUser);
		
		shapePoly.dispose();
		shapePoly = null;
		return bodyPoly;
	}
	
	
	public static Body createRectangle(World world, float fHalfWidth, float fHalfHeight, float fPosX, float fPosY,
			BodyType bodyType, float fAngle, float fDensity, float fRestitution, float fFriction,
			Object objUser, Filter filter){
		
		BodyDef bdRect = new BodyDef(); 
		bdRect.type = bodyType;
		bdRect.angle = fAngle;
		bdRect.position.x = fPosX;
		bdRect.position.y = fPosY;
		
		Body bodyRect = world.createBody(bdRect);

		PolygonShape shapeRect = new PolygonShape();
		shapeRect.setAsBox(fHalfWidth, fHalfHeight);
		
		FixtureDef fdRect = new FixtureDef();
		fdRect.shape = shapeRect;
		fdRect.density = fDensity;
		fdRect.restitution = fRestitution;
		fdRect.friction = fFriction;
		Fixture fixture = bodyRect.createFixture(fdRect);
		if(filter != null){
			fixture.setFilterData(filter);
		}
		shapeRect.dispose();
		
		if(objUser != null) bodyRect.setUserData(objUser);
		return bodyRect;
	}
}
