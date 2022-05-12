package model.effects;

import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
		
	}
	public void apply(Champion c){
		c.getAppliedEffects().add(this) ;
		//int rand = (int) (Math.random()*2) ;
		int s = c.getSpeed() ;
		c.setSpeed( (int) ( s+ (0.05*s))) ;
		// almost finished 
	}
	public void remove(Champion c){
		c.getAppliedEffects().remove(this) ;
		//int decS = (int) (c.getSpeed() /1.05) ; 
		c.setSpeed((int)(c.getSpeed()/1.05)) ;
		//not finished yet 
	}
}
