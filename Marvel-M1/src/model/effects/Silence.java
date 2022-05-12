package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this);
		c.setCurrentActionPoints(c.getCurrentActionPoints() +2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() +2);
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		c.setCurrentActionPoints(c.getCurrentActionPoints() -2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() -2);
		//not finished yet 
	}
}
