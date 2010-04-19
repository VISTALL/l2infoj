package jds.l2infoj.objects;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 3:01:43
 */
public class Creature extends GameObject
{
	protected String _name;

	protected int _maxHP;
	protected int _maxMP;

	private boolean _isDead;

	private int _pAttack;
	private int _mAttack;
	private int _pDef;
	private int _mDef;
	private int _pSpeed;
	private int _mSpeed;
	private int _level;

	private int _typeAttack = -1; //последний тип аттаки, либо скил айди либо -1 для физ аттки

	private boolean _nextDamageIsCritical;


	protected Creature(int od)
	{
		super(od);
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public int getMaxHP()
	{
		return _maxHP;
	}

	public void setMaxHP(int maxHP)
	{
		_maxHP = maxHP;
	}

	public int getMaxMP()
	{
		return _maxMP;
	}

	public void setMaxMP(int maxMP)
	{
		_maxMP = maxMP;
	}

	public boolean isDead()
	{
		return _isDead;
	}

	public void setDead(boolean dead)
	{
		_isDead = dead;
	}

	public int getPAttack()
	{
		return _pAttack;
	}

	public void setPAttack(int pAttack)
	{
		_pAttack = pAttack;
	}

	public int getMAttack()
	{
		return _mAttack;
	}

	public void setMAttack(int mAttack)
	{
		_mAttack = mAttack;
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

	public int getPSpeed()
	{
		return _pSpeed;
	}

	public void setPSpeed(int pSpeed)
	{
		_pSpeed = pSpeed;
	}

	public int getMSpeed()
	{
		return _mSpeed;
	}

	public void setMSpeed(int mSpeed)
	{
		_mSpeed = mSpeed;
	}

	public int getTypeAttack()
	{
		return _typeAttack;
	}

	public void setTypeAttack(int typeAttack)
	{
		_typeAttack = typeAttack;
	}

	public boolean isNextDamageIsCritical()
	{
		return _nextDamageIsCritical;
	}

	public void setNextDamageIsCritical(boolean nextDamageIsCritical)
	{
		_nextDamageIsCritical = nextDamageIsCritical;
	}
	
	public int getLevel()
	{
		return _level;
	}

	public void setLevel(int level)
	{
		_level = level;
	}
}
