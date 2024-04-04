package me.cworldstar.craftcrazesf.api.dimensions;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.builder.StructureBuilder;
import me.cworldstar.craftcrazesf.api.builder.WorldEditStructureBuilder;
import me.cworldstar.craftcrazesf.api.dimensions.chunk_generators.VoidChunkGenerator;

public class WorldBuilder {
	
	private World to_return;
	
	public WorldBuilder() {};
	
	public static World createCompactMachineWorld(String uuid, int machine_size) {
		WorldCreator creator = new WorldCreator(uuid);
		creator.generator(new VoidChunkGenerator());
		creator.generateStructures(false);
		World w = creator.createWorld();
		
		StructureBuilder b = new StructureBuilder();
		b.at(w.getSpawnLocation());
		b.setSize(machine_size);
		b.withBlock(Material.BEDROCK);
		b.build();
		
		return w;
	}
	
	public static World loadCompactMachineWorld(String uuid) {
		WorldCreator creator = new WorldCreator(uuid);
		return Bukkit.createWorld(creator);
	}

	public World createCompactMachineWorldSync(String uuid, int machine_size) {

		WorldBuilder self = this;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				WorldCreator creator = new WorldCreator(uuid);
				creator.generator(new VoidChunkGenerator());
				creator.generateStructures(false);
				self.to_return = creator.createWorld();
				
				StructureBuilder b = new StructureBuilder();
				b.at(self.to_return.getSpawnLocation());
				b.setSize(machine_size);
				b.withBlock(Material.BEDROCK);
				b.build();
			}
		}.runTask(CraftCrazeSF.getPlugin(CraftCrazeSF.class));
		
		return self.to_return;
	}
	
	public World createVoidDimension(String uuid, String schematic) {
		WorldCreator creator = new WorldCreator(uuid);
		creator.generator(new VoidChunkGenerator());
		creator.generateStructures(false);
		World w = creator.createWorld();
		
		WorldEditStructureBuilder web = new WorldEditStructureBuilder();
		web.location(w.getSpawnLocation());
		web.schematic(new File(CraftCrazeSF.data_folder.getAbsolutePath() + File.separator + schematic + ".schem"));
		web.build();
		
		return w;
	}
	
}
