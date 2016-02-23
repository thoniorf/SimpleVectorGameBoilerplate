package it.vectorobjectmovement.gamecore.object.actor;

import it.vectorobjectmovement.gamecore.object.direction.Direction;
import it.vectorobjectmovement.gamecore.vector2d.Vector2D;

public interface Actor {

	public Vector2D getVelocity();

	public void setVelocity(Vector2D velocity);

	public Direction getFaceDirection();

	public void setFaceDirection(Direction face_dir);

	public Direction getMoveDirection();

	public void setMoveDirection(Direction face_dir);
}
