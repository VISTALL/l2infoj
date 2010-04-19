package jds.l2infoj.gui;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.MForm;
import jds.l2infoj.network.Winpcap;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:08:26
 */
public class JActionListener
{
	public static void onEvt(JEvent event)
	{
		switch(event)
		{
			case START_STOP:
				if(Config.IS_STARTED)
				{
					Config.IS_STARTED = false;
					Winpcap.getInstance().stop();
				}
				else
				{
					Config.IS_STARTED = true;
					Winpcap.getInstance().start();
				}

				MForm.getInstance().setIcon(Config.IS_STARTED);
				break;
		}
	}
}
