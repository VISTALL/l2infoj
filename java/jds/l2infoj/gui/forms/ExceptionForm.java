package jds.l2infoj.gui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javolution.util.FastList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 12:02:15
 */
public class ExceptionForm extends JFrame
{
	private static ExceptionForm _instance;
	private JTextPane _pane;
	private JPanel root;
	private FastList<Throwable> _exceptionList = new FastList<Throwable>();

	public static ExceptionForm getInstance()
	{
		if (_instance == null)
		{
			_instance = new ExceptionForm();
		}
		return _instance;
	}

	public ExceptionForm()
	{
		setContentPane(root);
		try
		{
			setIconImage(ImageIO.read(getClass().getResource("/jds/l2infoj/resources/images/icon.png")));
		}
		catch (IOException e)
		{
		}

		setSize(450, 700);
		setTitle("Exception List");
		setResizable(false);
	}

	public void addException(Throwable e)
	{
		MForm.getInstance().exception();
		_exceptionList.add(e);
		update();
	}

	public void update()
	{
		_pane.setText("");

		for (Throwable e : _exceptionList)
		{
			_pane.setText(_pane.getText() + stackTrace(e) + "\n");
		}
	}


	public String stackTrace(Throwable e)
	{
		String ee = e.getMessage();
		StackTraceElement[] trace = e.getStackTrace();

		for (StackTraceElement aTrace : trace)
		{
			ee = ee + "\n\tat " + aTrace;
		}
		return ee;
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
		root.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
		final JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setVerticalScrollBarPolicy(22);
		root.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		_pane = new JTextPane();
		_pane.setEditable(false);
		scrollPane1.setViewportView(_pane);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return root;
	}
}
