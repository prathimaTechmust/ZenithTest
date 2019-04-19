<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(SalesData/m_oSalesLineItems/SalesLineItemData/m_nAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							SALES
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="images/AdnanLogo.png" title="Logo"/>
						</td>
						<td class="trademust" style="line-height:30%; width:30%;">
							<p>4114 0266,4114 1266,2224 2466</p>
							<p>Fax:2223 9595</p>
							<p>E-mail:adnanalco@yahoo.com</p>
							<p>Website:www.asiancorp.in</p>
							<p>86,Narasimharaja Road</p>
							<p>BENGALOORU-560 002</p>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="print_tableBorder trademust">
								<tr>
									<td class="trademust print_tdBorder" style="width:70%; vertical-align: top">
										To :
										<xsl:value-of select="SalesData/ClientData/m_strCompanyName" /><br/>
										<xsl:value-of select="SalesData/ClientData/m_strAddress" /><br/>
										<xsl:value-of select="SalesData/ClientData/m_strCity" /> - <xsl:value-of select="SalesData/ClientData/m_strPinCode" /><br/>
										<xsl:value-of select="SalesData/ClientData/m_strTelephone" /><br/>
										<xsl:value-of select="SalesData/ClientData/m_strMobileNumber" /><br/>
										<xsl:value-of select="SalesData/ClientData/m_strEmail" />
									</td>
									<td>
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strInvoiceNo">
													Invoice No.&#xA0;: <xsl:value-of select="SalesData/m_strInvoiceNo" />
	                     						</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strDateTime">
													Date&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/m_strDate"/> <br/>
													Time&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/m_strTime"/>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData  print_tdBorderBottom" style="text-align:left">
													TIN No.&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/ClientData/m_strTinNumber" /><br/>
													C.S.T. No.&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/ClientData/m_strCSTNumber" />
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													Order No.&#xA0;&#xA0;&#xA0;:
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													DC No.&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/m_strChallanNumber"/>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData" style="text-align:left">
													DC Date&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;: <xsl:value-of select="SalesData/m_strChallanDate"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust print_tdBorder" colspan="2"> 
										<div style="height:450px">
											<table class="trademust">
												<tr>
													<td class="trademust" style="vertical-align:top;height:420px">
														<table class="print_tableBorder trademust">
															<tr>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:5%">
																	#
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:40%">
																	Articles 
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:10%">
																	Qty
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:10%">
																	Price
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:10%">
																	Disc(%)
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="width:12%">
																	Disc Price
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
																	Amount
																</td>
															</tr>
															<xsl:for-each select="SalesData/m_oSalesLineItems/SalesLineItemData">
																<tr>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																		<xsl:value-of select="position()"/>
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="m_strArticleNumber" />-
																		<xsl:value-of select="m_oItemData/ItemData/m_strItemName" />-
																		<xsl:value-of select="m_oItemData/ItemData/m_strDetail" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<xsl:value-of select="format-number(m_nQuantity, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nPrice, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<xsl:value-of select="m_nDiscount" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right"> 
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nPrice - (m_nPrice * m_nDiscount div 100), '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nQuantity * (m_nPrice - (m_nPrice * m_nDiscount div 100)), '##,##,##,###.00')"/>
																	</td>
																</tr>
															</xsl:for-each> 
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" colspan="6" style="text-align:right">
																	Total
																</td>
																<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
																	<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($subTotal,'##,##,##,###.00')"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td class="trademust" style="vertical-align:bottom">
														<table class="print_tableBorder trademust">
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:87%">
																	Grand Total
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($subTotal,'##,##,##,###.00')"/>
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
													delivered to the carries. Interest at the rate of 24% per annum will be charged	on all accounts due over
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