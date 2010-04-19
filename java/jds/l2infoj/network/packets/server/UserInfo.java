package jds.l2infoj.network.packets.server;

import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:49:25
 */
public class UserInfo extends GPacket
{

	@Override
	public void read()
	{
	 	readD(); //x
		readD(); //y
		readD(); //z
		readD(); //boat id
		int objId = readD();
		String name = readS();
		readD();//race
		readD();//sex
		readD();//base class
		int level = readD();
		readQ(); //exp
		readD(); //str
		readD(); //dex
		readD(); //con
		readD(); //int
		readD(); //win
		readD(); //men
		readD(); // max hp
		readD(); // cur hp
		readD(); // max mp
		readD(); // cur mp
	    readD(); // sp
		readD(); // cur load
		readD(); // max load
		readD(); //weapon equiped

		for(int i = 1; i <= 26; i ++)
		{
			readD();
		}

		for(int i = 1; i <= 26; i ++)
		{
			readD();
		}

		for(int i = 1; i <= 26; i ++)
		{
			readD();
		}

		readD(); //talisman
		readD(); //cloak
		int pAttack = readD();
		int pSpeed = readD();
		int pDef = readD();
		readD(); //evasion
		readD(); //acc
		readD(); //crit
		int mAttack = readD();
		int mSpeed = readD();
		readD(); //att speed
		int mDef = readD();
		
		Player player =  new Player(objId);
		player.setName(name);

		if(getPHandler().getSession().getPlayer() == null)
		{
			getPHandler().getSession().setPlayer(player);
		}

		if(getPHandler().getSession().getWorld().getPlayer(objId) == null)
		{
			getPHandler().getSession().getWorld().addObject(player);
		}
		else
		{
			player = getPHandler().getSession().getWorld().getPlayer(objId);
		}

		player.setPAttack(pAttack);
		player.setMAttack(mAttack);
		player.setPSpeed(pSpeed);
		player.setMSpeed(mSpeed);
		player.setPDef(pDef);
		player.setMDef(mDef);
		player.setLevel(level);
	}
}
