package game.entities;
import game.Dir;
import game.GOType;
import game.Play;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;


/*
 * Unit with input
 */
public class Player extends Human{
	
	Input input;
	Animation normalAtk;

	public Player(Input input, Point p) throws SlickException {
		super(p);
		
		this.input = input;
		
		// move
		speed = 2;
		
		// health
		health = 100;
		
		// atk
		atkDelay = 300;
	}
	
	@Override
	public void render(Graphics g){
		super.render(g);
		g.drawString("Health: " + health, 10, 10);
	}
	
	@Override
	public void update(int delta) throws SlickException{
		super.update(delta);
		
		// Movement Input handler
		if(input.isKeyDown(Input.KEY_A)){
			move.x = -1;
		}
		else if(input.isKeyDown(Input.KEY_D)){
			move.x = 1;
		}
		else{
			move.x = 0;
		}
		
		if(input.isKeyDown(Input.KEY_W)){				
			move.y = -1;
		}
		else if(input.isKeyDown(Input.KEY_S)){				
			move.y = 1;
		}
		else{
			move.y = 0;
		}
		
		// Attack input and attack animation handler

		int attacktype = 1;
		
		if(attacktype == 0){
			if(input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_I)){
				useAttack();
			}
		}
		else{
			int bx = 0, by = 0;
			if(input.isKeyDown(Input.KEY_UP)){
				isAttacking = true;
				by = -1;
				dir = Dir.up;
			}
			else if(input.isKeyDown(Input.KEY_DOWN)){
				isAttacking = true;
				by = 1;
				dir = Dir.down;
			}
			
			if(input.isKeyDown(Input.KEY_LEFT)){
				isAttacking = true;
				bx = -1;
				dir = Dir.left;
			}
			else if(input.isKeyDown(Input.KEY_RIGHT)){
				isAttacking = true;
				bx = 1;
				dir = Dir.right;
			}
			
			if(isAttacking){
				useAttack(bx, by);

				if(bx == 0 && by == 0){
					isAttacking = false;
				}
			}
		}
	}
	
	@Override
	public void move(int delta) throws SlickException{
		canMoveX = true;
		canMoveY = true;
		
		for(GameObject go : Play.getEnemies()){
			if(canMoveX && getNewXBounds().intersects(go.getBounds())){
				canMoveX = false;
			}
			
			if(canMoveY && getNewYBounds().intersects(go.getBounds())){
				canMoveY = false;
			}
		}
		
		super.move(delta);
	}
	
	// arrow keys
	public void useAttack(int bx, int by) throws SlickException{
		if(canAtk){
			attack(bx, by);
		}
	}
	
	// 1 key for attack
	@Override	
	public void useAttack() throws SlickException{
		System.out.println(dir);
		
		if(canAtk){			
			switch(dir){
			case up: useAttack(0, -1); break;
			case down: useAttack(0, 1); break;
			case left: useAttack(-1, 0); break;
			case right: useAttack(1, 0); break;
			case upleft: useAttack(-1, -1); break;
			case upright: useAttack(1, -1); break;
			case downleft: useAttack(-1, 1); break;
			case downright: useAttack(1, 1); break;
			default: break; 
			}
		}
	}
	
	// attack based on faced direction
	public void attack() throws SlickException{
		super.attack();
	}
	
	// attack based on arrow keys
	public void attack(int bx, int by) throws SlickException{
		super.attack();
	}
	
	@Override
	public void takeDamage(int dmg){
		super.takeDamage(dmg);
		new GameText("-" + dmg, new Point(pos.getX(), pos.getY() - 30));
//		Play.addGameText("-" + dmg, new Point(pos.getX(), pos.getY() - 30));
	}

}
