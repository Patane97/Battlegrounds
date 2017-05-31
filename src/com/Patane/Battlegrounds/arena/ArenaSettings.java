package com.Patane.Battlegrounds.arena;

public class ArenaSettings {
	// blocks can be placed/broken whilst in-game || COMPLETE ||
	boolean destructable;
	// Whether players can damage other players || COMPLETE ||
	boolean pvpEnabled;
	// Whether players spectate after dying in-game || COMPLETE || <--- Soft removePlayer in Game.teleportPlayer
	boolean spectateDeath;
	// Globally announces the opening of a new lobby to the server
	boolean globalNewLobby;
	// Globally announces the ending of a game to the server
	boolean globalEndGame;
	
	// Final wave of this arenas game (-1 = infinite)
	int finalWave;
	// Minimum amount of players
	int minimumPlayers;
	// Minimum amount of players (-1 = infinite)
	int maximumPlayers;
	// Delay per-wave
	int waveDelay;
	// Delay for First wave
	int firstDelay;
	// How quickly food-level regenerates (x points per 5 seconds)(-1 = Food-Level locked, 0 = no regen)
	int foodRegen;
	/*
	 * Itegrate Entry-fee's and money rewards with vault
	 */
	
	public ArenaSettings(boolean destructable, boolean pvpEnabled, boolean spectateDeath, boolean globalNewLobby, boolean globalEndGame, 
			int finalWave, int minimumPlayers, int maximumPlayers, int waveDelay, int firstDelay, int foodRegen){
		this.destructable = destructable;
		this.pvpEnabled = pvpEnabled;
		this.spectateDeath = spectateDeath;
		this.globalNewLobby = globalNewLobby;
		this.globalEndGame = globalEndGame;
		this.finalWave = finalWave;
		this.minimumPlayers = minimumPlayers;
	}
	
	public boolean isDestructable() {
		return destructable;
	}
	public boolean isPvpEnabled() {
		return pvpEnabled;
	}
	public boolean isSpectateDeath() {
		return spectateDeath;
	}
	public boolean isglobalNewLobby() {
		return globalNewLobby;
	}
	public boolean isGlobalEnd() {
		return globalEndGame;
	}
	public int getFinalWave() {
		return finalWave;
	}
	public int getMinimumPlayers() {
		return minimumPlayers;
	}
	public int getMaximumPlayers() {
		return maximumPlayers;
	}
	public int getWaveDelay() {
		return waveDelay;
	}
	public int getFirstDelay() {
		return firstDelay;
	}
	public int getFoodRegen() {
		return foodRegen;
	}
	public void setDestructable(boolean destructable) {
		this.destructable = destructable;
	}
	public void setPvpEnabled(boolean pvpEnabled) {
		this.pvpEnabled = pvpEnabled;
	}
	public void setSpectateDeath(boolean spectateDeath) {
		this.spectateDeath = spectateDeath;
	}
	public void setglobalNewLobby(boolean globalNewLobby) {
		this.globalNewLobby = globalNewLobby;
	}
	public void setGlobalEnd(boolean globalEndGame) {
		this.globalEndGame = globalEndGame;
	}
	public void setFinalWave(int finalWave) {
		this.finalWave = finalWave;
	}
	public void setMinimumPlayers(int minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}
	public void setMaximumPlayers(int maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}
	public void setWaveDelay(int waveDelay) {
		this.waveDelay = waveDelay;
	}
	public void setFirstDelay(int firstDelay) {
		this.firstDelay = firstDelay;
	}
	public void setFoodRegen(int foodRegen) {
		this.foodRegen = foodRegen;
	}
}
