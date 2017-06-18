package com.Patane.Battlegrounds.arena.settings;

import com.Patane.Battlegrounds.arena.Arena;

public class ArenaSettings {
	// blocks can be placed/broken whilst in-game || COMPLETE || ||| Icon: TNT
	public final boolean DESTRUCTABLE;
	// Whether players can damage other players || COMPLETE || ||| Icon: WOODEN SWORD
	public final boolean PVP_ENABLED;
	// Whether players spectate after dying in-game || COMPLETE || ||| Icon: 
	public final boolean SPECTATE_DEATH;
	// Globally announces the opening of a new lobby to the server || COMPLETE || <-| ||| Icon: 
	public final boolean GLOBAL_NEW_ANNOUNCE;								   //   | Eventually need to add custom announcements
	// Globally announces the ending of a game to the server || COMPLETE || <-------|
	public final boolean GLOBAL_END_ANNOUNCE;

	// Delay for First wave || COMPLETE ||
	public final float FIRST_DELAY;
	// Delay per-wave || COMPLETE ||
	public final float WAVE_DELAY;
	// Final wave of this arenas game (-1 = infinite) || COMPLETE ||
	public final int FINAL_WAVE;
	// How quickly food-level regenerates (x points per 5 seconds)(-1 = Food-Level locked, 0 = no regen)
	public final int FOOD_REGEN;
	// Minimum amount of players || COMPLETE ||
	public final int MIN_PLAYERS;
	// Minimum amount of players (-1 = infinite) || COMPLETE ||
	public final int MAX_PLAYERS;
	/*
	 * Itegrate Entry-fee's and money rewards with vault
	 */
	
	public ArenaSettings(Arena arena){
		this.DESTRUCTABLE 			= (boolean) Arena.YML().getSetting(arena.getName(), Setting.DESTRUCTABLE);
		this.PVP_ENABLED 			= (boolean) Arena.YML().getSetting(arena.getName(), Setting.PVP_ENABLED);
		this.SPECTATE_DEATH 		= (boolean) Arena.YML().getSetting(arena.getName(), Setting.SPECTATE_DEATH);
		this.GLOBAL_NEW_ANNOUNCE 	= (boolean) Arena.YML().getSetting(arena.getName(), Setting.GLOBAL_NEW_ANNOUNCE);
		this.GLOBAL_END_ANNOUNCE 	= (boolean) Arena.YML().getSetting(arena.getName(), Setting.GLOBAL_END_ANNOUNCE);
		this.WAVE_DELAY 			= (float) Arena.YML().getSetting(arena.getName(), Setting.WAVE_DELAY);
		this.FIRST_DELAY 			= (float) Arena.YML().getSetting(arena.getName(), Setting.FIRST_DELAY);
		this.FINAL_WAVE 			= (int) Arena.YML().getSetting(arena.getName(), Setting.FINAL_WAVE);
		this.FOOD_REGEN 			= (int) Arena.YML().getSetting(arena.getName(), Setting.FOOD_REGEN);
		this.MIN_PLAYERS 			= (int) Arena.YML().getSetting(arena.getName(), Setting.MIN_PLAYERS);
		this.MAX_PLAYERS 			= (int) Arena.YML().getSetting(arena.getName(), Setting.MAX_PLAYERS);
	}
}
