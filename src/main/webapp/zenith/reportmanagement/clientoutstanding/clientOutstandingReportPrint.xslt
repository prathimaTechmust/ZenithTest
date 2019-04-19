<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="grandTotal"
			select="sum(root/ClientOutstandingList/ClientOutstandingReportData/m_nOutstandingAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" colspan="2">
							Client Outstanding Report
          				</td>
					</tr>
					<tr>
						<td class="trademust" style="width:70%; vertical-align: top"
							colspan="2">
							<table class="trademust print_tableBorder">
								<tr>
									<td colspan="2">
										<div style="min-height:1025px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:35%">
														Client Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:15%">
														Opening Balance
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:15%">
														Invoiced Amount
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:15%">
														Receipt Amount
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:15%">
														Outstanding Amount
													</td>
												</tr>
												<xsl:for-each
													select="root/ClientOutstandingList/ClientOutstandingReportData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<b><xsl:value-of select="m_strCompanyName" /></b><br></br><xsl:value-of select="m_strCity" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of
																select="format-number(m_nOpeningBalance, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of
																select="format-number(m_nInvoiced, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of
																select="format-number(m_nReceipts, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of
																select="format-number(m_nOutstandingAmount, '##,##,##,##0.00')" />
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
										<span class="rupeeSign" style="font-weight: normal;">R </span>
										<xsl:value-of select="format-number($grandTotal,'##,##,##,##0.00')" />
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