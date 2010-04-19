package jds.l2infoj;

import ca.beq.util.win32.registry.RegistryKey;
import jds.l2infoj.config.Config;
import jds.l2infoj.datas.NpcNameHolder;
import jds.l2infoj.gui.forms.MForm;
import jds.l2infoj.threads.ShutdownThread;
import jds.l2infoj.threads.ThreadPoolManager;

import javax.swing.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 01/01/2010
 * Time: 2:25:29
 */
public class L2InfoJ
{
	public static void main(String... arg) throws Exception
	{
		try
		{
			RegistryKey.initialize();
			Config.load();
			ThreadPoolManager.getInstance();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			MForm.getInstance();

			NpcNameHolder.getInstance();

			MForm.getInstance().setVisible(true);

			Runtime.getRuntime().addShutdownHook(new ShutdownThread());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			System.exit(-1);
		}
	}
}
