package tankgame.tank.controller;

import tankgame.tank.Tank;

public class FixedTankAI extends BasicAI implements TankController {

	private Tank own;
	private Tank tgt;
	private int effectiveRange = 100;
	
	public FixedTankAI(Tank tgt) {
		this.tgt = tgt;
	}
	
	@Override
	public void setTank(Tank t) {
		own = t;
	}

	@Override
	public void action() {
		int range = rangeFind(own.getCenter(), tgt.getCenter());
		
		if (range < effectiveRange) {
			attack();
		} else {
			standby();
		}
	}	
	
	
	private void standby() {
		int front = own.getFrontDirection().getDegree();
		int head = own.getHeadDirection().getDegree();
		
		if (front == head) {
			//向きがあってたら何もしないで抜ける
			return;
		}
		
		int ad = angleDiffalence(front, head);
		if (ad < 0) {
			own.turnHead(-1);
		} else if (ad > 0) {
			own.turnHead(1);
		}
	}
	
	
	private void attack() {
		if (isLeft(own.getCenter(), tgt.getCenter(), own.getHeadDirection())) {
			own.turnHead(Tank.LEFT);
		} else {
			own.turnHead(Tank.RIGHT);
		}
		own.fire(0);
	}
	
	
	//スレッドを持たないので何もしない
	@Override public void dead() {}
	@Override public void start() {}
}