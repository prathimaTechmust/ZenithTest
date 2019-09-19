package com.techmust.scholarshipmanagement.institution;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "institutions")
public class InstitutionInformationData extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "institutionid")
	private int m_nInstitutionId;
	
	@Column(name = "institutionname")
	private String m_strInstitutionName;
	
	@Column(name = "institutionemail")
	private String m_strInstitutionEmailAddress;
	
	@Column(name="institutionType")
    private String m_strInstitutionType;
	
	@Column(name = "institutionaddress")
	private String m_strInstitutionAddress;
	
	@Column(name = "contactpersonname")
	private String m_strContactPersonName;
	
	@Column(name = "contactpersonemail")
	private String m_strContactPersonEmail;
	
	@Column(name = "phonenumber")
	private String m_strPhoneNumber;
	
	@Column(name = "city")
	private String m_strCity;
	
	@Column(name = "state")
	private String m_strState;
	
	@Column(name = "pincode")
	private int m_nPincode;	
	
	@Column(name = "cheque_favourof")
	private boolean m_bChequeFavouOf;
	
	//Mandatory Columns
	@Column(name = "created_on")
	private Date m_dCreatedOn;
	
	@Column(name = "updated_on")
	private Date m_dUpdatedOn;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private UserInformationData m_oUserCreatedBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updated_by")
	private UserInformationData m_oUserUpdatedBy;
	
	//Mappings
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "m_oInstitutionInformationData")	
	private Set<ChequeInFavourOf> m_oChequeInFavourOf;
	
	//Transient Variables
	@Transient
	public ChequeInFavourOf[] m_arrChequeFavourof;	

	public InstitutionInformationData()
	{
		m_nInstitutionId = -1;
		m_strInstitutionName = "";
		m_strInstitutionEmailAddress = "";
		m_strInstitutionType ="";
		m_strInstitutionAddress = "";
		m_strContactPersonName = "";
		m_strContactPersonEmail = "";
		m_strPhoneNumber = "";
		m_strCity = "";
		m_strState = "";
		m_nPincode = -1;
		m_bChequeFavouOf = false;
		m_oChequeInFavourOf = new HashSet<ChequeInFavourOf>();
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();
		m_oUserCreatedBy = new UserInformationData();
		m_oUserUpdatedBy = new UserInformationData();
	}	
	
	public Date getM_dCreatedOn()
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date m_dCreatedOn)
	{
		this.m_dCreatedOn = m_dCreatedOn;
	}
	
	public Date getM_dUpdatedOn()
	{
		return m_dUpdatedOn;
	}

	public void setM_dUpdatedOn(Date m_dUpdatedOn) 
	{
		this.m_dUpdatedOn = m_dUpdatedOn;
	}

	public UserInformationData getM_oUserCreatedBy()
	{
		return m_oUserCreatedBy;
	}

	public void setM_oUserCreatedBy(UserInformationData m_oUserCreatedBy) 
	{
		this.m_oUserCreatedBy = m_oUserCreatedBy;
	}

	public UserInformationData getM_oUserUpdatedBy()
	{
		return m_oUserUpdatedBy;
	}

	public void setM_oUserUpdatedBy(UserInformationData m_oUserUpdatedBy)
	{
		this.m_oUserUpdatedBy = m_oUserUpdatedBy;
	}

	public ChequeInFavourOf[] getM_arrChequeFavourof()
	{
		return m_arrChequeFavourof;
	}

	public void setM_arrChequeFavourof(ChequeInFavourOf[] m_arrChequeFavourof)
	{
		this.m_arrChequeFavourof = m_arrChequeFavourof;
	}
	
	public boolean isM_bChequeFavouOf()
	{
		return m_bChequeFavouOf;
	}

	public void setM_bChequeFavouOf(boolean m_bChequeFavouOf)
	{
		this.m_bChequeFavouOf = m_bChequeFavouOf;
	}	

	public Set<ChequeInFavourOf> getM_oChequeInFavourOf()
	{
		return m_oChequeInFavourOf;
	}

	public void setM_oChequeInFavourOf(Set<ChequeInFavourOf> m_oChequeInFavourOf)
	{
		this.m_oChequeInFavourOf = m_oChequeInFavourOf;
	}

	public int getM_nInstitutionId()
	{
		return m_nInstitutionId;
	}

	public void setM_nInstitutionId(int m_nInstitutionId)
	{
		this.m_nInstitutionId = m_nInstitutionId;
	}

	public String getM_strInstitutionName()
	{
		return m_strInstitutionName;
	}

	public void setM_strInstitutionName(String m_strInstitutionName)
	{
		this.m_strInstitutionName = m_strInstitutionName;
	}

	public String getM_strInstitutionEmailAddress() 
	{
		return m_strInstitutionEmailAddress;
	}

	public void setM_strInstitutionEmailAddress(String m_strInstitutionEmailAddress)
	{
		this.m_strInstitutionEmailAddress = m_strInstitutionEmailAddress;
	}

	public String getM_strInstitutionAddress() 
	{
		return m_strInstitutionAddress;
	}

	public void setM_strInstitutionAddress(String m_strInstitutionAddress)
	{
		this.m_strInstitutionAddress = m_strInstitutionAddress;
	}

	public String getM_strContactPersonName()
	{
		return m_strContactPersonName;
	}

	public void setM_strContactPersonName(String m_strContactPersonName) 
	{
		this.m_strContactPersonName = m_strContactPersonName;
	}

	public String getM_strContactPersonEmail()
	{
		return m_strContactPersonEmail;
	}

	public void setM_strContactPersonEmail(String m_strContactPersonEmail)
	{
		this.m_strContactPersonEmail = m_strContactPersonEmail;
	}
	
	public String getM_strInstitutionType() 
	{
		return m_strInstitutionType;
	}

	public void setM_strInstitutionType(String m_strInstitutionType)
	{
		this.m_strInstitutionType = m_strInstitutionType;
	}

	public String getM_strPhoneNumber() 
	{
		return m_strPhoneNumber;
	}

	public void setM_strPhoneNumber(String m_strPhoneNumber)
	{
		this.m_strPhoneNumber = m_strPhoneNumber;
	}

	public String getM_strCity()
	{
		return m_strCity;
	}

	public void setM_strCity(String m_strCity)
	{
		this.m_strCity = m_strCity;
	}

	public String getM_strState()
	{
		return m_strState;
	}

	public void setM_strState(String m_strState) 
	{
		this.m_strState = m_strState;
	}

	public int getM_nPincode()
	{
		return m_nPincode;
	}

	public void setM_nPincode(int m_nPincode)
	{
		this.m_nPincode = m_nPincode;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nInstitutionId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nInstitutionId"), m_nInstitutionId));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nInstitutionId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nInstitutionId"), m_nInstitutionId));
		}
		if (!m_strInstitutionName.isEmpty())
		{
			Expression<String> oExpression = oRootObject.get("m_strInstitutionName");
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oExpression,"%"+m_strInstitutionName+"%"));
		}
		if (!m_strPhoneNumber.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strPhoneNumber"), m_strPhoneNumber));
		}		
		if (!m_strCity.isEmpty())
		{
			Expression<String> oExpression = oRootObject.get("m_strCity");
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oExpression,"%"+m_strCity+"%"));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strInstitutionInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "InstitutionInformationData");
			Document oChequeInFavourDocument = getXmlDocument("<m_oChequeInFavourOf>"+buidChequeFavourDetails(m_oChequeInFavourOf)+"</m_oChequeInFavourOf>");
			Node oChequeInFavourOfNode = oXmlDocument.importNode(oChequeInFavourDocument.getFirstChild(), true);
			oRootElement.appendChild(oChequeInFavourOfNode);
			addChild (oXmlDocument, oRootElement, "m_nInstitutionId", m_nInstitutionId);
			addChild (oXmlDocument, oRootElement, "m_strInstitutionName", m_strInstitutionName);
			addChild (oXmlDocument, oRootElement, "m_strInstitutionEmailAddress", m_strInstitutionEmailAddress);			
			addChild (oXmlDocument, oRootElement, "m_strInstitutionAddress", m_strInstitutionAddress);
			addChild (oXmlDocument, oRootElement, "m_strContactPersonName", m_strContactPersonName);
			addChild (oXmlDocument, oRootElement, "m_strContactPersonEmail", m_strContactPersonEmail);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);			
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_strState", m_strState);
			addChild (oXmlDocument, oRootElement, "m_nPincode", m_nPincode);
			addChild(oXmlDocument, oRootElement, "m_strInstitutionType", m_strInstitutionType);
			strInstitutionInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strInstitutionInfoXML;		
	}

	private String buidChequeFavourDetails(Set<ChequeInFavourOf> m_oChequeInFavourOf)
	{
		String strFavourXML = "";
		Object[] chequeArray = m_oChequeInFavourOf.toArray();
		for(int nIndex = 0; nIndex < chequeArray.length; nIndex++)
		{
			ChequeInFavourOf oChequeInFavourOf = (ChequeInFavourOf) chequeArray[nIndex];
			strFavourXML += oChequeInFavourOf.generateXML();
		}
		return strFavourXML;
	}	
}
