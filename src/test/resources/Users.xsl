<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Title</title>
            </head>
            <body>
                <h1>Users list</h1>
                <table style="width:50%;" border="1">
                    <tr>
                        <th>fullName</th>
                        <th>email</th>
                    </tr>
                    <xsl:for-each select="/*[name()='Payload']/*[name()='Users']/*[name()='User']">
                        <tr>
                            <td><xsl:value-of select="*[name()='fullName']"/></td>
                            <td><xsl:value-of select="@email"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>