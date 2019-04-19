<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(SalesData/m_oSalesLineItems/LineItemData/SalesLineItemData/m_nAmount)" />
		<html>
			<body>
				<table class="trademust">
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
																	style="width:44%">
																	Articles 
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:8%">
																	Qty
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:10%">
																	Price
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:8%">
																	Disc(%)
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="width:12%">
																	Disc Price
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
																	Amount
																</td>
															</tr>
															<xsl:for-each select="SalesData/m_oSalesLineItems/LineItemData">
																<tr>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:center">
																		<xsl:value-of select="position()"/>
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="SalesLineItemData/m_strArticleDescription" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<xsl:value-of select="format-number(SalesLineItemData/m_nQuantity, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nPrice, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<xsl:value-of select="SalesLineItemData/m_nDiscount" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right"> 
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nPrice - (SalesLineItemData/m_nPrice * SalesLineItemData/m_nDiscount div 100), '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(SalesLineItemData/m_nQuantity * (SalesLineItemData/m_nPrice - (SalesLineItemData/m_nPrice * SalesLineItemData/m_nDiscount div 100)), '##,##,##,###.00')"/>
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
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>