<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(PaymentData/m_oPurchasePayementSet/PurchasePaymentData/PurchaseData/m_nBalanceAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_formHeading" style="color:#000; background:#efefef;" colspan="2">Payment Details
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:100%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Payment Number :</td>
												<td class="xslt_fieldData" id="m_strPaymentNumber">
													<xsl:value-of select="PaymentData/m_strPaymentNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Vendor Name :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of
														select="PaymentData/VendorData/m_strVendorCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Mode :</td>
												<td class="xslt_fieldData" id="m_strModeName">
													<xsl:value-of select="PaymentData/TransactionMode/m_strModeName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="PaymentData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Amount :</td>
												<td class="xslt_fieldData" id="m_nAmount">
													<span class="rupeeSign">R </span>
													<xsl:value-of select="PaymentData/m_nAmount" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Details :</td>
												<td class="xslt_fieldData" id="m_strDetails">
													<xsl:value-of select="PaymentData/m_strDetails" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
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
																	INV#
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:15%">
																	Date
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Invoice Amount
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Total Paid
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Balance
																</td>
															</tr>
															<xsl:for-each
																select="PaymentData/m_oPurchasePayementSet/PurchasePaymentData">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:center">
																		<xsl:value-of select="position()" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="PurchaseData/m_strInvoiceNo" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="PurchaseData/m_strDate" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(PurchaseData/m_nPaymentAmount + PurchaseData/m_nBalanceAmount, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(PurchaseData/m_nPaymentAmount, '##,##,##,###.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<xsl:value-of
																			select="format-number(PurchaseData/m_nBalanceAmount, '##,##,##,###.00')" />
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
																		select="format-number($subTotal,'##,##,##,###.00')" />
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