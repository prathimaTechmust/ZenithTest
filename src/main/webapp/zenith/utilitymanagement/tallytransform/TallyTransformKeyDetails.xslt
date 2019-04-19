<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeader" colspan="2">Tally Transform Key Details</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="xslt_fieldHeading">Key :</td>
									<td class="xslt_fieldData" id="m_strKey">
										<xsl:value-of select="TallyTransformKeyData/m_strKey" />
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