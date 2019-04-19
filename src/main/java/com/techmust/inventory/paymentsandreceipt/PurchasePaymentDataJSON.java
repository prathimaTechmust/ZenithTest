package com.techmust.inventory.paymentsandreceipt;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.vendormanagement.VendorData;

public class PurchasePaymentDataJSON 
{
	private int m_nPurchasePaymentId;
	private PurchaseData m_oPurchaseData;
	private float m_nAmount;
	@JsonBackReference
	private PaymentDataJSON m_oPaymentData;
	private int m_nSerialNumber;	
	
	public PurchasePaymentDataJSON ()
	{
		m_nPurchasePaymentId = -1;
		m_nSerialNumber = -1;
		setM_nAmount(0);
		m_oPurchaseData= new PurchaseData();
		m_oPaymentData= new PaymentDataJSON();
	}
	
	public int getM_nPurchasePaymentId() {
		return m_nPurchasePaymentId;
	}
	public void setM_nPurchasePaymentId(int purchasePaymentId) {
		m_nPurchasePaymentId = purchasePaymentId;
	}
	public void setM_oPaymentData(PaymentDataJSON m_oPaymentData) {
		this.m_oPaymentData = m_oPaymentData;
	}
	public PaymentDataJSON getM_oPaymentData() {
		return m_oPaymentData;
	}
	public int getM_nSerialNumber() {
		return m_nSerialNumber;
	}
	public void setM_nSerialNumber(int serialNumber) {
		m_nSerialNumber = serialNumber;
	}

	public void setM_oPurchaseData(PurchaseData m_oPurchaseData) {
		this.m_oPurchaseData = m_oPurchaseData;
	}

	public PurchaseData getM_oPurchaseData() {
		return m_oPurchaseData;
	}

	public void setM_nAmount(float m_nAmount) {
		this.m_nAmount = m_nAmount;
	}

	public float getM_nAmount() {
		return m_nAmount;
	}

}
