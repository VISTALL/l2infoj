package jds.l2infoj.world;

import javolution.util.FastMap;
import jds.l2infoj.objects.GameObject;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.objects.Player;

import java.util.Collection;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:04:24
 */
public class World
{
	private FastMap<Integer, GameObject> _objects = new FastMap<Integer, GameObject>();

	public void addObject(GameObject obj)
	{
		_objects.put(obj.getObjectId(), obj);
	}

	public GameObject getObject(int obj)
	{
		return _objects.get(obj);
	}

	public Npc getNpc(int obj)
	{
		GameObject oj = _objects.get(obj);

		if(oj instanceof Npc)
			return (Npc)oj;
		else
			return null;
	}

	public Player getPlayer(int obj)
	{
		GameObject oj = _objects.get(obj);

		if(oj instanceof Player)
			return (Player)oj;
		else
			return null;
	}

	public Collection<GameObject> objects()
	{
		return _objects.values() ;
	}

	public void clear()
	{
		_objects.clear();
	}
}
