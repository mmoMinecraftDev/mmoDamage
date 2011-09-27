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

import mmo.Core.DamageAPI.MMODamageEvent;
import mmo.Core.DamageAPI.MMODamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MMODamageEventAPI extends Event implements MMODamageEvent {

	private MMODamageType damageType;
	private Entity attacker;
	private Entity realAttacker;
	private Entity defender;
	private Entity realDefender;
	private EntityDamageEvent evt;

	public MMODamageEventAPI(EntityDamageEvent evt) {
		super("mmoDamageEvent");
		this.evt = evt;

		// Get the defender
		realDefender = defender = evt.getEntity();
		if (defender instanceof Tameable) {
			Tameable pet = (Tameable) evt.getEntity();
			if (pet.getOwner() instanceof Entity) {
				defender = (Entity) pet.getOwner();
			}
		}
		// Get the attacker
		if (evt.getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) evt;
			realAttacker = attacker = e.getDamager();
			if (attacker instanceof Tameable) {
				Tameable pet = (Tameable) attacker;
				if (pet.getOwner() instanceof Entity) {
					attacker = (Entity) pet.getOwner();
				}
			}
		} else if (evt.getCause() == DamageCause.PROJECTILE) {
			Projectile arrow = (Projectile) ((EntityDamageByEntityEvent) evt).getDamager();
			if (arrow.getShooter() instanceof Entity) {
				realAttacker = attacker = arrow.getShooter();
			}
		}

		if (attacker instanceof Player && defender instanceof Player) {
			damageType = MMODamageType.PVP;
		} else if (!(attacker instanceof Player) && !(defender instanceof Player)) {
			damageType = MMODamageType.EVE;
		} else {
			damageType = MMODamageType.PVE;
		}
	}

	@Override
	public int getDamage() {
		return evt.getDamage();
	}

	@Override
	public void setDamage(int damage) {
		evt.setDamage(damage);
	}

	@Override
	public MMODamageType getDamageType() {
		return damageType;
	}

	@Override
	public EntityDamageEvent getEvent() {
		return evt;
	}

	@Override
	public Entity getAttacker() {
		return attacker;
	}

	@Override
	public Entity getRealAttacker() {
		return realAttacker;
	}

	@Override
	public Entity getDefender() {
		return defender;
	}

	@Override
	public Entity getRealDefender() {
		return realDefender;
	}

	@Override
	public boolean isCancelled() {
		return evt.isCancelled();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		evt.setCancelled(cancelled);
	}
}
