package jds.l2infoj.objects;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:00:54
 */
public class GameObject
{
 	protected final int _objectId;
	private int _x;
	private int _y;
	private int _z;

	protected GameObject(int od)
	{
		_objectId = od;
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public int getX()
	{
		return _x;
	}

	public void setX(int x)
	{
		_x = x;
	}

	public int getY()
	{
		return _y;
	}

	public void setY(int y)
	{
		_y = y;
	}

	public int getZ()
	{
		return _z;
	}

	public void setZ(int z)
	{
		_z = z;
	}
}
