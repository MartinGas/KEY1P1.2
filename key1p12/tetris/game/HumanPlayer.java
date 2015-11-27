package key1p12.tetris.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Direction.*;


public class HumanPlayer implements KeyListener extends Player{

	private String name;
	private direction move;
	
	public Player(String name){
		this.name = name;
	}
	
	public void KeyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.move = GameMove.MLEFT;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.move = GameMove.MRIGHT;
        }
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.move = GameMove.TURN;
        }
		if (e.getKeyCode() == KeyEvent.VK_SPACE){
			this.move = GameMove.FALL; 
		}
    }
        
	public Direction getMove(){
		Direction temp = move;
		this.move = null;
		return temp;
    }
	
	public String getName(){
		return name;
	}
}	