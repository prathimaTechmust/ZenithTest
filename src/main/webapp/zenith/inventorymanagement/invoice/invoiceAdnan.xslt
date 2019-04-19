<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							TAX INVOICE
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="images/AdnanLogo.png" title="Logo" width="100%" height="90px"/>
						</td>
						<td class="trademust" style="line-height:30%; width:30%;">
							<p>Ph:4114 0266,4114 1266,2224 2466</p>
							<p>Fax:2223 9595</p>
							<p>E-mail:adnanalco@yahoo.com</p>
							<p>Website:www.asiancorp.in</p>
							<p>86,Narasimharaja Road</p>
							<p>BENGALOORU-560 002</p>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="trademust print_tableBorder">
								<tr>
									<td class ="print_tdBorder" style="width:70%; vertical-align: top;" >
										<b>To: </b>
										<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strCompanyName" /><br/>
										<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strAddress" /><br/>
										<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strCity" /> - <xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strPinCode" /><br/>
										<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strTelephone" /><br/>
										<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strMobileNumber" /><br/>
									</td>
									<td class ="print_tdBorder" rowspan="2" style="padding-left:0px; padding-right:0px">
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strInvoiceNo">
		                     						<table  class="trademust">
														<tr>
															<td style="width:40%;">Invoice No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_strInvoiceNumber" /></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strDateTime">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">Date<br/>Time</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_strDate"/><br/><xsl:value-of select="InvoiceData/m_strTime"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData  print_tdBorderBottom" style="text-align:left">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">TIN No.<br/>C.S.T. No.</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strTinNumber" /><br/><xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/ClientData/m_strCSTNumber" /></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">Order No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/m_strPurchaseOrderNumber"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">DC No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/m_strChallanNumber"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData" style="text-align:left">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">DC Date</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/m_strChallanDate"/></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class ="print_tdBorder" style="width:70%; vertical-align: top;">
										<b>Site: </b>
											<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/SiteData/m_strSiteName" /><br/>
											<xsl:value-of select="InvoiceData/m_oSalesSet/SalesData/SiteData/m_strSiteAddress" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div style="min-height:528px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:5%">
														Sl. No.
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:40%">
														Description
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="10%">
														Size
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%">
														Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%;">
														Unit
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%">
														Rate
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="InvoiceData/m_oSalesSet/SalesData">
											      <tr>
													<td class="print_fieldTableHeading print_tdBorder" style="text-align:left" colspan="7">
														<xsl:choose>
													         <xsl:when test="m_strChallanNumber != ''">
													           Vide Challan Number : <xsl:value-of select="m_strChallanNumber"/>
													         </xsl:when>
													         <xsl:otherwise>
													          	Direct Bill
													         </xsl:otherwise>
													    </xsl:choose>
													</td>
												  </tr>
													<xsl:for-each select="m_oSalesLineItems/LineItemData">
														<tr>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="SerialNumber"/>
															</td>
															<td class="print_fieldTableData print_tdBorder">
																<xsl:value-of select="SalesLineItemData/m_strArticleDescription" />
															</td>
															<td class="print_fieldTableData print_tdBorder">
																<xsl:value-of select="SalesLineItemData/m_strDetail" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(SalesLineItemData/m_nQuantity, '##,##,##,###.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="SalesLineItemData/m_strUnit" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nPrice, '##,##,##,###.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number((SalesLineItemData/m_nQuantity * SalesLineItemData/m_nPrice), '##,##,##,###.00')"/>
															</td>
														</tr>
													</xsl:for-each>
													<tr>
														<td class="print_fieldTableData print_tdBorder">
														</td>
														<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="text-align:right">
															Sub Total
														</td>
														<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_oSalesLineItems/subTotal,'##,##,##,###.00')"/>
														</td>
													</tr>
													<xsl:for-each select="m_oSalesLineItems/discounts/LineItemData">
														<tr>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="SerialNumber"/>
															</td>
															<td class="print_fieldTableData print_tdBorder" colspan="4">
																<xsl:value-of select="SalesLineItemData/m_strArticleDescription" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nPrice, '##,##,##,###.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="m_nAmount"/>
															</td>
														</tr>
													</xsl:for-each>
													<xsl:if test= "m_oSalesLineItems/discounts/totalDiscount &gt; 0">
														<tr>
															<td class="print_fieldTableData print_tdBorder">
															</td>
															<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="text-align:right">
																Total Discount
															</td>
															<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_oSalesLineItems/discounts/totalDiscount,'##,##,##,###.00')"/>
															</td>
														</tr>
														<tr>
															<td class="print_fieldTableData print_tdBorder">
															</td>
															<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="text-align:right">
																Sub Total
															</td>
															<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number((m_oSalesLineItems/subTotal - m_oSalesLineItems/discounts/totalDiscount),'##,##,##,###.00')"/>
															</td>
														</tr>
													</xsl:if>
													<xsl:for-each select="m_oSalesLineItems/taxes/LineItemData">
														<tr>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="SerialNumber"/>
															</td>
															<td class="print_fieldTableData print_tdBorder" colspan="5">
																<xsl:value-of select="SalesLineItemData/m_strArticleDescription" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nAmount,'##,##,##,###.00')"/>
															</td>
														</tr>
													</xsl:for-each>
													<tr>
														<td class="print_fieldTableData print_tdBorder">
														</td>
														<td class="print_fieldTableHeading print_tdBorder" style="text-align:right;" colspan="5">
															Total
														</td>
														<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="format-number(((m_oSalesLineItems/subTotal - m_oSalesLineItems/discounts/totalDiscount) + m_oSalesLineItems/taxes/totalTaxAmount),'##,##,##,###.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:70%">
										Bill Total (Rounded <xsl:value-of select="format-number((round(InvoiceData/m_oSalesSet/grandTotal) - InvoiceData/m_oSalesSet/grandTotal) ,'##,##,##,##0.00')"/>)
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
										<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="format-number(round(InvoiceData/m_oSalesSet/grandTotal) ,'##,##,##,##0.00')"/>
									</td>
								</tr>
								<tr>
									<td class="trademust print_tdBorder" colspan="2"> 
										<table class="trademust">
											<tr>
												<td class="print_fieldTableData" style="text-align:left;">
													<xsl:value-of select="m_strRemarks" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="print_tdBorder print_footerData" colspan="2"> 
										<center>SALES TAX CLEARANCE CERTIFICATE</center>
										Certified that the goods, on which Sales Tax has been charged have not been exempted
										under the Central Tax act or the State Tax act or the rules made thereunder. The amounts
										charged on account of Sales Tax on these goods are not more than what is payable under the provisions
										of the relevant act of the rules thereunder. <b>TIN NO. 29680066568 CST. No. 0315992 dt.1-4-91</b>
									</td>
								</tr>
								<tr>
									<td class="print_tdBorder" colspan="2">
										<table class="trademust">
											<tr>
												<td class="print_footerData" style="width:60%"> 
													Our responsibility as to breakage, damage or complete loss of the articles ceases soon after they are 
													delivered to the carriers. Interest at the rate of 24% per annum will be charged on all accounts due over
													one month after the date of supply.
												</td>
												<td class="print_fieldTableData" style="text-align:right"> 
													For <b>ADNAN ALLIED CONCERN</b>
													<br/><br/><br/>		
													Authorized Signatory
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>