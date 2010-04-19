package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 5:10:16
 */
public class DropInfo
{
 	private int _itemId;
	private long _count;

	public DropInfo(int itemId, long count)
	{
		_itemId = itemId;
		_count = count;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}

	@Override
	public String toString()
	{
		return "item " + getItemId() + ":" + getCount();
	}
}
