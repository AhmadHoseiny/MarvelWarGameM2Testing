package model.abilities;

import model.effects.*;

import java.util.ArrayList;
import model.world.Damageable;
import model.world.* ;
public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException{
		for(Damageable da : targets){
			if(da instanceof Champion){
				Champion c = (Champion) da ;
				Effect e = (Effect) (this.getEffect().clone()) ;
				e.apply(c);
				/*for(Ability a : c.getAbilities() ) {
					if(a.equals(this)) {
						a.setCurrentCooldown(a.getBaseCooldown());
					}
				}*/
			}
			
		}
		
	}

}
