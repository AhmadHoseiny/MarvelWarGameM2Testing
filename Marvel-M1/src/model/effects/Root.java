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
		
		//commented because of public tests
		//c.getAppliedEffects().add(this);
		
		//temp = c.getCondition();
		if(!c.getCondition().equals(Condition.INACTIVE)){
			c.setCondition(Condition.ROOTED);
		}
		
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		setOldCondition(c);
	
		/*if(! c.getCondition().equals(Condition.INACTIVE)){
			if(!c.getCondition().equals(Condition.ROOTED)){
				c.setCondition(Condition.ACTIVE);
			}
		}*/
		
	}
		
			
		//c.setCondition(temp);
		//not finished yet 
}


