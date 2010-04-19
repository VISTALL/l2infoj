package jds.l2infoj.network.packets.server;

import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:55:18
 */
public class CharInfo extends GPacket
{

	@Override
	public void read()
	{
 	 	readD(); //x
		readD(); //y
		readD(); //z
		readD(); //?
		int objId = readD();
		String name = readS();
	    readD();  //race
		readD();   //sex
		readD();   //b. class

		Player player =  new Player(objId);
		player.setName(name);
		
		if(getPHandler().getSession().getWorld().getPlayer(objId) == null)
		{
			getPHandler().getSession().getWorld().addObject(player);
		}
	}
}
