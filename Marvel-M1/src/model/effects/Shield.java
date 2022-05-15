package model.effects;

import model.world.Champion;

public class Shield extends Effect {
	
	//static int temp = 0; 
	
	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}
	public void apply(Champion c){
		
		//commented because of public tests
		//c.getAppliedEffects().add(this);
		
		//temp = c.getSpeed();
		int incsp = (int) (c.getSpeed() + c.getSpeed()*0.02);
		c.setSpeed(incsp);
		// not finished yet 
	}
	public void remove(Champion c){
		//c.getAppliedEffects().remove(this);
		c.setSpeed( (int) (c.getSpeed()/1.02));
		//not finished yet 
	}
}
