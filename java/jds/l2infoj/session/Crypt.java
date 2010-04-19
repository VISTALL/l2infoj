package jds.l2infoj.session;

import jds.l2infoj.network.Type;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 3:26:58
 */
public class Crypt
{
	private boolean _isEnable;
	private final byte[] _inKey = new byte[16];
	private final byte[] _outKey = new byte[16];

	public void init(byte[] key)
	{
		System.arraycopy(key, 0, _inKey, 0, 16);
		System.arraycopy(key, 0, _outKey, 0, 16);
		_isEnable = true;
	}

	public void decode(byte[] content, Type type)
	{
		if(!isEnable())
			return;
		
		if(type == Type.CLIENT)
			decode(content, _inKey);
		else
			decode(content, _outKey);
	}

	public void decode(byte[] raw, byte[] key)
	{
		int size = raw.length;
		int temp = 0;

		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[i] & 0xFF;
			raw[i] = (byte) (temp2 ^ key[i & 15] ^ temp);
			temp = temp2;
		}

		int old = key[8] & 0xff;
		old |= key[9] << 8 & 0xff00;
		old |= key[10] << 0x10 & 0xff0000;
		old |= key[11] << 0x18 & 0xff000000;

		old += size;

		key[8] = (byte) (old & 0xff);
		key[9] = (byte) (old >> 0x08 & 0xff);
		key[10] = (byte) (old >> 0x10 & 0xff);
		key[11] = (byte) (old >> 0x18 & 0xff);
	}

	public boolean isEnable()
	{
		return _isEnable;
	}
}
