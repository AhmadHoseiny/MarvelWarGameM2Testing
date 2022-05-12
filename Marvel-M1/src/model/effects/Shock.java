package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
		
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this);
		int decSP = (int)(c.getSpeed() - c.getSpeed()*0.1);
		c.setSpeed(decSP);
		int decAD = (int)(c.getAttackDamage()-c.getAttackDamage()*0.1);
		c.setAttackDamage(decAD);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		c.setSpeed((int)(c.getSpeed()/0.9));
		c.setAttackDamage((int)(c.getAttackDamage()/0.9));
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		//not finished yet 
	}
}
