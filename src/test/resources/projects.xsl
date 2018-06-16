<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/*[name()='Payload']/*[name()='Projects']/*[name()='Project']">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Projects</title>
            </head>
            <body>
                <h1>Project name: <xsl:value-of select="*[name()='name']"/></h1>
            </body>
            <h3>Groups:</h3>
            <xsl:for-each select="*[name()='Groups']/*[name()='Group']">
                <p><xsl:value-of select="text()"/> => <xsl:value-of select="@type"/></p>
            </xsl:for-each>
        </html>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>