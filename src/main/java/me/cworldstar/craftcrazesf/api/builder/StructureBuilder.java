package me.cworldstar.craftcrazesf.api.builder;

import org.bukkit.Location;
import org.bukkit.Material;

public class StructureBuilder extends AbstractBuilder {

	private int size;
	private Location location;
	private Material block;

	public StructureBuilder() {
		super();
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public void at(Location l) {
		this.location = l;
	}
	public void withBlock(Material m) {
		this.block = m;
	}
	
	@Override
	public void build() {
		if(this.completed) {
			return;
		}
		this.completed = true;
		
		double minX = this.location.getX() - this.size;
		double maxX = this.location.getX() + this.size;

		
		double minY = this.location.getY() - this.size;
		double maxY = this.location.getY() + this.size;
		
		double minZ = this.location.getZ() - this.size;
		double maxZ = this.location.getZ() + this.size;
	
		//double X = maxX - minX;
		//double Z = maxZ - minZ;
		
        for (double x = minX; x <= maxX; x++) {
            for (double y = minY; y <= maxY; y++) {
                for (double z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(this.location.getWorld(), x, y, z);
                    if(		   x == minX 
                    		|| y == minY 
                    		|| z == minZ 
                    		|| x == maxX 
                    		|| y == maxY 
                    		|| z == maxZ) 
                    {
                    	 loc.getBlock().setType(this.block);
                    	 loc.add(0,1,0).getBlock().setType(this.block);
                    	 loc.add(1,0,0).getBlock().setType(this.block);
                    	 loc.add(0,0,1).getBlock().setType(this.block);
                    }
                }
            }
        }
	}

	@Override
	protected void onBuild() {
		// TODO Auto-generated method stub
		
	}
}
