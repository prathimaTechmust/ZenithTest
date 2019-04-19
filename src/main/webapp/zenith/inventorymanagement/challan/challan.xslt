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
							<img src="images/defaultLogo.PNG" title="Logo"/>
						</td>
						<td class="trademust" style="line-height:30%; width:30%;">
							<p>Phone:##########</p>
							<p>Fax:########</p>
							<p>E-mail:default@default.com</p>
							<p>Website:www.default.com</p>
							<p>Address</p>
							<p>City-pincode</p>
						</td>
					</tr>
					<tr>
						<td class="trademust" style="width:70%; vertical-align: top" colspan="2">
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
									<td rowspan="2" style="padding-left:0px; padding-right:0px">
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
										<div style="min-height:760px">
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
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>