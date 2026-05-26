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
 * DashboardServlet.java
 * Role-based dashboard:
 * - Admin sees ALL animals from ALL farmers
 * - Farmer sees ONLY their own animals
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/DashboardServlet"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        try (PrintWriter out = response.getWriter()) {

            // Check if user is logged in
            if (session == null || session.getAttribute("farmer_id") == null) {
                response.sendRedirect("login.html");
                return;
            }

            String farmerName = (String) session.getAttribute("farmer_name");
            int farmerId      = (int) session.getAttribute("farmer_id");
            String role       = (String) session.getAttribute("farmer_role");

            // Default to farmer if role is null
            if (role == null) role = "farmer";
            boolean isAdmin = role.equals("admin");

            // Page head
            out.println("<!DOCTYPE html><html><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Dashboard - Livestock Monitoring System</title>");
            out.println("<style>");
            out.println("*{box-sizing:border-box;margin:0;padding:0;}");
            out.println("body{font-family:Arial;background:#f4f4f4;color:#333;}");
            out.println(".navbar{background:#2c7a2c;padding:16px 30px;display:flex;align-items:center;justify-content:space-between;box-shadow:0 2px 8px rgba(0,0,0,0.2);}");
            out.println(".navbar .brand{color:white;font-size:18px;font-weight:bold;}");
            out.println(".navbar .nav-right{display:flex;align-items:center;gap:6px;}");
            out.println(".navbar a{color:white;text-decoration:none;font-size:14px;padding:7px 14px;border-radius:5px;transition:background 0.2s;}");
            out.println(".navbar a:hover{background:rgba(255,255,255,0.15);}");
            out.println(".navbar .logout-btn{background:rgba(255,255,255,0.15);border:1px solid rgba(255,255,255,0.3);}");
            out.println(".navbar .logout-btn:hover{background:rgba(255,255,255,0.25);}");
            out.println(".welcome-badge{background:rgba(255,255,255,0.15);color:white;padding:6px 14px;border-radius:20px;font-size:13px;margin-right:4px;}");
            out.println(".role-badge{padding:4px 10px;border-radius:20px;font-size:11px;font-weight:bold;margin-right:8px;}");
            out.println(".role-admin{background:#3949ab;color:white;}");
            out.println(".role-farmer{background:#388e3c;color:white;}");
            out.println(".welcome-banner{padding:28px 30px;color:white;}");
            out.println(".welcome-banner.admin-banner{background:linear-gradient(135deg,#1a237e,#283593);}");
            out.println(".welcome-banner.farmer-banner{background:linear-gradient(135deg,#2c7a2c,#1b5e20);}");
            out.println(".welcome-banner h2{font-size:22px;margin-bottom:4px;}");
            out.println(".welcome-banner p{font-size:14px;color:rgba(255,255,255,0.75);}");
            out.println(".summary{display:flex;gap:12px;padding:20px 30px;flex-wrap:wrap;}");
            out.println(".summary-card{background:white;border-radius:8px;padding:14px 18px;flex:1;min-width:100px;max-width:160px;box-shadow:0 2px 8px rgba(0,0,0,0.07);border-left:4px solid #ccc;text-align:center;}");
            out.println(".summary-card.total{border-left-color:#2c7a2c;}");
            out.println(".summary-card.healthy{border-left-color:#4caf50;}");
            out.println(".summary-card.sick{border-left-color:#cc0000;}");
            out.println(".summary-card.treatment{border-left-color:#f0a500;}");
            out.println(".summary-card.quarantine{border-left-color:#6a0dad;}");
            out.println(".summary-card.recovering{border-left-color:#00838f;}");
            out.println(".summary-card.deceased{border-left-color:#555;}");
            out.println(".summary-card .num{font-size:26px;font-weight:bold;line-height:1;color:#1b5e20;}");
            out.println(".summary-card.healthy .num{color:#4caf50;}");
            out.println(".summary-card.sick .num{color:#cc0000;}");
            out.println(".summary-card.treatment .num{color:#f0a500;}");
            out.println(".summary-card.quarantine .num{color:#6a0dad;}");
            out.println(".summary-card.recovering .num{color:#00838f;}");
            out.println(".summary-card.deceased .num{color:#555;}");
            out.println(".summary-card .lbl{font-size:11px;color:#888;text-transform:uppercase;letter-spacing:0.05em;margin-top:5px;}");
            out.println(".container{padding:0 30px 40px;}");
            out.println(".table-header{display:flex;align-items:center;justify-content:space-between;margin-bottom:16px;flex-wrap:wrap;gap:10px;}");
            out.println(".table-header h3{font-size:18px;color:#1b5e20;}");
            out.println(".btn-add{background:#2c7a2c;color:white;padding:10px 22px;border-radius:6px;text-decoration:none;font-size:14px;font-weight:bold;transition:background 0.2s;}");
            out.println(".btn-add:hover{background:#1e5c1e;}");
            out.println(".table-wrap{background:white;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.08);overflow:hidden;}");
            out.println("table{width:100%;border-collapse:collapse;}");
            out.println("thead tr{background:#2c7a2c;}");
            out.println(".admin-table thead tr{background:#1a237e;}");
            out.println("thead th{color:white;padding:14px 12px;font-size:13px;font-weight:600;text-align:center;letter-spacing:0.03em;}");
            out.println("tbody tr{border-bottom:1px solid #f0f0f0;transition:background 0.15s;}");
            out.println("tbody tr:hover{background:#f7fdf7;}");
            out.println("tbody tr:last-child{border-bottom:none;}");
            out.println("td{padding:12px;font-size:13px;text-align:center;color:#444;}");
            out.println("td:first-child{color:#999;font-size:12px;}");
            out.println(".badge{display:inline-block;padding:4px 12px;border-radius:20px;font-size:11px;font-weight:bold;letter-spacing:0.04em;}");
            out.println(".badge-healthy{background:#e8f5e9;color:#2c7a2c;}");
            out.println(".badge-sick{background:#fdecea;color:#cc0000;}");
            out.println(".badge-treatment{background:#fff8e1;color:#f0a500;}");
            out.println(".badge-quarantine{background:#f3e5f5;color:#6a0dad;}");
            out.println(".badge-other{background:#f0f0f0;color:#555;}");
            out.println(".btn{padding:6px 14px;border-radius:5px;text-decoration:none;color:white;font-size:12px;font-weight:bold;display:inline-block;transition:opacity 0.2s;}");
            out.println(".btn:hover{opacity:0.85;}");
            out.println(".btn-edit{background:#f0a500;}");
            out.println(".btn-delete{background:#cc0000;}");
            out.println(".empty{text-align:center;padding:50px 20px;color:#aaa;}");
            out.println(".empty .empty-icon{font-size:48px;margin-bottom:12px;}");
            out.println(".empty p{font-size:15px;margin-bottom:20px;}");
            out.println(".farmer-tag{display:inline-block;background:#e8f5e9;color:#2c7a2c;font-size:11px;padding:3px 8px;border-radius:10px;font-weight:bold;}");
            out.println("footer{background:#1b5e20;color:rgba(255,255,255,0.6);text-align:center;padding:20px;font-size:13px;margin-top:40px;}");
            out.println("footer a{color:#a5d6a7;text-decoration:none;margin:0 10px;}");
            out.println("footer a:hover{text-decoration:underline;}");
            out.println(".footer-links{margin-bottom:8px;}");
            out.println("</style></head><body>");

            // Navbar
            out.println("<div class='navbar'>");
            out.println("<span class='brand'>🐄 Livestock Monitoring System</span>");
            out.println("<div class='nav-right'>");
            out.println("<span class='welcome-badge'>👤 " + farmerName + "</span>");
            if (isAdmin) {
                out.println("<span class='role-badge role-admin'>Admin</span>");
            } else {
                out.println("<span class='role-badge role-farmer'>Farmer</span>");
            }
            out.println("<a href='contact.html'>Contact</a>");
            out.println("<a href='LogoutServlet' class='logout-btn'>Logout</a>");
            out.println("</div></div>");

            // Welcome banner — different colour for admin vs farmer
            if (isAdmin) {
                out.println("<div class='welcome-banner admin-banner'>");
                out.println("<h2>Welcome, Administrator " + farmerName + "!</h2>");
                out.println("<p>You have full access — viewing all animal records across all farmers.</p>");
            } else {
                out.println("<div class='welcome-banner farmer-banner'>");
                out.println("<h2>Welcome back, " + farmerName + "!</h2>");
                out.println("<p>Here is an overview of your animal records.</p>");
            }
            out.println("</div>");

            try {
                Connection conn = DBConnection.getConnection();

                // Count query — admin counts ALL, farmer counts only theirs
                int total = 0, healthy = 0, sick = 0, treatment = 0,
                    quarantine = 0, recovering = 0, deceased = 0;

                String countSql;
                PreparedStatement countPs;

                if (isAdmin) {
                    countSql = "SELECT health_status, COUNT(*) as cnt FROM cattle GROUP BY health_status";
                    countPs  = conn.prepareStatement(countSql);
                } else {
                    countSql = "SELECT health_status, COUNT(*) as cnt FROM cattle WHERE farmer_id = ? GROUP BY health_status";
                    countPs  = conn.prepareStatement(countSql);
                    countPs.setInt(1, farmerId);
                }

                ResultSet countRs = countPs.executeQuery();
                while (countRs.next()) {
                    int cnt       = countRs.getInt("cnt");
                    String status = countRs.getString("health_status");
                    total += cnt;
                    switch (status) {
                        case "Healthy":         healthy    += cnt; break;
                        case "Sick":            sick       += cnt; break;
                        case "Under Treatment": treatment  += cnt; break;
                        case "Quarantined":     quarantine += cnt; break;
                        case "Recovering":      recovering += cnt; break;
                        case "Deceased":        deceased   += cnt; break;
                    }
                }
                countRs.close();
                countPs.close();

                // Summary cards
                out.println("<div class='summary'>");
                out.println("<div class='summary-card total'><div class='num'>" + total + "</div><div class='lbl'>Total Animals</div></div>");
                out.println("<div class='summary-card healthy'><div class='num'>" + healthy + "</div><div class='lbl'>Healthy</div></div>");
                out.println("<div class='summary-card sick'><div class='num'>" + sick + "</div><div class='lbl'>Sick</div></div>");
                out.println("<div class='summary-card treatment'><div class='num'>" + treatment + "</div><div class='lbl'>Under Treatment</div></div>");
                out.println("<div class='summary-card quarantine'><div class='num'>" + quarantine + "</div><div class='lbl'>Quarantined</div></div>");
                out.println("<div class='summary-card recovering'><div class='num'>" + recovering + "</div><div class='lbl'>Recovering</div></div>");
                out.println("<div class='summary-card deceased'><div class='num'>" + deceased + "</div><div class='lbl'>Deceased</div></div>");
                out.println("</div>");

                // Table
                out.println("<div class='container'>");
                out.println("<div class='table-header'>");

                if (isAdmin) {
                    out.println("<h3>🛡️ All Animal Records — Admin View</h3>");
                } else {
                    out.println("<h3>🐄 My Animal Records</h3>");
                    out.println("<a href='AddAnimalServlet' class='btn-add'>+ Add New Animal</a>");
                }
                out.println("</div>");

                out.println("<div class='table-wrap'>");
                out.println("<table" + (isAdmin ? " class='admin-table'" : "") + ">");
                out.println("<thead><tr>");
                out.println("<th>#</th><th>Tag No.</th><th>Type</th><th>Breed</th>");
                out.println("<th>Age</th><th>Weight (kg)</th><th>Location</th>");
                out.println("<th>Health Status</th>");

                // Admin sees extra Farmer column and no action buttons
                if (isAdmin) {
                    out.println("<th>Farmer</th>");
                } else {
                    out.println("<th>Actions</th>");
                }
                out.println("</tr></thead><tbody>");

                // Fetch records — admin gets all, farmer gets their own
                String sql;
                PreparedStatement ps;

                if (isAdmin) {
                    sql = "SELECT c.*, u.full_name as farmer_name FROM cattle c "
                        + "JOIN users u ON c.farmer_id = u.id ORDER BY c.id";
                    ps  = conn.prepareStatement(sql);
                } else {
                    sql = "SELECT * FROM cattle WHERE farmer_id = ? ORDER BY id";
                    ps  = conn.prepareStatement(sql);
                    ps.setInt(1, farmerId);
                }

                ResultSet rs      = ps.executeQuery();
                boolean hasRecords = false;
                int rowNum         = 1;

                while (rs.next()) {
                    hasRecords    = true;
                    int id        = rs.getInt("id");
                    String health = rs.getString("health_status");

                    String badgeClass;
                    switch (health) {
                        case "Healthy":         badgeClass = "badge-healthy";    break;
                        case "Sick":            badgeClass = "badge-sick";       break;
                        case "Under Treatment": badgeClass = "badge-treatment";  break;
                        case "Quarantined":     badgeClass = "badge-quarantine"; break;
                        default:                badgeClass = "badge-other";      break;
                    }

                    out.println("<tr>");
                    out.println("<td>" + rowNum++ + "</td>");
                    out.println("<td><strong>" + rs.getString("tag_number") + "</strong></td>");
                    out.println("<td>" + rs.getString("animal_type") + "</td>");
                    out.println("<td>" + rs.getString("breed") + "</td>");
                    out.println("<td>" + rs.getInt("age") + " yrs</td>");
                    out.println("<td>" + rs.getDouble("weight") + " kg</td>");
                    out.println("<td>" + rs.getString("location") + "</td>");
                    out.println("<td><span class='badge " + badgeClass + "'>" + health + "</span></td>");

                    if (isAdmin) {
                        // Admin sees farmer name — no edit or delete
                        out.println("<td><span class='farmer-tag'>" + rs.getString("farmer_name") + "</span></td>");
                    } else {
                        // Farmer sees edit and delete buttons
                        out.println("<td>");
                        out.println("<a href='EditAnimalServlet?id=" + id + "' class='btn btn-edit'>Edit</a> ");
                        out.println("<a href='DeleteAnimalServlet?id=" + id + "' class='btn btn-delete' "
                                + "onclick=\"return confirm('Are you sure you want to delete this animal record?')\">Delete</a>");
                        out.println("</td>");
                    }

                    out.println("</tr>");
                }

                if (!hasRecords) {
                    int colspan = isAdmin ? 9 : 9;
                    out.println("<tr><td colspan='" + colspan + "'>");
                    out.println("<div class='empty'>");
                    out.println("<div class='empty-icon'>🐄</div>");
                    if (isAdmin) {
                        out.println("<p>No animal records found in the system.</p>");
                    } else {
                        out.println("<p>No animal records found.</p>");
                        out.println("<a href='AddAnimalServlet' class='btn-add'>+ Add Your First Animal</a>");
                    }
                    out.println("</div></td></tr>");
                }

                out.println("</tbody></table></div>");
                out.println("</div>");

                rs.close();
                ps.close();
                conn.close();

            } catch (Exception e) {
                out.println("<div class='container'>");
                out.println("<p style='color:red;padding:20px;'>Database error: " + e.getMessage() + "</p>");
                out.println("</div>");
            }

            // Footer
            out.println("<footer>");
            out.println("<div class='footer-links'>");
            out.println("<a href='index.html'>Home</a>");
            out.println("<a href='contact.html'>Contact Us</a>");
            out.println("<a href='LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("<p>© 2026 Livestock Monitoring System | NUST</p>");
            out.println("</footer>");

            out.println("</body></html>");
        }
    }
}