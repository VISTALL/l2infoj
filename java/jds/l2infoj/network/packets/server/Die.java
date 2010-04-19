package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.objects.Creature;
import jds.l2infoj.objects.GameObject;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:47:43
 */
public class Die extends GPacket
{

	@Override
	public void read()
	{
		int objId = readD();

		GameObject obj = getPHandler().getSession().getWorld().getObject(objId);

		if(obj instanceof Npc)
		{
			if(Config.DEBUG && ((Npc) obj).getLastAttacker() != null)
			{
				LogForm.getInstance().log(obj + " is dead. Killer " + ((Npc) obj).getLastAttacker());
			}

			((Creature) obj).setDead(true);
		}
	}
}
