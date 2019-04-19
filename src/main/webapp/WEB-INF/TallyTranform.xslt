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
						<xsl:for-each select="ReportInvoiceData/InvoiceData">
							<VOUCHER VCHTYPE="Sales" ACTION="Create">
								<xsl:variable name="subSalesTotal"
									select="sum(m_oSalesSet/SalesData/m_oSalesLineItems/LineItemData/SalesLineItemData/m_nAmount)" />
								<xsl:variable name="subTaxTotal" select="sum(m_oTaxes/Tax/m_nTaxAmount)" />
								<xsl:variable name="grandTotal" select="$subSalesTotal + $subTaxTotal" />
								<xsl:variable name="strInvoiceNumber" select="m_strInvoiceNumber" />

								<DATE>
									<xsl:value-of select="m_strTallyTransformDate" />
								</DATE>

								<EFFECTIVEDATE>
									<xsl:value-of select="m_strTallyTransformDate" />
								</EFFECTIVEDATE>

								<ISOPTIONAL>No</ISOPTIONAL>

								<USEFORGAINLOSS>No</USEFORGAINLOSS>

								<USEFORCOMPOUND>No</USEFORCOMPOUND>

								<VOUCHERTYPENAME>Sales</VOUCHERTYPENAME>

								<VOUCHERNUMBER>
									<xsl:value-of select="$strInvoiceNumber" />
								</VOUCHERNUMBER>

								<REFERENCE><xsl:value-of select="$strInvoiceNumber" /></REFERENCE>

								<NARRATION>Sale Bill No <xsl:value-of select="$strInvoiceNumber" /> dated <xsl:value-of select="m_strDate" /></NARRATION>

								<PARTYLEDGERNAME>
									<xsl:value-of select="m_oClientData/ClientData/m_strCompanyName" />
								</PARTYLEDGERNAME>

								<ALLLEDGERENTRIES.LIST>

									<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>

									<LEDGERFROMITEM>No</LEDGERFROMITEM>

									<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>

									<ROUNDLIMIT>0</ROUNDLIMIT>

									<LEDGERNAME>
										<xsl:value-of select="m_oClientData/ClientData/m_strCompanyName" />
									</LEDGERNAME>

									<ISPARTYLEDGER>NO</ISPARTYLEDGER>

									<AMOUNT>-<xsl:value-of select="round($grandTotal)" /></AMOUNT>

								</ALLLEDGERENTRIES.LIST>

								<xsl:for-each select="m_oTaxes/Tax">
									<ALLLEDGERENTRIES.LIST>

										<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>

										<LEDGERFROMITEM>No</LEDGERFROMITEM>

										<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>

										<ROUNDLIMIT>0</ROUNDLIMIT>

										<TAXCLASSIFICATIONNAME>
											<xsl:value-of select="m_strOutputTaxClassificationName" />
										</TAXCLASSIFICATIONNAME>

										<LEDGERNAME>
											<xsl:value-of select="m_strSalesAmountLedgerName" />
										</LEDGERNAME>

										<ISPARTYLEDGER>YES</ISPARTYLEDGER>

										<AMOUNT>
											<xsl:value-of
												select="format-number((m_nTaxAmount div m_nTaxPercent) * 100,'##,##,##,###.00')" />
										</AMOUNT>

										<BILLALLOCATIONS.LIST>

											<BILLTYPE>New Ref</BILLTYPE>

											<NAME>
												<xsl:value-of select="$strInvoiceNumber" />
											</NAME>

											<BILLDATE></BILLDATE>

											<BILLCREDITPERIOD>90 Days</BILLCREDITPERIOD>

											<AMOUNT>
												<xsl:value-of
													select="format-number($subSalesTotal ,'##,##,##,###.00')" />
											</AMOUNT>

										</BILLALLOCATIONS.LIST>

									</ALLLEDGERENTRIES.LIST>
								</xsl:for-each>
								<xsl:for-each select="m_oTaxes/Tax">
									<ALLLEDGERENTRIES.LIST>

										<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>

										<LEDGERFROMITEM>No</LEDGERFROMITEM>

										<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>

										<ROUNDLIMIT>0</ROUNDLIMIT>

										<TAXCLASSIFICATIONNAME>
											<xsl:value-of select="m_strOutputTaxClassificationName" />
										</TAXCLASSIFICATIONNAME>

										<LEDGERNAME>
											<xsl:value-of select="m_strOutputTaxAmountLedgerName" />
										</LEDGERNAME>

										<ISPARTYLEDGER>YES</ISPARTYLEDGER>

										<AMOUNT>
											<xsl:value-of select="format-number(m_nTaxAmount ,'##,##,##,###.00')" />
										</AMOUNT>

										<VATASSESSABLEVALUE>
											<xsl:value-of
												select="format-number($subSalesTotal ,'##,##,##,###.00')" />
										</VATASSESSABLEVALUE>

										<BILLALLOCATIONS.LIST>

											<BILLTYPE>New Ref</BILLTYPE>

											<NAME>
												<xsl:value-of select="$strInvoiceNumber" />
											</NAME>

											<BILLDATE></BILLDATE>

											<BILLCREDITPERIOD>90 Days</BILLCREDITPERIOD>

											<AMOUNT>
												<xsl:value-of
													select="format-number($subTaxTotal ,'##,##,##,###.00')" />
											</AMOUNT>

										</BILLALLOCATIONS.LIST>

									</ALLLEDGERENTRIES.LIST>
								</xsl:for-each>
								<ALLLEDGERENTRIES.LIST>
								
									<xsl:variable name="nRoundOff" select="format-number((round($grandTotal) - $grandTotal),'##,##,##,##0.00')" />
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

									<AMOUNT>
										<xsl:value-of select="$nRoundOff" />
									</AMOUNT>

								</ALLLEDGERENTRIES.LIST>
								
								<xsl:variable name="nZeroTaxedAmount" select="$subSalesTotal - m_strTaxedAmount" />
								<xsl:if test="$nZeroTaxedAmount &gt; 0">
									<ALLLEDGERENTRIES.LIST>

										<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>

										<LEDGERFROMITEM>No</LEDGERFROMITEM>

										<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>

										<ROUNDLIMIT>0</ROUNDLIMIT>

										<TAXCLASSIFICATIONNAME></TAXCLASSIFICATIONNAME>

										<LEDGERNAME>
											<xsl:value-of select="m_strZeroTaxLedgerName" />
										</LEDGERNAME>

										<ISPARTYLEDGER>YES</ISPARTYLEDGER>

										<AMOUNT>
											<xsl:value-of select="format-number($nZeroTaxedAmount ,'##,##,##,###.00')" />
										</AMOUNT>

										<VATASSESSABLEVALUE>
											<xsl:value-of
												select="format-number($subSalesTotal ,'##,##,##,###.00')" />
										</VATASSESSABLEVALUE>
	
									</ALLLEDGERENTRIES.LIST>
								</xsl:if>
									
							</VOUCHER>
						</xsl:for-each>
					</TALLYMESSAGE>

				</IMPORTDATA>

			</BODY>

		</ENVELOPE>
	</xsl:template>
</xsl:stylesheet>