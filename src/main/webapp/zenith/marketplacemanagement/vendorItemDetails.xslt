<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust" align="center">
							<a href="javascript:vendorItemList_setPreview ({ItemData/m_nItemId})">
								<img id="ItemListDetails_img_itemImage{ItemData/m_nItemId}"
									class="itemDetailImage" />
							</a>
							<br></br>
							<br></br>
							<b><xsl:value-of select="ItemData/m_strItemName" /></b>
						</td>
					</tr>
					<tr>
						<td class="trademust">
						<hr></hr>
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:50%">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">
													Article Number:
											</td>
												<td class="xslt_fieldData" id="m_strArticleNumber">
													<xsl:value-of select="ItemData/m_strArticleNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Item Name:
											</td>
												<td class="xslt_fieldData" id="m_strItemName">
													<xsl:value-of select="ItemData/m_strItemName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Brand:
											</td>
												<td class="xslt_fieldData" id="m_strBrand">
													<xsl:value-of select="ItemData/m_strBrand" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Detail:
											</td>
												<td class="xslt_fieldData" id="m_strDetail">
													<xsl:value-of select="ItemData/m_strDetail" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Unit:
											</td>
												<td class="xslt_fieldData" id="m_strArticleNumber">
													<xsl:value-of select="ItemData/m_strUnit" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Location Code:
											</td>
												<td class="xslt_fieldData" id="m_strArticleNumber">
													<xsl:value-of select="ItemData/m_strLocationCode" />
												</td>
											</tr>

										</table>
									</td>
									<td class="trademust" style="width:50%">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">
													Selling Price:
											</td>
												<td class="xslt_fieldData" id="m_nSellingPrice">
													<span class="rupeeSign">R </span>
													<xsl:value-of
														select="format-number(ItemData/m_nSellingPrice,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Current Stock :
											</td>
												<td class="xslt_fieldData" id="m_nReceived ">
													<xsl:value-of
														select="format-number(ItemData/m_nOpeningStock + ItemData/m_nReceived - ItemData/m_nIssued,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Reorder Level:
											</td>
												<td class="xslt_fieldData" id="m_nReorderLevel">
													<xsl:value-of
														select="format-number(ItemData/m_nReorderLevel,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Applicable Tax:
											</td>
												<td class="xslt_fieldData" id="m_strApplicableTaxName">
													<xsl:value-of
														select="ItemData/m_oApplicableTax/Applicabletax/m_strApplicableTaxName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Tax With C Form:
											</td>
												<td class="xslt_fieldData" id="m_strApplicableTaxName">
													<xsl:value-of
														select="ItemData/m_oTaxWithCForm/Applicabletax/m_strApplicableTaxName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Group
													Names:</td>
												<td class="xslt_fieldData">
													<xsl:for-each select="ItemData/m_oItemGroups">
														<xsl:value-of select="m_strGroupName" />
														<xsl:if test="position()!=last()">
															<xsl:text>, </xsl:text>
														</xsl:if>
													</xsl:for-each>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="vertical-align:top;width:10%;" colspan="2" align="right">
							<hr></hr>
							<xsl:if test="ItemData/m_bPublishOnline=1">
								<button class="trademust"
									id="vendorList_button_verify{ItemData/m_nItemId}" type="button"
									onclick="vendorItemList_blockItem ({ItemData/m_nItemId})">Block
								</button>
							</xsl:if>
							<xsl:if test="ItemData/m_bPublishOnline=0">
								<button class="trademust"
									id="vendorList_button_verify{ItemData/m_nItemId}" type="button"
									onclick="vendorItemList_verifyItem ({ItemData/m_nItemId})">Verify
										</button>
							</xsl:if>
							<button class="trademust"
								id="vendorList_button_verify{ItemData/m_nItemId}" type="button"
								onclick="vendorItemList_cancel ()">Cancel
										</button>
						</td>

					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>