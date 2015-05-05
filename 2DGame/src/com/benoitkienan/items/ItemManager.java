package com.benoitkienan.items;

import java.util.Hashtable;

import com.benoit.tiles.Tile;
import com.benoit.tiles.TileManager;
import com.benoitkienan.jeu.Mob;

public class ItemManager {
    Hashtable<String, Item> ItemList = new Hashtable<String, Item>();

    public ItemManager() {
	TileManager tileManager = new TileManager();

	for (Tile tile : tileManager.getTileList()) {
	    ItemList.put(tile.getName(), new ItemTile(tile));
	}
	
    }

    public Hashtable<String, Item> getItemList() {
	return ItemList;
    }

}