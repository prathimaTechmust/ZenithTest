package com.techmust.usermanagement.userinfo;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.role.RoleData;

@Entity
@Table (name = "users_table")
//@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue("userinfo")
public class UserInformationData extends MasterData implements IUserInformationData
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	protected int m_nUserId;	
	
	@Column(name = "USER_NAME")
	@Size(max = 128)
	protected String m_strUserName;
	
	@Column (name = "DOB")
	protected Date m_dDOB;	
	
	@Column(name = "GENDER")
	protected String m_strGender;	
	
	@Column(name = "USER_PHOTO_FILENAME")
	protected String m_strUserPhotoFileName;
	
	@Column(name = "EMPLOYEE_ID")
	protected String m_strEmployeeId;	
	
	@Column(name = "LOGIN_ID")
	@Size(max = 128)
	protected String m_strLoginId;
	
	@Column(name = "PASSWORD")
	@Size(max = 128)
	protected String m_strPassword;	
	
	@Column(name = "ADDRESS")
	protected String m_strAddress;
	
	@Column(name = "PHONE_NUM")
	protected String m_strPhoneNumber;
	
	@Column(name = "EMAIL_ADDRESS")
	protected String m_strEmailAddress;
	
	@Column(name = "STATUS")
	@Enumerated(EnumType.ORDINAL) 
	protected UserStatus m_nStatus;
	
	@Column(name = "USER_PHOTO")
	@Lob
	protected Blob m_oUserPhoto;	
	
	@Basic
	@Temporal(TemporalType.TIME)
	@Column(name = "CREATION_DATE")
	protected Date m_dCreationDate;
	
	@Basic
	@Temporal(TemporalType.TIME)
	@Column(name = "UPDATION_DATE")
	protected Date m_dUpdationDate;
	
	@Column(name = "UID")
	@ColumnDefault("-1")
	protected long m_nUID;
	
	@ManyToOne
	@JoinColumn(name = "USER_ROLE")
	protected RoleData m_oRole;
	
	@Transient
	protected String m_strNewPassword;
	
	@Transient
	protected String m_strDOB;
	
	@Transient
	protected BufferedImage m_buffImgUserPhoto;
	
	
	
	public UserInformationData () 
	{
		m_strLoginId = "";
		m_nUserId = -1;
		m_strPassword = "";
		m_strNewPassword = "";
		m_strUserName = "";
		m_dDOB = null;
		m_strDOB = "";
		m_strGender = "";
		m_buffImgUserPhoto = null;
		m_oUserPhoto = null;
		m_strEmployeeId = "";
		m_strAddress = "";
		m_strPhoneNumber = "";
		m_strEmailAddress = "";
		m_nStatus = UserStatus.kActive;
		m_strUserPhotoFileName = "";
		m_dCreationDate = Calendar.getInstance ().getTime ();
		m_dUpdationDate = Calendar.getInstance ().getTime ();
		m_nUID = System.currentTimeMillis();
		m_oRole = new RoleData ();
	}
	
	public void setM_strLoginId (String strLoginId) 
	{
		m_strLoginId = strLoginId;
	}

	public String getM_strLoginId () 
	{
		return m_strLoginId;
	}

	public void setM_nUserId (int nUserId) 
	{
		m_nUserId = nUserId;
	}

	public int getM_nUserId () 
	{
		return m_nUserId;
	}

	public void setM_strPassword (String strPassword) 
	{
		m_strPassword = strPassword;
	}

	public  String getM_strPassword () 
	{
		return m_strPassword;
	}

	public void setM_strNewPassword (String strNewPassword) 
	{
		m_strNewPassword = strNewPassword;
	}

	public String getM_strNewPassword () 
	{
		return m_strNewPassword;
	}

	public void setM_strUserName (String strUserName) 
	{
		m_strUserName = strUserName;
	}

	public String getM_strUserName () 
	{
		return m_strUserName;
	}

	public void setM_dDOB (Date dDOB) 
	{
		m_dDOB = dDOB;
	}

	public Date getM_dDOB () 
	{
		return m_dDOB;
	}

	public void setM_strDOB (String strDOB) 
	{
		m_strDOB = strDOB;
	}

	public String getM_strDOB () 
	{
		return m_strDOB;
	}

	public void setM_strGender (String strGender) 
	{
		m_strGender = strGender;
	}

	public String getM_strGender () 
	{
		return m_strGender;
	}

	public void setM_buffImgUserPhoto (BufferedImage buffImgUserPhoto) 
	{
		m_buffImgUserPhoto = buffImgUserPhoto;
	}

	public BufferedImage getM_buffImgUserPhoto () 
	{
		return m_buffImgUserPhoto;
	}

	public void setM_oUserPhoto (Blob oUserPhoto) 
	{
		m_oUserPhoto = oUserPhoto;
	}

	public Blob getM_oUserPhoto () 
	{
		return m_oUserPhoto;
	}

	public void setM_strEmployeeId (String strEmployeeId) 
	{
		m_strEmployeeId = strEmployeeId;
	}

	public String getM_strEmployeeId () 
	{
		return m_strEmployeeId;
	}

	public void setM_strAddress (String strAddress) 
	{
		m_strAddress = strAddress;
	}

	public String getM_strAddress () 
	{
		return m_strAddress;
	}

	public void setM_strPhoneNumber (String strPhoneNumber) 
	{
		m_strPhoneNumber = strPhoneNumber;
	}

	public String getM_strPhoneNumber () 
	{
		return m_strPhoneNumber;
	}

	public void setM_strEmailAddress (String strEmailAddress) 
	{
		m_strEmailAddress = strEmailAddress;
	}

	public String getM_strEmailAddress () 
	{
		return m_strEmailAddress;
	}

	public void setM_nStatus (UserStatus nStatus) 
	{
		m_nStatus = nStatus;
	}

	public UserStatus getM_nStatus () 
	{
		return m_nStatus;
	}
	
	public void setM_strUserPhotoFileName (String strUserPhotoFileName) 
	{
		m_strUserPhotoFileName = strUserPhotoFileName;
	}

	public String getM_strUserPhotoFileName () 
	{
		return m_strUserPhotoFileName;
	}

	public RoleData getm_oRole ()
	{
		return m_oRole;
	}
	
	public void setm_oRole (RoleData oRole)
	{
		m_oRole = oRole;
	}
	
	public void setM_dCreationDate (Date dCreationDate)
	{
		m_dCreationDate = dCreationDate;
	}

	public Date getM_dCreationDate () 
	{
		return m_dCreationDate;
	}

	public void setM_dUpdationDate (Date dUpdationDate) 
	{
		m_dUpdationDate = dUpdationDate;
	}

	public Date getM_dUpdationDate ()
	{
		return m_dUpdationDate;
	}
	
	public void setM_nUID(long m_nUID)
    {
	    this.m_nUID = m_nUID;
    }

	public long getM_nUID()
    {
	    return m_nUID;
    }
	
	public String getActionsAsXML ()
	{
		m_oLogger.info ("getActionsAsXML");
		String strXML = "";
		if(!this.getm_oRole ().getm_oActions ().isEmpty()) //add by swetha if it is empty it ll return empty string
		{
			strXML = "<root>";
			PersistentSet oActionSet = (PersistentSet) (this.getm_oRole ().getm_oActions ());
			strXML += getActionsAsXML (oActionSet);
			strXML += "</root>";
			m_oLogger.debug ( "getActionsAsXML - strXML [OUT] : " + strXML);
		}
		return strXML;
	}
	
	public static String getActionsAsXML (PersistentSet oActionSet)
	{
		String strXML = "";
		Object [] arrActionData = oActionSet.toArray ();
		ArrayList <String> arrActionArea = new ArrayList <String> ();
		for (int nIndex = 0; nIndex < arrActionData.length; nIndex ++)
		{
			ActionData oActionData = (ActionData) arrActionData [nIndex];
			String strActionAreaName = oActionData.getM_oActionArea ().getM_strActionAreaName ();
			int nActionAreaSequence = oActionData.getM_oActionArea().getM_nSequenceNumber();
			if (!isActionAreaBuilt (arrActionArea, strActionAreaName))
			{
				strXML += buildActionareaList (arrActionData, strActionAreaName, nActionAreaSequence);
				arrActionArea.add (strActionAreaName);
			}
		}
		return strXML;
	}

	@Override
    public String generateXML ()
    {	
		m_oLogger.info ("generateXML");
		String strUserInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "UserInformationData");
			addChild (oXmlDocument, oRootElement, "m_strLoginId", m_strLoginId);
			addChild (oXmlDocument, oRootElement, "m_nUserId", m_nUserId);
			addChild (oXmlDocument, oRootElement, "m_strPassword", m_strPassword);
			addChild (oXmlDocument, oRootElement, "m_strNewPassword", m_strNewPassword);
			addChild (oXmlDocument, oRootElement, "m_strUserName", m_strUserName);
			addChild (oXmlDocument, oRootElement, "m_dDOB", m_dDOB != null ? getDate(m_dDOB.toString()) : "");
			addChild (oXmlDocument, oRootElement, "m_strGender", m_strGender);
			Document oRoleXmlDocument = getXmlDocument("<m_oRole>"+m_oRole.generateXML()+"</m_oRole>");
			Node oRoleDataNode = oXmlDocument.importNode (oRoleXmlDocument.getFirstChild(), true);
			oRootElement.appendChild(oRoleDataNode);
			addChild (oXmlDocument, oRootElement, "m_strEmployeeId", m_strEmployeeId);
			addChild (oXmlDocument, oRootElement, "m_strAddress", m_strAddress);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);
			addChild (oXmlDocument, oRootElement, "m_strEmailAddress", m_strEmailAddress);
			addChild (oXmlDocument, oRootElement, "m_nStatus", m_nStatus.toString ());
			addChild (oXmlDocument, oRootElement, "m_strUserPhotoFileName",m_strUserPhotoFileName);
			addChild (oXmlDocument, oRootElement, "m_nUID", m_nUID);
			addChild (oXmlDocument, oRootElement, "m_strUserImageUrl", getUserImageURL(m_nUserId,m_strUserPhotoFileName));
			strUserInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strUserInfoXML [OUT] : " + strUserInfoXML);
	    return strUserInfoXML;
    }
	
	private String getDate(String srtUserDOB)
	{
		return  srtUserDOB.substring(0, 10);			 
	}

	private String getUserImageURL(int nUserId, String strUserPhotoFileName) 
	{	
		String strFileExtension = FilenameUtils.getExtension(strUserPhotoFileName);
		String strUserImageUrl = Constants.USERIMAGEURL + Constants.USERIMAGEFOLDER+ nUserId + "." + strFileExtension;
		return strUserImageUrl;		
	}

	public String generateForgotPasswordXML (String strPassword)
	{
		m_oLogger.info ("generateForgotPasswordXML");
		String strXML = "";
		try
		{
			Document oXMLDoc = createNewXMLDocument();
			Element oRootElement = createRootElement(oXMLDoc, "ForgotPasswordData");
			addChild (oXMLDoc, oRootElement, "m_strUserName", m_strUserName);
			addChild (oXMLDoc, oRootElement, "m_strPassword", strPassword);
			strXML = getXmlString (oXMLDoc);
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateForgotPasswordXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateForgotPasswordXML - strXML [OUT] : " + strXML);
		return strXML;
	}
	
	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
 		if (!m_strUserName.isEmpty ())
 			oCriteria.add (Restrictions.ilike ("m_strUserName", m_strUserName, MatchMode.ANYWHERE));
		if (m_oRole != null && m_oRole.getM_nRoleId () > 0)
			oCriteria.add (Restrictions.eq ("m_oRole", m_oRole));
		if (!m_strLoginId.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strLoginId", m_strLoginId, MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nUserId");
		return oCriteria;
	}
	
	private static String buildActionareaList (Object[] arrActionData, String strActionAreaName, int nActionAreaSequence)
	{
		String strXml = "<ActionArea><ActionAreaName>" + strActionAreaName + "</ActionAreaName><Sequence>" + nActionAreaSequence + "</Sequence><ActionList>";
		for (int nIndex = 0; nIndex < arrActionData.length; nIndex++)
		{
			ActionData oActionData = (ActionData) arrActionData [nIndex];
			if (oActionData.getM_oActionArea ().getM_strActionAreaName ().equalsIgnoreCase (strActionAreaName))
				strXml += buildActionDataXml ((ActionData) arrActionData [nIndex]);
		}
		strXml += "</ActionList></ActionArea>";
		return strXml;
	}

	private static String buildActionDataXml (ActionData oActionData)
	{
		String strActionXml = "<Action>";
		strActionXml += "<ActionName>"+oActionData.getM_strActionName ()+"</ActionName>";
		strActionXml += "<Target>"+oActionData.getM_strActionTarget ()+"</Target>";
		strActionXml += "<Sequence>"+oActionData.getM_nSequenceNumber()+"</Sequence>";
		strActionXml += "</Action>";
		return strActionXml;
	}

	private static boolean isActionAreaBuilt (ArrayList<String> arrActionArea, String strActionAreaName)
	{
		boolean bIsActionAreaBuilt = false;
		for (int nIndex = 0; nIndex < arrActionArea.size () && !bIsActionAreaBuilt; nIndex ++)
			bIsActionAreaBuilt = strActionAreaName.equals (arrActionArea.get (nIndex));
		return bIsActionAreaBuilt;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria (CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(!m_strUserName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct,oCriteriaBuilder.equal(oRootObject.get("m_strUserName"), m_strUserName));
		}
		if (!m_strUserName.isEmpty ())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strUserName")), m_strUserName.toLowerCase())); 
		}
		if (m_oRole != null && m_oRole.getM_nRoleId () > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strRoleName"), this.m_oRole.getM_strRoleName()));
		}
		if (!m_strLoginId.isEmpty ())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strLoginId"), m_strLoginId));
		}
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(!m_strUserName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct,oCriteriaBuilder.equal(oRootObject.get("m_strUserName"), m_strUserName));
		}
		if (!m_strPassword.isEmpty ())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strPassword")), m_strPassword.toLowerCase())); 
		}
		if (!m_strEmailAddress.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strEmailAddress"), m_strEmailAddress));
		}
		if (!m_strLoginId.isEmpty ())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oRootObject.get("m_strLoginId"), m_strLoginId));
		}
		if (getM_nUserId () > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nUserId"), m_nUserId));
		}
		return oConjunct;
	}
}