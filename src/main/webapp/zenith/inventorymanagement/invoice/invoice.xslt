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
							          	<div style="min-height:650px">
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
												<xsl:for-each select="InvoiceData/m_oSalesSet/SalesData">
												      <tr>
														<td class="print_fieldTableHeading print_tdBorder" style="text-align:left" colspan="9">
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
																<xsl:value-of select="format-number(SalesLineItemData/m_nQuantity, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="SalesLineItemData/m_strUnit" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nPrice, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(SalesLineItemData/m_nDiscount, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(SalesLineItemData/m_nTax, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nQuantity * (SalesLineItemData/m_nPrice - (SalesLineItemData/m_nPrice * SalesLineItemData/m_nDiscount div 100)), '##,##,##,##0.00')"/>
															</td>
														</tr>
													</xsl:for-each> 
													<tr>
														<td class="print_fieldTableHeading print_tdBorder" style="text-align:right;" colspan="8">
															Total
														</td>
														<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="format-number((m_oSalesLineItems/subTotal - m_oSalesLineItems/discounts/totalDiscount),'##,##,##,##0.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
											</table>
											<table class="trademust">
												<tr>
													<td style="width:65%;">
													</td>
													<td style="width:35%;">
														<table class="print_tableBorder trademust">
															<xsl:for-each select="InvoiceData/m_oTaxes/Tax">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="width:10%; text-align:right">
																		<xsl:value-of select="m_strTaxName" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:12%; text-align:right">
																		<xsl:value-of select="format-number(m_nTaxPercent, '##,##,##,###.00')"/>
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:13%; text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nTaxAmount, '##,##,##,##0.00')" />
																	</td>
																</tr>
															</xsl:for-each> 
															<xsl:if test= "m_oSalesLineItems/taxes/totalTaxAmount &gt; 0">
																<tr>
																	<td></td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		Total
																	</td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_oSalesLineItems/taxes/totalTaxAmount,'##,##,##,##0.00')"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right" colspan="2">
																	Round Off
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																	<xsl:value-of select="format-number((round(InvoiceData/m_oSalesSet/grandTotal) - InvoiceData/m_oSalesSet/grandTotal) ,'##,##,##,##0.00')"/>
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
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(round(InvoiceData/m_oSalesSet/grandTotal),'##,##,##,##0.00')"/>
									</td>
								</tr>
								<tr>
									<td class="trademust print_tdBorder" colspan="2" height="80px"> 
										<table class="trademust">
											<tr>
												<td class="print_fieldTableData" style="text-align:left;">
													LRNumber : <xsl:value-of select="InvoiceData/m_strLRNumber" />
												</td>
											</tr>
											<tr>
												<td class="print_fieldTableData" style="text-align:left;">
													ESugamNumber : <xsl:value-of select="InvoiceData/m_strESugamNumber" />
												</td>
											</tr>
											<tr>
												<td class="print_fieldTableData" style="text-align:left;">
													<xsl:value-of select="InvoiceData/m_strRemarks" />
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