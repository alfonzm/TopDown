package game.entities;

import game.PickableType;
import game.Play;
import game.Sounds;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Pickable extends GameObject{	
	PickableType type;
	int value;
	
	int lifespan = 7000, life;
	
	public static float expMod = 1;
	public static float goldMod = 1;
	
	public Pickable(Point p, int value, PickableType type) throws SlickException {
		super(p);
		this.type = type;
		this.value = value;
		life = lifespan;
		
		sprite = new Image("res/lifeBar.png");
		bounds = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());
	}

	@Override
	public void update(int delta) throws SlickException {
		life -= delta;
		
		if(life < 0 ){
			isAlive = false;
		}
	}

	@Override
	public void render(Graphics g) {
		if(type == PickableType.exp){
			sprite.setImageColor(0, 0, 255, (float) life/(lifespan/2));
		}
		else if(type == PickableType.gold){
			sprite.setImageColor(255, 0, 255, (float) life/(lifespan/2));
		}
		
		g.drawImage(sprite, pos.getX(), pos.getY());
	}

	public void pickedUp(){
		isAlive = false;
		
		switch(type){			
		case exp:
			String text;
			value *= expMod;
			if(expMod > 1)
				text = "EXP x2\n+" + value;
			else
				text = "+" + value;

			new GameText(text, pos, Color.cyan);
			Play.p.addExp(value);
			Play.totalExp += value;
			
			Sounds.exp.play();
			break;
			
		case gold:
			String text2;
			
			value *= goldMod;
			
			if(goldMod > 1)
				text2 = "GOLD x2\n+" + value;
			else
				text2 = "+" + value;

			new GameText(text2, pos, Color.yellow);
			Play.p.goldToAdd += value;
			Play.totalGold += value;
			
			Sounds.coin.play();
			break;
			
		default:
			break;
		}
	}
}