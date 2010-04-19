package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.ExceptionForm;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.infos.SayInfo;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:22:45
 */
public class NpcSay extends GPacket
{

	@Override
	public void read()
	{
		try
		{
			int obj = readD();
			int type = readD();
			readD();
			String text = readS();

			Npc npc = getPHandler().getSession().getWorld().getNpc(obj);
			if(npc == null)
				return;

			SayInfo say = new SayInfo(type, text);
			npc.addSay(say);

			if(Config.DEBUG)
			{
				LogForm.getInstance().log(npc + " " + say);
			}
		}
		catch (Exception e)
		{
			ExceptionForm.getInstance().addException(e);
		}
	}
}
