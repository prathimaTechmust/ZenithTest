package com.techmust.generic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;


import com.techmust.generic.data.GenericData;
import com.techmust.generic.listener.ITradeMustEventListener;
@Component
public class HibernateUtil 
{
	public static Logger m_oLogger = Logger.getLogger(HibernateUtil.class);
    private static List<ITradeMustEventListener> m_arrListeners = new ArrayList<ITradeMustEventListener>();
	
	protected static EntityManager m_oTenantEntityManager;
	
    private static int m_nConnectionsOpened = 0;
    private static int m_nConnectionsClosed = 0;
    
    public static void addConnection ()
    {
    	m_nConnectionsOpened++;
    	m_oLogger.info("Connections opened from Admin app:"+ m_nConnectionsOpened);
    }
	
    public static void removeConnection()
    {
    	m_nConnectionsClosed++;
    	m_oLogger.info("Connections closed from admin app :"+ m_nConnectionsOpened);
    }

	@PersistenceContext(unitName = "tenantdb-persistence-unit")
	public void setEntityManager(EntityManager oEntityManager)
	{
        HibernateUtil.m_oTenantEntityManager =  oEntityManager;
	}
	
	protected static EntityManager m_oMasterEntityManager;
	@PersistenceContext(unitName = "masterdb-persistence-unit")
	public void setMasterEntityManager(EntityManager oEntityManager)
	{
        HibernateUtil.m_oMasterEntityManager =  oEntityManager;
	}
	
	public static EntityManager getTenantEntityManager ()
	{
		EntityManager oEntityManager = null;
 		m_oLogger.info("getEntityManager");
 		try
		{
 	 		oEntityManager = m_oTenantEntityManager.getEntityManagerFactory().createEntityManager();
 		}
		catch(HibernateException oHibernateException)
		{
			oHibernateException.printStackTrace();
			System.out.println("getEntityManager - oHibernateException : " + oHibernateException);
			m_oLogger.error("getEntityManager - oHibernateException : " +oHibernateException);
		}
		catch (Exception oException) 
		{
			System.out.println("getEntityManager - oException : " + oException);
			m_oLogger.error("getEntityManager - oException : " +oException);
		}
 		addConnection();
		return oEntityManager;
	}
	
	public static EntityManager getMasterEntityManager ()
	{
		EntityManager oEntityManager = null;
 		m_oLogger.info("getMasterEntityManager");
 		try
		{
 	 		oEntityManager = m_oMasterEntityManager.getEntityManagerFactory().createEntityManager();
 		}
		catch(HibernateException oHibernateException)
		{
			oHibernateException.printStackTrace();
			System.out.println("getMasterEntityManager - oHibernateException : " + oHibernateException);
			m_oLogger.error("getMasterEntityManager - oHibernateException : " +oHibernateException);
		}
		catch (Exception oException) 
		{
			System.out.println("getEntityManager - oException : " + oException);
			m_oLogger.error("getEntityManager - oException : " +oException);
		}
 		addConnection();
		return oEntityManager;
	}
	public static <T> void registerListener (ITradeMustEventListener<T> oListener)
	{
		m_arrListeners.add(oListener);
	}

	public static void addListeners(GenericData oGenericData)
    {
	    // TODO Auto-generated method stub
	    for (int nIndex = 0; nIndex < m_arrListeners.size(); nIndex++)
	    {
	    	try
	    	{
	    		m_arrListeners.get(nIndex).register(oGenericData);
	    	}
	    	catch (Exception oException)
	    	{
	    		
	    	}
	    }
    }
}	
