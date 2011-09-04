package mmo.Damage;

import mmo.Core.MMODamageEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * A custom damage event used for canceling a damage event.
 * 
 * @author Sebastian Mayr
 */
public class MMODamageEventEvent extends Event implements MMODamageEvent {

	private static final long serialVersionUID = -4525101002681081247L;
	
	private DamageType damageType;
	protected Entity attacker;
	protected Entity defender;
	protected boolean cancelled = false;
	protected EntityDamageEvent evt;
	
	public MMODamageEventEvent(EntityDamageEvent evt) {
		super("mmoDamageEvent");
		this.evt = evt;

		determineDefender();
		determineAttacker();
		
		if (isPVPDamage()) {
			damageType = DamageType.PVP;
		}
	}
	
	@Override
	public EntityDamageEvent getEvent() {
		return evt;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	/**
	 * Returns the damage this event does.
	 * 
	 * @return the amount of damage
	 */
	public int getDamage() {
		return evt.getDamage();
	}
	
	/**
	 * Sets the damage this event does.
	 * 
	 * @param damage the amount of damage
	 */
	public void setDamage(int damage) {
		evt.setDamage(damage);
	}
	
	@Override
	public Entity getAttacker() {
		return attacker;
	}
	
	@Override
	public Entity getDefender() {
		return defender;
	}

	@Override
    public DamageType getDamageType() {
	    return damageType;
    }
	
	/**
	 * Checks if both, attacker and defender, are players. This considers tamed pets!
	 * 
	 * @return true, if both are players, false otherwise
	 */
	private boolean isPVPDamage() {
		return attacker instanceof Player && defender instanceof Player;
	}
	
	/**
	 * Determines the defender. This considers normal hits and arrows.
	 * It also takes care of getting the owner of a pet.
	 */
	private void determineDefender() {
		defender = evt.getEntity();
		if (defender instanceof Tameable) {
			Tameable pet = (Tameable) evt.getEntity();
			if (pet.getOwner() instanceof Entity) {
				defender = (Entity) pet.getOwner();
			}
		}
	}
	
	/**
	 * Determines the attacker. This considers normal hits and arrows.
	 * It also takes care of getting the owner of a pet.
	 */
	private void determineAttacker() {
		if (evt.getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) evt;
			
			attacker = e.getDamager();
			
			if (attacker instanceof Tameable) {
				Tameable pet = (Tameable) attacker;
				
				if (pet.getOwner() instanceof Entity) {
					attacker = (Entity) pet.getOwner();
				}
			}
		} else if (evt.getCause() == DamageCause.PROJECTILE) {
			Projectile arrow = (Projectile) ((EntityDamageByEntityEvent) evt).getDamager();
			
			if (arrow.getShooter() instanceof Entity) {
				attacker = arrow.getShooter();
			}
		}
	}
}