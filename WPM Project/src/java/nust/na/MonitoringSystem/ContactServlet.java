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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ContactServlet.java
 * Saves contact form messages to the database.
 */
@WebServlet(name = "ContactServlet", urlPatterns = {"/ContactServlet"})
public class ContactServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Read all four fields from the contact form
        String name    = request.getParameter("name");
        String email   = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        try (PrintWriter out = response.getWriter()) {

            // Page head
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Message Sent - Livestock System</title>");
            out.println("<style>");
            out.println("* { box-sizing: border-box; margin: 0; padding: 0; }");
            out.println("body { font-family: Arial, sans-serif; background: #f4f4f4; }");
            out.println(".navbar { background: #2c7a2c; padding: 16px 30px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }");
            out.println(".navbar .brand { color: white; font-size: 18px; font-weight: bold; }");
            out.println(".navbar a { color: white; text-decoration: none; margin-left: 20px; font-size: 14px; }");
            out.println(".navbar a:hover { text-decoration: underline; }");
            out.println(".page { display: flex; justify-content: center; align-items: center; min-height: calc(100vh - 118px); padding: 40px 20px; background: linear-gradient(135deg, #e8f5e9, #c8e6c9); }");
            out.println(".box { background: white; padding: 50px 44px; border-radius: 12px; text-align: center; box-shadow: 0 8px 30px rgba(0,0,0,0.12); border-top: 5px solid #2c7a2c; max-width: 460px; width: 100%; }");
            out.println(".box-error { border-top: 5px solid #cc0000; }");
            out.println(".icon { font-size: 56px; margin-bottom: 16px; }");
            out.println("h2 { font-size: 22px; margin-bottom: 10px; }");
            out.println(".success { color: #1b5e20; }");
            out.println(".error { color: #cc0000; }");
            out.println("p { color: #555; font-size: 15px; line-height: 1.7; margin-bottom: 10px; }");
            out.println(".detail { background: #f4f4f4; border-radius: 8px; padding: 14px 18px; text-align: left; margin: 16px 0 24px; font-size: 13px; color: #444; line-height: 2; }");
            out.println(".detail strong { color: #1b5e20; }");
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

            out.println("<div class='page'>");

            try {
                Connection conn = DBConnection.getConnection();

                // Now inserting all four fields including subject
                String sql = "INSERT INTO contact_messages (name, email, subject, message) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, subject);
                ps.setString(4, message);
                ps.executeUpdate();
                ps.close();
                conn.close();

                // Success page
                out.println("<div class='box'>");
                out.println("<div class='icon'>✅</div>");
                out.println("<h2 class='success'>Message Sent Successfully!</h2>");
                out.println("<p>Thank you, <strong>" + name + "</strong>. We have received your message and will get back to you as soon as possible.</p>");

                // Show message summary
                out.println("<div class='detail'>");
                out.println("<strong>Name:</strong> " + name + "<br/>");
                out.println("<strong>Email:</strong> " + email + "<br/>");
                out.println("<strong>Subject:</strong> " + subject + "<br/>");
                out.println("<strong>Message:</strong> " + message);
                out.println("</div>");

                out.println("<div class='divider'><hr/><span>or</span><hr/></div>");
                out.println("<a href='index.html' class='btn'>Back to Home</a>");
                out.println("<a href='contact.html' class='btn-outline'>Send Another</a>");
                out.println("</div>");

            } catch (Exception e) {
                // Error page
                out.println("<div class='box box-error'>");
                out.println("<div class='icon'>❌</div>");
                out.println("<h2 class='error'>Something Went Wrong!</h2>");
                out.println("<p>Your message could not be sent.<br/>");
                out.println("<small>" + e.getMessage() + "</small></p>");
                out.println("<a href='contact.html' class='btn'>Try Again</a>");
                out.println("<a href='index.html' class='btn-outline'>Back to Home</a>");
                out.println("</div>");
            }

            out.println("</div>");

            // Footer
            out.println("<footer>");
            out.println("</div>");
            out.println("<p>© 2026 Livestock Monitoring System | NUST</p>");
            out.println("</footer>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}