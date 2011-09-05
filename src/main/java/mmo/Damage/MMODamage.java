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

import mmo.Core.MMOPlugin;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.util.config.Configuration;

/**
 * Controls the damage between entities.
 * 
 * @author Sebastian Mayr
 */
public class MMODamage extends MMOPlugin {

	@Override
	public void onEnable() {
		super.onEnable();
		pm.registerEvent(Type.ENTITY_DAMAGE, new MMODamageListener(), Priority.High, this);
	}

	@Override
	public void loadConfiguration(Configuration cfg) {
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	class MMODamageListener extends EntityListener implements Listener {

		@Override
		public void onEntityDamage(EntityDamageEvent event) {
			if (event.isCancelled()) {
				return;
			}

			MMODamageEventEvent dmgEvt = new MMODamageEventEvent(event);

			pm.callEvent(dmgEvt);
			if (dmgEvt.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}
}
