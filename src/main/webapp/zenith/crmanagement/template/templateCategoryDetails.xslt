<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeader" >Template Category Details
						</td>
					</tr>
					<tr>
					<td class="trademust">
					<table class="trademust">
					<tr>
						<td class="xslt_fieldHeading">Category Name :</td>
						<td class="xslt_fieldData" id="m_strCategoryName">
							<xsl:value-of select="TemplateCategoryData/m_strCategoryName" />
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