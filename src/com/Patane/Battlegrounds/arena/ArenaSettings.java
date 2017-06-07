package com.Patane.Battlegrounds.arena;

public class ArenaSettings {
	// blocks can be placed/broken whilst in-game || COMPLETE ||
	public final boolean DESTRUCTABLE;
	// Whether players can damage other players || COMPLETE ||
	public final boolean PVP_ENABLED;
	// Whether players spectate after dying in-game || COMPLETE || <--- Soft removePlayer in Game.teleportPlayer (see Game.teleportPlayer()) [check if this was done with spectator remake]
	public final boolean SPECTATE_DEATH;
	// Globally announces the opening of a new lobby to the server || COMPLETE || <-|
	public final boolean GLOBAL_NEW_ANNOUNCE;								   //   | Eventually need to add custom announcements
	// Globally announces the ending of a game to the server || COMPLETE || <-------|
	public final boolean GLOBAL_END_ANNOUNCE;
	
	// Final wave of this arenas game (-1 = infinite) || COMPLETE ||
	public final int FINAL_WAVE;
	// Minimum amount of players || COMPLETE ||
	public final int MIN_PLAYERS;
	// Minimum amount of players (-1 = infinite) || COMPLETE ||
	public final int MAX_PLAYERS;
	// Delay per-wave || COMPLETE ||
	public final float WAVE_DELAY;
	// Delay for First wave || COMPLETE ||
	public final float FIRST_DELAY;
	// How quickly food-level regenerates (x points per 5 seconds)(-1 = Food-Level locked, 0 = no regen)
	public final int FOOD_REGEN;
	/*
	 * Itegrate Entry-fee's and money rewards with vault
	 */
	
	public ArenaSettings(){
		this.DESTRUCTABLE 			= false;
		this.PVP_ENABLED 			= false;
		this.SPECTATE_DEATH 		= true;
		this.GLOBAL_NEW_ANNOUNCE 	= true;
		this.GLOBAL_END_ANNOUNCE 	= true;
		this.FINAL_WAVE 			= -1;
		this.MIN_PLAYERS 			= 1;
		this.MAX_PLAYERS 			= -1;
		this.WAVE_DELAY 			= 5;
		this.FIRST_DELAY 			= 10;
		this.FOOD_REGEN 			= -1;
	}
}
