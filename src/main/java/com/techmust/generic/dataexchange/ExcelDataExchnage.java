package com.techmust.generic.dataexchange;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Row;
import org.directwebremoting.io.FileTransfer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.generic.dataexchange.ChildKey;
import com.techmust.generic.dataexchange.DataExchangeResponse;
import com.techmust.generic.dataexchange.XMLDataExchange;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ExcelDataExchnage extends DataExchange<GenericData> 
{
	@Override
	public FileTransfer exportData(ArrayList<?> arrGenericData) throws Exception 
	{
		FileTransfer oXMLFile = null;
		XMLDataExchange oXMLDataExchange = new XMLDataExchange ();
		String strClassName  = "";
		HSSFWorkbook oWorkbook = new HSSFWorkbook();
		int nPos = 0;
		try
		{
			oXMLFile = oXMLDataExchange.exportData(arrGenericData);
			Document oXMLDoc = getXmlDocument(oXMLFile);
			strClassName = arrGenericData.get(0).getClass().getSimpleName();
			NodeList oNodeList = oXMLDoc .getElementsByTagName(strClassName);
			HSSFSheet oWorksheet = oWorkbook.createSheet(strClassName);
			Row oClassRow = oWorksheet.createRow((short)0);
			oWorksheet.addMergedRegion(new CellRangeAddress(0,0,0,50));
			oClassRow.createCell(0).setCellValue(strClassName);
			String strClass = arrGenericData.get(0).getClass().getName();
			Class<?> oClass = Class.forName(strClass);
  	        Object oObj = oClass.newInstance();
  	        Method oGetHeaderKey = oClass.getMethod("getHeaderKeys", null);
	        HashMap oHeaderKeys = (HashMap) oGetHeaderKey.invoke(oObj, null);
	        Method oGetChildKey = oClass.getMethod("getChildKeys", null);
	        ArrayList<ChildKey> arrChildKeys =  (ArrayList<ChildKey>) oGetChildKey.invoke(oObj, null);
	        createHeaderRows (oHeaderKeys, oWorksheet, nPos);
	        createHeaderData (oNodeList, oWorksheet,oHeaderKeys, nPos, arrChildKeys);
		}
		catch (Exception oException)
		{
		    m_oLogger.info("exportData - oException :" + oException);
		}
		InputStream oInputStreamWorkBook = new ByteArrayInputStream(oWorkbook.getBytes());
		FileTransfer oDwrXLSFile = new FileTransfer(strClassName + "_" + +System.currentTimeMillis()+".xls", "application/xml", oInputStreamWorkBook);
		return oDwrXLSFile;
	}

	@SuppressWarnings("unchecked")
	private void createHeaderRows(HashMap oHeaderKeys, HSSFSheet oWorksheet, int nPos) 
	{
		Iterator oHeaderIterator = oHeaderKeys.keySet().iterator();
		setCellValue (oHeaderIterator, oWorksheet, nPos);
	}
	
	@SuppressWarnings("unchecked")
	private void buildChildSubHeader(ChildKey oChildKey, HSSFSheet oWorksheet) 
	{
		HashMap oHashMap = oChildKey.m_oChildKey;
		Iterator oSubHeaderIterator = oHashMap.keySet().iterator();
		if(!oHashMap.isEmpty())
		{
			if (oSubHeaderIterator.hasNext())
				oSubHeaderIterator.next();
			if (oSubHeaderIterator.hasNext())
				oSubHeaderIterator.next();
			setCellValue (oSubHeaderIterator, oWorksheet, 2);
		}
	}
	
	private void setCellValue (Iterator oIterator, HSSFSheet oWorksheet, int nPos )
	{
		int nLastIndex = oWorksheet.getLastRowNum(); 
		Row oRow = oWorksheet.createRow((short)nLastIndex+1);
		while(oIterator.hasNext())
		{
			String strCellValue = (String) oIterator.next();
			oRow.createCell(nPos).setCellValue(strCellValue);
			oWorksheet.autoSizeColumn(nPos);
			nPos++;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void createHeaderData(NodeList oList, HSSFSheet oWorksheet, HashMap oHeaderKeys, int nPos, ArrayList<ChildKey> arrChildKeys) 
	{
		try
		{
			 for (int nIndex = 0; nIndex < oList.getLength(); nIndex++)
	         {
				int nRowValue = oWorksheet.getLastRowNum()+1;
				buildExcelData (oList.item(nIndex), oWorksheet, nPos, oHeaderKeys);
				createChildRows (arrChildKeys,oWorksheet, oList.item(nIndex));
				oWorksheet.groupRow(nRowValue+1, oWorksheet.getLastRowNum());
				oWorksheet.setRowGroupCollapsed(nRowValue+1, true);
	         }
		}
		catch (Exception oException) 
		{
			 m_oLogger.info("createHeaderData - oException :" + oException);
		}
	}
	
	private void createChildRows(ArrayList<ChildKey> arrChildKeys, HSSFSheet oWorksheet, Node oNode) 
	{
		try
		{
			NodeList oNodeList = oNode.getChildNodes();
			for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
			{
				if(oNodeList.item(nIndex).getChildNodes().item(0) != null)
				{
					Node oList = oNodeList.item(nIndex).getChildNodes().item(0);
					if(oList.hasChildNodes())
						buildChildHeader (arrChildKeys, oWorksheet, oNodeList.item(nIndex));
				}
			}
		}
		catch(Exception oException)
		{
			m_oLogger.info("createChildRows - oException :" + oException);
		}
	}

	private void buildChildHeader(ArrayList<ChildKey> arrChildKeys,	HSSFSheet oWorksheet, Node oChildNode) 
	{
		String strNodeName = oChildNode.getNodeName();
		ChildKey oChildKey = getHashMap (arrChildKeys, strNodeName, oWorksheet);
		buildChildSubHeader(oChildKey, oWorksheet);
		buildChildData (oChildKey.m_oChildKey, oWorksheet, oChildNode.getChildNodes(), strNodeName);
	}
	
	@SuppressWarnings("unchecked")
	private void buildChildData(HashMap oHashMap, HSSFSheet oWorksheet, NodeList oNodeList, String strNodeName) 
	{
		try
		{
			for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
			{
				String strTagName = (String) oHashMap.values().toArray()[1];
				Node oNode =  getKeyNode (strTagName, oNodeList.item(nIndex));
				if(oNode != null && oNode.getNodeName().equalsIgnoreCase(strTagName))
					buildExcelData (oNode, oWorksheet, 2, oHashMap);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.info("buildChildData - oException :" + oException);
		}
	}

	private Node getKeyNode(String strTagName, Node oNode) throws Exception 
	{
		Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		oDocument.appendChild(oDocument.importNode(oNode, true));
		NodeList oNodeList = oDocument.getElementsByTagName(strTagName);
		Node oList = oNodeList.item(0);
		return oList;
	}

	@SuppressWarnings("unchecked")
	private ChildKey getHashMap(ArrayList<ChildKey> arrChildKeys, String strTagName, HSSFSheet oWorksheet) 
	{
		ChildKey oChildKey = new ChildKey ();
		for (int nIndex = 0; nIndex < arrChildKeys.size(); nIndex++)
		{
			Iterator oIterator = arrChildKeys.get(nIndex).m_oChildKey.keySet().iterator();
			HashMap oKeySet = (HashMap) arrChildKeys.get(nIndex).m_oChildKey;
			if(oIterator.hasNext())
			{
				String strKeyValue = (String) oIterator.next();
				String strValue = (String) oKeySet.get(strKeyValue);
				if(strValue.trim().equalsIgnoreCase(strTagName))
				{
					oChildKey =  arrChildKeys.get(nIndex);
					Row oHeaderRow = oWorksheet.createRow((short)oWorksheet.getLastRowNum()+1);
					oHeaderRow.createCell(1).setCellValue(strKeyValue);
					oWorksheet.autoSizeColumn(1);
					break;
				}
			}
		}
		return oChildKey;
	}
	
	@SuppressWarnings("unchecked")
	private void buildExcelData(Node oNode, HSSFSheet oWorksheet, int nPos, HashMap oHeaderKeys) 
	{
		try
		{
			NodeList oNodeList = oNode.getChildNodes();
			int nLastIndex = oWorksheet.getLastRowNum(); 
			Row oRow = oWorksheet.createRow((short)nLastIndex+1);
			 for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
	         {
				String strNodeName =  oNodeList.item(nIndex).getNodeName();
				Boolean bIsHavingKeyValue = isHavingKeyValue (oHeaderKeys, strNodeName);
				if(bIsHavingKeyValue)
				 {
					 oRow.createCell(nPos).setCellValue(UserManagementInitializer.getValue(oNode, strNodeName));
					 oWorksheet.autoSizeColumn(nPos);
					 nPos++;
				 }
	         }
		}
		catch (Exception oException)
		{
		    m_oLogger.info("buildExcelData - oException :" + oException);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Boolean isHavingKeyValue(HashMap oHeaderKeys, String strNodeName) 
	{
		Boolean bIsHavingKeyValue = false;
		Iterator oIterator = oHeaderKeys.values().iterator();
		String strValue = "";
		while(oIterator.hasNext())
		{
			strValue = (String) oIterator.next();
			if(strNodeName.trim().equalsIgnoreCase(strValue.trim()))
			{
				bIsHavingKeyValue = true;	
				break;
			}
		}
		return bIsHavingKeyValue;
	}

	@Override
	public FileTransfer exportData(ArrayList<?> arrGenericData, String strClassName, File file) 
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse importData(FileTransfer oFileTransfer, String strClassName, UserInformationData oCredentials)throws Exception 
	{
		m_oLogger.debug("importData :");
		GenericUtil oGenericUtil = new GenericUtil ();
		DataExchangeResponse oResponse = new DataExchangeResponse ();
		Document oXmlDocument = oGenericUtil.createNewXMLDocument();
		XMLDataExchange oXMLDataExchange = new XMLDataExchange ();
		Element oRootElement = oGenericUtil.createRootElement(oXmlDocument, "ExcelImportData");
		try
		{
			Class oClass = Class.forName(strClassName);
			Object oObj = oClass.newInstance();
  	        Method oGetHeaderKey = oClass.getMethod("getHeaderKeys", null);
		    HashMap oHeaderKeys = (HashMap) oGetHeaderKey.invoke(oObj, null);
		    Method oGetChildKey = oClass.getMethod("getChildKeys", null);
	        ArrayList<ChildKey> arrChildKeys =  (ArrayList<ChildKey>) oGetChildKey.invoke(oObj, null);
			HSSFWorkbook oWorkbook = new HSSFWorkbook( new POIFSFileSystem(oFileTransfer.getInputStream()));
	        HSSFSheet oSpreadsheet = oWorkbook.getSheetAt(0);
	        generateXMLFromExcel (oSpreadsheet, oHeaderKeys, arrChildKeys, oRootElement, oXmlDocument, oClass.getSimpleName());
	        FileTransfer oXMLFileTransfer = writeToFile (oGenericUtil.getXmlString(oXmlDocument).getBytes(), oClass.getSimpleName());
	        oResponse = (DataExchangeResponse) oXMLDataExchange.importData(oXMLFileTransfer, strClassName, oCredentials);
		}
		catch (Exception oException)
		{
			m_oLogger.info("importData - oException :" + oException);
		}
		return oResponse;
	}

	@SuppressWarnings("unchecked")
	private void buildXMLDocument(Row oRow, Document oXmlDocument, Element oRootElement, HSSFSheet oSpreadsheet, HashMap oHeaderKeys, int nPos)
	{
		try
		{
			HSSFRow oHeaderRow = oSpreadsheet.getRow(nPos);
			for (int nIndex = 0; nIndex < oRow.getLastCellNum(); nIndex++) 
				addChild (oXmlDocument, oRootElement, getTagName(oHeaderRow, nIndex, oHeaderKeys), getCellValue(oRow, nIndex));
		}
		catch (Exception oException)
		{
			m_oLogger.info("buildXMLDocument - oException :" + oException);
		}
	}

	@SuppressWarnings("unchecked")
	private String getTagName(Row oRow, int nValue, HashMap oHeaderKeys) 
	{
		String strHeaderValue = "";
		strHeaderValue = (oRow.getCell(nValue) == null) ? "" : oRow.getCell(nValue).getStringCellValue();
		Iterator oIterator = oHeaderKeys.keySet().iterator();
		while(oIterator.hasNext())
		{
			String strValue = (String) oIterator.next();
			if(strValue.equals(strHeaderValue))
			{
				strHeaderValue = (String) oHeaderKeys.get(strValue);
				break;
			}
		}
		return strHeaderValue;
	}

	private String getCellValue(Row oRow, int nValue) 
	{
		return (oRow.getCell(nValue) == null) ? "" : oRow.getCell(nValue).getStringCellValue();
	}
	
	private Element addChild (Document oXmlDocument, Element oRootElement, String strTagName, String strTagValue) 
	{
		Element oElement = null;
		try
		{
			oElement = oXmlDocument.createElement(strTagName.trim());
			oElement.appendChild(oXmlDocument.createTextNode(strTagValue != null ? strTagValue : ""));
			oRootElement.appendChild(oElement);
		}
		catch (Exception oException)
		{
			m_oLogger.info("addChild - oException :" + oException);
		}
		return oElement;
	}
	
	@SuppressWarnings("unchecked")
	private void generateXMLFromExcel (HSSFSheet oSpreadsheet, HashMap oHeaderKeys, ArrayList<ChildKey> arrChildKeys, Element oXMLExportRootElement, Document oDocument, String strClassName)
	{
		String strXML = "";
		int nStartIndex = 2;
		int nLastIndex;
		GenericUtil oGenericUtil = new GenericUtil ();
		try
		{
			for(int nIndex = nStartIndex; nIndex <= oSpreadsheet.getLastRowNum(); nIndex++)
			{
				Row oRow = oSpreadsheet.getRow(nIndex);
				String strCell = (oRow.getCell(0) == null) ? "" : oRow.getCell(0).getStringCellValue();
	        	if(!(strCell.equals("")))
	        	{
	        		nLastIndex = getLastIndex(nIndex, oSpreadsheet.getLastRowNum(), oSpreadsheet, 0);
	        		strXML = buildXML (nStartIndex, nLastIndex, oSpreadsheet, oHeaderKeys, arrChildKeys, strClassName);
	        		Document oXMLDoc = oGenericUtil.getXmlDocument (strXML);
					Node oXMLNode = oDocument.importNode (oXMLDoc.getFirstChild (), true);
					oXMLExportRootElement.appendChild (oXMLNode);
	        		nStartIndex = nLastIndex+1;
	        	}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.info("generateXMLFromExcel - oException :" + oException);
		}
	}

	@SuppressWarnings("unchecked")
	private String buildXML(int nStartIndex, int nLastIndex, HSSFSheet oSpreadsheet, HashMap oHeaderKeys, ArrayList<ChildKey> arrChildKeys, String strClassName) 
	{
		String strXML = "";
		GenericUtil oGenericUtil = new GenericUtil ();
		try
		{
			Document oXmlDocument = oGenericUtil.createNewXMLDocument ();
			Element oRootElement = oGenericUtil.createRootElement(oXmlDocument, strClassName);
			Row oRow = oSpreadsheet.getRow(nStartIndex);
			buildXMLDocument (oRow, oXmlDocument, oRootElement, oSpreadsheet, oHeaderKeys, 1);
			buildChildXMLDocument(nStartIndex, nLastIndex, oSpreadsheet, arrChildKeys, oRootElement, oXmlDocument);
			strXML = oGenericUtil.getXmlString (oXmlDocument);
		}
		catch (Exception oException)
		{
			m_oLogger.info("buildXML - oException :" + oException);
		}
		return strXML;
	}

	private void buildChildXMLDocument(int nStartIndex, int nLastIndex, HSSFSheet oSheet, ArrayList<ChildKey> arrChildKeys, Element oRootElement, Document oDocument) 
	{
		try
		{
			for(int nIndex = nStartIndex+1; nIndex <= nLastIndex; nIndex++)
			{
				Row oRow = oSheet.getRow(nIndex);
				String strCell = (oRow.getCell(1) == null) ? "" : oRow.getCell(1).getStringCellValue();	
				if(!(strCell.equals("")))
				{
					int nChildLastIndex = getLastIndex(nIndex, nLastIndex, oSheet, 1);
					ChildKey oChildKey = getChildKey (strCell, arrChildKeys);
					buildChildRow (nIndex, nChildLastIndex, oSheet, oChildKey, oRootElement, oDocument);
					nStartIndex = nChildLastIndex;;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.info("buildChildXMLDocument - oException :" + oException);
		}
	}

	private int getLastIndex(int nChildIndex, int nLastIndex, HSSFSheet oSheet, int nCellPos)
	{
		int nChildLastIndex = nLastIndex;
		try
		{
			for(int nIndex = nChildIndex+1; nIndex <= nLastIndex; nIndex++)
			{
				Row oRow = oSheet.getRow(nIndex);
				String strCell = (oRow.getCell(nCellPos) == null) ? "" : oRow.getCell(nCellPos).getStringCellValue();	
				if(!(strCell.equals("")))
				{
					nChildLastIndex = nIndex-1;
					break;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.info("getLastIndex - oException :" + oException);
		}
		return nChildLastIndex;
	}

	private void buildChildRow(int nStartIndex, int nLastIndex,	HSSFSheet oSpreadsheet, ChildKey oChildKey, Element oElement, Document oDocument) 
	{
		GenericUtil oGenericUtil = new GenericUtil ();
		try
		{
			Document oXmlDocument = oGenericUtil.createNewXMLDocument ();
			String strValue = (String) oChildKey.m_oChildKey.values().iterator().next();
			Element oRootElement = oGenericUtil.createRootElement(oXmlDocument, strValue);
			if(strValue.equalsIgnoreCase("m_oDemography"))
				buildDemographyTag (oXmlDocument, oRootElement, nStartIndex, nLastIndex, oSpreadsheet, oChildKey);
			else
				buildChildXML (oSpreadsheet, nStartIndex, nLastIndex, oRootElement, oChildKey, oXmlDocument);
			Node oChildNode = oDocument.importNode (oXmlDocument.getFirstChild (), true);
			oElement.appendChild (oChildNode);
		}
		catch (Exception oException)
		{
			m_oLogger.info("buildChildRow - oException :" + oException);
		}
	}

	private void buildDemographyTag(Document oDocument, Element oElement, int nStartIndex, int nLastIndex,	HSSFSheet oSpreadsheet,  ChildKey oChildKey) 
	{
		GenericUtil oGenericUtil = new GenericUtil ();
		Document oXmlDocument = oGenericUtil.createNewXMLDocument ();
		Element oRootElement = oGenericUtil.createRootElement(oXmlDocument, "DemographyData");
		buildBusinessTag (oXmlDocument, oRootElement, nStartIndex, nLastIndex, oSpreadsheet, oChildKey);
		Node oChildNode = oDocument.importNode (oXmlDocument.getFirstChild (), true);
		oElement.appendChild (oChildNode);
	}

	private void buildBusinessTag(Document oDocument, Element oElement, int nStartIndex, int nLastIndex, HSSFSheet oSpreadsheet, ChildKey oChildKey) 
	{
		GenericUtil oGenericUtil = new GenericUtil ();
		Document oBusinessDocument = oGenericUtil.createNewXMLDocument ();
		Element oBusinessElement = oGenericUtil.createRootElement(oBusinessDocument, "m_oBusinessType");
		buildChildXML (oSpreadsheet, nStartIndex, nLastIndex, oBusinessElement, oChildKey, oBusinessDocument);
		Node oNode = oDocument.importNode (oBusinessDocument.getFirstChild (), true);
		oElement.appendChild (oNode);
	}

	private void buildChildXML(HSSFSheet oSpreadsheet, int nStartIndex, int nLastIndex, Element oRootElement, ChildKey oChildKey, Document oDocument)
	{
		GenericUtil oGenericUtil = new GenericUtil ();
		try
		{
			String strValue = (String)  oChildKey.m_oChildKey.values().toArray()[1];
			for(int nIndex = nStartIndex+2; nIndex <= nLastIndex; nIndex++)
			{
				Document oXmlDocument = oGenericUtil.createNewXMLDocument ();
				Element oElement = oGenericUtil.createRootElement(oXmlDocument, strValue);
				Row oRow = oSpreadsheet.getRow(nIndex);
				buildXMLDocument (oRow, oXmlDocument, oElement, oSpreadsheet, oChildKey.m_oChildKey, nStartIndex+1);
				Node oChildNode = oDocument.importNode (oXmlDocument.getFirstChild (), true);
				oRootElement.appendChild (oChildNode);
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.info("buildChildXML - oException :" + oException);
		}
	}

	@SuppressWarnings("unchecked")
	private ChildKey getChildKey(String strCell, ArrayList<ChildKey> arrChildKeys)
	{
		ChildKey oChildKey = new ChildKey ();
		try
		{
			for(int nIndex = 0; nIndex < arrChildKeys.size(); nIndex++)
			{
				Iterator oIterator = arrChildKeys.get(nIndex).m_oChildKey.keySet().iterator();
				if(oIterator.hasNext())
				{
					String strKeyValue = (String) oIterator.next();
					if(strKeyValue.trim().equalsIgnoreCase(strCell.trim()))
						oChildKey =  arrChildKeys.get(nIndex);	
				}
			}
		}
		catch (Exception oException)
		{
			 m_oLogger.info("getChildKey - oException ::" + oException);
		}
		return oChildKey;
	}
}
