package jds.l2infoj.network.packets.server;

import jds.l2infoj.config.Config;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.infos.SkillInfo;
import jds.l2infoj.objects.GameObject;
import jds.l2infoj.objects.Npc;
import jds.l2infoj.objects.Player;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 2:58:47
 */
public class MagicSkillUse extends GPacket
{

	@Override
	public void read()
	{
		int objId = readD();
		int targetId = readD();//target
		int skillId = readD();
		int skillLevel = readD();
		int hitTime = readD();
		int reuseTime = readD();

		GameObject attacket = getPHandler().getSession().getWorld().getObject(objId);
		SkillInfo skill = new SkillInfo(skillId, skillLevel, hitTime, reuseTime);
		boolean print = false;
		
		if(attacket instanceof Player)
		{
			Player attacker = (Player)attacket;
			Npc target  = getPHandler().getSession().getWorld().getNpc(targetId);

			if(target == null)
				return;
			
			target.setLastAttacker(attacker);

			attacker.setTypeAttack(skillId);
			attacker.setLastNpc(target);

			print = true;
		} 		
		else if (attacket instanceof Npc)
		{
			Npc npc = (Npc)attacket;

			SkillInfo $ = npc.getSkill(skillId);

			if($ == null)
			{
				npc.addSkill(skill);
			}

			print = true;
		}
		
		if(Config.DEBUG && print)
		{
			LogForm.getInstance().log(attacket + " magic use " + skill);
		}
	}
}
