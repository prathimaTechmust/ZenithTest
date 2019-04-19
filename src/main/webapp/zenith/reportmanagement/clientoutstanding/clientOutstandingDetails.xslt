<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(root/ClientOutstandingList/ClientInvoiceDetailsData/m_nBalanceAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_formHeading" style="color:#000; background:#efefef;" colspan="2">Client Outstanding Details
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="print_tableBorder trademust">
								<tr>
									<td class="trademust print_tdBorder" colspan="2">
										<div style="min-height:450px">
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
																	style="width:20%">
																	Date
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:15%">
																	InvoiceNo
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Invoice Amount
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Receipt Amount
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Balance Amount
																</td>
															</tr>
															<xsl:for-each
																select="root/ClientOutstandingList/ClientInvoiceDetailsData">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:center">
																		<xsl:value-of select="position()" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="m_strDate" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="m_strInvoiceNumber" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(m_nInvoiceAmount, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(m_nReceiptAmount, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(m_nBalanceAmount, '##,##,##,###.00')" />
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
																<td class="print_fieldTableHeading print_tdBorder"
																	style="text-align:right; width:80%">
																	Total Balance
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="text-align:right">
																	<span class="rupeeSign" style="font-weight: normal;">R </span>
																	<xsl:value-of
																		select="format-number($subTotal,'##,##,##,##0.00')" />
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