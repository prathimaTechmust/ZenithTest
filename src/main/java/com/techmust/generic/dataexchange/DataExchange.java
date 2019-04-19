package com.techmust.generic.dataexchange;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.directwebremoting.io.FileTransfer;
import org.w3c.dom.Document;

import com.techmust.generic.data.IGenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

public abstract class DataExchange <T extends IGenericData> 
{
	public static Logger m_oLogger = Logger.getLogger(GenericIDataProcessor.class);
	public abstract GenericResponse importData (FileTransfer oFiletransfer, String strClassName, UserInformationData oCredentials)throws Exception;
	public abstract FileTransfer  exportData (ArrayList<?> arrGenericData) throws Exception;
	public abstract FileTransfer  exportData (ArrayList<?> arrGenericData, String strClassName, File oXSLFile);

	public static  FileTransfer writeToFile(byte[] oByteStream,String srtClassName)
	{
		m_oLogger.debug("writeToFile");
		m_oLogger.info("writeToFile - oByteStream [IN] : " +oByteStream);
		FileTransfer oDwrXMLFile = null;
		try
		{
			InputStream oInputStream = new ByteArrayInputStream(oByteStream);
	    	oDwrXMLFile = new FileTransfer(srtClassName+ "_"+ System.currentTimeMillis()+".xml", "application/xml", oInputStream);
		}
		catch (Exception oException)
		{
		    m_oLogger.info("writeToFile - oException :" + oException);
		}
    	return oDwrXMLFile;
	}
	
	public Document getXmlDocument (FileTransfer oFiletransfer)
	{
		m_oLogger.info ("getXmlDocument");
		m_oLogger.debug ("getXmlDocument - oFiletransfer [IN] : " + oFiletransfer);
		Document oXmlDocument = null;
		try
		{
			InputStream oInputStream = oFiletransfer.getInputStream();
			DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();
			oXmlDocument = oDocumentBuilder.parse(oInputStream);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getXmlDocument - oException" +oException);
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}
	
}
