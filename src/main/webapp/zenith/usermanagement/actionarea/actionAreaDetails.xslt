<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">Action Area
											Details
								</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td class="xslt_fieldHeading">Action Area Name :</td>
									<td class="xslt_fieldData" id="m_strActionAreaName">
										<xsl:value-of select="ActionAreaData/m_strActionAreaName" />
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