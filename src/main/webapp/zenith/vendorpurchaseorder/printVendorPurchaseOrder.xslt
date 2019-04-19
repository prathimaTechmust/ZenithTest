<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(VendorPurchaseOrderData/m_oVendorPOLineItemData/VendorPOLineItemData/m_nAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_formHeading">Vendor Purchase
											Order Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:100%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">From :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of
														select="VendorPurchaseOrderData/VendorData/m_strVendorCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">PO Number :</td>
												<td class="xslt_fieldData" id="m_strPurchaseOrderNumber">
													<xsl:value-of
														select="VendorPurchaseOrderData/m_strPurchaseOrderNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strPurchaseOrderDate">
													<xsl:value-of
														select="VendorPurchaseOrderData/m_dPurchaseOrderDate" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="print_tableBorder trademust">
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
																	style="width:15%">
																	Articles 
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:15%">
																	Item Name
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:8%">
																	Qty
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:8%">
																	Price
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:8%">
																	Disc(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Tax Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%">
														Tax(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:8%">
														Received Quantity
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="VendorPurchaseOrderData/m_oVendorPOLineItemData/VendorPOLineItemData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strArticleNumber" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strItemName" />
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
															<xsl:value-of select="m_strTaxName" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="m_nTax" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="m_nReceivedQty" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nAmount, '##,##,##,###.00')" />
														</td>
														</tr>
												</xsl:for-each> 
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
				</table>
				</td></tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>