package model.effects;
import model.world.*;
import model.abilities.* ;
import java.util.* ;
public class Disarm extends Effect {
	
	//static int tempAD = 0;
	
	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
	}
	public void apply(Champion c){
		
		//commented because of public tests
		//c.getAppliedEffects().add(this) ;
		
		//tempAD = c.getAttackDamage() ;
		//c.setAttackDamage(0);
		DamagingAbility da = new DamagingAbility("Punch" , 0, 1,1,AreaOfEffect.SINGLETARGET, 1, 50 ) ;
		c.getAbilities().add(da) ;
		// almost finished  
	}
	public void remove(Champion c){
		//c.getAppliedEffects().remove(this) ;
		//c.setAttackDamage(tempAD);
		ArrayList<Ability> curA = c.getAbilities() ;
		ArrayList<Ability> toBeRemoved = new ArrayList<>() ;
		for(Ability a : curA){
			if(a.getName().equals("Punch")){
				//curA.remove(a) ;
				toBeRemoved.add(a) ;
				break ;
			}
		}
		for(Ability a : toBeRemoved){
			curA.remove(a) ;
		}
		//not finished yet 
	}
	
}
