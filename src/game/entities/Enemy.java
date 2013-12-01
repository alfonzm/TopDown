
package game.entities;

import game.Dir;
import game.Game;
import game.Play;
import game.Vectors;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class Enemy extends Human{
	
	Random r;
	Point targetPoint;

	public Enemy(Point p) throws SlickException {
		super(p);
		r = new Random();
		targetPoint = new Point(0, 0);
		
		speed = 1;
		
		atkDelay = 2000;
		
		initMoveAnimations("enemy1");
		initAttackAnimations("enemy1");
	}
	
	@Override
	public void move(int delta) throws SlickException{
		canMoveX = true;
		canMoveY = true;
		
		// Collision with other mobs and player
		for(GameObject go : Play.getEnemies()){
			if(this != go && getNewXBounds().intersects(go.getBounds())){
				canMoveX = false;
			}
			
			if(this != go && getNewYBounds().intersects(go.getBounds())){
				canMoveY = false;
			}
		}
		
		// while at entrance gates
		if(pos.getY() > 85 && pos.getY() < 185 && pos.getX() < 20){
			canMoveY = false;
		}
		if((pos.getX() < 305 || pos.getX() > 417 - Game.TS) && pos.getY() < 95){
			canMoveX = false;
		}
		
//		if(!canMoveX){ // temporary solution, move down if cannot move x
//			move.y = speed/2;
//		}
		
		checkCollisionWithPlayer();
		
		super.move(delta);
	}
	
	@Override
	public void updateDirection(){
		if(Math.abs(move.y) > Math.abs(move.x)){
			if(move.y > 0)
				dir = Dir.down;
			else
				dir = Dir.up;
		}
		else if(move.x < 0)
			dir = Dir.left;
		else if(move.x > 0)
			dir = Dir.right;
	}
	
	public void checkCollisionWithPlayer() throws SlickException{
//		System.out.println(move.x + " " + move.y);
		
		boolean atk = false;
		
		if(getNewXBounds().intersects(Play.p.getNewXBounds())){
			if(Play.p.isDashing != true){
				canMoveX = false;
				atk = true;	
			}
			else{
				this.takeDamage(Play.p.damage);
			}
		}
		
		if(getNewYBounds().intersects(Play.p.getNewYBounds())){
			if(Play.p.isDashing != true){
				canMoveY = false;
				atk = true;
			}
			else{
				this.takeDamage(Play.p.damage);
			}
		}
		
		if(atk){
			useAttack();
			isAttacking = true;
		}
		else{
			isAttacking = false;
		}
	}
	
	public void attack() throws SlickException{
		super.attack();
		Play.p.takeDamage(damage);
	}
	
	public void recalculateVector(float newX, float newY){
		float rad = Vectors.getRad(pos.getX(), newX, pos.getY(), newY);
		move.x = (float) Math.sin(rad) * speed;
		move.y = (float) Math.cos(rad) * speed;
	}
}