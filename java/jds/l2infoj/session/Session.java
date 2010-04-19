package jds.l2infoj.session;

import jds.l2infoj.config.Config;
import jds.l2infoj.network.PHandler;
import jds.l2infoj.network.Type;
import jds.l2infoj.objects.Player;
import jds.l2infoj.samurai.PacketBuffer;
import jds.l2infoj.samurai.Sequenced;
import jds.l2infoj.world.World;
import jpcap.packet.TCPPacket;

import java.net.InetAddress;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 2:32:57
 * Client is not need
 */
public class Session
{
	private final long _id;

	private final PacketBuffer _serverbuf = new PacketBuffer();
	private final PacketBuffer _clientbuf = new PacketBuffer();

	private final Sequenced _clientSequenced = new Sequenced();
	private final Sequenced _serverSequenced = new Sequenced();

	private final PHandler _handler = new PHandler(this);
	private Crypt _crypt = new Crypt();
	private World _world = new World();
	private InetAddress _address = null;
	private int _protocol = -1;

	private Player _player;

	public Session(long id)
	{
		_id = id;
	}

	public void receivePacket(TCPPacket p)
	{
		int size = 0;

		if (p.dst_port != Config.PORT) // client
		{
			if (p.data.length > 0)
            {
                _serverSequenced.add(p);
            }

			_clientSequenced.ack(p);

			for (TCPPacket packet : _clientSequenced.getSequencedPackets())
            {
                _clientbuf.putData(packet.data);
                while ((size = _clientbuf.nextAvaliablePacket()) > 0)
                {
                    byte[] header = new byte[2];
                    byte[] packetData = new byte[size];
                    _clientbuf.getNextPacket(header, packetData);
                    _handler.receive(packetData, Type.CLIENT);
                }
            }
            _clientSequenced.flush();
		}
		else
		{
			if (p.data.length > 0)
            {
                _clientSequenced.add(p);
            }

			_serverSequenced.ack(p);

			for (TCPPacket packet : _serverSequenced.getSequencedPackets())
            {
                _serverbuf.putData(packet.data);
                while ((size = _serverbuf.nextAvaliablePacket()) > 0)
                {
                    byte[] header = new byte[2];
                    byte[] packetData = new byte[size];
                    _serverbuf.getNextPacket(header, packetData);
                    _handler.receive(packetData, Type.SERVER);
                }
            }
            _serverSequenced.flush();
		}

		if(p.dst_port == Config.PORT && _address == null)
		{
			_address = p.dst_ip;
		}
	}

	public boolean isValidVersion()
	{
		return _protocol == Config.VALID_VERSION;
	}

	public Crypt getCrypt()
	{
		return _crypt;
	}

	public long getId()
	{
		return _id;
	}

	public Player getPlayer()
	{
		return _player;
	}

	public void setPlayer(Player player)
	{
		_player = player;
	}

	public World getWorld()
	{
		return _world;
	}

	public InetAddress address()
	{
		return _address;
	}

	public int getProtocol()
	{
		return _protocol;
	}

	public void setProtocol(int protocol)
	{
		_protocol = protocol;
	}
}
