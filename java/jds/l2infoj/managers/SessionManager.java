package jds.l2infoj.managers;

import javolution.util.FastMap;
import jds.l2infoj.gui.forms.LogForm;
import jds.l2infoj.session.Session;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 2:33:19
 */
public class SessionManager
{
	private static SessionManager _instance;

	private FastMap<Long, Session> _sessions = new FastMap<Long, Session>();

	public static SessionManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SessionManager();
		}
		return _instance;
	}

	private SessionManager()
	{

	}

	public Session getSession(long sessionId, boolean createIfNOtExists)
	{
		if(!_sessions.containsKey(sessionId) && createIfNOtExists)
		{
			LogForm.getInstance().log("Session started " + sessionId);
			_sessions.put(sessionId, new Session(sessionId));
		}

		return _sessions.get(sessionId);
	}

	public void removeSession(Session session)
	{
		_sessions.remove(session.getId());
	}        
}
