<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="Total"
			select="sum(root/StockMovementDataList/StockMovementData/m_nCurrentValue)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" colspan="2">
							Stock Movement Report [From:<xsl:value-of select="root/m_strFromDate"/> - To:<xsl:value-of select="root/m_strToDate"/>]
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
										Aricle Number : <xsl:value-of select="root/m_strArticleNumberFilterBox"/>
									</td >
									<td class="print_fieldTableHeading" style="width:30%;">
										Item Name : <xsl:value-of select="root/m_strItemNameFilterBox"/>
									</td>
									<td class="print_fieldTableHeading" style="width:30%;">
										Item Brand : <xsl:value-of select="root/m_strItemBrandFilterBox"/>
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
													<td class="print_fieldTableHeading print_tdBorder" style="width:16%">
														Item Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:11%">
														Brand
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:10%">
														Details
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:9%">
														Recieved Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:9%">
														Issued Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:9%">
														Balance Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:9%">
														Current Stock
													</td>
												</tr>
												<xsl:for-each select="root/StockMovementDataList/StockMovementData">
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
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nReceived, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nIssued, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nBalanceQuantity, '##,##,##,###.00')" />
														</td>
															<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nCurrentStock, '##,##,##,###.00')" />
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