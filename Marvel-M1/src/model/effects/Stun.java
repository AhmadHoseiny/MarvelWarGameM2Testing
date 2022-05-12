package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {
	
	//static Condition temp = Condition.ACTIVE;
	
	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this);
		//temp = c.getCondition();
		c.setCondition(Condition.INACTIVE);
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		setOldCondition(c);
		//c.setCondition(temp);
		//not finished yet 
	}

}
