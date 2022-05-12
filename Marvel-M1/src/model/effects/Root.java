package model.effects;

import java.util.ArrayList;
import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.*;


public class Root extends Effect {
	
	//static Condition temp = Condition.ACTIVE;

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this);
		//temp = c.getCondition();
		c.setCondition(Condition.ROOTED);
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		setOldCondition(c);
	}
		
			
		//c.setCondition(temp);
		//not finished yet 
}


