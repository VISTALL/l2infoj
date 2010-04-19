package jds.l2infoj.network.packets.server;

import jds.l2infoj.infos.LocInfo;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:06:51
 */
public class NpcInfo extends GPacket
{

	@Override
	public void read()
	{
		int objId = readD();
		int npcId = readD() - 1000000;
		boolean isNpc = readD() == 0; // npc or mob
		int x = readD();
		int y = readD();
		int z = readD();
		int h = readD();
		readD();//?

		LocInfo info = new LocInfo(x, y, z, h);
		Npc npc = getPHandler().getSession().getWorld().getNpc(objId);

		if(npc == null)
		{
			npc = new Npc(objId, npcId);
			getPHandler().getSession().getWorld().addObject(npc);
		}
		else
		{
			//	
		}

		if(!npc.hasLoc(info))
			npc.addLocationInfo(info);
	}
}
