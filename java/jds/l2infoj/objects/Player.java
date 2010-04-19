package jds.l2infoj.objects;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:01:58
 */
public class Player extends Creature
{
	private Npc _lastNpc;

	public Player(int od)
	{
		super(od);
	}

	public Npc getLastNpc()
	{
		return _lastNpc;
	}

	public void setLastNpc(Npc lastNpc)
	{	
		_lastNpc = lastNpc;
	}

	@Override
	public String toString()
	{
		return "Player : " + _name;	
	}
}
