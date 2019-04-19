package com.techmust.clientmanagement.initializer;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.techmust.generic.data.AppProperties;
import com.techmust.generic.data.GenericData;
import com.techmust.master.businesstype.BusinessTypeData;
import com.techmust.master.businesstype.BusinessTypeDataProcessor;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.role.RoleDataProcessor;
import com.techmust.usermanagement.role.RoleResponse;

public class ClientManagementInitilizer
{
	public static Logger m_oLogger = Logger.getLogger (ClientManagementInitilizer.class);
	private static final String kDefaultXmlPath = "com/techmust/clientmanagement/resources/ClientManagement.defaults.xml";	

	public static void initilize ()
	{
		m_oLogger.info ("initilize");
		try
		{
			String strClientManagementXmlPath = AppProperties.getProperty ("ClientManagement.defaults");
			InputStream oDefaultXmlStream = ClientManagementInitilizer.class.getClassLoader ().getResourceAsStream (kDefaultXmlPath);
			Document oXMLClientDocument = getXmlDocument (strClientManagementXmlPath, oDefaultXmlStream);
			insertClientManagementActions (oXMLClientDocument);
			insertBusinessTypes (oXMLClientDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("initilize - oException : " + oException);
		}
	}
	
	private static Document getXmlDocument (String strXmlpath, InputStream oInputStream) throws Exception
	{
		m_oLogger.info ("getXmlDocument");
		m_oLogger.debug ("getXmlDocument - strXmlpath [IN] : " + strXmlpath);
		m_oLogger.debug ("getXmlDocument - oInputStream [IN] : " + oInputStream);
		Document oXmlDocument = null;
		try
		{
			DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance ();
			DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder ();
			if(strXmlpath == null || (! new File(strXmlpath).exists ()))
				oXmlDocument = oDocumentBuilder.parse (oInputStream);
			else
				oXmlDocument = oDocumentBuilder.parse (strXmlpath);
		} 
		catch (FileNotFoundException oFileNotFoundException)
		{  
			m_oLogger.error ("getXmlDocument - oFileNotFoundException : " + oFileNotFoundException);
			throw new FileNotFoundException ("The initializer file not found. Please ensure the file provided in the property" + strXmlpath + "exists");
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getXmlDocument - oException : " + oException);
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}
	
	private static void insertClientManagementActions (Document oXMLClientDocument) throws Exception
	{
		m_oLogger.info ("insertClientManagementActions");
		m_oLogger.debug ("insertClientManagementActions - oXMLClientDocument [IN] : " + oXMLClientDocument);
		
		RoleDataProcessor oRoleDataProcessor = new RoleDataProcessor ();
		RoleData oRoleData = new RoleData ();
		try 
		{
			ArrayList<ActionData> arrClientActions = new ArrayList<ActionData> ();
			arrClientActions = UserManagementInitializer.insertActions (oXMLClientDocument);
			oRoleData.setM_nRoleId (getAdminRoleId ());
			RoleResponse oRoleResponse  = oRoleDataProcessor.get (oRoleData);
			Set<ActionData> oActions  = oRoleResponse.m_arrRoleData.get (0).getm_oActions ();
			arrClientActions.addAll (oActions);
			oRoleData.setM_strRoleName ("admin");
			oRoleData.m_arrActionData = (ActionData[]) arrClientActions.toArray (new ActionData [arrClientActions.size ()]);
			oRoleDataProcessor.update (oRoleData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("insertClientManagementActions - oException : " + oException);
			throw oException;
		}
	}

	private static void insertBusinessTypes (Document oDocument) throws Exception
	{
		m_oLogger.info ("insertBusinessTypes");
		m_oLogger.debug ("insertBusinessTypes - oDocument [IN] : " + oDocument);
		
		try
		{
			NodeList oNodeList = oDocument.getElementsByTagName ("BusinessTypes");
			for(int nIndex = 0 ; nIndex < oNodeList.getLength (); nIndex++)
			{
				Node oNode = oNodeList.item (nIndex);
				processBusinessTypes (oNode);
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("insertBusinessTypes - oException" + oException);
			throw oException;
		}
	}

	private static void processBusinessTypes (Node oBusinessTypeNode) throws Exception 
	{
		m_oLogger.info ("processBusinessTypes");
		String strNodeValue  = "";
		try 
		{
			NodeList oBusinessNodeList = oBusinessTypeNode.getChildNodes ();
			for(int nIndex = 0 ; nIndex <oBusinessNodeList.getLength (); nIndex++)
			{
				Node oBusinessNode = oBusinessNodeList.item (nIndex);
				strNodeValue = getValue (oBusinessNode, "BusinessTypeName");
				if (!strNodeValue.isEmpty ())
					insertBusinessTypeData (strNodeValue);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("processBusinessType - oException : " + oException);
			throw oException;
		}
	}
	
	private static void insertBusinessTypeData (String strNodeValue) throws Exception
	{
		m_oLogger.info ("insertBusinessTypeData");
		m_oLogger.debug ("insertBusinessTypeData - strNodeValue [IN] : " + strNodeValue );
		try
		{
			BusinessTypeDataProcessor oBusinessTypeDataProcessor = new BusinessTypeDataProcessor ();
			BusinessTypeData oBusinessTypeData = new BusinessTypeData ();
			oBusinessTypeData.setM_strBusinessName (strNodeValue);
			oBusinessTypeDataProcessor.create (oBusinessTypeData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("insertBusinessTypeData - oException" + oException);
			throw oException;
		}
	}

	private static int getAdminRoleId () throws Exception
	{
		m_oLogger.info ("getAdminRoleId");
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> (); 
		int nRoleId = -1;
		try
		{
			RoleData oRolaData = new RoleData ();
			oRolaData.setM_strRoleName ("admin");
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			arrGenericData = oRolaData.list (oOrderBy);
			oRolaData = (RoleData) arrGenericData.get (0);
			nRoleId = oRolaData.getM_nRoleId ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getAdminRoleId  - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("getAdminRoleId -  nRoleId [OUT] :  " + nRoleId);
		return nRoleId;
	}
	
	private static String getValue (Node oBusinessNode, String strTagName) throws Exception
	{  
		m_oLogger.info ("getValue");
		m_oLogger.debug ("getValue - oBusinessNode [IN] : " + oBusinessNode);
		m_oLogger.debug ("getValue - strTagName [IN] : " + strTagName);
		String strValue = "";
		try 
		{
			NodeList oNodeList = oBusinessNode.getChildNodes ();
			for (int nIndex = 0; nIndex < oNodeList.getLength (); nIndex++)
		    {
		    	Node oChildNode = oNodeList.item (nIndex);
		    	if (oChildNode.getNodeName ().equalsIgnoreCase (strTagName))
		    	{
		    		strValue = oChildNode.getChildNodes ().item (0).getNodeValue ();
		    		break;
		    	}
		    }
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("getValue - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("getValue - strValue [OUT] : " + strValue);
		return strValue;
	}
}