<?xml version="1.0"?>

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
					<TALLYMESSAGE xmlns:UDF="TallyUDF">
						<xsl:for-each select="ReportPurchaseData/PurchaseData">
							<VOUCHER VCHTYPE="Purchase" ACTION="Create" OBJVIEW="Accounting Voucher View">
							
								<xsl:variable name="strInvoiceNumber" select="m_strInvoiceNo" />
								
								<ADDRESS.LIST TYPE="String">
       								<ADDRESS><xsl:value-of select="VendorData/m_strVendorAddress" /></ADDRESS>
       								<ADDRESS><xsl:value-of select="VendorData/m_strVendorCity" /></ADDRESS>
       								<ADDRESS><xsl:value-of select="VendorData/m_strVendorPinCode" /></ADDRESS>
      							</ADDRESS.LIST>
						        <OLDAUDITENTRYIDS.LIST TYPE="Number">
						       		<OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>
						        </OLDAUDITENTRYIDS.LIST>
      							<DATE><xsl:value-of select="m_strTallyTransformDate" /></DATE>
      							<GUID></GUID>
      							<NARRATION>Purchase Invoice No. <xsl:value-of select="$strInvoiceNumber" /> dated <xsl:value-of select="m_strDate" /></NARRATION>
      							<VOUCHERTYPENAME>Purchase</VOUCHERTYPENAME>
      							<VOUCHERNUMBER><xsl:value-of select="$strInvoiceNumber" /></VOUCHERNUMBER>
      							<REFERENCE><xsl:value-of select="$strInvoiceNumber" /></REFERENCE>
      							<PARTYLEDGERNAME><xsl:value-of select="m_strFrom" /></PARTYLEDGERNAME>
      							<BASICBASEPARTYNAME><xsl:value-of select="m_strFrom" /></BASICBASEPARTYNAME>
      							<CSTFORMISSUETYPE/>
      							<CSTFORMRECVTYPE/>
      							<BUYERSCSTNUMBER></BUYERSCSTNUMBER>
      							<FBTPAYMENTTYPE>Default</FBTPAYMENTTYPE>
     							<PERSISTEDVIEW>Accounting Voucher View</PERSISTEDVIEW>
      							<VCHGSTCLASS/>
      							<DIFFACTUALQTY>No</DIFFACTUALQTY>
      							<AUDITED>No</AUDITED>
      							<FORJOBCOSTING>No</FORJOBCOSTING>
      							<ISOPTIONAL>No</ISOPTIONAL>
      							<EFFECTIVEDATE><xsl:value-of select="m_strTallyTransformDate" /></EFFECTIVEDATE>
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
     							<HASCASHFLOW>No</HASCASHFLOW>
      							<ISPOSTDATED>No</ISPOSTDATED>
      							<USETRACKINGNUMBER>No</USETRACKINGNUMBER>
      							<ISINVOICE>No</ISINVOICE>
      							<MFGJOURNAL>No</MFGJOURNAL>
      							<HASDISCOUNTS>No</HASDISCOUNTS>
      							<ASPAYSLIP>No</ASPAYSLIP>
      							<ISCOSTCENTRE>No</ISCOSTCENTRE>
      							<ISSTXNONREALIZEDVCH>No</ISSTXNONREALIZEDVCH>
      							<ISEXCISEMANUFACTURERON>Yes</ISEXCISEMANUFACTURERON>
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
								<INVOICEEXPORTLIST.LIST> </INVOICEEXPORTLIST.LIST>
								
      							<ALLLEDGERENTRIES.LIST>
							       <OLDAUDITENTRYIDS.LIST TYPE="Number">
							        <OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>
							       </OLDAUDITENTRYIDS.LIST>
       							   <LEDGERNAME><xsl:value-of select="m_strFrom" /></LEDGERNAME>
       							   <GSTCLASS/>
                                   <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>
       							   <LEDGERFROMITEM>No</LEDGERFROMITEM>
       							   <REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
       							   <ISPARTYLEDGER>Yes</ISPARTYLEDGER>
       							   <ISLASTDEEMEDPOSITIVE>No</ISLASTDEEMEDPOSITIVE>
       							   <AMOUNT><xsl:value-of select="round(m_nTotalAmount)" /></AMOUNT>
       							   <BANKALLOCATIONS.LIST></BANKALLOCATIONS.LIST>
       							   <BILLALLOCATIONS.LIST>
								        <NAME><xsl:value-of select="$strInvoiceNumber" /></NAME>
								        <BILLTYPE>New Ref</BILLTYPE>
								        <TDSDEDUCTEEISSPECIALRATE>No</TDSDEDUCTEEISSPECIALRATE>
								        <AMOUNT><xsl:value-of select="round(m_nTotalAmount)" /></AMOUNT>
								        <INTERESTCOLLECTION.LIST></INTERESTCOLLECTION.LIST>
							       </BILLALLOCATIONS.LIST>
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
      							
						        <xsl:for-each select="m_oInputTaxes/Tax">
									<ALLLEDGERENTRIES.LIST>
										<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>
										<LEDGERFROMITEM>No</LEDGERFROMITEM>
										<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
										<ROUNDLIMIT>0</ROUNDLIMIT>
										<TAXCLASSIFICATIONNAME><xsl:value-of select="m_strInputTaxClassificationName" /></TAXCLASSIFICATIONNAME>
										<LEDGERNAME>
											<xsl:value-of select="m_strPurchasesAmountLedgerName" />
										</LEDGERNAME>
										<ISPARTYLEDGER>YES</ISPARTYLEDGER>
										<AMOUNT>-<xsl:value-of select="format-number((m_nTaxAmount div m_nTaxPercent) * 100,'##,##,##,###.00')" /></AMOUNT>
										<BILLALLOCATIONS.LIST>
											<BILLTYPE>New Ref</BILLTYPE>
											<NAME>
												<xsl:value-of select="$strInvoiceNumber" />
											</NAME>
											<BILLDATE></BILLDATE>
											<BILLCREDITPERIOD>90 Days</BILLCREDITPERIOD>
											<AMOUNT><xsl:value-of select="format-number((m_nTaxAmount div m_nTaxPercent) * 100,'##,##,##,###.00')" /></AMOUNT>
										</BILLALLOCATIONS.LIST>
									</ALLLEDGERENTRIES.LIST>
								</xsl:for-each>
								
								<xsl:for-each select="m_oInputTaxes/Tax">
									<ALLLEDGERENTRIES.LIST>
										<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>
										<LEDGERFROMITEM>No</LEDGERFROMITEM>
										<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
										<ROUNDLIMIT>0</ROUNDLIMIT>
										<TAXCLASSIFICATIONNAME>
											<xsl:value-of select="m_strInputTaxClassificationName" />
										</TAXCLASSIFICATIONNAME>
										<LEDGERNAME>
											<xsl:value-of select="m_strInputTaxAmountLedgerName" />
										</LEDGERNAME>
										<ISPARTYLEDGER>YES</ISPARTYLEDGER>
										<AMOUNT>-<xsl:value-of select="format-number(m_nTaxAmount ,'##,##,##,###.00')" /></AMOUNT>
										<VATASSESSABLEVALUE>
											<xsl:value-of select="format-number((m_nTaxAmount div m_nTaxPercent) * 100,'##,##,##,###.00')" />
										</VATASSESSABLEVALUE>
										<BILLALLOCATIONS.LIST>
											<BILLTYPE>New Ref</BILLTYPE>
											<NAME>
												<xsl:value-of select="$strInvoiceNumber" />
											</NAME>
											<BILLDATE></BILLDATE>
											<BILLCREDITPERIOD>90 Days</BILLCREDITPERIOD>
											<AMOUNT>
												<xsl:value-of select="format-number(m_nTaxAmount ,'##,##,##,###.00')" />
											</AMOUNT>
										</BILLALLOCATIONS.LIST>
									</ALLLEDGERENTRIES.LIST>
								</xsl:for-each>
						        
						        <ALLLEDGERENTRIES.LIST>
									<xsl:variable name="nRoundOff" select="format-number((m_nTotalAmount - round(m_nTotalAmount)),'##,##,##,##0.00')" />
									<xsl:choose>
							          <xsl:when test="$nRoundOff &gt; 0">
							            <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>
							          </xsl:when>
							          <xsl:otherwise>
							            <ISDEEMEDPOSITIVE>YES</ISDEEMEDPOSITIVE>
							          </xsl:otherwise>
							        </xsl:choose>
									<LEDGERFROMITEM>No</LEDGERFROMITEM>
									<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>
									<ROUNDLIMIT>0</ROUNDLIMIT>
									<LEDGERNAME>ROUNDED OFF</LEDGERNAME>
									<ISPARTYLEDGER>NO</ISPARTYLEDGER>
									<AMOUNT><xsl:value-of select="$nRoundOff" /></AMOUNT>
								</ALLLEDGERENTRIES.LIST>
						        
      							<PAYROLLMODEOFPAYMENT.LIST></PAYROLLMODEOFPAYMENT.LIST>
   								<ATTDRECORDS.LIST></ATTDRECORDS.LIST>
						        <UDF:REFERENCEDATE.LIST DESC="`ReferenceDate`" ISLIST="YES" TYPE="Date" INDEX="">
						       		<UDF:REFERENCEDATE DESC="`ReferenceDate`"><xsl:value-of select="m_strTallyTransformDate" /></UDF:REFERENCEDATE>
						      	</UDF:REFERENCEDATE.LIST>
     						</VOUCHER>
						</xsl:for-each>
					</TALLYMESSAGE>
				</IMPORTDATA>
			</BODY>
		</ENVELOPE>
	</xsl:template>
</xsl:stylesheet>