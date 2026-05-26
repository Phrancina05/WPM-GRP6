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
 * EditAnimalServlet.java
 * GET  → loads the animal record into a form for editing
 * POST → updates the animal record in the database
 */
@WebServlet(name = "EditAnimalServlet", urlPatterns = {"/EditAnimalServlet"})
public class EditAnimalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        try (PrintWriter out = response.getWriter()) {

            // Check if farmer is logged in
            if (session == null || session.getAttribute("farmer_id") == null) {
                response.sendRedirect("login.html");
                return;
            }

            int id = Integer.parseInt(request.getParameter("id"));

            out.println("<!DOCTYPE html><html><head><title>Edit Animal</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body{font-family:Arial;margin:0;background:#f4f4f4;}");
            out.println(".navbar{background:#2c7a2c;padding:15px;color:white;}");
            out.println(".navbar a{color:white;text-decoration:none;margin-right:20px;}");
            out.println(".navbar a:hover{text-decoration:underline;}");
            out.println(".container{max-width:500px;margin:40px auto;background:white;padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);}");
            out.println("h2{color:#f0a500;}");
            out.println("label{font-weight:bold;font-size:14px;}");
            out.println("input,select{width:100%;padding:10px;margin:8px 0 16px 0;border:1px solid #ccc;border-radius:5px;box-sizing:border-box;font-family:Arial;}");
            out.println(".btn{width:100%;padding:12px;background:#f0a500;color:white;border:none;border-radius:5px;font-size:16px;cursor:pointer;}");
            out.println(".btn:hover{background:#d4920a;}");
            out.println(".btn-back{display:inline-block;margin-top:15px;color:#2c7a2c;text-decoration:none;font-size:14px;}");
            out.println("</style></head><body>");

            out.println("<div class='navbar'>");
            out.println("<strong>🐄 Livestock Monitoring System</strong>");
            out.println("<span style='float:right;'>");
            out.println("<a href='DashboardServlet'>Dashboard</a>");
            out.println("<a href='index.html'>Home</a>");
            out.println("<a href='LogoutServlet'>Logout</a>");
            out.println("</span></div>");

            out.println("<div class='container'>");
            out.println("<h2>✏️ Edit Animal Record</h2>");
            out.println("<p style='color:#555;font-size:14px;margin-bottom:20px;'>Update the details of this animal record below.</p>");

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT * FROM cattle WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<form action='EditAnimalServlet' method='POST'>");
                    out.println("<input type='hidden' name='id' value='" + id + "'/>");

                    // Tag Number
                    out.println("<label>Tag Number:</label>");
                    out.println("<input type='text' name='tag_number' value='"
                            + rs.getString("tag_number") + "' required/>");

                    // Animal Type — expanded for all livestock
                    out.println("<label>Animal Type:</label>");
                    out.println("<select name='animal_type' required>");
                    String[] types = {
                        "Cattle", "Goat", "Sheep", "Horse",
                        "Pig", "Donkey", "Chicken", "Other"
                    };
                    for (String t : types) {
                        String sel = t.equals(rs.getString("animal_type")) ? " selected" : "";
                        out.println("<option value='" + t + "'" + sel + ">" + t + "</option>");
                    }
                    out.println("</select>");

                    // Breed
                    out.println("<label>Breed:</label>");
                    out.println("<input type='text' name='breed' value='"
                            + rs.getString("breed") + "' required/>");

                    // Age
                    out.println("<label>Age (years):</label>");
                    out.println("<input type='number' name='age' min='0' value='"
                            + rs.getInt("age") + "' required/>");

                    // Weight
                    out.println("<label>Weight (kg):</label>");
                    out.println("<input type='number' step='0.01' min='0' name='weight' value='"
                            + rs.getDouble("weight") + "' required/>");

                    // Location
                    out.println("<label>Location / Paddock:</label>");
                    out.println("<input type='text' name='location' value='"
                            + rs.getString("location") + "' required/>");

                    // Health Status — expanded
                    out.println("<label>Health Status:</label>");
                    out.println("<select name='health_status' required>");
                    String[] statuses = {
                        "Healthy", "Sick", "Under Treatment",
                        "Quarantined", "Recovering", "Deceased"
                    };
                    for (String s : statuses) {
                        String sel = s.equals(rs.getString("health_status")) ? " selected" : "";
                        out.println("<option value='" + s + "'" + sel + ">" + s + "</option>");
                    }
                    out.println("</select>");

                    out.println("<br/>");
                    out.println("<input type='submit' class='btn' value='Update Animal Record'/>");
                    out.println("</form>");

                } else {
                    out.println("<p style='color:red;'>Animal record not found.</p>");
                }

                rs.close();
                ps.close();
                conn.close();

            } catch (Exception e) {
                out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            }

            out.println("<a href='DashboardServlet' class='btn-back'>← Back to Dashboard</a>");
            out.println("</div></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("farmer_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int id             = Integer.parseInt(request.getParameter("id"));
        String tagNumber   = request.getParameter("tag_number");
        String animalType  = request.getParameter("animal_type");
        String breed       = request.getParameter("breed");
        int age            = Integer.parseInt(request.getParameter("age"));
        double weight      = Double.parseDouble(request.getParameter("weight"));
        String location    = request.getParameter("location");
        String healthStatus= request.getParameter("health_status");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE cattle SET tag_number=?, animal_type=?, breed=?, "
                       + "age=?, weight=?, location=?, health_status=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tagNumber);
            ps.setString(2, animalType);
            ps.setString(3, breed);
            ps.setInt(4, age);
            ps.setDouble(5, weight);
            ps.setString(6, location);
            ps.setString(7, healthStatus);
            ps.setInt(8, id);
            ps.executeUpdate();
            ps.close();
            conn.close();

            // Redirect back to dashboard after successful update
            response.sendRedirect("DashboardServlet");

        } catch (Exception e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html><html><body>");
                out.println("<p style='color:red;font-family:Arial;padding:20px;'>");
                out.println("Error updating record: " + e.getMessage());
                out.println("</p>");
                out.println("<a href='DashboardServlet' style='font-family:Arial;color:#2c7a2c;padding:20px;display:block;'>← Back to Dashboard</a>");
                out.println("</body></html>");
            }
        }
    }
}