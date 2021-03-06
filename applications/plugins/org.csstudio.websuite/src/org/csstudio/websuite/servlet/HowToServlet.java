
/*
 * Copyright (c) 2012 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 * $Id: DesyKrykCodeTemplates.xml,v 1.7 2010/04/20 11:43:22 bknerr Exp $
 */

package org.csstudio.websuite.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mmoeller
 * @version 1.0
 * @since 04.07.2012
 */
public class HowToServlet extends HttpServlet {

    private static final long serialVersionUID = 6126473431230489993L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.createPage(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.createPage(response);
    }
    
    private void createPage(HttpServletResponse response)
            throws IOException {
        
        StringBuilder page = null;
        
        page = new StringBuilder();
        page.append("<html>\n");
        page.append("<head>\n");
        page.append(" <title>HowTo </title>\n");
        page.append(" <link rel=\"stylesheet\" type=\"text/css\" href=\"/style/webviewer.css\">\n");
        page.append(" <meta http-equiv=\"Pragma\" content=\"no-cache\">\n");
        page.append("</head>\n");
        page.append("<body>\n");
        page.append("<center>\n");

        page.append("<h1>HowTo Search Page</h1>");
        
        // Content
        page.append("<form action=\"./HowToSearchHandler\" method=\"get\">\n");
        page.append("<table>\n");

        page.append("<tr>\n");
        page.append("<th class=\"howto_search\">Suchen nach:</th>\n");
        page.append("<td class=\"howto_search\"><input type=\"text\" name=\"keywords\"></td>\n");
        page.append("</tr>\n");
        
        page.append("<tr>\n");
        page.append("<th class=\"howto_search\" align=\"left\">Suchmodus:</th>\n");
        page.append("<td class=\"howto_search\">\n");
        page.append("<input type=\"radio\" name=\"search_mode\" value=\"OR\" checked=\"checked\">OR<br>\n");
        page.append("<input type=\"radio\" name=\"search_mode\" value=\"AND\">AND\n");
        page.append("</td>\n");
        page.append("</tr>\n");

        page.append("<tr>\n");
        page.append("<td colspan=\"2\">&nbsp;</td>\n");
        page.append("</tr>\n");
        
        page.append("<tr align=\"center\">\n");
        page.append("<td colspan=\"2\"><input type=\"submit\" value=\"Suchen\"></td>\n");

        page.append("</table>\n");
        page.append("</form>\n");
        
        page.append("</center>\n");
        page.append("</body>\n</html>\n");

        response.getOutputStream().print(page.toString());
        response.getOutputStream().flush();
    }
}
