package jds.l2infoj.gui.models;

import javolution.util.FastList;

import javax.swing.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:18:41
 */
@SuppressWarnings("unchecked")
public class ComboListModel<T> extends AbstractListModel implements MutableComboBoxModel, Serializable
{
	private FastList<T> _list = new FastList<T>();
	private T _selectedItem;

	public ComboListModel(T[] ar)
	{
		_list.addAll(Arrays.asList(ar));
	}

	@Override
	public int getSize()
	{
		return _list.size();
	}

	@Override
	public T getElementAt(int index)
	{
		if (index >= 0 && index < _list.size())
		{
			return _list.get(index);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
		_selectedItem = (T) anItem;
	}

	@Override
	public T getSelectedItem()
	{
		return _selectedItem;
	}

	@Override
	public void addElement(Object obj)
	{

	}

	@Override
	public void removeElement(Object obj)
	{

	}

	@Override
	public void insertElementAt(Object obj, int index)
	{

	}

	@Override
	public void removeElementAt(int index)
	{

	}
}
