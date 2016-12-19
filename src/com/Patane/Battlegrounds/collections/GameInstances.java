package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import com.Patane.Battlegrounds.game.GameHandler;

public class GameInstances {

	private static Collection<GameHandler> GameInstances = new ArrayList<GameHandler>();
	
	// adding or removing from the collection of game instances
	public static void add(GameHandler game){
		GameInstances.add(game);
	}
	public static void remove(GameHandler game){
		GameInstances.remove(game);
	}
	// method to simply end all games running
	public static void endGameAll(){
		for(GameHandler selectedgame : GameInstances){
			selectedgame.gameOver();
		}
	}
	public static boolean gameHasName(String name){
		for(GameHandler selectedgame : GameInstances){
			if(name.equals(selectedgame.getName())) return true;
		}
		return false;
	}
	
	public static ArrayList<String> listGameNames(){
		ArrayList<String> gameNames = new ArrayList<String>();
		for(GameHandler selectedgame : GameInstances){
			gameNames.add(selectedgame.getName());
		}
		return gameNames;
	}
	public static GameHandler getGame(String gameName){
		for(GameHandler selectedgame : GameInstances){
			if(gameName.equals(selectedgame.getName())){
				return selectedgame;
			}
		}
		return null;
	}
}
