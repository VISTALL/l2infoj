package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 15:56:13
 */
public class DamageToPlayer
{
	private int _damage;

	private int _type = -1;
	private int _pDef;
	private int _mDef;

	private int _earth;
	private int _holy;
	private int _wind;
	private int _dark;
	private int _water;
	private int _fire;

	public DamageToPlayer(int damage, int type, int pDef, int mDef, int earth, int holy, int wind, int water, int dark, int fire)
	{
		setDamage(damage);
		setType(type);
		setPDef(pDef);
		setMDef(mDef);
		setEarth(earth);
		setHoly(holy);
		setWind(wind);
		setWater(water);
		setDark(dark);
		setFire(fire);
	}

	public int getDamage()
	{
		return _damage;
	}

	public void setDamage(int damage)
	{
		_damage = damage;
	}

	public int getType()
	{
		return _type;
	}

	public void setType(int type)
	{
		_type = type;
	}

	public int getPDef()
	{
		return _pDef;
	}

	public void setPDef(int pDef)
	{
		_pDef = pDef;
	}

	public int getMDef()
	{
		return _mDef;
	}

	public void setMDef(int mDef)
	{
		_mDef = mDef;
	}

	public int getEarth()
	{
		return _earth;
	}

	public void setEarth(int earth)
	{
		_earth = earth;
	}

	public int getHoly()
	{
		return _holy;
	}

	public void setHoly(int holy)
	{
		_holy = holy;
	}

	public int getWind()
	{
		return _wind;
	}

	public void setWind(int wind)
	{
		_wind = wind;
	}

	public int getDark()
	{
		return _dark;
	}

	public void setDark(int dark)
	{
		_dark = dark;
	}

	public int getWater()
	{
		return _water;
	}

	public void setWater(int water)
	{
		_water = water;
	}

	public int getFire()
	{
		return _fire;
	}

	public void setFire(int fire)
	{
		_fire = fire;
	}

	@Override
	public String toString()
	{
		return (_type == -1 ? "Phys attack " : ("Magic skill " + _type + " ")) + String.format("damage %d from npc to player[pDef(%d), mDer(%d), fire(%d), water(%d), holy(%d), dark(%d), wind(%d), earth(%d)]", _damage, _pDef, _mDef, _fire, _water, _holy, _dark, _wind, _earth);
	}
}
