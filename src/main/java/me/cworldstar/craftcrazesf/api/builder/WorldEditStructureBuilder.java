package me.cworldstar.craftcrazesf.api.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Location;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class WorldEditStructureBuilder extends AbstractBuilder {

	private File schematic;
	private Location to_place;
	
	public WorldEditStructureBuilder(File schematic, Location toPlace) {
		this.schematic = schematic;
		this.to_place = toPlace;
	}
	
	public WorldEditStructureBuilder() {
		
	}
	
	public void schematic(File schematic) {
		this.schematic = schematic;
	}
	
	public void location(Location to_place) {
		this.to_place = to_place;
	}
	
	@Override
	protected void onBuild() throws FileNotFoundException, IOException {
		
		if(schematic == null) {
			CraftCrazeSF.logger.log(Level.SEVERE, "WorldEditStructureBuilder fed an invalid schematic file.");
			return;
		}
		
		if(to_place == null) {
			CraftCrazeSF.logger.log(Level.SEVERE, "WorldEditStructureBuilder fed an invalid location.");
			return;
		}
		
		ClipboardFormat format = ClipboardFormats.findByFile(this.schematic);
		ClipboardReader reader = format.getReader(new FileInputStream(this.schematic));
		Clipboard clipboard = reader.read();
		com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(this.to_place.getWorld());
		EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
			.world(adaptedWorld)
			.maxBlocks(-1)
			.build();
		Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
				.to(BlockVector3.at(to_place.getBlockX(), to_place.getBlockY(), to_place.getBlockZ())).ignoreAirBlocks(true).build(); 
		try {
			Operations.complete(operation);
			editSession.close();
		} catch(WorldEditException e) {
			CraftCrazeSF.logger.log(Level.SEVERE, e.getMessage());
		}
	}
}
