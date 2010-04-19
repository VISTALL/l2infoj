package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:57:27
 */
public class Attack extends GPacket
{

	@Override
	public void read()
	{
	 	int obj = readD();
		int targetId = readD();

		Player attacker = getPHandler().getSession().getWorld().getPlayer(obj);
		Npc target = getPHandler().getSession().getWorld().getNpc(targetId);

		if(attacker == null || target == null)
			return;

		target.setLastAttacker(attacker);

		attacker.setLastNpc(target); 		
		attacker.setTypeAttack(-1);


		if(Config.DEBUG)
		{
			LogForm.getInstance().log(attacker + " attack " + target);
		}
	}
}
