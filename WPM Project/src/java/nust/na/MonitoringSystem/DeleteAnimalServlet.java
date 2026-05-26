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
import javax.servlet.http.HttpSession;

/**
 * DeleteAnimalServlet.java
 * Deletes any animal record from the database by ID.
 * Only the logged-in farmer can delete their own records.
 */
@WebServlet(name = "DeleteAnimalServlet", urlPatterns = {"/DeleteAnimalServlet"})
public class DeleteAnimalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        // Check if farmer is logged in
        if (session == null || session.getAttribute("farmer_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Make sure an ID was passed
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("DashboardServlet");
            return;
        }

        int id          = Integer.parseInt(idParam);
        int farmerId    = (int) session.getAttribute("farmer_id");

        try {
            Connection conn = DBConnection.getConnection();

            // Only delete if the animal belongs to the logged-in farmer
            // This prevents one farmer from deleting another farmer's records
            String sql = "DELETE FROM cattle WHERE id = ? AND farmer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, farmerId);
            ps.executeUpdate();
            ps.close();
            conn.close();

            // Redirect back to dashboard after successful delete
            response.sendRedirect("DashboardServlet");

        } catch (Exception e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html><html><head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Error</title>");
                out.println("<style>");
                out.println("body{font-family:Arial;margin:0;background:#f4f4f4;}");
                out.println(".navbar{background:#2c7a2c;padding:15px;color:white;}");
                out.println(".navbar a{color:white;text-decoration:none;margin-right:20px;}");
                out.println(".navbar a:hover{text-decoration:underline;}");
                out.println(".container{max-width:500px;margin:60px auto;background:white;");
                out.println("padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);text-align:center;}");
                out.println(".error{color:red;font-size:15px;margin-bottom:20px;}");
                out.println(".btn-back{display:inline-block;padding:10px 24px;background:#2c7a2c;");
                out.println("color:white;border-radius:5px;text-decoration:none;font-size:14px;}");
                out.println(".btn-back:hover{background:#1e5c1e;}");
                out.println("</style></head><body>");

                out.println("<div class='navbar'>");
                out.println("<strong>🐄 Livestock Monitoring System</strong>");
                out.println("<span style='float:right;'>");
                out.println("<a href='DashboardServlet'>Dashboard</a>");
                out.println("<a href='index.html'>Home</a>");
                out.println("<a href='LogoutServlet'>Logout</a>");
                out.println("</span></div>");

                out.println("<div class='container'>");
                out.println("<h2 style='color:#cc0000;'>❌ Delete Failed</h2>");
                out.println("<p class='error'>Error deleting animal record: " + e.getMessage() + "</p>");
                out.println("<a href='DashboardServlet' class='btn-back'>← Back to Dashboard</a>");
                out.println("</div></body></html>");
            }
        }
    }
}