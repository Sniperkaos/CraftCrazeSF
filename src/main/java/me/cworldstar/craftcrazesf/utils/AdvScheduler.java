package me.cworldstar.craftcrazesf.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class AdvScheduler {
	
	public enum RunType {
		ONCE(0),
		LOOP(1);
		
		private int id;
		
		RunType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
	
	public static Map<String, BukkitRunnable> scheduled_tasks = new HashMap<String, BukkitRunnable>();
	
	public static void schedule(String id, BukkitRunnable r, RunType type, int delay) {
		
		if(scheduled_tasks.containsKey(id)) {
			return;
		}
	
		switch(type) {
			case ONCE:
				r.run();
				break;
			case LOOP:
				r.runTaskTimer(CraftCrazeSF.getMainPlugin(), 0, delay);
				AdvScheduler.scheduled_tasks.put(id, r);
				break;
			default:
				r.run();
				break;
		}
	}
	
	public static boolean IsScheduled(String id) {
		return scheduled_tasks.containsKey(id);
	}

	public static void cancel(String string) {
		scheduled_tasks.get(string).cancel();
		scheduled_tasks.remove(string);
	}
}
