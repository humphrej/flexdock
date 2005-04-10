package org.flexdock.view.perspective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

/**
 * @author Mateusz Szczap
 */
public class PerspectiveManager implements IPerspectiveManager {
	
	private HashMap m_perspectives = new HashMap();
	
	private ArrayList m_listeners = new ArrayList();
	
	private static IPerspectiveManager SINGLETON = null;
	
	private IPerspective m_defaultPerspective = null;
	
	private IPerspective m_currentPerspective = null;
	
	public static IPerspectiveManager getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new PerspectiveManager();
		}
		return SINGLETON;
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#addPerspective(java.lang.String, org.flexdock.view.perspective.IPerspective)
	 */
	public void addPerspective(String perspectiveId, IPerspective perspective) {
		if (perspectiveId == null) throw new NullPointerException("perspectiveId cannot be null");
		if (perspective == null) throw new NullPointerException("perspective cannot be null");
		
		m_perspectives.put(perspectiveId, perspective);
		
		firePerspectiveAdded(perspective);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#removePerspective(java.lang.String)
	 */
	public void removePerspective(String perspectiveId) {
		if (perspectiveId == null) throw new NullPointerException("perspectiveId cannot be null");
		
		IPerspective perspective = getPerspective(perspectiveId);
		if (perspective == null) throw new RuntimeException("Unable to find perspective: "+perspectiveId);
		
		m_perspectives.remove(perspectiveId);

		firePerspectiveRemoved(perspective);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#getPerspective()
	 */
	public IPerspective getPerspective(String perspectiveId) {
		if (perspectiveId == null) throw new NullPointerException("perspectiveId cannot be null");
		
		return (IPerspective) m_perspectives.get(perspectiveId);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#addPerspectiveListener(org.flexdock.view.perspective.PerspectiveListener)
	 */
	public void addPerspectiveListener(PerspectiveListener perspectiveListener) {
		if (perspectiveListener == null) throw new NullPointerException("perspectiveListener cannot be null");
		
		m_listeners.add(perspectiveListener);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#removePerspectiveListener(org.flexdock.view.perspective.PerspectiveListener)
	 */
	public void removePerspectiveListener(PerspectiveListener perspectiveListener) {
		if (perspectiveListener == null) throw new NullPointerException("perspectiveListener cannot be null");
		m_listeners.remove(perspectiveListener);
	}
	
	/**
	 * @see org.flexdock.view.perspective.PerspectiveMonitor#getPerspectiveListners()
	 */
	public IPerspective[] getPerspectiveListners() {
		return (IPerspective[]) m_listeners.toArray(new IPerspective[]{});
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#setDefaultPerspective(org.flexdock.view.perspective.IPerspective)
	 */
	public void setDefaultPerspective(String perspectiveId) {
		m_defaultPerspective = getPerspective(perspectiveId);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#getDefaultPerspective()
	 */
	public IPerspective getDefaultPerspective() {
		return m_defaultPerspective;
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#applyPerspective(java.awt.Container, org.flexdock.view.perspective.IPerspective)
	 */
	public void applyPerspective(IPerspective perspective) {
		if (perspective == null) throw new NullPointerException("perspective cannot be null");

		clearPerspective(perspective);

		Viewport mainViewPort = perspective.getMainViewport();
		View centerView = perspective.getTerritoralView();
		if (!DockingManager.isDocked((Dockable)centerView)) {
			mainViewPort.dock(centerView);
		}

		Perspective.ViewDockingInfo[] dockingInfos = perspective.getDockingInfoChain();
		for (int i=0; i<dockingInfos.length; i++) {
			Perspective.ViewDockingInfo dockingInfo = (Perspective.ViewDockingInfo) dockingInfos[i];
			View sourceView = dockingInfo.getSourceView();
			View targetView = dockingInfo.getTargetView();
			String region = dockingInfo.getRelativeRegion();
			float ratio = dockingInfo.getRatio();
			
			sourceView.dock(targetView, region, ratio);
		}
		
		IPerspective oldPerspective = m_currentPerspective;
		m_currentPerspective = perspective;
		
		firePerspectiveChanged(oldPerspective, perspective);
	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#undockAll(org.flexdock.view.perspective.IPerspective)
	 */
	public void clearPerspective(IPerspective perspective) {
		Viewport mainViewPort = perspective.getMainViewport();

		Set viewSet = mainViewPort.getViewset();
		for (Iterator it = viewSet.iterator(); it.hasNext();) {
			View view = (View) it.next();
			if (DockingManager.isDocked((Dockable)view)) {
				DockingManager.undock(view);
			}
		}

	}
	
	/**
	 * @see org.flexdock.view.perspective.IPerspectiveManager#applyPerspective(java.awt.Container, java.lang.String)
	 */
	public void applyPerspective(String perspectiveId) {
		IPerspective perspective = getPerspective(perspectiveId);

		if (perspective != null) {
			applyPerspective(perspectiveId);
		}
	}
	
	protected void firePerspectiveChanged(IPerspective oldPerspective, IPerspective newPerspective) {
		for (int i=0; i<m_listeners.size(); i++) {
			PerspectiveListener perspectiveListener = (PerspectiveListener) m_listeners.get(i);
			perspectiveListener.onPerspectiveChanged(oldPerspective, newPerspective);
		}
	}

	protected void firePerspectiveAdded(IPerspective perspective) {
		for (int i=0; i<m_listeners.size(); i++) {
			PerspectiveListener perspectiveListener = (PerspectiveListener) m_listeners.get(i);
			perspectiveListener.onPerspectiveAdded(perspective);
		}
	}

	protected void firePerspectiveRemoved(IPerspective perspective) {
		for (int i=0; i<m_listeners.size(); i++) {
			PerspectiveListener perspectiveListener = (PerspectiveListener) m_listeners.get(i);
			perspectiveListener.onPerspectiveRemoved(perspective);
		}
	}

}