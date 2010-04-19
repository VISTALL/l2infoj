package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 18:50:36
 */
public class MyTargetSelected  extends GPacket
{

	@Override
	public void read()
	{
	 	int objId = readD();
		int color = readH();
		Npc npc = getPHandler().getSession().getWorld().getNpc(objId);
		Player player = getPHandler().getSession().getPlayer();

		if(npc == null || player == null)
			return;

		npc.setLevel(player.getLevel() - color);

		if(Config.DEBUG)
		{
			LogForm.getInstance().log(npc + " level " + npc.getLevel());
		}
	}
}
