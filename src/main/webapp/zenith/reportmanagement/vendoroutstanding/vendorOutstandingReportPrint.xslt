<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="OpeningBalance"
			select="sum(root/VendorOutstandingReportDataList/VendorOutstandingReportData/m_nOpeningBalance)" />
			<xsl:variable name="Invoiced"
			select="sum(root/VendorOutstandingReportDataList/VendorOutstandingReportData/m_nInvoicedAmount)" />
			<xsl:variable name="Paid"
			select="sum(root/VendorOutstandingReportDataList/VendorOutstandingReportData/m_nPaymentAmount)" />
			<xsl:variable name="Outstanding"
			select="sum(root/VendorOutstandingReportDataList/VendorOutstandingReportData/m_nOutstandingAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading">
							Vendor Outstanding Report 
          				</td>
					</tr>
					<tr>
						<td class="trademust" style="width:100%; vertical-align: top">
							<table class="trademust print_tableBorder">
								<tr>
									<td class="trademust" colspan="5">
										<div style="height:1025px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:35%;text-align:center">
														Vendor Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder"  style="width:15%;text-align:center">
														Opening Balance
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%;text-align:center">
														Invoiced
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%;text-align:center">
														Payments
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:15%;text-align:center">
														Outstanding
													</td>
												</tr>
												<xsl:for-each select="root/VendorOutstandingReportDataList/VendorOutstandingReportData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<b><xsl:value-of select="m_strCompanyName" /></b> <br></br><xsl:value-of select="m_strCity" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nOpeningBalance, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nInvoicedAmount, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right"> 
															<xsl:value-of select="format-number(m_nPaymentAmount, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right"> 
															<xsl:value-of select="format-number(m_nOutstandingAmount, '##,##,##,###.00')" />
														</td>
													</tr>
												</xsl:for-each> 
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:40%">
										Grand Total
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:15%">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($OpeningBalance,'##,##,##,###.00')"/>
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:15%">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($Invoiced,'##,##,##,###.00')"/>
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:15%">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($Paid,'##,##,##,###.00')"/>
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:15%">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($Outstanding,'##,##,##,###.00')"/>
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