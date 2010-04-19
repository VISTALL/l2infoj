package jds.l2infoj.objects;

import javolution.util.FastList;
import javolution.util.FastMap;
import jds.l2infoj.datas.NpcNameHolder;
import jds.l2infoj.infos.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:02:13
 */
public class Npc extends Creature
{
	private final FastMap<Integer, SkillInfo> _skills = new FastMap<Integer, SkillInfo>();

	private final FastList<ExpSpInfo> _expSp = new FastList<ExpSpInfo>();
	private final FastList<DropInfo> _drops = new FastList<DropInfo>();
	private final FastList<SayInfo> _says = new FastList<SayInfo>();
	private final FastList<DialogInfo> _dialogs = new FastList<DialogInfo>();
	private final FastList<DamageToNpc> _damagesToNpc = new FastList<DamageToNpc>();
	private final FastList<DamageToPlayer> _damagesToPlayer = new FastList<DamageToPlayer>();
	private final FastList<LocInfo> _locationsInfo = new FastList<LocInfo>();

	private final int _npcId;
	private Player _lastAttacker;

	public Npc(int objectId, int npcId)
	{
		super(objectId);

		_npcId = npcId;
		_name = NpcNameHolder.getInstance().name(npcId);
	}

	public FastList<DamageToNpc> getDamageToNpc()
	{
		return _damagesToNpc;
	}

	public SkillInfo getSkill(int id)
	{
		return getSkills().get(id);
	}

	public void addSkill(SkillInfo info)
	{
		getSkills().put(info.getId(), info);
	}

	public void addDropInfo(DropInfo i)
	{
		getDrops().add(i);
	}

	public void addSay(SayInfo i)
	{
		getSays().add(i);
	}

	public void addExpSp(ExpSpInfo i)
	{
		getExpSp().add(i);
	}

	public boolean hasDialog(String text)
	{
		for(DialogInfo dia : getDialogs())
		{
			if(dia.getDialog().equalsIgnoreCase(text))
				return true;
		}

		return false;
	}

	public void addDialog(DialogInfo i)
	{
		getDialogs().add(i);
	}

	public void addDamageToNpc(DamageToNpc tp)
	{
		_damagesToNpc.add(tp);
	}       	

	public void addDamageToPlayer(DamageToPlayer tp)
	{
		_damagesToPlayer.add(tp);
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public int getNpcBigId()
	{
		return _npcId + 1000000;
	}

	public Player getLastAttacker()
	{
		return _lastAttacker;
	}

	public void setLastAttacker(Player lastAttacker)
	{
		_lastAttacker = lastAttacker;
	}

	public FastMap<Integer, SkillInfo> getSkills()
	{
		return _skills;
	}

	public FastList<ExpSpInfo> getExpSp()
	{
		return _expSp;
	}

	public FastList<DropInfo> getDrops()
	{
		return _drops;
	}

	public FastList<SayInfo> getSays()
	{
		return _says;
	}

	public FastList<DialogInfo> getDialogs()
	{
		return _dialogs;
	}

	public FastList<DamageToPlayer> getDamageToPlayer()
	{
		return _damagesToPlayer;
	}
 	public FastList<LocInfo> getLocationsInfo()
	{
		return _locationsInfo;
	}

	public void addLocationInfo(LocInfo a)
	{
		_locationsInfo.add(a);
	}

	public boolean hasLoc(LocInfo l)
	{
		for(LocInfo c : _locationsInfo)
		{
			if(c == l)
				return true;
		}

		return false;
	}

	@Override
	public String toString()
	{
		return "Npc: " + _name + " (" + _npcId + ")";
	}
}
