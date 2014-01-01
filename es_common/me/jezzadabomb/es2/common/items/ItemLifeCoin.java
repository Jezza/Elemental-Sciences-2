package me.jezzadabomb.es2.common.items;

public class ItemLifeCoin extends ItemES{

	public ItemLifeCoin(int id, String name) {
		super(id, name);
	}

	@Override
	protected void addInformation() {
		defaultInfoList();
		shiftList.add("You got it for getting");
		shiftList.add("a perfect pacman game.");		
	}

}
