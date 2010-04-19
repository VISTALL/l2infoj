package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.ExceptionForm;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.objects.Npc;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:09:51
 */
public class StatusUpdate extends GPacket
{
 	public final static int CUR_HP = 0x09;
	public final static int MAX_HP = 0x0a;
	public final static int CUR_MP = 0x0b;
	public final static int MAX_MP = 0x0c;

	@Override
	public void read()
	{
		try
		{
			int objId = readD();
			int size = readD();

			Npc npc = getPHandler().getSession().getWorld().getNpc(objId);

			if(npc == null || size == 0)
				return;

			for(int i = 0; i < size; i ++)
			{
				int name = readD();
				int val = readD();

				switch (name)
				{
				 	case MAX_HP:
						 if(Config.DEBUG)
						 {
							 LogForm.getInstance().log(npc + " max HP " + val);
						 }
						 npc.setMaxHP(val);
						 break;
					 case MAX_MP:
						 if(Config.DEBUG)
						 {
							 LogForm.getInstance().log(npc + " maxMP " + val);
						 }
						 npc.setMaxMP(val);
						 break;
					 default:
						 //System.out.println("Status update: " + name);
						 break;
				}
			}
		}
		catch (Exception e)
		{
			ExceptionForm.getInstance().addException(e);
		}
	}
}
