package jds.l2infoj.network.packets.client;

import jds.l2infoj.network.packets.server.GPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:45:04
 */
public class ProtocolVersion extends GPacket
{

	@Override
	public void read()
	{
		if (getPHandler().getSession().getProtocol() == -1)
		{
			getPHandler().getSession().setProtocol(readD());
		}
	}
}
