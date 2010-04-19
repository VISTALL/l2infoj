package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:57:11
 */
public class SkillInfo
{
 	private final int _id;
	private final int _level;
	private final int _hitTime;
	private final int _reuseTime;

	public SkillInfo(int id, int level, int hitTime, int reuseTime)
	{
		_id = id;
		_level = level;
		_hitTime = hitTime;
		_reuseTime = reuseTime;
	}

	public int getId()
	{
		return _id;
	}

	public int getLevel()
	{
		return _level;
	}

	public int getHitTime()
	{
		return _hitTime;
	}

	public int getReuseTime()
	{
		return _reuseTime;
	}

	@Override
	public String toString()
	{
	 	return String.format("Skill: id: %d; level: %d; hitTime: %d; reuseTime: %d" , _id, _level, _hitTime, _reuseTime);
	}
}
