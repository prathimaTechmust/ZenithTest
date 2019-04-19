<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table id="vatReportXSLT_table_report"  class="print_tableBorder trademust">
					<tr>
						<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="vertical-align:top">
							Details of Local Sales/ URD Purchases and Output Tax/Purchase Tax Payable
						</td>
					</tr>
					<xsl:for-each select="VatReportData/VatLocalSalesReportList/VatLocalSalesReportData">
						<tr>
							<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
								<xsl:value-of select="position()"/>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								Taxable TO of Sales at <b><xsl:value-of select="m_nTax"/>%</b>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
								<xsl:value-of select="m_nTaxableAmount" />
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								O/p tax Payable(rel. to BNo.<xsl:value-of select="position()"/>)
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
								<xsl:value-of select="m_nTaxAmount" />
							</td>
						</tr>
					</xsl:for-each> 
					<tr>
						<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total</b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
							<xsl:value-of select="format-number(sum(VatReportData/VatLocalSalesReportList/VatLocalSalesReportData/m_nTaxableAmount),'##0.00')" /> 
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total O/p tax Payable</b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
							<xsl:value-of select="format-number(sum(VatReportData/VatLocalSalesReportList/VatLocalSalesReportData/m_nTaxAmount),'##0.00')" />
						</td>
					</tr>
					<tr>
						<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="vertical-align:top;height:20px">
						</td>
					</tr>
					<tr>
						<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="vertical-align:top">
							Details of Interstate Sales(ISS) and CST Payable
						</td>
					</tr>
					<xsl:for-each select="VatReportData/VatInterStateSalesReportList/VatInterStateSalesReport">
						<tr>
							<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
								<xsl:value-of select="position()"/>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								<xsl:choose>
						          <xsl:when test="m_bIsCFormProvided = 'true'">
						            Taxable TO of ISS against C Forms at <b><xsl:value-of select="m_nTax"/>%</b>
						          </xsl:when>
						          <xsl:otherwise>
						            Taxable TO of ISS without C Forms at <b><xsl:value-of select="m_nTax"/>%</b>
						          </xsl:otherwise>
						        </xsl:choose>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
								<xsl:value-of select="m_nTaxableAmount" />
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								O/p tax Payable(rel. to BNo.<xsl:value-of select="position()"/>)
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
								<xsl:value-of select="m_nTaxAmount" />
							</td>
						</tr>
					</xsl:for-each> 
					<tr>
						<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total</b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
							<xsl:value-of select="format-number(sum(VatReportData/VatInterStateSalesReportList/VatInterStateSalesReport/m_nTaxableAmount),'##0.00')" /> 
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total O/p tax Payable</b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
							<xsl:value-of select="format-number(sum(VatReportData/VatInterStateSalesReportList/VatInterStateSalesReport/m_nTaxAmount),'##0.00')" />
						</td>
					</tr>
					<tr>
						<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="vertical-align:top;height:20px">
						</td>
					</tr>
					<tr>
						<td class="print_fieldTableHeading print_tdBorder" colspan="5" style="vertical-align:top">
							Details of Purchases and Input Tax
						</td>
					</tr>
					<xsl:for-each select="VatReportData/VatPurchaseReportList/VatPurchaseReportData">
						<tr>
							<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
								<xsl:value-of select="position()"/>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								Net value of purchases at <b><xsl:value-of select="m_nTax"/>%</b>
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
								<xsl:value-of select="m_nTaxableAmount" />
							</td>
							<td class="print_fieldTableData print_tdBorder" style="width:35%">
								Input tax(rel. to BNo.<xsl:value-of select="position()"/>)
							</td>
							<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
								<xsl:value-of select="m_nTaxAmount" />
							</td>
						</tr>
					</xsl:for-each> 
					<tr>
						<td class="print_fieldTableData print_tdBorder" style="width:5%; text-align:center">
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total</b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:13%">
							<xsl:value-of select="format-number(sum(VatReportData/VatPurchaseReportList/VatPurchaseReportData/m_nTaxableAmount),'##0.00')" /> 
						</td>
						<td class="print_fieldTableData print_tdBorder" style="width:35%">
							<b>Total input tax </b>
						</td>
						<td class="print_fieldTableData print_tdBorder" style="text-align:right; width:12%">
							<xsl:value-of select="format-number(sum(VatReportData/VatPurchaseReportList/VatPurchaseReportData/m_nTaxAmount),'##0.00')" />
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>