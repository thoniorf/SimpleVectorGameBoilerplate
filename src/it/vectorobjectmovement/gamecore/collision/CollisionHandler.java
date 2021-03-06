package it.vectorobjectmovement.gamecore.collision;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import it.vectorobjectmovement.gamecore.GameWorld;
import it.vectorobjectmovement.gamecore.object.AbstractEntity;
import it.vectorobjectmovement.gamecore.object.Entity;
import it.vectorobjectmovement.gamecore.object.actor.AbstractActor;
import it.vectorobjectmovement.gamecore.vector2d.Vector2D;

public class CollisionHandler {

	public static void check() {
		// get world objects
		ArrayList<Entity> objects = GameWorld.getObjects();
		// temp entity
		Entity first_entity;
		Entity second_entity;
		// check loop
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				if (objects.get(i).getAlive() && objects.get(j).getAlive()) {
					// refer current objects
					first_entity = objects.get(i);
					second_entity = objects.get(j);
					// detect collision
					if (detect(first_entity, second_entity)) {
						// resolve
						resolve((AbstractEntity) first_entity, (AbstractEntity) second_entity);
					}
				}

			}
		}
	}

	private static boolean detect(Entity first, Entity second) {
		// return shape intersection
		return first.getShape().intersects((Rectangle2D) second.getShape());
	}

	private static Vector2D penetrationDepth(AbstractEntity first, AbstractEntity second) {
		// collision axis
		int x_depth = 0;
		int y_depth = 0;
		// penetration side
		if (first.getOrigin().x <= second.getOrigin().x) {
			// LEFT
			x_depth = second.getOrigin().x - (first.getOrigin().x + first.getWidth()) - 1;
		} else {
			// RIGHT
			x_depth = (second.getOrigin().x + second.getShape().getBounds().width) - first.getOrigin().x + 1;
		}
		if (first.getOrigin().y <= second.getOrigin().y) {
			// TOP
			y_depth = second.getOrigin().y - (first.getOrigin().y + first.getHeight()) - 1;
		} else {
			// BOTTOM
			y_depth = (second.getOrigin().y + second.getShape().getBounds().height) - first.getOrigin().y + 1;
		}

		return new Vector2D(x_depth, y_depth);
	}

	private static Vector2D computeMtd(AbstractEntity first, AbstractEntity second) {
		// init minimum translation distance vector
		Vector2D mtd = new Vector2D(0, 0);
		// compute penetration depth
		Vector2D depth = penetrationDepth(first, second);
		// set mtd values
		if (Math.abs(depth.getComponents().x) <= Math.abs(depth.getComponents().y)) {
			mtd.setX(depth.getComponents().x);
		}
		if (Math.abs(depth.getComponents().y) <= Math.abs(depth.getComponents().x)) {
			mtd.setY(depth.getComponents().y);
		}
		// return the mtd vector
		return mtd;
	}

	private static Vector2D fixOverlap(AbstractEntity first, AbstractEntity second) {
		// compute mtd;
		Vector2D mtd = computeMtd(first, second);
		// translate
		first.getOrigin().move(first.getOrigin().x + mtd.getComponents().x,
				first.getOrigin().y + mtd.getComponents().y);
		// return again the mtd vector
		return mtd;
	}

	private static void resolve(AbstractEntity first, AbstractEntity second) {
		// TODO add sound effect
		// fix overlap an get the mtd vector
		Vector2D response = fixOverlap(first, second);

		// old velocity
		Vector2D oldVel_first = new Vector2D(0, 0);
		Vector2D oldVel_second = new Vector2D(0, 0);

		if (first instanceof AbstractActor && second instanceof AbstractActor) {
			// cast
			AbstractActor a1 = (AbstractActor) first;
			AbstractActor a2 = (AbstractActor) second;
			// TODO actor->actor
			oldVel_first = new Vector2D(a1.getVelocity().getComponents().x, a1.getVelocity().getComponents().y);
			oldVel_second = new Vector2D(a2.getVelocity().getComponents().x, a2.getVelocity().getComponents().y);

			// check if mtd is along x-axis
			if (response.getComponents().x != 0) {
				a1.setVelocity(new Vector2D((oldVel_first.getComponents().x + 2 * oldVel_second.getComponents().x) * -1,
						oldVel_first.getComponents().y));
				a2.setVelocity(new Vector2D(oldVel_second.getComponents().x + 2 * oldVel_first.getComponents().x,
						oldVel_second.getComponents().y));
			}
			// check if mtd is along y-axis
			if (response.getComponents().y != 0) {
				a1.setVelocity(new Vector2D(oldVel_first.getComponents().x,
						(oldVel_first.getComponents().y + 2 * oldVel_second.getComponents().y) * -1));
				a2.setVelocity(new Vector2D(oldVel_second.getComponents().x,
						oldVel_second.getComponents().y + 2 * oldVel_first.getComponents().y));
			}

		}

	}

	private CollisionHandler() {
	}
}
