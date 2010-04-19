package jds.l2infoj.gui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import jds.l2infoj.config.Config;
import jds.l2infoj.gui.JActionListener;
import jds.l2infoj.gui.JEvent;
import jds.l2infoj.gui.list.NetworkInterfaceItem;
import jds.l2infoj.gui.models.ComboListModel;
import jds.l2infoj.network.Winpcap;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 7:18:36
 */
public class MForm extends JFrame
{
	private static MForm _instance;
	private static final String VERSION = String.format("L2InfoJ v. %d.%d", Config.MAJOR_V, Config.MINOR_V);

	private JPanel root;
	private JComboBox _deviceBox;
	private JButton _btn;
	private JCheckBox _miniBox;
	private JLabel _desc;
	private JButton _logBtn;
	private JButton _exception;
	private JTextField _userField;
	private JCheckBox _ediT;

	public static MForm getInstance()
	{
		if (_instance == null)
		{
			_instance = new MForm();
		}

		return _instance;
	}

	public MForm()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(root);

		try
		{
			setIconImage(ImageIO.read(getClass().getResource("/jds/l2infoj/resources/images/icon.png")));
		}
		catch (IOException e)
		{
		}

		setTitle(VERSION);
		setResizable(false);
		setSize(450, 220);

		NetworkInterface[] list = JpcapCaptor.getDeviceList();
		NetworkInterfaceItem[] l2 = new NetworkInterfaceItem[list.length];

		for (int i = 0; i < list.length; i++)
		{
			l2[i] = new NetworkInterfaceItem(list[i]);
		}

		ComboListModel<NetworkInterfaceItem> items = new ComboListModel<NetworkInterfaceItem>(l2);
		_deviceBox.setModel(items);

		_deviceBox.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				_desc.setText(((NetworkInterfaceItem) e.getItem()).description());
				Config.DEVICE_ID = _deviceBox.getSelectedIndex();
				try
				{
					Winpcap.getInstance().init();
				}
				catch (IOException e1)
				{
					ExceptionForm.getInstance().addException(e1);
				}
			}
		});

		_btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JActionListener.onEvt(JEvent.START_STOP);
			}
		});

		_miniBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Config.USE_TRAY = _miniBox.isSelected();
			}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowIconified(WindowEvent e)
			{
				if (Config.USE_TRAY)
				{
					setVisible(!isVisible());
				}
			}
		});

		_logBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LogForm.getInstance().setVisible(!LogForm.getInstance().isVisible());
			}
		});
		_exception.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ExceptionForm.getInstance().setVisible(!ExceptionForm.getInstance().isVisible());
			}
		});

		_ediT.setSelected(false);
		_userField.setEditable(false);
		_ediT.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_userField.setEditable(_ediT.isSelected());
			}
		});

		tray();

		_userField.setText(Config.USER_NAME);
		_miniBox.setSelected(Config.USE_TRAY);

		if (Config.DEVICE_ID > _deviceBox.getModel().getSize())
		{
			Config.DEVICE_ID = 0;
		}

		_deviceBox.setSelectedIndex(Config.DEVICE_ID);

		if(Config.X != -1 && Config.Y != -1)
		{
			setLocation(Config.X, Config.Y);
		}
		else
		{
			setLocationRelativeTo(null);			
		}
	}

	public void tray()
	{
		if (!SystemTray.isSupported())
		{
			return;
		}

		SystemTray sy = SystemTray.getSystemTray();

		try
		{
			TrayIcon ico = new TrayIcon(ImageIO.read(getClass().getResource("/jds/l2infoj/resources/images/icon2.png")), VERSION);
			ico.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					setVisible(!isVisible());
				}
			});

			sy.add(ico);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setIcon(boolean start)
	{
		Config.USER_NAME = _userField.getText();

		_ediT.setEnabled(!start);
		_deviceBox.setEnabled(!start);
		_userField.setEnabled(!start);

		if (start)
		{
			_btn.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/stop.png")));
		}
		else
		{
			_btn.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/start.png")));
		}
	}

	public void exception()
	{
		_exception.setVisible(true);
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		root = new JPanel();
		root.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel1.setBackground(new Color(-16777216));
		root.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/logo.png")));
		label1.setText("");
		panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel2.setBackground(new Color(-16777216));
		panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		final JLabel label2 = new JLabel();
		label2.setBackground(new Color(-16777216));
		label2.setForeground(new Color(-1));
		label2.setHorizontalAlignment(4);
		label2.setHorizontalTextPosition(4);
		label2.setText("J Develop Station © 2010");
		panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
		root.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 5), -1, -1));
		panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_deviceBox = new JComboBox();
		panel4.add(_deviceBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(1, 5, new Insets(0, 5, 0, 8), -1, -1));
		panel3.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Devices:");
		panel5.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel5.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JLabel label4 = new JLabel();
		label4.setText("User:");
		panel5.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_userField = new JTextField();
		_userField.setHorizontalAlignment(4);
		_userField.setText("Anonimus");
		panel5.add(_userField, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
		_ediT = new JCheckBox();
		_ediT.setText("Editable");
		panel5.add(_ediT, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 5), -1, -1));
		panel3.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_desc = new JLabel();
		_desc.setHorizontalAlignment(0);
		_desc.setHorizontalTextPosition(0);
		_desc.setText("-");
		panel6.add(_desc, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JToolBar toolBar1 = new JToolBar();
		toolBar1.setFloatable(false);
		toolBar1.setMargin(new Insets(10, 10, 10, 10));
		panel3.add(toolBar1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		_miniBox = new JCheckBox();
		_miniBox.setText("Minimize to tray?");
		toolBar1.add(_miniBox);
		final Spacer spacer3 = new Spacer();
		toolBar1.add(spacer3);
		_exception = new JButton();
		_exception.setBorderPainted(false);
		_exception.setFocusPainted(false);
		_exception.setFocusable(false);
		_exception.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/exception.png")));
		_exception.setText("");
		_exception.setVisible(false);
		toolBar1.add(_exception);
		_logBtn = new JButton();
		_logBtn.setBorderPainted(false);
		_logBtn.setFocusPainted(false);
		_logBtn.setFocusable(false);
		_logBtn.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/log.png")));
		_logBtn.setText("");
		toolBar1.add(_logBtn);
		_btn = new JButton();
		_btn.setBorderPainted(false);
		_btn.setContentAreaFilled(true);
		_btn.setFocusPainted(false);
		_btn.setFocusable(false);
		_btn.setHorizontalAlignment(0);
		_btn.setHorizontalTextPosition(0);
		_btn.setIcon(new ImageIcon(getClass().getResource("/jds/l2infoj/resources/images/start.png")));
		_btn.setOpaque(true);
		_btn.setPreferredSize(new Dimension(65, 43));
		_btn.setRequestFocusEnabled(true);
		_btn.setRolloverEnabled(true);
		_btn.setText("");
		toolBar1.add(_btn);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return root;
	}
}
