package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.infos.DamageToNpc;
import jds.l2infoj.infos.DamageToPlayer;
import jds.l2infoj.infos.ExpSpInfo;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:30:12
 */
public class SystemMessage extends GPacket
{
	public static final int EARNED_S1_EXP_S2_SP = 95;
	public static final int C1_GIVEN_C2_S3_DAMAGE = 2261;
	public static final int C1_RECEIVE_S3_DAMAGE_FROM_C2 = 2262;
    public static final int C1_HAD_A_CRITICAL_HIT = 2266;

	@Override
	public void read()
	{
   	    int id = readD();
		int size = readD();
		Player player;
		String name;
		int damage, npcID;
		Npc npc;
		switch (id)
		{
			case EARNED_S1_EXP_S2_SP:
				readD();//t1
				long exp = readQ();
				readD();//t2
				int sp = readD();
				player = getPHandler().getSession().getPlayer();

				if(player != null && player.getLastNpc() != null /*&& player.getLastNpc().isDead()*/)
				{
					ExpSpInfo expSp = new ExpSpInfo(exp, sp);
					player.getLastNpc().addExpSp(expSp);

					if(Config.DEBUG)
					{
						//LogForm.getInstance().log(player.getLastNpc() + " give " + expSp);
					}
				}
				break;
			case C1_GIVEN_C2_S3_DAMAGE:
				readD();  //t1
				name = readS();
				readD();
				npcID = readD();
				readD();
				damage = readD();
				npc = getPHandler().getSession().getPlayer().getLastNpc();
			   	player = getPHandler().getSession().getPlayer();

				if(!player.getName().equalsIgnoreCase(name))
					return;

				if(npc == null || npc.getNpcBigId() != npcID)
					return;

				DamageToNpc dam = new DamageToNpc(damage, player.getTypeAttack(), player.getPAttack(), player.getMAttack(), -2, 0, player.isNextDamageIsCritical());
				player.setNextDamageIsCritical(false);
				npc.addDamageToNpc(dam);

				if(Config.DEBUG)
				{
					//LogForm.getInstance().log(player + " give " + dam + " to " + npc);
				}
				break;
			case C1_RECEIVE_S3_DAMAGE_FROM_C2:
				readD();  //t1
				name = readS();
				readD();
				npcID = readD();
				readD();
				damage = readD();
				npc = getPHandler().getSession().getPlayer().getLastNpc();
			   	player = getPHandler().getSession().getPlayer();

				if(!player.getName().equalsIgnoreCase(name))
					return;

				if(npc == null || npc.getNpcBigId() != npcID)
					return;

				DamageToPlayer da = new DamageToPlayer(damage, npc.getTypeAttack(), player.getPDef(), player.getMDef(), 0,0,0,0,0,0);
				npc.addDamageToPlayer(da);

				if(Config.DEBUG)
				{
					//LogForm.getInstance().log(npc + " give " + da + " to " + player);	
				}
				
				break;
			case C1_HAD_A_CRITICAL_HIT:
				readD(); //type
				String text = readS();

				if(text.equalsIgnoreCase(getPHandler().getSession().getPlayer().getName()))
				{
					getPHandler().getSession().getPlayer().setNextDamageIsCritical(true);
				}
				break;
		}

	}
}
