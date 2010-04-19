package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:24:00
 */
public class SayInfo
{
	private final int _type;
	private final String _text;

	public SayInfo(int type, String text)
	{
		_type = type;
		_text = text;
	}

	public int getType()
	{
		return _type;
	}

	public String getText()
	{
		return _text;
	}

	@Override
	public String toString()
	{
		return "Say " + _text;
	}
}
