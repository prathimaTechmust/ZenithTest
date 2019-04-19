<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subSalesTotal"
			select="sum(ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/LineItemData/SalesLineItemData/m_nAmount)" />
		<xsl:variable name="subTaxTotal"
			select="sum(ChallanData/m_oSalesData/SalesData/m_oTaxes/Tax/m_nTaxAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							DELIVERY CHALLAN CUM TAX INVOICE
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="images/AdnanLogo.png" title="Logo" width="100%" height="100px"/>
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
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCompanyName" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strAddress" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCity" /> - <xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strPinCode" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strTelephone" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strMobileNumber" /><br/>
									</td>
									<td class ="print_tdBorder" rowspan="2" style="padding-left:0px; padding-right:0px">
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strDateTime">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">Date<br/>Time</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strDate"/><br/><xsl:value-of select="ChallanData/m_strTime"/></td>
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
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strTinNumber" /><br/><xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCSTNumber" /></td>
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
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_oSalesData/SalesData/m_strPurchaseOrderNumber"/></td>
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
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strChallanNumber"/></td>
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
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strDate"/></td>
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
											<xsl:value-of select="ChallanData/m_oSalesData/SalesData/SiteData/m_strSiteName" /><br/>
											<xsl:value-of select="ChallanData/m_oSalesData/SalesData/SiteData/m_strSiteAddress" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div style="min-height:550px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:35%">
														Articles
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="7%">
														Size
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%">
														Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%;">
														Unit
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%">
														Price
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:7%">
														Disc(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:7%">
														Tax(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/LineItemData">
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
															<xsl:value-of select="format-number(SalesLineItemData/m_nDiscount, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(SalesLineItemData/m_nTax, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nQuantity * (SalesLineItemData/m_nPrice - (SalesLineItemData/m_nPrice * SalesLineItemData/m_nDiscount div 100)), '##,##,##,###.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" colspan="8" style="text-align:right">
														Total
													</td>
													<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
														<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/subTotal - ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/discounts/totalDiscount,'##,##,##,###.00')"/>
													</td>
												</tr>
											</table>
											<table class="trademust">
												<tr>
													<td class="trademust" style="vertical-align:top;" colspan="2">
													</td>
												</tr>
												<tr>
													<td style="width:65%;">
													</td>
													<td style="width:35%;">
														<table class="print_tableBorder trademust">
															<xsl:for-each select="ChallanData/m_oSalesData/SalesData/m_oTaxes/Tax">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="width:10%; text-align:right">
																		<xsl:value-of select="m_strTaxName" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:12%; text-align:right">
																		<xsl:value-of select="format-number(m_nTaxPercent, '##,##,##,###.00')"/>
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:13%; text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nTaxAmount, '##,##,##,###.00')" />
																	</td>
																</tr>
															</xsl:for-each> 
															<xsl:if test= "$subTaxTotal &gt; 0">
																<tr>
																	<td></td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		Total
																	</td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/taxes/totalTaxAmount,'##,##,##,###.00')"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right" colspan="2">
																	Round Off
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																	<xsl:value-of select="format-number(round($subSalesTotal + $subTaxTotal) - ($subSalesTotal + $subTaxTotal) ,'##,##,##,##0.00')"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:70%">
										Grand Total
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(round($subSalesTotal + $subTaxTotal),'##,##,##,###.00')"/>
									</td>
								</tr>
								<tr>
									<td class="trademust print_tdBorder" colspan="2"> 
										<table class="trademust">
											<tr>
												<td class="print_fieldTableHeading" style="text-align:left;">
													Transport:
												</td>
												<td>
												</td>
											</tr>
											<tr>
												<td class="trademust">
													<table class="trademust">
														<tr>
															<td class="print_fieldTableHeading" style="text-align:left; width:70%"> 
																L.R/R.R. No.:
															</td>
															<td>
															</td>
															<td class="print_fieldTableHeading" style="text-align:left;">
																L.R. Date :
															</td>
															<td>
															</td>
														</tr>
													</table>
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