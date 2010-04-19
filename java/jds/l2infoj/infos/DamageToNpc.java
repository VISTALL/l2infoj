package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 15:46:29
 */
public class DamageToNpc
{
	private int _damage;
	//player stats
	private int _type = -1;
	private int _pAttack;
	private int _mAttack;
	private boolean _crit;
	private int _attackElement;
	private int _attackValue;

	public DamageToNpc(int damage, int type, int pAttack, int mAttack, int attackElement, int attackValue, boolean crit)
	{
		setCrit(crit);
		setDamage(damage);
		setType(type);
		setPAttack(pAttack);
		setMAttack(mAttack);
		setAttackElement(attackElement);
		setAttackValue(attackValue);
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

	public int getAttackElement()
	{
		return _attackElement;
	}

	public void setAttackElement(int attackElement)
	{
		_attackElement = attackElement;
	}

	public int getAttackValue()
	{
		return _attackValue;
	}

	public void setAttackValue(int attackValue)
	{
		_attackValue = attackValue;
	}

	public boolean isCrit()
	{
		return _crit;
	}

	public void setCrit(boolean crit)
	{
		_crit = crit;
	}

	@Override
	public String toString()
	{
		return (_type == -1 ? "Phys attack " : ("Magic skill " + _type + " ")) + String.format("damage %d from player[crit(%b), pAttack(%d), mAttack(%d), element(%d), elemval(%d)]", _damage, _crit, _pAttack, _mAttack, _attackElement, _attackValue);
	}
}
