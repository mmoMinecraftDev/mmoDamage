/*
 * This file is part of mmoMinecraft (https://github.com/mmoMinecraftDev).
 *
 * mmoMinecraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mmo.Damage;

import mmo.Core.events.MMODamageEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * A custom damage event used for cancelling a damage event.
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

		if (attacker instanceof Player && defender instanceof Player) {
			damageType = DamageType.PVP;
		} else if (!(attacker instanceof Player) && !(defender instanceof Player)) {
			damageType = DamageType.EVE;
		} else {
			damageType = DamageType.PVE;
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
