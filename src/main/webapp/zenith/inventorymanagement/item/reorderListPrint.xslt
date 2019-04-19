<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
				<tr>
						<td class="print_formHeading" colspan="2">
							Reorder List
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
										Article Number: <xsl:value-of select="root/m_strArticleNumberFilterBox"/>
									</td>
									<td class="print_fieldTableHeading" style="width:30%;">
										Item Name: <xsl:value-of select="root/m_strItemNameFilterBox"/>
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
										<div style="min-height:1025px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%">
														Article Number
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:25%">
														Item Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%">
														Brand
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:14%">
														Details
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
														Reorder Level
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
														Current Stock
													</td>
												</tr>
												<xsl:for-each select="root/ReorderListData/ItemData">
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
														<td class="print_fieldTableData print_tdBorder"  style="text-align:right">
															<xsl:value-of select= "format-number(m_nReorderLevel, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder"  style="text-align:right">
															<xsl:value-of select="format-number(m_nCurrentStock, '##,##,##,##0.00')"/>
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