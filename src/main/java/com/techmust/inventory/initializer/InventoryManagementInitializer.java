package com.techmust.inventory.initializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.util.GenericUtil;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.inventory.items.ItemAddEventListener;
import com.techmust.inventory.items.ItemCategoryData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.role.RoleDataProcessor;
import com.techmust.usermanagement.role.RoleResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class InventoryManagementInitializer
{
	private static final String kDefaultXmlPath = "com/techmust/inventory/resource/InventoryManagement.defaults.xml";
	public static Logger m_oLogger = Logger.getLogger(InventoryManagementInitializer.class);
	
	public static void initialize () throws Exception
	{
		m_oLogger.info ("initialize");
		Document oXMLDocument = null;
		try
		{
			String strInventoryManagementDefaultPath  = GenericUtil.getProperty("kINVENTORYMANAGEMENTDEFAULTPATH");
			InputStream oDefaultXmlStream = InventoryManagementInitializer.class.getClassLoader().getResourceAsStream (kDefaultXmlPath);
			oXMLDocument = getXmlDocument (strInventoryManagementDefaultPath, oDefaultXmlStream);
			createListeners ();
			insertInventoryManagementActions (oXMLDocument);
			if(isCategoryTableEmpty ())
				insertDefaultItemCategory ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("initialize - oException : " + oException);
			throw oException;
		}
	}
	
	private static boolean isCategoryTableEmpty() 
	{
		m_oLogger.info ("insertDefaultItemCategory");
		boolean bCategoryTableEmpty = false;
		ItemCategoryData oCategoryData = new ItemCategoryData ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			if(oCategoryData.list(oOrderBy).size() == 0)
				bCategoryTableEmpty = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("insertDefaultItemCategory - oException" + oException);
		}
		return bCategoryTableEmpty;
	}

	private static void insertDefaultItemCategory () throws Exception
	{
		m_oLogger.info ("insertDefaultItemCategory");
		ItemCategoryData oCategoryData = new ItemCategoryData ();
		try
		{
			oCategoryData.setM_strCategoryName("Others");
			oCategoryData.setM_nCreatedBy(getAdminData ().getM_nUserId());
			oCategoryData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("insertDefaultItemCategory - oException" + oException);
			throw oException;
		}
	}
	
	private static UserInformationData getAdminData() throws Exception 
	{
		UserInformationData oUserInformationData = new UserInformationData ();
		oUserInformationData.setM_strLoginId("admin");
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oUserInformationData = (UserInformationData) oUserInformationData.list(oOrderBy).get(0);
		return oUserInformationData;
	}

	public static void createListeners()
    {
		HibernateUtil.registerListener(new ItemAddEventListener ());
    }
	
	private static Document getXmlDocument (String strXmlpath, InputStream oInputStream) throws Exception
	{
		m_oLogger.info ("getXmlDocument");
		m_oLogger.debug ("getXmlDocument - strXmlpath [IN] : " + strXmlpath);
		m_oLogger.debug ("getXmlDocument - oInputStream [IN] : "+ oInputStream);
		Document oXmlDocument = null;
		try
		{
			DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();
			if(strXmlpath == null || (! new File(strXmlpath).exists()))
				oXmlDocument = oDocumentBuilder.parse(oInputStream);
			else
				oXmlDocument = oDocumentBuilder.parse(strXmlpath);
		} 
		catch (FileNotFoundException oFileNotFoundException)
		{  
			m_oLogger.error ("getXmlDocument - oFileNotFoundException : " +oFileNotFoundException);
			throw new FileNotFoundException ("The initializer file not found. Please ensure the file provided in the property" +strXmlpath+ "exists");
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getXmlDocument - oException" +oException);
			throw oException;
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}

	private static void insertInventoryManagementActions (Document oDocument) throws Exception
	{
		m_oLogger.debug ("insertInventoryManagementActions - oDocument [IN] : " + oDocument);
		
		RoleDataProcessor oRoleDataProcessor = new RoleDataProcessor ();
		RoleData oRoleData = new RoleData ();
		try
		{
			ArrayList<ActionData> arrClientActions = new ArrayList<ActionData> ();
			arrClientActions = UserManagementInitializer.insertActions (oDocument);
			oRoleData.setM_nRoleId (getUserRoleId ());
			RoleResponse oRoleResponse  = oRoleDataProcessor.get (oRoleData);
			Set<ActionData> oActions  = oRoleResponse.m_arrRoleData.get (0).getm_oActions ();
			arrClientActions.addAll (oActions);
			oRoleData.setM_strRoleName (Constants.kUserRole);
			oRoleData.m_arrActionData = (ActionData[]) arrClientActions.toArray (new ActionData [arrClientActions.size ()]);
			oRoleDataProcessor.update (oRoleData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("insertInventoryManagementActions - oException" + oException);
			throw oException;
		}
	}
	
	private static int getUserRoleId () throws Exception
	{
		m_oLogger.info ("getUserRoleId");
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> (); 
		int nRoleId = -1;
		try
		{
			RoleData oRolaData = new RoleData ();
			oRolaData.setM_strRoleName (Constants.kUserRole);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			arrGenericData = oRolaData.list (oOrderBy);
			oRolaData = (RoleData) arrGenericData.get (0);
			nRoleId = oRolaData.getM_nRoleId ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getUserRoleId  - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("getUserRoleId -  nRoleId [OUT] :  " + nRoleId);
		return nRoleId;
	}
}
