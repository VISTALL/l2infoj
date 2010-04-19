using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace L2InfoJ
{
	public partial class MessageWindow : Form
	{
		public MessageWindow(String msg)
		{
			InitializeComponent();
			_msg.Text = msg;

		}

		private void Message_Load(object sender, EventArgs e)
		{
		}

		private void _okBtn_Click(object sender, EventArgs e)
		{
			Close();
		}
	}
}
