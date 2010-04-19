package jds.l2infoj.config;

import ca.beq.util.win32.registry.RegistryKey;
import ca.beq.util.win32.registry.RegistryValue;
import ca.beq.util.win32.registry.RootKey;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 4:07:46
 */
public class Config
{
	//versions
	public static final byte MAJOR_V = 0;
	public static final byte MINOR_V = 1;
	public static final int VALID_VERSION = 146;

	public static String USER_NAME = "Anonymous";
	public static int DEVICE_ID;
	public static boolean DEBUG = true;
	public static boolean USE_TRAY;
	// winpcap
	public static final int LENGTH = Short.MAX_VALUE & 0xFFFF;
	public static final boolean PROMICS_MODE = true;
	public static final int PORT = 7777;
	public static final int TIMEOUT = 10;

	public static int X = -1;
	public static int Y = -1;

	public static boolean IS_STARTED;

	public static void load()
	{
		RegistryKey key = new RegistryKey(RootKey.HKEY_CURRENT_USER, "Software\\J Develop Station\\L2InfoJ");

		if(!key.exists())
			return;

		if(key.hasValue("DeviceId"))
		{
			DEVICE_ID = (Integer)key.getValue("DeviceId").getData();
		}
		if(key.hasValue("UseTray"))
		{
			USE_TRAY = ((Integer)key.getValue("UseTray").getData()) == 1;
		}
		if(key.hasValue("UserName"))
		{
			USER_NAME = (String)key.getValue("UserName").getData();
		}

		if(key.hasValue("X"))
		{
			X = (Integer)key.getValue("X").getData();
		}

		if(key.hasValue("Y"))
		{
			Y = (Integer)key.getValue("Y").getData();
		}
	}

	public static void save()
	{
		RegistryKey key = new RegistryKey(RootKey.HKEY_CURRENT_USER, "Software\\J Develop Station\\L2InfoJ");

		if(!key.exists())
		{
			key.create();
		}

		key.setValue(new RegistryValue("DeviceId", DEVICE_ID));
		key.setValue(new RegistryValue("UserName", USER_NAME));
		key.setValue(new RegistryValue("UseTray", USE_TRAY ? 1 : 0));
		key.setValue(new RegistryValue("X", X));
		key.setValue(new RegistryValue("Y", Y));
	}
}
