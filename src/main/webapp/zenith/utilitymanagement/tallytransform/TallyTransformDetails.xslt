<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeader" colspan="2">Tally Transform Details</td>
						<td class="trademust" align="right" colspan="2">
							<table>
								<tr>
									<td>
										<img src="images/edit_database_24.png" width="20" title="Edit"
											align="center" id="editImageId" onClick="tallyTransformList_edit({TallyTransformData/m_nTallyTranformId})" />
									</td>
									<td>
										<img src="images/delete.png" width="20" title="Delete"
											align="center" id="deleteImageId" onClick="tallyTransformList_listDetail_delete ()" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Key :</td>
												<td class="xslt_fieldData" id="m_strKey">
													<xsl:value-of select="TallyTransformData/m_strKey" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Tax Name :</td>
												<td class="xslt_fieldData" id="m_strTaxName">
													<xsl:value-of select="TallyTransformData/m_strTaxName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Percentage :</td>
												<td class="xslt_fieldData" id="m_nPercentage">
													<xsl:value-of select="TallyTransformData/m_nPercentage" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Tally Key :</td>
												<td class="xslt_fieldData" id="m_strTallyKey">
													<xsl:value-of select="TallyTransformData/m_strTallyKey" />
												</td>
											</tr>
										</table>
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