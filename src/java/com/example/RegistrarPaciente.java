import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegistrarPaciente", urlPatterns = {"/RegistrarPaciente"})
public class RegistrarPaciente extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String DPI = request.getParameter("DPI");
        String NOMBRE = request.getParameter("NOMBRE");
        String EDAD = request.getParameter("EDAD");
        String GENERO = request.getParameter("GENERO");

        // Establecer conexión a la base de datos Oracle
        try {
            String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe"; // Reemplaza con tu URL de conexión
            String usuario = "c##HOSPITAL"; // Reemplaza con tu usuario de Oracle
            String contraseña = "1234"; // Reemplaza con tu contraseña de Oracle

            Class.forName(jdbcDriver);
            Connection conexion = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

            // Preparar la sentencia SQL para la inserción
            String sql = "INSERT INTO PACIENTES (DPI, NOMBRE, EDAD, GENERO, FECHA_INGRESO) VALUES (?, ?, ?, ?, SYSDATE)";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, DPI);
            statement.setString(2, NOMBRE);
            statement.setString(3, EDAD);
            statement.setString(4, GENERO);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Registro exitoso
                response.setContentType("text/plain");
                PrintWriter out = response.getWriter();
                out.print("success");
            } else {
                // Registro fallido
                response.setContentType("text/plain");
                PrintWriter out = response.getWriter();
                out.print("error");
            }

            statement.close();
            conexion.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.print("error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para registrar pacientes en la base de datos.";
    }
}
