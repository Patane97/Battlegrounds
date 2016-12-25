package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import com.Patane.Battlegrounds.arena.ArenaHandler;
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
		// to fix ConcurrentModificationException
		Collection<GameHandler> UnmodifiedGameInstances = new ArrayList<GameHandler>();
		UnmodifiedGameInstances.addAll(GameInstances);
		for(GameHandler selectedGame : UnmodifiedGameInstances){
			selectedGame.gameOver();
		}
	}
	// collects all game names and returns a string ArrayList
	public static ArrayList<String> listGameNames(){
		ArrayList<String> gameNames = new ArrayList<String>();
		for(GameHandler selectedGame : GameInstances){
			gameNames.add(selectedGame.getName());
		}
		return gameNames;
	}
	// looks for and returns game with given name, returns null if none
	public static GameHandler getGame(String gameName){
		for(GameHandler selectedGame : GameInstances){
			if(gameName.equals(selectedGame.getName())){
				return selectedGame;
			}
		}
		return null;
	}
	public static GameHandler getGame(ArenaHandler arena){
		for(GameHandler selectedGame : GameInstances){
			if(arena.equals(selectedGame.getArena())){
				return selectedGame;
			}
		}
		return null;
	}
	public static Collection<GameHandler> get(){
		return GameInstances;
	}
}
