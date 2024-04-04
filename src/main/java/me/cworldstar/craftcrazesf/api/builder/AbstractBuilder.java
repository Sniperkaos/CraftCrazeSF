package me.cworldstar.craftcrazesf.api.builder;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class AbstractBuilder {

	
	protected boolean completed;
	
	public AbstractBuilder() {
		this.completed = false;
	}
	
	protected abstract void onBuild() throws FileNotFoundException, IOException;
	
	public void build() {
		if(this.completed == false) {
			try {
				this.onBuild();
			} catch(FileNotFoundException e1) {
				
			} catch(IOException e2) {
				
			} finally {
				this.completed = true;
			}
		}
	};
	
	
}