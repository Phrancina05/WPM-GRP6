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

/**
 * RegisterServlet.java
 * Handles farmer/admin registration.
 * Checks if email already exists, then inserts new user into the database.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Read all fields including role
        String fullName        = request.getParameter("full_name");
        String email           = request.getParameter("email");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String role            = request.getParameter("role");

        // Default to farmer if role is missing
        if (role == null || role.isEmpty()) role = "farmer";

        try (PrintWriter out = response.getWriter()) {

            // Page head
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Register - Livestock System</title>");
            out.println("<style>");
            out.println("* { box-sizing: border-box; margin: 0; padding: 0; }");
            out.println("body { font-family: Arial, sans-serif; background: #f4f4f4; }");
            out.println(".navbar { background: #2c7a2c; padding: 16px 30px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }");
            out.println(".navbar .brand { color: white; font-size: 18px; font-weight: bold; }");
            out.println(".navbar a { color: white; text-decoration: none; margin-left: 20px; font-size: 14px; }");
            out.println(".navbar a:hover { text-decoration: underline; }");
            out.println(".page { display: flex; justify-content: center; align-items: center; min-height: calc(100vh - 118px); padding: 40px 20px; background: linear-gradient(135deg, #e8f5e9, #c8e6c9); }");
            out.println(".box { background: white; padding: 50px 44px; border-radius: 12px; text-align: center; box-shadow: 0 8px 30px rgba(0,0,0,0.12); max-width: 420px; width: 100%; }");
            out.println(".box-success { border-top: 5px solid #2c7a2c; }");
            out.println(".box-error { border-top: 5px solid #cc0000; }");
            out.println(".icon { font-size: 56px; margin-bottom: 16px; }");
            out.println("h2 { font-size: 22px; margin-bottom: 10px; }");
            out.println(".success { color: #1b5e20; }");
            out.println(".error { color: #cc0000; }");
            out.println("p { color: #555; font-size: 15px; line-height: 1.7; margin-bottom: 10px; }");
            out.println(".role-badge { display: inline-block; background: #e8f5e9; color: #2c7a2c; font-size: 13px; font-weight: bold; padding: 6px 16px; border-radius: 20px; margin-bottom: 20px; }");
            out.println(".role-badge.admin { background: #e8eaf6; color: #3949ab; }");
            out.println(".divider { display: flex; align-items: center; gap: 10px; margin: 20px 0; }");
            out.println(".divider hr { flex: 1; border: none; border-top: 1px solid #e0e0e0; }");
            out.println(".divider span { font-size: 12px; color: #aaa; }");
            out.println(".btn { display: inline-block; padding: 12px 32px; background: #2c7a2c; color: white; text-decoration: none; border-radius: 6px; font-size: 15px; font-weight: bold; transition: background 0.2s; margin: 6px; }");
            out.println(".btn:hover { background: #1e5c1e; }");
            out.println(".btn-red { background: #cc0000; }");
            out.println(".btn-red:hover { background: #a00000; }");
            out.println(".back { display: block; margin-top: 14px; font-size: 13px; color: #2c7a2c; text-decoration: none; }");
            out.println(".back:hover { text-decoration: underline; }");
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

            // Check passwords match
            if (!password.equals(confirmPassword)) {
                out.println("<div class='box box-error'>");
                out.println("<div class='icon'>❌</div>");
                out.println("<h2 class='error'>Passwords Do Not Match!</h2>");
                out.println("<p>The passwords you entered do not match.<br/>Please go back and try again.</p>");
                out.println("<a href='register.html' class='btn btn-red'>Go Back</a>");
                out.println("<a href='index.html' class='back'>← Back to Home</a>");
                out.println("</div>");

            } else {
                try {
                    try (Connection conn = DBConnection.getConnection()) {

                        // Check if email already exists
                        String checkSql = "SELECT * FROM users WHERE email = ?";
                        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                            checkPs.setString(1, email);
                            try (ResultSet rs = checkPs.executeQuery()) {

                                if (rs.next()) {
                                    // Email already registered
                                    out.println("<div class='box box-error'>");
                                    out.println("<div class='icon'>⚠️</div>");
                                    out.println("<h2 class='error'>Email Already Registered!</h2>");
                                    out.println("<p>An account with this email already exists.<br/>Please log in or use a different email.</p>");
                                    out.println("<a href='login.html' class='btn'>Login Instead</a>");
                                    out.println("<a href='register.html' class='btn btn-red'>Try Again</a>");
                                    out.println("<a href='index.html' class='back'>← Back to Home</a>");
                                    out.println("</div>");

                                } else {
                                    // Insert new user with role
                                    String sql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                                        ps.setString(1, fullName);
                                        ps.setString(2, email);
                                        ps.setString(3, password);
                                        ps.setString(4, role);
                                        ps.executeUpdate();

                                        // Role display label
                                        String roleLabel = role.equals("admin") ? "Administrator" : "Farm Worker";
                                        String roleBadgeClass = role.equals("admin") ? "role-badge admin" : "role-badge";

                                        // Success
                                        out.println("<div class='box box-success'>");
                                        out.println("<div class='icon'>✅</div>");
                                        out.println("<h2 class='success'>Registration Successful!</h2>");
                                        out.println("<span class='" + roleBadgeClass + "'>" + roleLabel + "</span>");
                                        out.println("<p>Welcome, <strong>" + fullName + "</strong>!<br/>");
                                        out.println("Your account has been created successfully.<br/>");
                                        out.println("Please log in to access your dashboard.</p>");
                                        out.println("<div class='divider'><hr/><span>or</span><hr/></div>");
                                        out.println("<a href='login.html' class='btn'>Login Now →</a>");
                                        out.println("<a href='index.html' class='back'>← Back to Home</a>");
                                        out.println("</div>");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // Database error
                    out.println("<div class='box box-error'>");
                    out.println("<div class='icon'>⚠️</div>");
                    out.println("<h2 class='error'>Something Went Wrong!</h2>");
                    out.println("<p>A database error occurred:<br/><small>" + e.getMessage() + "</small></p>");
                    out.println("<a href='register.html' class='btn btn-red'>Try Again</a>");
                    out.println("<a href='index.html' class='back'>← Back to Home</a>");
                    out.println("</div>");
                }
            }

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
    }
}