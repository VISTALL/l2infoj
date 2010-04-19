package jds.l2infoj.network.packets.server;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 14:41:56
 */
public class CharacterSelectionInfo extends GPacket
{

	@Override
	public void read()
	{
   	    /*int size = readD();
		readD();
		readC();

		for(int i = 0; i < size; i ++)
		{
			readS(); //name
			readD();
			String account = readS();
			getPHandler().getSession().setAccount(account);
			LogForm.getInstance().log("Account: " + account);
			break;
		}*/
	}
}
