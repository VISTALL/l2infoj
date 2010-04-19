package jds.l2infoj.datas;

import javolution.util.FastMap;
import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:29:57
 */
public class NpcNameHolder
{
	private static NpcNameHolder _instance;
	
	private FastMap<Integer, String> _npcNames = new FastMap<Integer, String>();

	public static NpcNameHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new NpcNameHolder();
		}
		return _instance;
	}

	private NpcNameHolder()
	{
		InputStream stream = getClass().getResourceAsStream("/jds/l2infoj/resources/datas/npcname-e.txt");
		if(stream == null)
		{
			if(Config.DEBUG)
			{
				LogForm.getInstance().log("Npc Name File is missing");
			}
			return;
		}

		LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(stream));

		try
		{
			String line = null;
			while((line = lineReader.readLine()) != null)
			{
				if(line.contains("#"))
					continue;

				StringTokenizer st = new StringTokenizer(line, "\t");
				int npcId = Integer.parseInt(st.nextToken());
				String npcName = st.nextToken();
				
				_npcNames.put(npcId, npcName);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				lineReader.close();
			}
			catch (IOException e)
			{
				//
			}
		}

		if(Config.DEBUG)
		{
			LogForm.getInstance().log("Load npc names " + _npcNames.size());
		}
	}

	public String name(int npcId)
	{
		return _npcNames.get(npcId) == null ? "None" : _npcNames.get(npcId);
	}
}
