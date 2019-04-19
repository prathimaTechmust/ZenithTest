<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							QUOTATION
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
										<xsl:value-of select="QuotationData/ClientData/m_strCompanyName" /><br/>
										<xsl:value-of select="QuotationData/ClientData/m_strAddress" /><br/>
										<xsl:value-of select="QuotationData/ClientData/m_strCity" /> - <xsl:value-of select="QuotationData/ClientData/m_strPinCode" /><br/>
										<xsl:value-of select="QuotationData/ClientData/m_strTelephone" /><br/>
										<xsl:value-of select="QuotationData/ClientData/m_strMobileNumber" /><br/>
									</td>
									<td class ="print_tdBorder" rowspan="2" style="padding-left:0px; padding-right:0px">
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strDate">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">Date</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="QuotationData/m_strDate"/></td>
														</tr>
														<tr>
															<td style="width:40%;">Quotation No</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="QuotationData/m_strQuotationNumber"/></td>
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
															<td style="width:55%;"><xsl:value-of select="QuotationData/ClientData/m_strTinNumber" /><br/><xsl:value-of select="QuotationData/ClientData/m_strCSTNumber" /></td>
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
											<xsl:value-of select="QuotationData/SiteData/m_strSiteName" /><br/>
											<xsl:value-of select="QuotationData/SiteData/m_strSiteAddress" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
							          	<div style="min-height:730px">
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
												<xsl:for-each select="QuotationData/m_oQuotationLineItems/LineItemData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="SerialNumber"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="QuotationLineItemData/m_strArticleDescription" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="QuotationLineItemData/m_strDetail" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(QuotationLineItemData/m_nQuantity, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="QuotationLineItemData/m_strUnit" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(QuotationLineItemData/m_nPrice, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(QuotationLineItemData/m_nDiscount, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(QuotationLineItemData/m_nTax, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(QuotationLineItemData/m_nQuantity * (QuotationLineItemData/m_nPrice - (QuotationLineItemData/m_nPrice * QuotationLineItemData/m_nDiscount div 100)), '##,##,##,###.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="text-align:right;" colspan="8">
														Total
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
														<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="format-number((QuotationData/m_oQuotationLineItems/subTotal - QuotationData/m_oQuotationLineItems/discounts/totalDiscount),'##,##,##,###.00')"/>
													</td>
												</tr>
											</table>
											<table class="trademust">
												<tr>
													<td style="width:65%;">
													</td>
													<td style="width:35%;">
														<table class="print_tableBorder trademust">
															<xsl:for-each select="QuotationData/m_oTaxes/Tax">
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
															<xsl:if test= "m_oQuotationLineItems/taxes/totalTaxAmount &gt; 0">
																<tr>
																	<td></td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		Total
																	</td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_oQuotationLineItems/taxes/totalTaxAmount,'##,##,##,###.00')"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right" colspan="2">
																	Round Off
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																	<xsl:value-of select="format-number((round(QuotationData/grandTotal) - QuotationData/grandTotal) ,'##,##,##,##0.00')"/>
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
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(round(QuotationData/grandTotal),'##,##,##,###.00')"/>
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