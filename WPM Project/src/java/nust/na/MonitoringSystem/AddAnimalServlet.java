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
 * AddAnimalServlet.java
 * GET  → shows the add animal form
 * POST → saves new animal record to the database
 */
@WebServlet(name = "AddAnimalServlet", urlPatterns = {"/AddAnimalServlet"})
public class AddAnimalServlet extends HttpServlet {

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

            out.println("<!DOCTYPE html><html><head><title>Add Animal</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body{font-family:Arial;margin:0;background:#f4f4f4;}");
            out.println(".navbar{background:#2c7a2c;padding:15px;color:white;}");
            out.println(".navbar a{color:white;text-decoration:none;margin-right:20px;}");
            out.println(".navbar a:hover{text-decoration:underline;}");
            out.println(".container{max-width:500px;margin:40px auto;background:white;padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);}");
            out.println("h2{color:#2c7a2c;}");
            out.println("label{font-weight:bold;font-size:14px;}");
            out.println("input,select{width:100%;padding:10px;margin:8px 0 16px 0;border:1px solid #ccc;border-radius:5px;box-sizing:border-box;font-family:Arial;}");
            out.println(".btn{width:100%;padding:12px;background:#2c7a2c;color:white;border:none;border-radius:5px;font-size:16px;cursor:pointer;}");
            out.println(".btn:hover{background:#1e5c1e;}");
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
            out.println("<h2>➕ Add New Animal</h2>");
            out.println("<p style='color:#555;font-size:14px;margin-bottom:20px;'>Fill in the details below to register a new animal record.</p>");

            out.println("<form action='AddAnimalServlet' method='POST'>");

            // Tag Number
            out.println("<label>Tag Number:</label>");
            out.println("<input type='text' name='tag_number' placeholder='e.g. ECT-001' required/>");

            // Animal Type — expanded for all livestock
            out.println("<label>Animal Type:</label>");
            out.println("<select name='animal_type' required>");
            out.println("<option value='' disabled selected>Select animal type</option>");
            out.println("<option value='Cattle'>Cattle</option>");
            out.println("<option value='Goat'>Goat</option>");
            out.println("<option value='Sheep'>Sheep</option>");
            out.println("<option value='Horse'>Horse</option>");
            out.println("<option value='Pig'>Pig</option>");
            out.println("<option value='Donkey'>Donkey</option>");
            out.println("<option value='Chicken'>Chicken</option>");
            out.println("<option value='Other'>Other</option>");
            out.println("</select>");

            // Breed
            out.println("<label>Breed:</label>");
            out.println("<input type='text' name='breed' placeholder='e.g. Brahman, Nguni, Boer' required/>");

            // Age
            out.println("<label>Age (years):</label>");
            out.println("<input type='number' name='age' min='0' placeholder='e.g. 3' required/>");

            // Weight
            out.println("<label>Weight (kg):</label>");
            out.println("<input type='number' step='0.01' min='0' name='weight' placeholder='e.g. 350.50' required/>");

            // Location
            out.println("<label>Location / Paddock:</label>");
            out.println("<input type='text' name='location' placeholder='e.g. Paddock A, North Pasture' required/>");

            // Health Status — expanded
            out.println("<label>Health Status:</label>");
            out.println("<select name='health_status' required>");
            out.println("<option value='' disabled selected>Select health status</option>");
            out.println("<option value='Healthy'>Healthy</option>");
            out.println("<option value='Sick'>Sick</option>");
            out.println("<option value='Under Treatment'>Under Treatment</option>");
            out.println("<option value='Quarantined'>Quarantined</option>");
            out.println("<option value='Recovering'>Recovering</option>");
            out.println("<option value='Deceased'>Deceased</option>");
            out.println("</select>");

            out.println("<br/>");
            out.println("<input type='submit' class='btn' value='Save Animal Record'/>");
            out.println("</form>");

            out.println("<a href='DashboardServlet' class='btn-back'>← Back to Dashboard</a>");
            out.println("</div></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        // Check if farmer is logged in
        if (session == null || session.getAttribute("farmer_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int farmerId      = (int) session.getAttribute("farmer_id");
        String tagNumber  = request.getParameter("tag_number");
        String animalType = request.getParameter("animal_type");
        String breed      = request.getParameter("breed");
        int age           = Integer.parseInt(request.getParameter("age"));
        double weight     = Double.parseDouble(request.getParameter("weight"));
        String location   = request.getParameter("location");
        String healthStatus = request.getParameter("health_status");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO cattle "
                       + "(farmer_id, tag_number, animal_type, breed, age, weight, location, health_status) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, farmerId);
            ps.setString(2, tagNumber);
            ps.setString(3, animalType);
            ps.setString(4, breed);
            ps.setInt(5, age);
            ps.setDouble(6, weight);
            ps.setString(7, location);
            ps.setString(8, healthStatus);
            ps.executeUpdate();
            ps.close();
            conn.close();

            // Redirect back to dashboard after successful save
            response.sendRedirect("DashboardServlet");

        } catch (Exception e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html><html><body>");
                out.println("<p style='color:red;font-family:Arial;padding:20px;'>");
                out.println("Error saving animal record: " + e.getMessage());
                out.println("</p>");
                out.println("<a href='AddAnimalServlet' style='font-family:Arial;color:#2c7a2c;padding:20px;display:block;'>← Try Again</a>");
                out.println("<a href='DashboardServlet' style='font-family:Arial;color:#2c7a2c;padding:20px;display:block;'>← Back to Dashboard</a>");
                out.println("</body></html>");
            }
        }
    }
}