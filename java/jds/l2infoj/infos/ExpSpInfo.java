package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:28:25
 */
public class ExpSpInfo
{
	private final long _exp;
	private final int _sp;

	public ExpSpInfo(long exp, int sp)
	{
		_exp = exp;
		_sp = sp;
	}

	public long getExp()
	{
		return _exp;
	}

	public int getSp()
	{
		return _sp;
	}

	@Override
	public String toString()
	{
		return "Expirence: " + _exp + "; SP: "  + _sp;
	}
}
