package jds.l2infoj.network.packets.server;

import jds.l2infoj.gui.forms.LogForm;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 3:35:44
 */
public class KeyPacket extends GPacket
{

	@Override
	public void read()
	{
		readC();//status

		byte[] key = new byte[16];

		readB(key, 0, 8);

		//xor key
		key[8] = (byte) 0xc8;
		key[9] = (byte) 0x27;
		key[10] = (byte) 0x93;
		key[11] = (byte) 0x01;
		key[12] = (byte) 0xa1;
		key[13] = (byte) 0x6c;
		key[14] = (byte) 0x31;
		key[15] = (byte) 0x97;

		getPHandler().getSession().getCrypt().init(key);

		LogForm.getInstance().log("Receive KeyPacket");
	}
}
