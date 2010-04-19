package jds.l2infoj.threads;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.JActionListener;
import jds.l2infoj.gui.JEvent;
import jds.l2infoj.gui.forms.MForm;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:51:36
 */
public class ShutdownThread extends Thread
{
	@Override
	public void run()
	{
		if(Config.IS_STARTED)
			JActionListener.onEvt(JEvent.START_STOP);

		ThreadPoolManager.getInstance().shutdown();
		Config.X = MForm.getInstance().getLocation().x;
		Config.Y = MForm.getInstance().getLocation().y;

		Config.save();
	}
}
