package me.cworldstar.craftcrazesf.items.armors.power;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;

public class PowerArmorAttribute {
	
	private String name;
	private double amount;
	private Operation operation;
	private Attribute attribute;
	
	public PowerArmorAttribute() {
		
	}
	
	public PowerArmorAttribute setAttribute(Attribute a) {
		this.attribute = a;
		return this;
	}
	
	public PowerArmorAttribute setName(String name) {
		this.name = name;
		return this;
	}
	
	public PowerArmorAttribute setAmount(double amount) {
		this.amount = amount;
		return this;
	}
	
	public PowerArmorAttribute setOperation(Operation o) {
		this.operation = o;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public Operation getOperation() {
		return this.operation;
	}
	
	public Attribute getAttribute() {
		return this.attribute;
	}
	
	public AttributeModifier build() {
		return new AttributeModifier(name, amount, operation);
	}
	
}
