package jds.l2infoj.network.packets.server;

import jds.l2infoj.gui.forms.ExceptionForm;
import jds.l2infoj.log.LogWriter;

import java.io.File;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 10:42:56
 */
public class LeaveWorld extends GPacket
{

	@Override
	public void read()
	{
		try
		{
			String path = "./logs/" + getPHandler().getSession().getId() + ".bin";

			File dir = new File("./logs/");

			if(!dir.exists())
				dir.mkdir();
			File file = new File(path);
			file.delete();
			file.createNewFile();

			LogWriter.write(file, getPHandler().getSession());

			//SessionManager.getInstance().removeSession(getPHandler().getSession());

			///System.out.println("Session " + getPHandler() + " closed.");
		}
		catch (Exception e)
		{
			ExceptionForm.getInstance().addException(e);
		}
	}
}
