package com.techmust.generic.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.techmust.generic.data.AppProperties;

public class GenericUtil
{
	public static Logger m_oLogger = Logger.getLogger(GenericUtil.class);
//	private String m_strXmlHeader;

	public GenericUtil ()
	{
//		m_strXmlHeader = getXmlString(createNewXMLDocument());
	}
	public static String getProperty (String strPropertyName)
	{
		m_oLogger.info("getProperty");
		m_oLogger.debug("getProperty - strPropertyName [IN] : " +strPropertyName);
		String strValue = "";
		try
		{
			String strPropertyFile = AppProperties.getProperty("PROJECT_PROPERTY_FILE");
			m_oLogger.debug("getProperty - strPropertyFile : " +strPropertyFile);
			Properties oProperties = new Properties();
			FileInputStream oFileInputStream = new FileInputStream(strPropertyFile);
			oProperties.load(oFileInputStream);
			oFileInputStream.close();
			strValue = oProperties.getProperty(strPropertyName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getProperty - Exception" +oException);
		}
		m_oLogger.debug("getProperty- strValue [OUT] : " +strValue);
		return strValue;
	}

	public static String getProjectProperty (String strPropertyName)
	{
		return AppProperties.getProperty(strPropertyName);
	}
	
	
	private static String getName(String strReplyTo)
    {
		m_oLogger.info("getName");
	    String strName = "";
	    try
	    {
	    	strName = strReplyTo.substring(0, strReplyTo.indexOf("<") - 1);
	    }
	    catch (Exception oException)
	    {
	    	strName = strReplyTo;
	    }
	    m_oLogger.debug("getName - strName [OUT] : " + strName);
	    return strName;
    }

	public static void writeBlobToFile (Blob oBlob, String strFileName, String strFolderName) throws Exception
	{
		m_oLogger.info("createFile");
		m_oLogger.debug("createFile - oBlob [IN] : " +oBlob);
		m_oLogger.debug("createFile - oData [IN] : " +strFileName);
		String strFilePath = getProperty("kTEMPFOLDER_PATH")+strFolderName;
		try
		{
			File oFile = new File(strFilePath);
			FileUtils.forceMkdir(oFile);
			File oProjectFile = new File(strFilePath+File.separator+strFileName);
			Streams.copy(oBlob.getBinaryStream(), new FileOutputStream(oProjectFile), true);
		}
		catch(Exception oException)
		{
			m_oLogger.error("createFile - oException : " +oException);
			throw oException;
		}
	}

	public static void makeZip (String strFilePath) throws Exception
	{
		m_oLogger.info("createzip");
		m_oLogger.debug("createzip - strFilePath [IN] : " +strFilePath);
		File oFile = new File (strFilePath);
		try
		{
			File oZipFile = new File(strFilePath + ".zip");
			ZipOutputStream oZipOutputStream = new ZipOutputStream(new FileOutputStream(oZipFile));
			addFolderContents(oFile, oZipOutputStream);
			oZipOutputStream.close();
		}
		catch(Exception oException)
		{
			m_oLogger.error("createzip - oException : " +oException);
			throw oException;
		}
	}
	
	public static void addFolderContents (File oFilePath, ZipOutputStream oZipFileStream)
	{
		m_oLogger.info("addFolderContents");
		m_oLogger.debug("addFolderContents - oFilePath [IN] : " +oFilePath);
		File [] arrFiles = oFilePath.listFiles();
		for (int nIndex = 0; nIndex < arrFiles.length; nIndex++)
		{
			File oFile = arrFiles [nIndex];
			if ((oFile.exists() && oFile.isFile() && !oFile.getName().equalsIgnoreCase("thumbs.db") ))
			{
				try
				{
					oZipFileStream.putNextEntry(new ZipEntry(oFile.getName()));
					writeZipEntry (oFile, oZipFileStream);
				}
				catch (Exception oFolderException)
				{
					m_oLogger.error("addFolderContents - oFolderException : " + oFolderException);
				}
				finally
				{
					try
					{
						oZipFileStream.closeEntry();
					}
					catch (Exception oZipException)
					{
						m_oLogger.error("addFolderContents - oZipException : " + oZipException);
					}
				}
			}
		}
	}
	
	public static void writeZipEntry (File oFile, ZipOutputStream oZipFileStream) throws Exception
	{
		FileInputStream oFileToWrite = null;
		try
		{
			int nLength = 0;
			byte[] buffer = new byte[1024];
			oFileToWrite = new FileInputStream(oFile);
			while ((nLength = oFileToWrite.read(buffer)) > 0)
				oZipFileStream.write(buffer, 0, nLength);
		}
		catch (Exception oException)
		{
			throw oException;
		}
		finally
		{
			oFileToWrite.close ();
		}
	}
	
	public static String buildHtml (String strXML, String strStyleSheet)
	{
		m_oLogger.info ("buildHtml");
		m_oLogger.debug ("buildHtml - strXML [IN] : " +strXML);
		m_oLogger.debug ("buildHtml - strStyleSheet [IN] : " +strStyleSheet);
	    String strHTML = "";
	    try 
	    {
	        TransformerFactory oFactory = TransformerFactory.newInstance ();
	        Transformer oTransformer = oFactory.newTransformer (new javax.xml.transform.stream.StreamSource (new File (strStyleSheet)));
	        StreamSource oSource = new StreamSource (new ByteArrayInputStream (strXML.getBytes ()));
	        ByteArrayOutputStream oHTML = new ByteArrayOutputStream ();
	        oTransformer.transform ( oSource, new StreamResult ( oHTML ) );
	        strHTML = oHTML.toString ();
	    }
	    catch (Exception oException) 
	    {
	    	m_oLogger.error ("buildHtml - oException : " +oException);
	    }
	    m_oLogger.debug ("buildHtml - strMenuHTML [OUT] : " +strHTML);
	    return strHTML;
	}



	private static void deleteAttachments(String strAttachments) 
	{
		String strAttachFolder = GenericUtil.getProperty("kATTACMENT_FOLDER_PATH");
		String  [] arrAttachments = strAttachments.split(",");
		for(int nIndex=0; nIndex < arrAttachments.length; nIndex++)
		{
			File oAttachFile = new File(strAttachFolder + File.separator + arrAttachments[nIndex]);
			oAttachFile.delete();
		}
	}
	
	public static boolean writeBlobToFile (Blob oBlob, File oFile)
	{ 
		m_oLogger.info("writeBlobToFile");
		m_oLogger.debug("writeBlobToFile - oBlob [IN] : " + oBlob);
		m_oLogger.debug("writeBlobToFile - oFile [IN] : " + oFile);
		boolean bSuccess = false;
		try 
		{
			Streams.copy(oBlob.getBinaryStream(), new FileOutputStream(oFile), true);
			bSuccess = true;
		}
		catch (Exception oException )
		{
			m_oLogger.error("writeBlobToFile - Exception : " + oException);
		}
		return bSuccess;
	}
	
	public Document createNewXMLDocument ()
	{
		Document oXmlDoc = null;
		try
		{
			DocumentBuilderFactory oDocFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocBuilder = oDocFactory.newDocumentBuilder();
			oXmlDoc = oDocBuilder.newDocument();
		}
		catch (Exception oException)
		{
			m_oLogger.error("createNewXMLDocument - oException : " +oException);
		}
		m_oLogger.debug("createNewXMLDocument - bIsUpdated [OUT] : " + oXmlDoc);
		return oXmlDoc;
	}
	
	public Document getXmlDocument (String strXmlContent)
	{
		m_oLogger.info ("getXmlDocument");
		m_oLogger.debug ("getXmlDocument - strXmlpath [IN] : " + strXmlContent);
		Document oXmlDocument = null;
		try
		{
			DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();
			InputStream oInputStream = new ByteArrayInputStream(strXmlContent.getBytes());
			oXmlDocument = oDocumentBuilder.parse(oInputStream);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getXmlDocument - oException" +oException);
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}
	
	public String getXmlString (Document oXmlDocument)
	{
		m_oLogger.info ("getXmlString");
		m_oLogger.debug ("getXmlString - oXmlDocument [IN] : " + oXmlDocument);
		String strXml = "";
		try 
		{
			/*TransformerFactory oTransformerFactory = TransformerFactory.newInstance();
			Transformer oTransformer = oTransformerFactory.newTransformer();
			DOMSource oXmlSource = new DOMSource(oXmlDocument);
			StringWriter oStringWriter = new StringWriter ();
			StreamResult oResult = new StreamResult(oStringWriter);
			oTransformer.transform(oXmlSource, oResult);
			strXml = oStringWriter.toString();
			if (m_strXmlHeader != null && m_strXmlHeader.length() > 0)
				strXml = strXml.replace (m_strXmlHeader, "");*/
			
			Document oHeader = createNewXMLDocument();
			DOMImplementationLS domImplementation = (DOMImplementationLS) oXmlDocument.getImplementation();
			LSSerializer lsSerializer = domImplementation.createLSSerializer();
			strXml = lsSerializer.writeToString(oXmlDocument); 
			String strHeader = lsSerializer.writeToString(oHeader);
			strXml = strXml.replace (strHeader, "");
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getXmlString - oException : " + oException);
		}
		m_oLogger.debug("getXmlString - strXml [OUT] : " + strXml);
		return strXml;
	}
	
	public Element createRootElement (Document oXmlDocument, String strRoot) 
	{
		Element oRootElement = oXmlDocument.createElement(strRoot);
		oXmlDocument.appendChild(oRootElement);
		return oRootElement;
	}
	

	
	public void deleteTempXslt(String strXsltpath) 
	{
		String strDefaultXSLTPath = GenericUtil.getProperty("kXSLT_CRMail");
		File oFile = new File (strXsltpath);
		if (!strXsltpath.equalsIgnoreCase(strDefaultXSLTPath) && oFile.exists())
			oFile.delete();
	}

}