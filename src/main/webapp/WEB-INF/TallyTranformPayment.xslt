﻿<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<ENVELOPE>
		 <HEADER>
		  <TALLYREQUEST>Import Data</TALLYREQUEST>
		 </HEADER>
		 <BODY>
		  <IMPORTDATA>
		   <REQUESTDESC>
		    <REPORTNAME>Vouchers</REPORTNAME>
		   </REQUESTDESC>
		   <REQUESTDATA>
		    <TALLYMESSAGE xmlns:UDF="TallyUDF">
		    <xsl:for-each select="ReportPaymentData/PaymentData">
			     <VOUCHER VCHTYPE="Payment" ACTION="Create" OBJVIEW="Accounting Voucher View">
			      <OLDAUDITENTRYIDS.LIST TYPE="Number">
			       <OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>
			      </OLDAUDITENTRYIDS.LIST>
			      <DATE><xsl:value-of select="m_strTallyTransformDate"/></DATE>
			      <GUID></GUID>
			      <NARRATION>Made Payment Rs <xsl:value-of select="m_nAmount" /> by <xsl:value-of select="TransactionMode/m_strModeName" /></NARRATION>
			      <VOUCHERTYPENAME>Payment</VOUCHERTYPENAME>
			      <VOUCHERNUMBER></VOUCHERNUMBER>
			      <PARTYLEDGERNAME><xsl:value-of select="VendorData/m_strVendorCompanyName" /></PARTYLEDGERNAME>
			      <CSTFORMISSUETYPE/>
			      <CSTFORMRECVTYPE/>
			      <FBTPAYMENTTYPE>Default</FBTPAYMENTTYPE>
			      <PERSISTEDVIEW>Accounting Voucher View</PERSISTEDVIEW>
			      <VCHGSTCLASS/>
		          <DIFFACTUALQTY>No</DIFFACTUALQTY>
			      <AUDITED>No</AUDITED>
			      <FORJOBCOSTING>No</FORJOBCOSTING>
			      <ISOPTIONAL>No</ISOPTIONAL>
			      <EFFECTIVEDATE><xsl:value-of select="m_strTallyTransformDate"/></EFFECTIVEDATE>
			      <ISFORJOBWORKIN>No</ISFORJOBWORKIN>
			      <ALLOWCONSUMPTION>No</ALLOWCONSUMPTION>
			      <USEFORINTEREST>No</USEFORINTEREST>
			      <USEFORGAINLOSS>No</USEFORGAINLOSS>
			      <USEFORGODOWNTRANSFER>No</USEFORGODOWNTRANSFER>
			      <USEFORCOMPOUND>No</USEFORCOMPOUND>
			      <ALTERID></ALTERID>
			      <EXCISEOPENING>No</EXCISEOPENING>
			      <USEFORFINALPRODUCTION>No</USEFORFINALPRODUCTION>
			      <ISCANCELLED>No</ISCANCELLED>
			      <HASCASHFLOW>Yes</HASCASHFLOW>
			      <ISPOSTDATED>No</ISPOSTDATED>
			      <USETRACKINGNUMBER>No</USETRACKINGNUMBER>
			      <ISINVOICE>No</ISINVOICE>
			      <MFGJOURNAL>No</MFGJOURNAL>
			      <HASDISCOUNTS>No</HASDISCOUNTS>
			      <ASPAYSLIP>No</ASPAYSLIP>
			      <ISCOSTCENTRE>No</ISCOSTCENTRE>
			      <ISSTXNONREALIZEDVCH>No</ISSTXNONREALIZEDVCH>
			      <ISEXCISEMANUFACTURERON>No</ISEXCISEMANUFACTURERON>
			      <ISBLANKCHEQUE>No</ISBLANKCHEQUE>
			      <ISVOID>No</ISVOID>
			      <ISONHOLD>No</ISONHOLD>
			      <ISDELETED>No</ISDELETED>
			      <ASORIGINAL>No</ASORIGINAL>
			      <VCHISFROMSYNC>No</VCHISFROMSYNC>
			      <MASTERID></MASTERID>
			      <VOUCHERKEY></VOUCHERKEY>
	      		  <OLDAUDITENTRIES.LIST></OLDAUDITENTRIES.LIST>
			      <ACCOUNTAUDITENTRIES.LIST></ACCOUNTAUDITENTRIES.LIST>
			      <AUDITENTRIES.LIST></AUDITENTRIES.LIST>
			      <INVOICEDELNOTES.LIST></INVOICEDELNOTES.LIST>
			      <INVOICEORDERLIST.LIST></INVOICEORDERLIST.LIST>
			      <INVOICEINDENTLIST.LIST></INVOICEINDENTLIST.LIST>
			      <ATTENDANCEENTRIES.LIST></ATTENDANCEENTRIES.LIST>
			      <ORIGINVOICEDETAILS.LIST></ORIGINVOICEDETAILS.LIST>
			      <INVOICEEXPORTLIST.LIST></INVOICEEXPORTLIST.LIST>
			      <ALLLEDGERENTRIES.LIST>
			       <OLDAUDITENTRYIDS.LIST TYPE="Number">
			        <OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>
			       </OLDAUDITENTRYIDS.LIST>
			       <LEDGERNAME><xsl:value-of select="VendorData/m_strVendorCompanyName" /></LEDGERNAME>
			       <GSTCLASS/>
			       <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>
			       <LEDGERFROMITEM>No</LEDGERFROMITEM>
			       <REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
			       <ISPARTYLEDGER>Yes</ISPARTYLEDGER>
			       <ISLASTDEEMEDPOSITIVE>Yes</ISLASTDEEMEDPOSITIVE>
			       <AMOUNT>-<xsl:value-of select="m_nAmount" /></AMOUNT>
			       <BANKALLOCATIONS.LIST></BANKALLOCATIONS.LIST>
			       <xsl:for-each select="m_oPurchasePayementSet/PurchasePaymentData">
				       <BILLALLOCATIONS.LIST>
				        <NAME><xsl:value-of select="PurchaseData/m_strInvoiceNo" /></NAME>
				        <BILLTYPE>Agst Ref</BILLTYPE>
				        <TDSDEDUCTEEISSPECIALRATE></TDSDEDUCTEEISSPECIALRATE>
				        <AMOUNT><xsl:value-of select="m_nAmount" /></AMOUNT>
				        <INTERESTCOLLECTION.LIST></INTERESTCOLLECTION.LIST>
				       </BILLALLOCATIONS.LIST>
			       </xsl:for-each>
			       <INTERESTCOLLECTION.LIST></INTERESTCOLLECTION.LIST>
			       <OLDAUDITENTRIES.LIST></OLDAUDITENTRIES.LIST>
			       <ACCOUNTAUDITENTRIES.LIST></ACCOUNTAUDITENTRIES.LIST>
			       <AUDITENTRIES.LIST></AUDITENTRIES.LIST>
			       <TAXBILLALLOCATIONS.LIST></TAXBILLALLOCATIONS.LIST>
			       <TAXOBJECTALLOCATIONS.LIST></TAXOBJECTALLOCATIONS.LIST>
			       <TDSEXPENSEALLOCATIONS.LIST></TDSEXPENSEALLOCATIONS.LIST>
			       <VATSTATUTORYDETAILS.LIST></VATSTATUTORYDETAILS.LIST>
			       <COSTTRACKALLOCATIONS.LIST></COSTTRACKALLOCATIONS.LIST>
			      </ALLLEDGERENTRIES.LIST>
			      <ALLLEDGERENTRIES.LIST>
			       <OLDAUDITENTRYIDS.LIST TYPE="Number">
			        <OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>
			       </OLDAUDITENTRYIDS.LIST>
			       <LEDGERNAME>Cash</LEDGERNAME>
			       <GSTCLASS/>
			       <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>
			       <LEDGERFROMITEM>No</LEDGERFROMITEM>
			       <REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
			       <ISPARTYLEDGER>Yes</ISPARTYLEDGER>
			       <ISLASTDEEMEDPOSITIVE>No</ISLASTDEEMEDPOSITIVE>
			       <AMOUNT><xsl:value-of select="m_nAmount" /></AMOUNT>
			       <BANKALLOCATIONS.LIST></BANKALLOCATIONS.LIST>
			       <BILLALLOCATIONS.LIST></BILLALLOCATIONS.LIST>
			       <INTERESTCOLLECTION.LIST></INTERESTCOLLECTION.LIST>
			       <OLDAUDITENTRIES.LIST></OLDAUDITENTRIES.LIST>
			       <ACCOUNTAUDITENTRIES.LIST></ACCOUNTAUDITENTRIES.LIST>
			       <AUDITENTRIES.LIST></AUDITENTRIES.LIST>
			       <TAXBILLALLOCATIONS.LIST></TAXBILLALLOCATIONS.LIST>
			       <TAXOBJECTALLOCATIONS.LIST></TAXOBJECTALLOCATIONS.LIST>
			       <TDSEXPENSEALLOCATIONS.LIST></TDSEXPENSEALLOCATIONS.LIST>
			       <VATSTATUTORYDETAILS.LIST></VATSTATUTORYDETAILS.LIST>
			       <COSTTRACKALLOCATIONS.LIST></COSTTRACKALLOCATIONS.LIST>
			      </ALLLEDGERENTRIES.LIST>
			      <PAYROLLMODEOFPAYMENT.LIST></PAYROLLMODEOFPAYMENT.LIST>
			      <ATTDRECORDS.LIST></ATTDRECORDS.LIST>
			     </VOUCHER>
		     </xsl:for-each>
		    </TALLYMESSAGE>
		   </REQUESTDATA>
		  </IMPORTDATA>
		 </BODY>
		</ENVELOPE>
	</xsl:template>
</xsl:stylesheet>
