package model.effects;

import java.util.ArrayList;

import model.abilities.*;
import model.world.*;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this) ;
		ArrayList<Ability> curA = c.getAbilities() ;
		for(Ability a : curA ){
			if(a instanceof DamagingAbility){
				int incDA = (int) (((DamagingAbility) a).getDamageAmount() * 0.2) ;
				((DamagingAbility) a).setDamageAmount( ((DamagingAbility) a).getDamageAmount() + incDA);
			}
			else{
				if(a instanceof HealingAbility){
					int incHA = (int) (((HealingAbility)a).getHealAmount() * 0.2) ;
					((HealingAbility) a).setHealAmount(((HealingAbility)a).getHealAmount() + incHA);
				}
			}
		}
		// not finished yet 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this) ;
		ArrayList<Ability> curA = c.getAbilities() ;
		for(Ability a : curA ){
			if(a instanceof DamagingAbility){
				//int decDA = (int) (((DamagingAbility) a).getDamageAmount() / 1.2) ;
				((DamagingAbility) a).setDamageAmount((int)  (((DamagingAbility) a).getDamageAmount()/1.2));
			}
			else{
				if(a instanceof HealingAbility){
					//int decHA = (int) (((HealingAbility)a).getHealAmount() / 1.2) ;
					((HealingAbility) a).setHealAmount((int)(((HealingAbility)a).getHealAmount()/1.2));
				}
			}
		}
		//not finished yet 
	}
}
