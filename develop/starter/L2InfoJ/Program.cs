using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Xml;
using System.Configuration;
using System.Collections.Specialized;
using System.Diagnostics;
using System.IO;

namespace L2InfoJ
{
	static class Program
	{
		static String JAVA_HOME = null;
		static String XMS = "-Xms512m";
		static String XMX = "-Xmx512m";

		[STAThread]
		static void Main()
		{
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);

			load();

			if (JAVA_HOME == null)
				return;


			ProcessStartInfo startInfo = new ProcessStartInfo();
			startInfo.FileName = JAVA_HOME + "javaw.exe";
			startInfo.Arguments = XMS + " " + XMX + " " + " -cp ./libs/*; jds.l2infoj.L2InfoJ";
			Process.Start(startInfo);
		}

		static void load()
		{
			if (!File.Exists("settings.xml"))
			{
				Application.Run(new MessageWindow("Not find 'settings.xml'"));
				return;
			}

			XmlDocument doc = new XmlDocument();
			doc.Load("settings.xml");

			XmlNode xNode = doc.GetElementsByTagName("settings").Item(0);

			IConfigurationSectionHandler csh = new NameValueSectionHandler();
			NameValueCollection nvc = (NameValueCollection)csh.Create(null, null, xNode);

			JAVA_HOME = nvc.Get("JAVA_HOME");
			XMS = nvc.Get("XMS");
			XMX = nvc.Get("XMX");

			if (JAVA_HOME == null)
			{
				Application.Run(new MessageWindow("Not find 'JAVA_HOME' in 'settings.xml'"));
				return;
			}
			if (!File.Exists(JAVA_HOME + "javaw.exe"))
			{
				Application.Run(new MessageWindow("Not find javaw.exe in 'JAVA_HOME'"));
				return;
			}

		}
	}
}
