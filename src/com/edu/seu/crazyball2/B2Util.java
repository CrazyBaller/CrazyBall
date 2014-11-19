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

	public static Body createCircle(World world, float fRadius, float fPosX,
			float fPosY, BodyType bodyType, float fAngle, float fDensity,
			float fRestitution, float fFriction, Object objUser, Filter filter) {

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
		if (filter != null) {
			fixture.setFilterData(filter);
		}
		if (objUser != null)
			bodyCircle.setUserData(objUser);

		shapeCircle.dispose();
		shapeCircle = null;
		return bodyCircle;
	}

	public static Body createRectangle(World world, float fHalfWidth,
			float fHalfHeight, float fPosX, float fPosY, BodyType bodyType,
			float fAngle, float fDensity, float fRestitution, float fFriction,
			Object objUser, Filter filter) {

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
		if (filter != null) {
			fixture.setFilterData(filter);
		}
		shapeRect.dispose();

		if (objUser != null)
			bodyRect.setUserData(objUser);
		return bodyRect;
	}

	public static Body createSensor(World world, float fRadius,
			Fixture m_sensor, float fPosX, float fPosY, Object objUser,
			Filter filter) {
		BodyDef bdSensor = new BodyDef();
		Body ground = world.createBody(bdSensor);

		CircleShape shapeSensor = new CircleShape();
		shapeSensor.setRadius(fRadius);
		Vector2 tempVe = new Vector2(fPosX, fPosY);
		shapeSensor.setPosition(tempVe);

		FixtureDef fxDef = new FixtureDef();
		fxDef.shape = shapeSensor;
		fxDef.isSensor = true;

		Fixture fixture = ground.createFixture(fxDef);
		if (filter != null) {
			fixture.setFilterData(filter);
		}
		if (objUser != null)
			ground.setUserData(objUser);
		m_sensor = fixture;
		System.out.println(m_sensor);
		shapeSensor.dispose();
		shapeSensor = null;

		return ground;
	}
}
