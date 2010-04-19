package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.infos.DialogInfo;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 3:57:27
 */
public class NpcHtmlMessage  extends GPacket
{

	@Override
	public void read()
	{
	 	int npcObjId = readD();
		String text = readS();

		Npc npc = getPHandler().getSession().getWorld().getNpc(npcObjId);
		if(npc == null)
			return;

		DialogInfo dialog = new DialogInfo(text);

		if(!npc.hasDialog(text))
			npc.addDialog(dialog);

		if(Config.DEBUG)
		{
			//LogForm.getInstance().log(npc + " " + dialog);
		}
	}
}
