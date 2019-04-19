package com.techmust.usermanagement.initializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.constants.Constants;
import com.techmust.generic.data.AppProperties;
import com.techmust.generic.data.GenericData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.action.ActionDataProcessor;
import com.techmust.usermanagement.action.ActionResponse;
import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.actionarea.ActionAreaDataProcessor;
import com.techmust.usermanagement.actionarea.ActionAreaResponse;
import com.techmust.usermanagement.actionmanager.ActionManagerDataProcessor;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.role.RoleDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.usermanagement.userinfo.UserInformationDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationResponse;

public class UserManagementInitializer
{
	private static final String kDefaultXmlPath = "com/techmust/usermanagement/resources/UserManagement.defaults.xml";
	public static Logger m_oLogger = Logger.getLogger(UserManagementInitializer.class);
	
	public static void initialize () throws Exception
	{
		m_oLogger.info ("initialize");
		int nRoleId = -1;
		Document oXMLDocument = null;
		try 
		{
			String strUserManagementDefaultPath = AppProperties.getProperty ("UserManagement.defaults");
			InputStream oDefaultXmlStream = UserManagementInitializer.class.getClassLoader().getResourceAsStream (kDefaultXmlPath);
			oXMLDocument = getXmlDocument (strUserManagementDefaultPath, oDefaultXmlStream);
			ArrayList<ActionData> oActions = insertActions (oXMLDocument);
			ActionManagerDataProcessor oActionManagerDataProcessor = new ActionManagerDataProcessor ();
			if(oActionManagerDataProcessor.isUserTableEmpty ())
			{
				nRoleId = insertRoleActions (oActions);
				insertAdminUser (oXMLDocument, nRoleId);
				insertRole (Constants.kUserRole);
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("initialize - oException : " + oException);
			throw oException;
		}
	}
	
	private static void insertRole (String strRoleName) 
	{
		m_oLogger.info("insertRole [IN] :" + strRoleName);
		try
		{
			RoleData oRoleData = new RoleData ();
			oRoleData.setM_strRoleName(strRoleName);
			oRoleData.saveObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("insertRole - oException  : " + oException);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void insertAdminUser (Document oDocument, int nRoleId) throws Exception
	{
		m_oLogger.info ("insertAdminUser");
		m_oLogger.debug ("insertAdminUser - oDocument [IN]" + oDocument);
		m_oLogger.debug ("insertAdminUser - nRoleId [IN]" + nRoleId);
		UserInformationData oUserInformationData = new UserInformationData ();
		UserInformationDataProcessor oDataProcessor = new UserInformationDataProcessor ();
		RoleData oRoleData = new RoleData ();
		try 
		{
			oRoleData.setM_nRoleId (nRoleId);
			oUserInformationData.setm_oRole (oRoleData);
			setUserInformationData (oUserInformationData, oDocument);
			UserInformationResponse oResponse = oDataProcessor.create (oUserInformationData);
			if (!oResponse.m_bSuccess)
				throw new SQLException ("DataBase transaction error: User Insertion Failed !") ;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("insertAdminUser - oException : " + oException);
			throw oException;
		}
	}
	
	public static int insertRoleActions (ArrayList<ActionData> oActions) throws Exception
	{
		m_oLogger.info ("insertRoleActions");
		m_oLogger.debug ("insertRoleActions - oActions [IN] : " +oActions);
		int nRoleId = -1;
		
		try 
		{
			RoleData oRoleData =  new RoleData ();
			oRoleData.m_arrActionData = (ActionData[]) oActions.toArray (new ActionData[oActions.size()]);
			RoleDataProcessor oRoleDataProcessor = new RoleDataProcessor ();
			oRoleData.setM_strRoleName (Constants.kAdmin);
			oRoleDataProcessor.create (oRoleData);
			nRoleId = oRoleData.getM_nRoleId ();
			if (nRoleId <= 0) 
				throw new SQLException ("DataBase transaction error: Role Insertion Failed !") ;
		} 
		catch (SQLException oSQLException)
		{
			throw oSQLException;
		}
		catch (Exception oException)
		{
			m_oLogger.error( "insertRoleActions - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("insertRoleActions - nRoleId [OUT] : " +nRoleId);
		return nRoleId;
	}
	
	public static ArrayList<ActionData> insertActions (Document oDocument) throws Exception
	{
		m_oLogger.info ("insertActions");
		m_oLogger.debug ("insertActions - oDocument [IN] : " +oDocument);
		ArrayList<ActionData> arrActions = new ArrayList<ActionData> ();
		try 
		{
			NodeList oNodeList	= oDocument.getElementsByTagName ("ActionArea");
			if(oNodeList != null)
			{
			    for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
			    {
			    	Node oNode = oNodeList.item(nIndex);
			    	String strActionAreaName = oNode.getAttributes().getNamedItem ("name").getNodeValue ();
			    	int nActionAreaSequenceId = getNumber(oNode.getAttributes().getNamedItem("SequenceId").getNodeValue());
			    	int nActionAreaId = insertActionArea (strActionAreaName, nActionAreaSequenceId);
			    	processActions (arrActions, nActionAreaId, oNode);
			    }
			}
		} 
		catch (DOMException oDOMException)
		{
			throw oDOMException;
		}
		catch (NullPointerException oNullPointerException)
		{
			throw new DOMException (DOMException.NOT_FOUND_ERR , "XML error - The attribute \"name\" not found in element \"ActionArea\"");
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("insertActions - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("insertActions - arrActions [OUT] : " +arrActions);
		return arrActions;
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
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}
	
	private static void processActions (ArrayList<ActionData> arrActions, int nActionAreaId, Node oActionAreaNode) throws Exception 
	{
		m_oLogger.info ("processActions");
		m_oLogger.debug ("processActions - oActions [IN] : " +arrActions );
	    try 
	    {
	    	NodeList oNodeList	= oActionAreaNode.getChildNodes ();
			if (oNodeList == null || (oNodeList != null && oNodeList.getLength() <= 0))
				throw new DOMException (DOMException.NOT_FOUND_ERR, "XML Error : Element Actions not found in default xml");
		    for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
		    {
		    	Node oActionsNode = oNodeList.item(nIndex);
		    	if (oActionsNode.getNodeName().equalsIgnoreCase("Actions"))
		    		processAction (arrActions, nActionAreaId, oActionsNode);
		    }
		} 
		catch (DOMException oDOMException)
		{
			throw oDOMException;
		}
	    catch (Exception oException) 
		{
	    	m_oLogger.error ("processActions - oException" +oException);
	    	throw oException;
		}
	}

	private static  void processAction (ArrayList<ActionData> arrActions, int nActionAreaId, Node oActionsNode) throws Exception
	{
		m_oLogger.info ("processAction");
		m_oLogger.debug ("processAction - arrActions [IN] : " +arrActions );
		m_oLogger.debug ("processAction - nActionAreaId [IN] : " +nActionAreaId );
		m_oLogger.debug ("processAction - oActionsNode [IN] : " +oActionsNode );
		try 
		{
			NodeList oNodeList	= oActionsNode.getChildNodes ();
			if (oNodeList == null || (oNodeList != null && oNodeList.getLength() <= 0))
				throw new DOMException (DOMException.NOT_FOUND_ERR, "XML Error : Element Action not found in default xml");
			for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
		    {
		    	Node oActionNode = oNodeList.item (nIndex);
		    	if (oActionNode.getNodeName ().equalsIgnoreCase ("Action"))
		    		insertAction (arrActions, nActionAreaId, oActionNode);
		    }
		} 
		catch (DOMException oDOMException)
		{
			throw oDOMException;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("processAction - oException : " +oException);
			throw oException;
		}
	}

	private static void insertAction (ArrayList<ActionData> arrActions, int nActionAreaId, Node oActionNode) throws Exception
	{
		m_oLogger.info ("insertAction");
		m_oLogger.debug ("insertAction - oActions [IN] : " +arrActions);
		m_oLogger.debug ("insertAction - nActionAreaId [IN] : " +nActionAreaId);
		m_oLogger.debug ("insertAction - oActionNode [IN] : " +oActionNode);
		try
		{
			ActionData oData = new ActionData ();
			oData.setM_strActionTarget(getValue (oActionNode, "Target"));
			
			ActionDataProcessor oActionDataProcessor = new ActionDataProcessor ();
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<GenericData> arrActionResponse = oData.list(oOrderBy);
			if(arrActionResponse.size() == 0) 
			{
				oData.setM_strActionName(getValue (oActionNode, "ActionName"));
				oData.setM_nSequenceNumber(getValue (oActionNode, "SequenceId").isEmpty() ? -1 : getNumber (getValue (oActionNode, "SequenceId")));
				ActionAreaData oActionAreaData = new ActionAreaData ();
				oActionAreaData.setM_nActionAreaId (nActionAreaId);
				oData.setM_oActionArea (oActionAreaData);
				ActionResponse oResponse = oActionDataProcessor.create (oData);
				arrActions.add (oResponse.m_arrActionData.get (0));
			}
		}
		catch (IndexOutOfBoundsException oIndexOutOfBoundsException)
		{
			throw new SQLException ("DataBase transaction error: Action Insertion Failed !") ;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("insertAction - oException : " +oException);
			throw oException;
		}
	}

	private static int getNumber (String strValue)
	{
		m_oLogger.info ("getNumber");
		m_oLogger.debug ("getNumber - strValue [IN] : " +strValue);
		int nValue = -1;
		try 
		{
			nValue = Integer.parseInt(strValue);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getNumber - oException : " +oException);
		}
		m_oLogger.debug ("getNumber - nValue [OUT] : " +nValue);
		return nValue;
	}

	@SuppressWarnings("unchecked")
	private static int insertActionArea (String strActionAreaName, int nActionAreaSequenceId) throws Exception
	{
		m_oLogger.info ("insertActionArea");
		m_oLogger.debug ("insertActionArea - strActionAreaName [IN] : " +strActionAreaName);
		m_oLogger.debug ("insertActionArea - nActionAreaSequenceId [IN] : " +nActionAreaSequenceId);
		ActionAreaData oActionAreaData = new ActionAreaData ();
		int nActionAreaId = 0;
		try 
		{
			oActionAreaData.setM_strActionAreaName (strActionAreaName);
			oActionAreaData.setM_nSequenceNumber (nActionAreaSequenceId);
			ActionAreaDataProcessor oActionAreaDataProcessor = new ActionAreaDataProcessor ();
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<ActionAreaData> arrActionAreaResponse = new ArrayList(oActionAreaData.list(oOrderBy));
			if(arrActionAreaResponse.size() == 0) 
			{
				ActionAreaResponse oResponse = oActionAreaDataProcessor.create (oActionAreaData);
				nActionAreaId = oResponse.m_arrActionArea.get(0).getM_nActionAreaId ();
			}
			else
				nActionAreaId = arrActionAreaResponse.get(0).getM_nActionAreaId();
		}
		catch (IndexOutOfBoundsException oIndexOutOfBoundsException)
		{
			throw new SQLException ("DataBase transaction error: ActionArea Insertion Failed !") ;
		}
		catch (Exception oException)
		{
			m_oLogger.error("insertActionArea - oException" +oException);
			throw oException;
		}
		m_oLogger.debug ("insertActionArea - nActionAreaId [OUT] : " +nActionAreaId);
		return nActionAreaId;
	}
	
	public static String getValue (Node oActionNode, String strTagName) throws Exception
	{  
		m_oLogger.info ("getValue");
		m_oLogger.debug ("getValue - oActionNode [IN] : " +oActionNode);
		m_oLogger.debug ("getValue - strTagName [IN] : " +strTagName);
		String strValue = "";
		try 
		{
			NodeList oNodeList = oActionNode.getChildNodes();
			for (int nIndex = 0; nIndex < oNodeList.getLength (); nIndex++)
		    {
		    	Node oChildNode = oNodeList.item(nIndex);
		    	if (oChildNode.getNodeName().equalsIgnoreCase (strTagName))
		    	{
		    		strValue = oChildNode.getChildNodes().item(0) != null ? oChildNode.getChildNodes().item(0).getNodeValue() : "";
		    		break;
		    	}
		    }
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("getValue - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getValue - strValue [OUT] : " +strValue);
		return strValue;
	}
	
	@SuppressWarnings("static-access")
	private static void setUserInformationData (UserInformationData oUserInformationData, Document oDocument) throws Exception
	{
		m_oLogger.info ("setUserInformationData");
		m_oLogger.debug ("setUserInformationData - oUserInformationData [IN] : " +oUserInformationData );
		m_oLogger.debug ("setUserInformationData - oDocument [IN] : " +oDocument);
		try 
		{
			UserInformationDataProcessor<UserInformationData> oUserInformationDataProcessor = new UserInformationDataProcessor<UserInformationData>();
			oUserInformationData.setM_strLoginId(getValueByTagName (oDocument, "LoginId"));
			oUserInformationData.setM_strPassword(oUserInformationDataProcessor.encryptPassword (getValueByTagName (oDocument, "Password")));
			oUserInformationData.setM_strUserName(getValueByTagName (oDocument, "UserName"));
			oUserInformationData.setM_strEmailAddress(getValueByTagName (oDocument, "Email"));
			oUserInformationData.setM_strAddress(getValueByTagName (oDocument, "Address"));
			oUserInformationData.setM_strPhoneNumber(getValueByTagName (oDocument, "PhoneNumber"));
			oUserInformationData.setM_strEmployeeId(getValueByTagName (oDocument, "EmployeeID"));
			oUserInformationData.setM_strGender(getValueByTagName (oDocument, "Gender"));
			oUserInformationData.setM_strDOB(getValueByTagName (oDocument, "DateofBirth"));
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("setUserInformationData - oException" +oException);
			throw oException;
		}
	}
	
	private  static String getValueByTagName (Document oDocument, String strTagName) throws Exception
	{
		m_oLogger.info ("getValueByTagName");
		m_oLogger.debug ("getValueByTagName - oDocument [IN] : " +oDocument);
		m_oLogger.debug ("getValueByTagName - strTagName [IN] : " +strTagName);
		String strNodeValue = "";
		try
		{
			NodeList oNodeList = oDocument.getElementsByTagName (strTagName);
			Element oElement = (Element)oNodeList.item(0);
			NodeList oNodeListElement = oElement.getChildNodes ();
			strNodeValue = oNodeListElement.item(0).getNodeValue ();
		} 
		catch (NullPointerException oNullPointerException)
		{
			throw new DOMException (DOMException.NOT_FOUND_ERR, "XML error: missing element "+strTagName+" in default xml");
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getValueByTagName - oException" +oException);
			throw oException;
		}
		m_oLogger.debug("getValueByTagName - strNodeValue [OUT] : " +strNodeValue);
		return strNodeValue;
	}

}