<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;"
							colspan="2">
							DEBIT NOTE
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="images/defaultLogo.PNG" title="Logo" />
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
						<td class="trademust" style="width:70%; vertical-align: top"
							colspan="2">
							<table class="trademust print_tableBorder">
								<tr>
									<td class="print_tdBorder" style="width:70%; vertical-align: top;">
										<b>To: </b>
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorCompanyName" />
										<br />
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorAddress" />
										<br />
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorCity" />
										-
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorPinCode" />
										<br />
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorTelephone" />
										<br />
										<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorMobileNumber" />
										<br />
									</td>
									<td class="print_tdBorder" 
										style="padding-left:0px; padding-right:0px">
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left"
													id="m_strDebitNoteNumber">
													<table class="trademust">
														<tr>
															<td style="width:40%;">Debit Note No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;">
																<xsl:value-of select="PurchaseReturnedData/m_strDebitNoteNumber" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left"
													id="m_strDateTime">
													<table class="trademust">
														<tr>
															<td style="width:40%;">
																Date
															</td>
															<td style="width:5%;">
																:
															</td>
															<td style="width:55%;">
																<xsl:value-of select="PurchaseReturnedData/m_strDate" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData  print_tdBorderBottom" style="text-align:left">
													<table class="trademust">
														<tr>
															<td style="width:40%;">
																TIN No.
																<br />
																C.S.T. No.
															</td>
															<td style="width:5%;">
																:
																<br />
																:
															</td>
															<td style="width:55%;">
																<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorTinNumber" />
																<br />
																<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorCSTNumber" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
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
														style="width:20%">
														Articles
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="22%">
														Details
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%">
														Received Quantity
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
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%">
														Return Quantity
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="PurchaseReturnedData/m_oPurchaseReturnedLineItemData/PurchaseReturnedLineItemData">
														<tr>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="position()"/>
															</td>
															<td class="print_fieldTableData print_tdBorder">
																<xsl:value-of select="m_oPurchaseLineItem/PurchaseLineItem/m_oItemData/ItemData/m_strArticleNumber" />
															</td>
															<td class="print_fieldTableData print_tdBorder">
																<xsl:value-of select="m_oPurchaseLineItem/PurchaseLineItem/m_oItemData/ItemData/m_strDetail" />
															</td>
															<td class="print_fieldTableData print_tdBorder">
																<xsl:value-of select="m_oPurchaseLineItem/PurchaseLineItem/m_nQuantity" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																<xsl:value-of select="m_oPurchaseLineItem/PurchaseLineItem/m_oItemData/ItemData/m_strUnit" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_oPurchaseLineItem/PurchaseLineItem/m_nPrice, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(m_oPurchaseLineItem/PurchaseLineItem/m_nDiscount, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(m_oPurchaseLineItem/PurchaseLineItem/m_nTax, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<xsl:value-of select="format-number(m_nQuantity, '##,##,##,##0.00')" />
															</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nQuantity * (m_oPurchaseLineItem/PurchaseLineItem/m_nPrice - (m_oPurchaseLineItem/PurchaseLineItem/m_nPrice * m_oPurchaseLineItem/PurchaseLineItem/m_nDiscount div 100) + (m_oPurchaseLineItem/PurchaseLineItem/m_nPrice * m_oPurchaseLineItem/PurchaseLineItem/m_nTax div 100)), '##,##,##,###.00')"/>
															</td>
														</tr>
												</xsl:for-each> 
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:70%">
										Grand Total
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
										<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="format-number(PurchaseReturnedData/m_oPurchaseReturnedLineItemData/grandTotal,'##,##,##,###.00')"/>
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