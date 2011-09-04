package mmo.Damage;

import mmo.Core.MMO;
import mmo.Core.MMOPlugin;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.PluginManager;

/**
 * Controls the damage between entities.
 * 
 * @author Sebastian Mayr
 */
public class MMODamage extends MMOPlugin {
	protected static Server server;
	protected static PluginManager pm;
	protected static MMO mmo;

	@Override
    public void onDisable() {
	    
    }

	@Override
    public void onEnable() {
	    server = this.getServer();
	    pm = server.getPluginManager();
	    mmo = MMO.create(this);
	    
	    pm.registerEvent(Type.ENTITY_DAMAGE, new MMODamageListener(), Priority.High, this);
    }
	
	class MMODamageListener extends EntityListener implements Listener {
		
		@Override
		public void onEntityDamage(EntityDamageEvent evt) {
			if (evt.isCancelled()) return;
			
			MMODamageEventEvent dmgEvt = new MMODamageEventEvent(evt);
			
			pm.callEvent(dmgEvt);
			if (dmgEvt.isCancelled()) {
				evt.setCancelled(true);
			}
		}
	}
}