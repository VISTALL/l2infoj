package jds.l2infoj.gui.list;

import jpcap.NetworkInterface;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:13:57
 */
public class NetworkInterfaceItem
{
	private final NetworkInterface _interface;

	public NetworkInterfaceItem(NetworkInterface in)
	{
		_interface = in;
	}

	public String description()
	{
		return _interface.description;
	}

	public String toString()
	{
		return _interface.name;
	}
}
