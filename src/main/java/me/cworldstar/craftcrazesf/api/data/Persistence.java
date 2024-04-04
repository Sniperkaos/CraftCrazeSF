package me.cworldstar.craftcrazesf.api.data;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;

import dev.sefiraat.sefilib.persistence.BukkitObjectDataType;

public class Persistence {
	
    private Persistence() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The PersistentDataType for an {@link Block}
     */
	
	public static final PersistentDataType<byte[], Location> LOCATION = new BukkitObjectDataType<>(Location.class);
}
