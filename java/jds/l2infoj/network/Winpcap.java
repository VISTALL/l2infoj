package jds.l2infoj.network;

import jds.l2infoj.config.Config;
import jds.l2infoj.threads.ThreadPoolManager;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:13:08
 */
public class Winpcap
{
	private static Winpcap _instance;
	private final Receiver _receiver = new Receiver();
	private JpcapCaptor _cap;

	public static Winpcap getInstance()
	{
		if (_instance == null)
		{
			_instance = new Winpcap();
		}
		return _instance;
	}

	private Winpcap()
	{}

	public void init() throws IOException
	{
		NetworkInterface[] list = JpcapCaptor.getDeviceList();

		_cap = JpcapCaptor.openDevice(list[Config.DEVICE_ID], Config.LENGTH, Config.PROMICS_MODE, Config.TIMEOUT);

		_cap.setFilter("tcp port " + Config.PORT, true);
	}

	public void start()
	{
		ThreadPoolManager.getInstance().execute(new Runnable()
		{

			@Override
			public void run()
			{
				_cap.loopPacket(-1, _receiver);
			}
		});
	}

	public void stop()
	{
		_cap.breakLoop();
	}
}
