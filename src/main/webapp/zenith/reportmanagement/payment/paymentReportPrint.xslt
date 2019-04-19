<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="grandTotal"
			select="sum(root/PaymentReportDataList/PaymentReportData/m_nAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" colspan="2">
							Payment Report [From:<xsl:value-of select="root/m_strFromDate"/> - To:<xsl:value-of select="root/m_strToDate"/>]
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
										Vendor Name : <xsl:value-of select="root/m_strVendorName"/>
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
										<div style="height:1025px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:75%">
														Vendor Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:20%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="root/PaymentReportDataList/PaymentReportData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strVendorName" />
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
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($grandTotal,'##,##,##,###.00')"/>
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