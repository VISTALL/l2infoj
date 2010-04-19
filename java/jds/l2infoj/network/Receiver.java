package jds.l2infoj.network;

import jds.l2infoj.managers.SessionManager;
import jds.l2infoj.session.Session;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 2:25:31
 */
public class Receiver implements PacketReceiver
{
	@Override
	public void receivePacket(Packet p)
	{
		if (!(p instanceof TCPPacket))
		{
			return;
		}

		TCPPacket packet = (TCPPacket) p;
		int sessionId = packet.dst_port * packet.src_port;

		Session session =  SessionManager.getInstance().getSession(sessionId, true);

		session.receivePacket(packet);
	}
}
