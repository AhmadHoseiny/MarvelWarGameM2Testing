package model.effects;

import java.util.ArrayList;

import model.world.* ;

public abstract class Effect implements Cloneable {
	private String name;
	private EffectType type;
	private int duration;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}

	public Object clone() throws CloneNotSupportedException{
		return super.clone() ;
	}
	public abstract void apply(Champion c) ;
	public abstract void remove(Champion c) ;
	
	public void setOldCondition(Champion c) {
		ArrayList<Effect> Effects = c.getAppliedEffects();
		
		boolean f1I = false;
		boolean f2R = false;
		for(Effect e : Effects) {
			if(e instanceof Stun){
				f1I = true;
			}
				
			else {
				if(e instanceof Root){
					f2R = true;
				}
					
			}
		}
		if(f1I)
			c.setCondition(Condition.INACTIVE);
		else {
			if(f2R)
				c.setCondition(Condition.ROOTED);
			else {
				c.setCondition(Condition.ACTIVE);
			}
	    }
	}
}
