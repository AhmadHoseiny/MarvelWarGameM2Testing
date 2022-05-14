package model.world;

import java.awt.Point;
import java.util.ArrayList;
import model.abilities.Ability;
import model.effects.Effect;

public abstract class Champion implements Damageable , Comparable<Champion> {
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	

	public Champion(String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = this.maxHP;
		this.maxActionPointsPerTurn = actions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.currentActionPoints=maxActionPointsPerTurn;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}

	public void setCurrentHP(int hp) {

		if (hp < 0) {
			currentHP = 0;
			
		} 
		else if (hp > maxHP)
			currentHP = maxHP;
		else
			currentHP = hp;

	}

	
	public int getCurrentHP() {

		return currentHP;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int currentSpeed) {
		if (currentSpeed < 0)
			this.speed = 0;
		else
			this.speed = currentSpeed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point currentLocation) {
		this.location = currentLocation;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints>maxActionPointsPerTurn)
			currentActionPoints=maxActionPointsPerTurn;
		else 
			if(currentActionPoints<0)
			currentActionPoints=0;
		this.currentActionPoints = currentActionPoints;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	
	
	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abilities == null) ? 0 : abilities.hashCode());
		result = prime * result + ((appliedEffects == null) ? 0 : appliedEffects.hashCode());
		result = prime * result + attackDamage;
		result = prime * result + attackRange;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + currentActionPoints;
		result = prime * result + currentHP;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + mana;
		result = prime * result + maxActionPointsPerTurn;
		result = prime * result + maxHP;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + speed;
		return result;
	}*/

	@Override
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Champion other = (Champion) obj;
		if (abilities == null) {
			if (other.abilities != null)
				return false;
		} else if (!abilities.equals(other.abilities))
			return false;
		if (appliedEffects == null) {
			if (other.appliedEffects != null)
				return false;
		} else if (!appliedEffects.equals(other.appliedEffects))
			return false;
		if (attackDamage != other.attackDamage)
			return false;
		if (attackRange != other.attackRange)
			return false;
		if (condition != other.condition)
			return false;
		if (currentActionPoints != other.currentActionPoints)
			return false;
		if (currentHP != other.currentHP)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (mana != other.mana)
			return false;
		if (maxActionPointsPerTurn != other.maxActionPointsPerTurn)
			return false;
		if (maxHP != other.maxHP)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (speed != other.speed)
			return false;
		return true;
	}*/

	//@Override
	public int compareTo(Champion c) {
		if(this.getSpeed() > c.getSpeed())
			return -1;
		else {
			if(this.getSpeed() < c.getSpeed())
				return 1;
			else {
				return this.getName().compareTo(c.getName());
			}
		}	
	}
	public abstract void useLeaderAbility(ArrayList<Champion> targets);
}
