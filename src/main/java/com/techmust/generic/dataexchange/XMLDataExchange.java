package com.techmust.generic.dataexchange;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.directwebremoting.io.FileTransfer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class XMLDataExchange extends DataExchange<GenericData> 
{

	@Override
	public FileTransfer exportData(ArrayList<?> arrGenericData) throws Exception 
	{
		m_oLogger.debug("exportData");
		String strXML = "";
		String srtClassName = "";
		try
		{
			GenericUtil oGenericUtil = new GenericUtil ();
			Document oXmlDocument = oGenericUtil.createNewXMLDocument();
			Element oRootElement = oGenericUtil.createRootElement(oXmlDocument, "XMLExportData");
			for (int nIndex = 0; nIndex < arrGenericData.size(); nIndex++)
			{
      	        String strClass = arrGenericData.get(nIndex).getClass().getName();
				Class<?> oClass = Class.forName(strClass);
      	        Object oObj = oClass.newInstance();
      	        oObj = arrGenericData.get(nIndex);
      	        srtClassName = oClass.getSimpleName();
      	        Method oGenerateXml = oClass.getMethod("generateXML", null);
      	        strXML = (String) oGenerateXml.invoke(oObj, null);
				Document oXmlDoc = oGenericUtil.getXmlDocument(strXML);
				Node oNode = oXmlDocument.importNode (oXmlDoc.getFirstChild(), true);
				oRootElement.appendChild (oNode);
			}
			strXML = oGenericUtil.getXmlString (oXmlDocument);
		}
		catch (Exception oException)
		{
		    m_oLogger.info("exportData - oException :" + oException);
		}
		
		return writeToFile (strXML.getBytes(), srtClassName);
	}

	@Override
	public FileTransfer exportData(ArrayList<?> arrGenericData, String strClassName, File oFile)
	{
		return null;
	}

	@Override
	public GenericResponse importData(FileTransfer oFiletransfer, String strClassName, UserInformationData oCredentials) throws Exception 
	{
		m_oLogger.debug("importData :");
		DataExchangeResponse oResponse = new DataExchangeResponse ();
		Document oXmlDocument = null;
		GenericUtil oGenericUtil = new GenericUtil ();
		try
		{
			oXmlDocument = getXmlDocument(oFiletransfer);
			Class oClass = Class.forName(strClassName);
  	        Object oObj = oClass.newInstance();
  	        Method oGetInstanceMethod = oClass.getMethod("getInstanceData", String.class, UserInformationData.class);
			NodeList oNodeList	= oXmlDocument.getElementsByTagName (oClass.getSimpleName());
			if (oNodeList == null || (oNodeList != null && oNodeList.getLength() <= 0))
				throw new DOMException (DOMException.NOT_FOUND_ERR, "XML Error : Element ItemData not found in default xml");
			for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
		    {
				GenericData oDataObject = null;
				Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Node oNode = oNodeList.item(nIndex);
				oDocument.appendChild(oDocument.importNode(oNode, true));
				String strXml = oGenericUtil.getXmlString(oDocument);
				try
				{
					oDataObject = (GenericData) oGetInstanceMethod.invoke(oObj, strXml, oCredentials);
				}
				catch (InvocationTargetException oException)
				{
					if(oException.getTargetException().getMessage().contains("Duplicate"))
					{
						oResponse.m_nDuplicateCount++ ;
						continue;
					}
					else 
					{
						if(oException.getTargetException().getMessage()!= "")
						{
							oResponse.m_arrException.add(oException.getTargetException().getMessage());
							continue;
						}
					}
				}
  	        	oResponse.m_bSuccess = oDataObject.saveObject();
  	        	oResponse.m_nInsertedCount++;
		    }
		}
		catch(Exception oException)
		{
		    m_oLogger.info("importData - oException :" + oException);
		    throw oException;
		}
		return  oResponse;
	}
}
