package jds.l2infoj.network.packets.server;

import javolution.text.TextBuilder;
import jds.l2infoj.network.PHandler;
import jds.nio.buffer.NioBuffer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 3:33:38
 */
public abstract class GPacket
{
	private NioBuffer _buffer;
	private PHandler _pHandler;

	public abstract void read();

	public NioBuffer getBuffer()
	{
		return _buffer;
	}

	public void setBuffer(NioBuffer buffer)
	{
		_buffer = buffer;
	}

	public PHandler getPHandler()
	{
		return _pHandler;
	}

	public void setPHandler(PHandler pHandler)
	{
		_pHandler = pHandler;
	}

	protected void readB(byte[] dst)
	{
		getBuffer().get(dst);
	}

	protected void readB(byte[] dst, int offset, int len)
	{
		getBuffer().get(dst, offset, len);
	}

	protected int readC()
	{
		return getBuffer().getUnsigned();
	}

	protected int readUH()
	{
		return getBuffer().getUnsignedShort();
	}

	protected int readH()
	{
		return getBuffer().getShort();
	}

	protected int readD()
	{
		return getBuffer().getInt();
	}

	protected long readQ()
	{
		return getBuffer().getLong();
	}

	protected double readF()
	{
		return getBuffer().getDouble();
	}

	protected final byte[] readB(int length)
	{
		byte[] result = new byte[length];
		getBuffer().get(result);
		return result;
	}

	protected String readS()
	{
		TextBuilder tb = TextBuilder.newInstance();
		char ch;

		while((ch = getBuffer().getChar()) != 0)
			tb.append(ch);
		String str = tb.toString();
		TextBuilder.recycle(tb);
		return str;
	}
}
