package jds.l2infoj.log;

import javolution.util.FastList;
import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.infos.*;
import jds.l2infoj.objects.GameObject;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.session.Session;
import jds.nio.buffer.NioBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 6:45:56
 *
 * C - major
 * C - minor
 *
 * b[4] - address
 * D - protocol version
 * S - user
 *
 * D - size
 * [
 *   D - npcId
 *   D - level
 * 
 *   D - max hp
 *   D - max mp
 *
 *   D - rhand
 *   D - chest
 *   D - lhand
 *
 *   D - skills size
 *   [
 *     D - id
 *     D - level
 *     D - hit time
 *     D - reuse time
 *   ]
 *
 *   D - exp sp size
 *   [
 *     Q - exp
 *     D - sp
 *   ]
 *
 *   D - drop info size
 *   [
 *     D - item id
 *     Q - count
 *   ]
 *
 *   D - damage to npc size
 *   [
 *     D  - damage
 *     D  - type
 *     D  - p attack
 *     D  - m attack
 *     D  - element type
 *     D  - element value
 *   ]
 *
 *   D - damage to player size
 *   [
 *     D  - damage
 *     D  - type
 *     D  - p def
 *     D  - m def
 *     D  - dark
 *     D  - holy
 *     D  - water
 *     D  - fire
 *     D  - earth
 *     D  - wind
 *   ]
 *
 *   D - locations size
 *   [
 *     D - x
 *     D - y
 *     D - z
 *     D - h
 *   ]
 * ]
 */
public class LogWriter
{
	public synchronized  static void write(File file, Session session) throws Exception
	{
		NioBuffer buf = NioBuffer.allocate(1);
		buf.setAutoExpand(true);
		buf.order(ByteOrder.LITTLE_ENDIAN);

		FastList<Npc> npcs = new FastList<Npc>();

		for(GameObject obj : session.getWorld().objects())
		{
			if(!(obj instanceof Npc))
				continue;
			
			Npc npc = (Npc)obj;
			npcs.add(npc);
		}
		session.getWorld().clear();

		buf.put(Config.MAJOR_V);
		buf.put(Config.MINOR_V);
		
		buf.put(session.address().getAddress() == null ? new byte[4] : session.address().getAddress());
		buf.putInt(session.getProtocol());
	    pubString(buf, Config.USER_NAME == null ? "N/A" : Config.USER_NAME);

		buf.putInt(npcs.size());
		for(Npc npc : npcs)
		{
			buf.putInt(npc.getNpcId());
			buf.putInt(npc.getLevel());
			
			buf.putInt(npc.getMaxHP());
			buf.putInt(npc.getMaxMP());

			//temp - 0
			buf.putInt(0);  // rhand
			buf.putInt(0);  // chest
			buf.putInt(0);  // lhand

			buf.putInt(npc.getSkills().size());
			for(SkillInfo o : npc.getSkills().values())
			{
				buf.putInt(o.getId());
				buf.putInt(o.getLevel());
				buf.putInt(o.getHitTime());
				buf.putInt(o.getReuseTime());
			}

			buf.putInt(npc.getExpSp().size());
			for(ExpSpInfo i : npc.getExpSp())
			{
				buf.putLong(i.getExp());
				buf.putInt(i.getSp());
			}

			buf.putInt(npc.getDrops().size());
			for(DropInfo d : npc.getDrops())
			{
				buf.putInt(d.getItemId());
				buf.putLong(d.getCount());
			}

			buf.putInt(npc.getDamageToNpc().size());
			for(DamageToNpc d : npc.getDamageToNpc())
			{
				buf.putInt(d.getDamage());
				buf.putInt(d.getType());
				buf.putInt(d.getPAttack());
				buf.putInt(d.getMAttack());
				buf.putInt(d.getAttackElement());
				buf.putInt(d.getAttackValue());
			}

			buf.putInt(npc.getDamageToPlayer().size());    			
			for(DamageToPlayer da : npc.getDamageToPlayer())
			{
				buf.putInt(da.getDamage());
				buf.putInt(da.getType());
				buf.putInt(da.getPDef());
				buf.putInt(da.getMDef());
				buf.putInt(da.getDark());
				buf.putInt(da.getHoly());
				buf.putInt(da.getWater());
				buf.putInt(da.getFire());
				buf.putInt(da.getEarth());
				buf.putInt(da.getWind());
			}

			buf.putInt(npc.getLocationsInfo().size());
			for(LocInfo l : npc.getLocationsInfo())
			{
				buf.putInt(l.getX());
				buf.putInt(l.getY());
				buf.putInt(l.getZ());
				buf.putInt(l.getH());
			}
		}

		OutputStream stream = new FileOutputStream(file);
		stream.write(buf.array());
		stream.close();

		LogForm.getInstance().log("Session " + session.getId() + " write to file");
	}

	public static void pubString( NioBuffer buf, CharSequence charSequence)
	{
		if(charSequence == null)
			charSequence = "";

		int length = charSequence.length();

		for(int i = 0; i < length; i++)
		{
			buf.putChar(charSequence.charAt(i));
		}

		buf.putChar('\000');
	}

}
