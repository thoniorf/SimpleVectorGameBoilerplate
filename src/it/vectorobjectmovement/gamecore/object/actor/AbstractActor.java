package it.vectorobjectmovement.gamecore.object.actor;

import java.awt.Point;
import java.awt.Rectangle;

import it.vectorobjectmovement.gamecore.animation.Sprite;
import it.vectorobjectmovement.gamecore.ia.AbstractIA;
import it.vectorobjectmovement.gamecore.object.AbstractEntity;
import it.vectorobjectmovement.gamecore.object.direction.Direction;
import it.vectorobjectmovement.gamecore.vector2d.Vector2D;

public abstract class AbstractActor extends AbstractEntity implements Actor {
	protected static final int max_velocity = 4;
	protected static final int charge_velocity = 8;

	protected static final long charge_countdown = 5000;

	public static int getMaxVelocity() {
		return max_velocity;
	}

	protected Vector2D velocity;
	protected Direction face_dir;
	protected Direction move_dir;
	protected Boolean can_charge;
	protected Boolean charge;
	protected long charge_time;
	protected AbstractIA strategy;
	// sprite images
	protected Sprite head, body, mount;

	public AbstractActor(Point origin, int idHead, int idBody, int idMount) {
		super(origin);
		this.velocity = new Vector2D(0, 0);
		this.move_dir = Direction.stop;
		this.face_dir = Direction.est;
		this.can_charge = Boolean.TRUE;
		this.charge_time = 0;
		this.charge = Boolean.FALSE;
		this.strategy = null;

		// TODO sprite loader
	}

	public Boolean canCharge() {
		return can_charge;
	}

	public void charge() {
		if (can_charge) {
			charge = Boolean.TRUE;
		}
	}

	@Override
	public Direction getFaceDirection() {
		return face_dir;
	}

	@Override
	public Direction getMoveDirection() {
		return move_dir;
	}

	@Override
	public Vector2D getVelocity() {
		return velocity;
	}

	private void incVelX(Integer v) {
		if (Math.abs(velocity.getComponents().x + v) <= max_velocity) {
			velocity.setX(velocity.getComponents().x + v);
		}
	}

	private void incVelY(Integer v) {
		if (Math.abs(velocity.getComponents().y + v) <= max_velocity) {
			velocity.setY(velocity.getComponents().y + v);
		}
	}

	@Override
	public void setFaceDirection(Direction face_dir) {
		this.face_dir = face_dir;

	}

	@Override
	public void setMoveDirection(Direction face_dir) {
		this.move_dir = face_dir;

	}

	@Override
	public void setVelocity(Vector2D velocity) {
		if (velocity.getComponents().x <= max_velocity) {
			this.velocity.setX(velocity.getComponents().x);
		} else {
			this.velocity.setX(max_velocity);
		}

		if (velocity.getComponents().y <= max_velocity) {
			this.velocity.setY(velocity.getComponents().y);
		} else {
			this.velocity.setY(max_velocity);
		}
	}

	@Override
	public void update() {
		super.update();
		if (strategy != null) {
			strategy.update();
		}
		// Movement
		switch (move_dir) {
		case est:
			incVelX(1);
			break;
		case nord:
			incVelY(-1);
			break;
		case ovest:
			incVelX(-1);
			break;
		case sud:
			incVelY(1);
			break;
		default:
			velocity.getComponents().x *= 0.99 * Math.random();
			velocity.getComponents().y *= 0.99 * Math.random();
			break;

		}
		// Charge
		if (!move_dir.equals(Direction.stop)) {
			face_dir = move_dir;
		}

		if (System.currentTimeMillis() - charge_time >= charge_countdown) {
			can_charge = Boolean.TRUE;
		}
		if (charge) {
			charge = Boolean.FALSE;
			can_charge = Boolean.FALSE;
			charge_time = System.currentTimeMillis();
			switch (face_dir) {
			case est:
				velocity.setX(charge_velocity);
				break;
			case nord:
				velocity.setY(charge_velocity * -1);
				break;
			case ovest:
				velocity.setX(charge_velocity * -1);
				break;
			case sud:
				velocity.setY(charge_velocity);
				break;
			default:
				break;

			}
		}

		// move
		origin.x += velocity.getComponents().x;
		origin.y += velocity.getComponents().y;
		// set shape bounds
		((Rectangle) shape).setBounds(origin.x - width / 2, origin.y - height / 2, width, height);

		// sprite updates
		head.update(face_dir);
		body.update(face_dir);
		mount.update(face_dir);
	}
}
