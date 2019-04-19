<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
				<tr>
						<td class="print_formHeading" colspan="2">
							Local Purchase List
          				</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="trademust">
								<tr>
									<td class="print_fieldTableHeading" style="width:20%;"> 
										Filter
									</td>
									<td class="print_fieldTableHeading" style="width:80%;">
										Item Name: <xsl:value-of select="root/m_strItemNameFilterBox"/>
									</td>
								</tr>
							</table>
          				</td>
					</tr>
					<tr>
						<td class="trademust" style="width:70%; vertical-align: top" colspan="2">
							<table class="trademust print_tableBorder">
								<tr>
									<td colspan="2">
										<div style="min-height:1025px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:35%">
														Item Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:20%">
														Order Quantity
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:20%">
														Shipped Quantity
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:20%">
														Balance Quantity
													</td>
												</tr>
												<xsl:for-each select="root/LocalPurchaseListData/LocalPurchaseData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strArticleDescription" />
														</td>
														<td class="print_fieldTableData print_tdBorder"  style="text-align:right">
															<xsl:value-of select= "format-number(m_nQty, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder"  style="text-align:right">
															<xsl:value-of select="format-number(m_nShippedQty, '##,##,##,##0.00')"/>
														</td>
														<td class="print_fieldTableData print_tdBorder"  style="text-align:right">
															<xsl:value-of select="format-number(m_nBalance, '##,##,##,##0.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
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