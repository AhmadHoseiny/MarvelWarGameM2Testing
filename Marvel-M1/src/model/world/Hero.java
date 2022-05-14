package model.world;

import java.util.ArrayList;

import model.effects.*;

public class Hero extends Champion {
	
	
	
	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	public void useLeaderAbility(ArrayList<Champion> targets)  {
		for(Champion c : targets) {
			ArrayList<Effect> curE = c.getAppliedEffects();
			ArrayList<Effect> toBeRemovedEf = new ArrayList<>() ;
			for(int i=0 ; i<curE.size() ; i++) {
				Effect e = curE.get(i) ;
				if(e.getType().equals(EffectType.DEBUFF)) {
					toBeRemovedEf.add(e) ;
					//e.remove(c);
				}		
			}
			for(Effect e : toBeRemovedEf){
				e.remove(c) ;
			}
			Effect newE = new Embrace(2);
			newE.apply(c);
			c.getAppliedEffects().add(newE) ;
		}
	}
	
}
