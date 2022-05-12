package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect{

	public SpeedUp(int duration) {
		super("SpeedUp",duration,EffectType.BUFF);
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this);
		int incSP = (int)(c.getSpeed() + c.getSpeed()*0.15);
		c.setSpeed(incSP);
		c.setCurrentActionPoints(c.getCurrentActionPoints() +1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() +1);
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		c.setSpeed((int)(c.getSpeed()/1.15));
		c.setCurrentActionPoints(c.getCurrentActionPoints() -1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() -1);
		//not finished yet 
	}
}
