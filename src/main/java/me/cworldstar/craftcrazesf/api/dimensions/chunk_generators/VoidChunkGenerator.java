package me.cworldstar.craftcrazesf.api.dimensions.chunk_generators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidChunkGenerator extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World w, Random r, int x, int z, BiomeGrid biome) {
		return createChunkData(w);
	}
}
