package com.techmust.inventory.paymentsandreceipt;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonManagedReference;

import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDataJSON
{
	private int m_nPaymentId;
	private VendorData m_oVendorData;
	@JsonManagedReference
	private Set<PurchasePaymentDataJSON> m_oPurchases;
	public PurchasePaymentDataJSON [] m_arrPurchases;
	
	private float m_nAmount;
	private TransactionMode m_oMode;
	private String m_strDetails;
	private String m_strPaymentNumber;
    private Date m_dCreatedOn;
    private UserInformationData m_oCreatedBy;
    
    private String m_strDate;
    private UserInformationData m_oUserCredentialsData;
    private String m_strFromDate = "";
    private String m_strToDate = "";	
    private String m_strTallyTransformDate;
    
    public PaymentDataJSON ()
    {
    	m_nAmount = 0;
    	m_nPaymentId = -1;
    	m_strDetails = "";	
    	m_strPaymentNumber = "";	
    	m_strDate = "";	
    	m_strFromDate = "";	
    	m_strToDate = "";	
    	m_strTallyTransformDate = "";	
    	m_oVendorData= new VendorData();
    	m_oMode= new TransactionMode();
    	m_oUserCredentialsData= new UserInformationData();
    	m_oCreatedBy= new UserInformationData();
    }

	public int getM_nPaymentId() {
		return m_nPaymentId;
	}

	public void setM_nPaymentId(int paymentId) {
		m_nPaymentId = paymentId;
	}

	public VendorData getM_oVendorData() {
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData vendorData) {
		m_oVendorData = vendorData;
	}

	public float getM_nAmount() {
		return m_nAmount;
	}

	public void setM_nAmount(float amount) {
		m_nAmount = amount;
	}

	public TransactionMode getM_oMode() {
		return m_oMode;
	}

	public void setM_oMode(TransactionMode mode) {
		m_oMode = mode;
	}

	public String getM_strDetails() {
		return m_strDetails;
	}

	public void setM_strDetails(String details) {
		m_strDetails = details;
	}

	public String getM_strPaymentNumber() {
		return m_strPaymentNumber;
	}

	public void setM_strPaymentNumber(String paymentNumber) {
		m_strPaymentNumber = paymentNumber;
	}

	public Date getM_dCreatedOn() {
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date createdOn) {
		m_dCreatedOn = createdOn;
	}

	public UserInformationData getM_oCreatedBy() {
		return m_oCreatedBy;
	}

	public void setM_oCreatedBy(UserInformationData createdBy) {
		m_oCreatedBy = createdBy;
	}

//	public PurchasePaymentData[] getM_arrPurchases() {
//		return m_arrPurchases;
//	}
//
//	public void setM_arrPurchases(PurchasePaymentData[] purchases) {
//		m_arrPurchases = purchases;
//	}

	public String getM_strDate() {
		return m_strDate;
	}

	public void setM_strDate(String date) {
		m_strDate = date;
	}

	public UserInformationData getM_oUserCredentialsData() {
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData userCredentialsData) {
		m_oUserCredentialsData = userCredentialsData;
	}

	public String getM_strFromDate() {
		return m_strFromDate;
	}

	public void setM_strFromDate(String fromDate) {
		m_strFromDate = fromDate;
	}

	public String getM_strToDate() {
		return m_strToDate;
	}

	public void setM_strToDate(String toDate) {
		m_strToDate = toDate;
	}

	public String getM_strTallyTransformDate() {
		return m_strTallyTransformDate;
	}

	public void setM_strTallyTransformDate(String tallyTransformDate) {
		m_strTallyTransformDate = tallyTransformDate;
	}

	public Set<PurchasePaymentDataJSON> getM_oPurchases() {
		return m_oPurchases;
	}

	public void setM_oPurchases(Set<PurchasePaymentDataJSON> purchases) {
		m_oPurchases = purchases;
	}

//	public Set<PurchasePaymentDataJSON> getM_oPurchases() {
//		return m_oPurchases;
//	}
//
//	public void setM_oPurchases(Set<PurchasePaymentDataJSON> purchases) {
//		m_oPurchases = purchases;
//	}
}
