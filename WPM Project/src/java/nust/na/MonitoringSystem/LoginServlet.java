/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nust.na.MonitoringSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginServlet.java
 * Handles farmer login authentication.
 * Checks email and password against the database.
 * Stores farmer details in session on success.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        try (PrintWriter out = response.getWriter()) {
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Login successful — store farmer details in session
                    HttpSession session = request.getSession();
                    session.setAttribute("farmer_id",    rs.getInt("id"));
                    session.setAttribute("farmer_name",  rs.getString("full_name"));
                    session.setAttribute("farmer_email", rs.getString("email"));
                    session.setAttribute("farmer_role",  rs.getString("role"));

                    rs.close();
                    ps.close();
                    conn.close();

                    // Redirect to dashboard
                    response.sendRedirect("DashboardServlet");

                } else {
                    rs.close();
                    ps.close();
                    conn.close();

                    // Login failed — show styled error page
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<meta charset='UTF-8'>");
                    out.println("<title>Login Failed - Livestock System</title>");
                    out.println("<style>");
                    out.println("* { box-sizing: border-box; margin: 0; padding: 0; }");
                    out.println("body { font-family: Arial, sans-serif; background: #f4f4f4; }");
                    out.println(".navbar { background: #2c7a2c; padding: 16px 30px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }");
                    out.println(".navbar .brand { color: white; font-size: 18px; font-weight: bold; }");
                    out.println(".navbar a { color: white; text-decoration: none; margin-left: 20px; font-size: 14px; }");
                    out.println(".navbar a:hover { text-decoration: underline; }");
                    out.println(".page { display: flex; justify-content: center; align-items: center; min-height: calc(100vh - 118px); padding: 40px 20px; background: linear-gradient(135deg, #e8f5e9, #c8e6c9); }");
                    out.println(".box { background: white; padding: 50px 44px; border-radius: 12px; text-align: center; box-shadow: 0 8px 30px rgba(0,0,0,0.12); border-top: 5px solid #cc0000; max-width: 420px; width: 100%; }");
                    out.println(".icon { font-size: 56px; margin-bottom: 16px; }");
                    out.println("h2 { color: #cc0000; font-size: 22px; margin-bottom: 10px; }");
                    out.println("p { color: #555; font-size: 15px; line-height: 1.7; margin-bottom: 24px; }");
                    out.println(".divider { display: flex; align-items: center; gap: 10px; margin: 20px 0; }");
                    out.println(".divider hr { flex: 1; border: none; border-top: 1px solid #e0e0e0; }");
                    out.println(".divider span { font-size: 12px; color: #aaa; }");
                    out.println(".btn { display: inline-block; padding: 12px 32px; background: #2c7a2c; color: white; text-decoration: none; border-radius: 6px; font-size: 15px; font-weight: bold; transition: background 0.2s; margin: 6px; }");
                    out.println(".btn:hover { background: #1e5c1e; }");
                    out.println(".btn-outline { background: transparent; color: #2c7a2c; border: 2px solid #2c7a2c; padding: 11px 30px; border-radius: 6px; font-size: 15px; font-weight: bold; text-decoration: none; display: inline-block; margin: 6px; transition: background 0.2s; }");
                    out.println(".btn-outline:hover { background: #e8f5e9; }");
                    out.println("footer { background: #1b5e20; color: rgba(255,255,255,0.6); text-align: center; padding: 20px; font-size: 13px; }");
                    out.println("footer a { color: #a5d6a7; text-decoration: none; margin: 0 10px; }");
                    out.println("footer a:hover { text-decoration: underline; }");
                    out.println(".footer-links { margin-bottom: 8px; }");
                    out.println("</style>");
                    out.println("</head>");
                    out.println("<body>");

                    // Navbar
                    out.println("<div class='navbar'>");
                    out.println("<span class='brand'>🐄 Livestock Monitoring System</span>");
                    out.println("<span>");
                    out.println("<a href='index.html'>Home</a>");
                    out.println("<a href='login.html'>Login</a>");
                    out.println("<a href='register.html'>Register</a>");
                    out.println("<a href='contact.html'>Contact Us</a>");
                    out.println("</span>");
                    out.println("</div>");

                    // Error box
                    out.println("<div class='page'>");
                    out.println("<div class='box'>");
                    out.println("<div class='icon'>❌</div>");
                    out.println("<h2>Login Failed!</h2>");
                    out.println("<p>The email or password you entered is incorrect.<br/>Please check your details and try again.</p>");
                    out.println("<div class='divider'><hr/><span>or</span><hr/></div>");
                    out.println("<a href='login.html' class='btn'>Try Again</a>");
                    out.println("<a href='register.html' class='btn-outline'>Create Account</a>");
                    out.println("</div>");
                    out.println("</div>");

                    // Footer
                    out.println("<footer>");
                    out.println("<div class='footer-links'>");
                    out.println("<a href='index.html'>Home</a>");
                    out.println("<a href='login.html'>Login</a>");
                    out.println("<a href='register.html'>Register</a>");
                    out.println("<a href='contact.html'>Contact Us</a>");
                    out.println("</div>");
                    out.println("<p>© 2026 Livestock Monitoring System | NUST</p>");
                    out.println("</footer>");

                    out.println("</body>");
                    out.println("</html>");
                }

            } catch (Exception e) {
                out.println("<!DOCTYPE html><html><body>");
                out.println("<p style='color:red;font-family:Arial;padding:20px;'>Error: " + e.getMessage() + "</p>");
                out.println("<a href='login.html' style='font-family:Arial;color:#2c7a2c;padding:20px;display:block;'>← Back to Login</a>");
                out.println("</body></html>");
            }
        }
    }
}