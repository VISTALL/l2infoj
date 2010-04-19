package jds.l2infoj.network;

import jds.l2infoj.network.packets.client.ProtocolVersion;
import jds.l2infoj.network.packets.server.*;
import jds.l2infoj.session.Session;
import jds.nio.buffer.NioBuffer;

import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 3:24:19
 */
public class PHandler
{
	private final Session _session;

	public PHandler(Session session)
	{
		_session = session;
	}

	public void receive(final byte[] content, Type type)
	{
		if(getSession().getCrypt().isEnable())
			getSession().getCrypt().decode(content, type);

		NioBuffer buf = NioBuffer.wrap(content);
		buf.order(ByteOrder.LITTLE_ENDIAN);

		int opcode = buf.getUnsigned();
		GPacket packet = null;

		switch (type)
		{
			case CLIENT:
				switch (opcode)
				{
					case 0x0E:
						packet = new ProtocolVersion();
						break;
				}
				break;
			case SERVER:
				if(!getSession().isValidVersion())
					return;
				switch (opcode)
				{
					case 0x09:
						//packet = new CharacterSelectionInfo();
						break;
					case 0x2E:
						packet = new KeyPacket();
						break;
					case 0x19:
						packet = new NpcHtmlMessage();
						break;
					case 0x18:
						packet = new StatusUpdate();
						break;
					case 0x00:
						packet = new Die();
						break;
					case 0x30:
						packet = new NpcSay();
						break;					
				   	//info's
					case 0x32:
						packet = new UserInfo();
						break;
					case 0x31:
						packet = new CharInfo();
						break;
					case 0x0C:
						packet = new NpcInfo();
						break;
					//attacks
					case 0x33:
						packet = new Attack();
						break;
					case 0x48:
						packet = new MagicSkillUse();
						break;
					//moves
					case 0x2f:
						//packet = new CharMoveToLocation();
						break;
					//items
					case 0x16:
						packet = new DropItem();
						break;
					case 0x62:
						packet = new SystemMessage();
						break;
					case 0x05:
						//System.out.println("SpawnItem");
						break;
					case 0x84:
						packet = new LeaveWorld();
						break;
					case 0xb9:
						packet  = new MyTargetSelected();
						break;
				}
				break;
		}

		if(packet != null)
		{
			/*if(Config.DEBUG)
			{
				System.out.println("Capture: " + packet.getClass().getSimpleName());
			}*/
			packet.setBuffer(buf);
			packet.setPHandler(this);
			packet.read();
		}
	}

	public Session getSession()
	{
		return _session;
	}
}
