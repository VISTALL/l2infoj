package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.infos.DropInfo;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:12:52
 */
public class DropItem extends GPacket
{

	@Override
	public void read()
	{
		int obj = readD();
		readD(); //item obj id
		int id = readD();
		readD(); //x
		readD(); //y
		readD(); //z
		readD();//stackable
		long count = readQ();

		Npc npc = getPHandler().getSession().getWorld().getNpc(obj);
		if(npc == null)
			return;

		DropInfo drop = new DropInfo(id, count);
		npc.addDropInfo(drop);

		if(Config.DEBUG)
		{
			LogForm.getInstance().log(npc + " drop " + drop);
		}
	}
}
