/**
 * Entity representing an employee.
 */
package com.unis.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "EMPLEADOS")
public class EmpleadoAcc implements Serializable {

    /** The unique identifier of the employee. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;

    /** The user account associated with the employee. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    /** The last name of the employee. */
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    /** The document identifier of the employee. */
    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    /** The birth date of the employee. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the employee. */
    @Column(name = "GENERO", length = 10)
    private String genero;

    /** The phone number of the employee. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The ID of the hospital where the employee works. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    /** The position of the employee. */
    @Column(name = "PUESTO", length = 50)
    private String puesto;

    // 🔹 Getters y Setters
    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public UserAcc getUsuario() { return usuario; }
    public void setUsuario(UserAcc usuario) { this.usuario = usuario; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Long getIdHospital() { return idHospital; }
    public void setIdHospital(Long idHospital) { this.idHospital = idHospital; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
}
