package jds.l2infoj.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 6:34:33
 */
public class DialogInfo
{
	private final int _questId;
	private final String _dialog;

	public DialogInfo(String text)
	{
 	    this(text, 0);
	}

	public DialogInfo(String text, int qu)
	{
		_questId = qu;
		_dialog = text;
	}

	@Override
	public String toString()
	{
		return "Dialog: " + (getQuestId() == 0 ? "" : (" questID: " + getQuestId())) + getDialog();
	}

	public int getQuestId()
	{
		return _questId;
	}

	public String getDialog()
	{
		return _dialog;
	}
}
