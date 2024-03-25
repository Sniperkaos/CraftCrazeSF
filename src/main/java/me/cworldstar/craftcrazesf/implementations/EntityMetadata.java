package me.cworldstar.craftcrazesf.implementations;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class EntityMetadata extends FixedMetadataValue {

	private Entity entity;
	
	public EntityMetadata(Plugin owningPlugin, Object value) {
		super(owningPlugin, value);
		// TODO Auto-generated constructor stub
	}
	
	public Entity getEntity() {
		return this.entity;
	}

}
