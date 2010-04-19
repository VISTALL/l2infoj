package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 15:13:46
 */
public class LocInfo implements Comparable<LocInfo>
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _h;

	public LocInfo(int x, int y, int z, int h)
	{
		_x = x;
		_y = y;
		_z = y;
		_h = h;
	}

	public int getX()
	{
		return _x;
	}

	public int getY()
	{
		return _y;
	}

	public int getZ()
	{
		return _z;
	}

	public int getH()
	{
		return _h;
	}


	@Override
	public int compareTo(LocInfo o)
	{
		if(_x == o._x && _y == o._y && _z == o._z)
			return 0;
		return -1;
	}
}
