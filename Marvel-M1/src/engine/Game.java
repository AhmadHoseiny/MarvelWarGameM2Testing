package engine;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.*;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Hero;
import model.world.Villain;
import model.world.Direction;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	public static int MnDist(Point p1,Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y-p2.y);
	}
	public static int MnDist(int x1, int y1 , int x2 , int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	 public Champion getCurrentChampion() {
		// if(!turnOrder.isEmpty()){
			 return (Champion)(turnOrder.peekMin());
		
		 /*}
		 else{
			 return null ;
		 }*/
		 
	 }
	 public Player checkGameOver() {
		
		 Player p1 = this.getFirstPlayer();
		 Player p2 = this.getSecondPlayer();
		 boolean f1 = true;
		 for(Champion c : p1.getTeam()) {
			 if(!c.getCondition().equals(Condition.KNOCKEDOUT))
			 	f1 = false;
		 }
		 if(f1)
			 return p2;
		 boolean f2 = true;
		 for(Champion c : p2.getTeam()) {
			 if(!c.getCondition().equals(Condition.KNOCKEDOUT))
			 	f2 = false;
		 }
		 if(f2)
			 return p1;
	     
		 return null;
	 }
	 // Start of move & it's helpers
	 public boolean checkOnGrid(Point p) {
		 if(p.x < 0 || p.x > 4 || p.y < 0 || p.y > 4)
			 return false;
		 return true;
	 }
	 public boolean checkNoObstacle(Point p) {
		 if(board[p.x][p.y] == null)
			 return true;
		 return false;
	 }
	 public boolean checkValidMove(Champion c , Direction d) {
		 Point newP = c.getLocation();
		 switch(d) {
		 case RIGHT : newP.y++;break;
		 case LEFT : newP.y--;break;
		 case UP : newP.x++;break;
		 case DOWN : newP.x--;break;
		 default : break;
		 }
		 if(checkOnGrid(newP) && checkNoObstacle(newP) && !c.getCondition().equals(Condition.ROOTED))
			 return true;
		 return false;
	 }
	 public void move(Direction d) throws GameActionException {
		 Champion curC = getCurrentChampion();
		 if(curC.getCurrentActionPoints() < 1) {
			 throw new NotEnoughResourcesException("Not enough ActoinPoints!");
		 }
		 else {
			 if(!checkValidMove(curC,d)) {
				 throw new UnallowedMovementException("Unallowed Movement!");
			 }
			 else {
				 switch(d) {
				 case RIGHT : curC.getLocation().y++;break;
				 case LEFT : curC.getLocation().y--;break;
				 case UP : curC.getLocation().x++;break;
				 case DOWN : curC.getLocation().x--;break;
				 default : break;
				 }
			 }
		 }
	 }
	 //end of move & it's helpers
	 
	 
	 //start of attack and its helpers
	 public boolean checkCanAttack(Champion c) throws NotEnoughResourcesException ,ChampionDisarmedException {
		 boolean res = true ;
		 if(c.getCondition().equals(Condition.INACTIVE) ||
				 c.getCondition().equals(Condition.KNOCKEDOUT)) {
			 res = false; 
		 }
		 if(c.getCurrentActionPoints()<2) {
			 res = false;
			 throw new NotEnoughResourcesException("Not enough action points") ;
		 }
		 for(Effect e : c.getAppliedEffects()) {
			 if(e instanceof Disarm) {
				 res = false ;
				 throw new ChampionDisarmedException("You are disarmed, you can't attack") ;
			 }
		 }
		 return res ;
	 }
	 public boolean checkSameTeam(Champion curCH , Champion target) {
		 ArrayList<Champion> team1 = getFirstPlayer().getTeam() ;
		 ArrayList<Champion> team2 = getSecondPlayer().getTeam() ; 
		 if(team1.contains(curCH)  ) {
			 if(team1.contains(target)) {
				 return true;
			 }
			 else {
				 return false ;
			 }
		 }
		 else {
			 if(team2.contains(target)) {
				 return true;
			 }
			 else {
				 return false ;
			 }
		 }
		 
	 }
	 public Damageable getTarget(Champion curCH , Direction d ) throws InvalidTargetException {
		 Point curLoc = curCH.getLocation();
		 int attackRange = curCH.getAttackRange() ;
		 for(int i=0 ; i<attackRange ; i++) {
			 switch(d) {
			 case RIGHT : curLoc.y++;break;
			 case LEFT : curLoc.y--;break;
			 case UP : curLoc.x++;break;
			 case DOWN : curLoc.x--;break;
			 default : break;
			 }
			 if(!checkOnGrid(curLoc)) {
				 return null ;
			 }
			 else {
				 Object[][] Grid =  this.getBoard();
				 if( Grid[curLoc.x][curLoc.y] instanceof Cover) {
					 return (Damageable) Grid[curLoc.x][curLoc.y] ;
				 }
				 else {
					 if(Grid[curLoc.x][curLoc.y] instanceof Champion) {
						 if(checkSameTeam(curCH , (Champion) Grid[curLoc.x][curLoc.y] )) {
							 return null ;
							 //throw new InvalidTargetException("Invalid Target") ;
						 }
						 else {
							 return (Damageable) Grid[curLoc.x][curLoc.y] ;
						 }
					 }
				 }
			 }
		 }
		 return null ;
	 }
	 public void dealNormalDamage(Champion c , Damageable target) {
		 target.setCurrentHP(target.getCurrentHP() - c.getAttackDamage());
	 }
	 public void dealExtraDamage(Champion c , Champion target) {
		 target.setCurrentHP(target.getCurrentHP() - (int) (c.getAttackDamage()*1.5));
	 }
	 public boolean checkAttackableTarget(Champion target) {
		 //boolean res = true ;
		 for(Effect e : target.getAppliedEffects()) {
			 if(e instanceof Shield) {
				 e.remove(target); // according to shield effect explanation
				 return false; 
			 }
			 else {
				 if(e instanceof Dodge) {
					 int rand = (int) (Math.random()*2) ; 
					 switch(rand) {
					 	case 0 : return true ; 
					 	case 1 : return false ; 
					 }
				 }
			 }
		 }
		 return true ;
	 }
	 public void attack(Direction d) throws NotEnoughResourcesException ,
	 	ChampionDisarmedException, InvalidTargetException{
		 Champion curCH = getCurrentChampion() ;
		 if(!checkCanAttack(curCH) ) {
			 return ;
		 }
		 else {
			 curCH.setCurrentActionPoints(curCH.getCurrentActionPoints() -2);
			 Damageable target = getTarget(curCH , d);
			 if( target != null) {
				// throw new InvalidTargetException("Invalid Target") ;
				 if(target instanceof Cover) {
					 dealNormalDamage(curCH,target) ;
				 }
				 else {
					 if(target instanceof Champion) {
						 Champion targetCH = (Champion) target ;
						 checkAttackableTarget(targetCH) ;
						 if(curCH instanceof Hero) {
							 if(targetCH instanceof Villain) {
								 dealExtraDamage(curCH , targetCH) ;
							 }
							 else {
								 dealNormalDamage(curCH , targetCH) ;
							 }
						 }
						 else {
							 if(curCH instanceof Villain) {
								 if(targetCH instanceof Hero) {
									 dealExtraDamage(curCH , targetCH) ;
								 }
								 else {
									 dealNormalDamage(curCH , targetCH) ;
								 }
							 }
							 else {
								 if(targetCH instanceof AntiHero ) {
									 dealNormalDamage(curCH , targetCH) ;
								 }
								 else {
									 dealExtraDamage(curCH , targetCH) ;
								 }
							 }
						 }
					 }
				 }
				 
			 }
			 }
			// else {
				
		 //}
		 
	 }
	 // end of attack and its helpers
	//start of first castAbility and its Helpers 
	 
	 
	 // a method that removes Shielded target from the final arraylist of targets 
	 //on which an ability should be executed (this method is added after tests) 
	 public void removeShieldedTargets(ArrayList<Damageable> targets){
		 for(Damageable da : targets){
			 if(da instanceof Champion ){
				 Champion curTarget = (Champion) da ;
				 for(int i=0 ; i<curTarget.getAppliedEffects().size() ; i++){
					 Effect curE = curTarget.getAppliedEffects().get(i) ;
					 if(curE instanceof Shield){
						 targets.remove(curTarget) ;
						 curE.remove(curTarget);
					 }
				 }
			 }
		 }
	 }
	public boolean checkCanCastAbility(Champion c ,Ability a) {
		if(c.getMana() < a.getManaCost() || c.getCurrentActionPoints() < a.getRequiredActionPoints()
				||c.getCondition().equals(Condition.INACTIVE) || 
				c.getCondition().equals(Condition.KNOCKEDOUT) || 
				a.getCurrentCooldown()>0 ||
				!c.getAbilities().contains(a)) // last condition may be 7anyka
			return false;
		ArrayList<Effect> Effects= c.getAppliedEffects();
		for(Effect e : Effects) {
			if(e.getName().equals("Silence"))
				return false;
		}
		return true;
	}
	public boolean checkGoodAbility(Ability a) {
		if(a instanceof DamagingAbility)
			return false;
		if(a instanceof HealingAbility)
			return true;
		CrowdControlAbility curA = (CrowdControlAbility) a ;
		Effect e = curA.getEffect() ;
		if(e.getType().equals(EffectType.BUFF)) {
			return true ;
		}
		else {
			return false;
		}
	}
	public ArrayList<Damageable> getSurroundTargets(Champion curCH){
		ArrayList<Damageable> res = new ArrayList<>();
		Point curLoc = curCH.getLocation();
		curLoc.x++; // first motion
		Object[][] Grid = this.getBoard();
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.y++;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.x--;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.x--;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.y--;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.y--;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.x++;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		curLoc.x++;
		if(checkOnGrid(curLoc) && Grid[curLoc.x][curLoc.y] != null)
			res.add((Damageable)Grid[curLoc.x][curLoc.y]);
		
		return res;
	}
	 public ArrayList<Damageable> validAbilityTargets(Champion curCH,Ability a, 
			 ArrayList<Damageable>targets) {
		 
		 ArrayList<Damageable>res = new ArrayList<>();
		 boolean damCov = false;
		 if(a instanceof DamagingAbility)
			 damCov = true;
		 if(checkGoodAbility(a)) {
			 for(Damageable da : targets) {
				 if(da instanceof Cover && damCov)
					 res.add(da);
				 else {
					 if(da instanceof Champion && checkSameTeam(curCH , (Champion)da))
						 res.add(da);
				 }
			 }
		 }
		 else {
			 for(Damageable da : targets) {
				 if(da instanceof Cover && damCov)
					 res.add(da);
				 else {
					 if(da instanceof Champion && !checkSameTeam(curCH , (Champion)da))
						 res.add(da);
				 }
			 }
		 }
		 return res;
	 }
	 public void castAbility(Ability a) throws AbilityUseException,InvalidTargetException ,CloneNotSupportedException{
		 Champion curCH = getCurrentChampion();
		 if(! checkCanCastAbility(curCH,a)) {
			 throw new AbilityUseException("You're in a condition that "
			 		+ "prevents you from casting an ability") ;
			// return ;
		 }
		 else {
			 curCH.setCurrentActionPoints(curCH.getCurrentActionPoints()-a.getRequiredActionPoints());
			 curCH.setMana(curCH.getMana()-a.getManaCost()) ;
			 for(Ability ab : curCH.getAbilities() ) {
					if(ab.getName().equals(a.getName())) {
						ab.setCurrentCooldown(ab.getBaseCooldown());
					}
			 }
			 boolean good = checkGoodAbility(a);
			 if(a.getCastArea().equals(AreaOfEffect.SELFTARGET)) {
				 if(good) {
					 ArrayList<Damageable> targets = new ArrayList<>() ;
					 targets.add(curCH) ;
					 
					 // added because of tests
					 removeShieldedTargets(targets) ;
					 
					 a.execute(targets);
				 }
			 }
			 else {
				 if(a.getCastArea().equals(AreaOfEffect.TEAMTARGET)) {
					 ArrayList<Champion> curTeam ;
					 ArrayList<Champion> oppTeam ;
					 if(getFirstPlayer().getTeam().contains(curCH)) {
						 curTeam = getFirstPlayer().getTeam();
						 oppTeam = getSecondPlayer().getTeam();
					 }
					 else {
						 curTeam = getSecondPlayer().getTeam();
						 oppTeam = getFirstPlayer().getTeam();
					 }
					 if(good) {
						 ArrayList<Damageable> targets = new ArrayList<>() ;
						 for(Champion c2  : curTeam) {
							 if(MnDist(c2.getLocation() , curCH.getLocation()) <= a.getCastRange()) {
								 targets.add(c2) ;
							 }
						 }
						 /*
						 // added because of tests
						 removeShieldedTargets(targets) ;
						 */
						 a.execute(targets);

						 
						 
					 }
					 else {
						 ArrayList<Damageable> targets = new ArrayList<>() ;
						 for(Champion c2  : oppTeam) {
							 if(MnDist(c2.getLocation() , curCH.getLocation()) <= a.getCastRange()) {
								 targets.add(c2) ;
							 }
						 }
						 /*
						 // added because of tests
						 removeShieldedTargets(targets) ;
						 */
						 a.execute(targets);
					 }
				 }
				 else {
					 if(a.getCastArea().equals(AreaOfEffect.SURROUND)) {
						 ArrayList<Damageable> potenialTargets = getSurroundTargets(curCH);
						 if(potenialTargets.isEmpty()) {
							//throw new InvalidTargetException("Invalid target!");
						 }
						 else {
							 ArrayList<Damageable>finalTargets = validAbilityTargets(curCH,a,potenialTargets);
							 if(finalTargets.isEmpty()){
								//throw new InvalidTargetException("Invalid target!"); 
							 }
							 else {
								 /*
								 // added because of tests
								 removeShieldedTargets(finalTargets) ;
								 */
								 a.execute(finalTargets);
							 }
						 }
					 }
				 }
			 }
			 
		 }
	 }
	 public ArrayList<Damageable> getDirectionalTargets(Champion curCH, Ability a,Direction d){
		 Point curLoc = curCH.getLocation();
		 ArrayList<Damageable> res = new ArrayList<>();
		 int range = a.getCastRange();
		 Object Grid[][] = this.getBoard();
		 
		 //1st implementation (a champion can act as a cover) 
		/* boolean good = checkGoodAbility(a) ;
		 for(int i=0 ; i<range ; i++) {
			 switch(d) {
			 case RIGHT : curLoc.y++;break;
			 case LEFT : curLoc.y--;break;
			 case UP : curLoc.x++;break;
			 case DOWN : curLoc.x--;break;
			 default : break;
			 }
			 if(checkOnGrid(curLoc) ) {
				 if(Grid[curLoc.x][curLoc.y] instanceof Cover) {
					 res.add((Damageable)Grid[curLoc.x][curLoc.y]) ;
					 break ;
				 }
				 else {
					 if(good) {
						 if(!checkSameTeam(curCH , (Champion) Grid[curLoc.x][curLoc.y]))
							 break ;
						 else {
							 res.add((Damageable) Grid[curLoc.x][curLoc.y] ) ;
						 }
					 }
					 else {
						 if(checkSameTeam(curCH , (Champion) Grid[curLoc.x][curLoc.y]))
							 break ;
						 else {
							 res.add((Damageable) Grid[curLoc.x][curLoc.y] ) ;
						 }
					 }
					 
				 }
				 
			 }
		 }*/
		 
		 //2nd implementation (a champion cannot act as a cover)
		 for(int i=0 ; i<range ; i++) {
			 switch(d) {
			 case RIGHT : curLoc.y++;break;
			 case LEFT : curLoc.y--;break;
			 case UP : curLoc.x++;break;
			 case DOWN : curLoc.x--;break;
			 default : break;
			 }
			 if(checkOnGrid(curLoc) ) {
				 if(Grid[curLoc.x][curLoc.y] instanceof Cover) {
					 res.add((Damageable)Grid[curLoc.x][curLoc.y]) ;
					 break ;
				 }
				 else {
					 res.add((Damageable) Grid[curLoc.x][curLoc.y] ) ; 
				 }
				 
			 }
		 }
	
		 return res;
	 }
	 public void castAbility(Ability a, Direction d) throws AbilityUseException, 
	 	InvalidTargetException, CloneNotSupportedException {
		 if(a.getCastArea().equals(AreaOfEffect.DIRECTIONAL)) {
			 Champion curCH = getCurrentChampion();
			 if(!checkCanCastAbility(curCH , a)) {
				 throw new AbilityUseException("You're in a condition that "
				 		+ "prevents you from casting an ability") ;
			 }
			 else {
				 curCH.setCurrentActionPoints(curCH.getCurrentActionPoints()-a.getRequiredActionPoints());
				 curCH.setMana(curCH.getMana()-a.getManaCost()) ;
				 for(Ability ab : curCH.getAbilities() ) {
						if(ab.getName().equals(a.getName())) {
							ab.setCurrentCooldown(ab.getBaseCooldown());
						}
				 }
				 ArrayList<Damageable>potenialTargets = getDirectionalTargets(curCH,a,d);
				 if(potenialTargets.isEmpty())
					 throw new InvalidTargetException("Invalid Target!");
				 ArrayList<Damageable>finalTargets = validAbilityTargets(curCH,a,potenialTargets);
				 if(finalTargets.isEmpty())
					 throw new InvalidTargetException("Invalid Target!");
				 /*
				 // added because of tests
				 removeShieldedTargets(finalTargets) ;
				 */
				 a.execute(finalTargets);
				 
			 }
		 }
		
		 
			 
	 }
	 public void castAbility(Ability a, int x, int y) throws AbilityUseException, InvalidTargetException,
	 					CloneNotSupportedException {
		 if(a.getCastArea().equals(AreaOfEffect.SINGLETARGET)) {
			 Champion curCH = getCurrentChampion();
			 Point curLoc = curCH.getLocation();
			 Object Grid [][] =  getBoard();
			 if(!checkCanCastAbility(curCH , a)) {
				 throw new AbilityUseException("You're in a condition that "
				 		+ "prevents you from casting an ability") ;
			 }
			 else {
				 Damageable potentialTarget = (Damageable)Grid[x][y];
				 if(potentialTarget==null) {
					 throw new InvalidTargetException("Invalid Target!");
				 }
				 else {
					 if(a.getCastRange() < MnDist(curLoc.x,curLoc.y,x,y)) {
						 throw new InvalidTargetException("Invalid Target!");
					 }
					 else {
							ArrayList<Damageable> pTArr=new ArrayList<>(); 
							pTArr.add(potentialTarget); 
							ArrayList<Damageable> TargetArr= validAbilityTargets(curCH,a,pTArr);
							if(TargetArr.isEmpty()) {
								throw new InvalidTargetException("Invalid Target!");
							}
							else {
								 curCH.setCurrentActionPoints(curCH.getCurrentActionPoints()-a.getRequiredActionPoints());
								 curCH.setMana(curCH.getMana()-a.getManaCost()) ;
								 for(Ability ab : curCH.getAbilities() ) {
										if(ab.getName().equals(a.getName())) {
											ab.setCurrentCooldown(ab.getBaseCooldown());
										}
								 }
								 /*
								 // added because of tests
								 removeShieldedTargets(TargetArr) ;
								 */
								a.execute(TargetArr);
							} 
							 
						 }
				 }
			
				 	 
			 }
			 			 
		 }
	 }
	 
	 public ArrayList<Champion> getLeaderTargets(Champion curLea,Champion oppLea,
			 ArrayList<Champion>curTeam,
			 ArrayList<Champion>oppTeam){
		 ArrayList<Champion> res = new ArrayList<>();
		 if(curLea instanceof Hero) {
			 res = curTeam;
		 }
		 else {
			 if(curLea instanceof Villain)
				 res = oppTeam;
			 else {
				 for(Champion c : curTeam ) {
					 if(!c.equals(curLea))
						 res.add(c);
				 }
				 for(Champion c : oppTeam ) {
					 if(!c.equals(oppLea))
						 res.add(c);
				 }
			 }
		 }
		 
		 
		 
		 return res;
	 }
	 
	 
	 public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException {
		 Champion curCH = getCurrentChampion();
		 //if(curCH != null){
			 Champion leader1 = getFirstPlayer().getLeader();
			 Champion leader2 = getSecondPlayer().getLeader();
			 ArrayList<Champion> team1 = getFirstPlayer().getTeam();
			 ArrayList<Champion> team2 = getSecondPlayer().getTeam();
			 
			 if(!curCH.equals(leader1) && !curCH.equals(leader2)) {
				 throw new LeaderNotCurrentException("This champion isn't the leader!");
			 }
			 else {
				 if((curCH.equals(leader1) && isFirstLeaderAbilityUsed()) || 
						 (curCH.equals(leader2) && isSecondLeaderAbilityUsed()) )
					 throw new LeaderAbilityAlreadyUsedException("Leader Ability already used!");
				 else {
					 ArrayList<Champion>targets = new ArrayList<>();
					 if(curCH.equals(leader1)) {
						 targets = getLeaderTargets(curCH,leader2,team1,team2);
					 }
					 else {
						 targets = getLeaderTargets(curCH,leader2,team2,team1);
					 }
					 curCH.useLeaderAbility(targets);
				 }
			 }
		 //}
		 
	 }
	 public boolean update(Champion c , Condition oldCondition) {
		 boolean res = true ;
		 for(/*Effect e : c.getAppliedEffects()*/ int i=0 ; i<c.getAppliedEffects().size() ; i++) {
			 Effect e = c.getAppliedEffects().get(i) ;
			 e.setDuration(e.getDuration() -1);
			 if(e.getDuration() == 0) {
				 e.remove(c);
			 }
		 }
		 for(Ability a : c.getAbilities()) {
			 a.setCurrentCooldown(a.getCurrentCooldown() -1);
		 }
		 c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		 if(c.getCurrentHP()==0) {
			 c.setCondition(Condition.KNOCKEDOUT);
			 Point locToBeEmptied = c.getLocation() ;
			 Object [][] Grid = getBoard() ;
			 Grid[locToBeEmptied.x][locToBeEmptied.y] = null ;
			 if(! oldCondition.equals(Condition.INACTIVE) ) {
				 turnOrder.remove() ;
			 }
			 res = false ;
		 }
		 return res ;
	 }
	 public void endTurn() {
		 Champion removedCH = (Champion) getTurnOrder().remove() ;
		 if(turnOrder.isEmpty()) {
			 prepareChampionTurns() ;
		 }
		 while(!turnOrder.isEmpty()) {
			 Champion curCH = (Champion) turnOrder.peekMin() ;
			 boolean isINACTIVE = false;
			 if(curCH.getCondition().equals(Condition.INACTIVE)) {
				 /*Champion removedCurrentlyCH = (Champion)*/ getTurnOrder().remove() ;
				 isINACTIVE = true;
			 }
			 //toBreeak indicates that the Champion is NOT knockedOut
			 boolean toBreak = update(curCH , curCH.getCondition()) ;
			 if(toBreak && !isINACTIVE) {
				 break ;
			 }
		 }
	 }
	 
	 private void prepareChampionTurns() {
		 for(Champion c : getFirstPlayer().getTeam()) {
			 if(!c.getCondition().equals(Condition.KNOCKEDOUT)) {
				 turnOrder.insert(c);
			 }
		 }
		 for(Champion c : getSecondPlayer().getTeam()) {
			 if(!c.getCondition().equals(Condition.KNOCKEDOUT)) {
				 turnOrder.insert(c);
			 }
		 }
	 }
	 
	 
	 
	 
	 
	 
	 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
}
