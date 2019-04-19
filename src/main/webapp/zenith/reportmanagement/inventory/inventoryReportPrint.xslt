<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="Total"
			select="sum(root/InventoryReportDataList/InventoryReportData/m_nAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" colspan="2">
							Inventory Report
          				</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="trademust">
								<tr>
									<td class="print_fieldTableHeading" style="width:10%;"> 
										Filters
									</td>
									<td class="print_fieldTableHeading" style="width:30%;">
										Article# : <xsl:value-of select="root/m_strArticleNumberFilterBox"/>
									</td >
									<td class="print_fieldTableHeading" style="width:30%;">
										Item Name : <xsl:value-of select="root/m_strItemNameFilterBox"/>
									</td>
									<td class="print_fieldTableHeading" style="width:30%;">
										Brand : <xsl:value-of select="root/m_strBrandFilterBox"/>
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
										<div style="height:850px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Article#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:30%">
														Item Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Brand
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Details
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Current Stock
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Selling Price
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="root/InventoryReportDataList/InventoryReportData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strArticleNumber" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strItemName" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strBrand" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strDetail" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_nCurrentStock" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_nSellingPrice" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right"> 
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nAmount, '##,##,##,###.00')" />
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
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($Total,'##,##,##,###.00')"/>
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