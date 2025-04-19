/**
 * Entity representing a user role.
 */
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROL")
public class Rol {

    /** The unique identifier of the role. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long id;

    /** The name of the role. */
    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    private String roleName;

    // Getters and Setters

    /** @return the unique identifier of the role. */
    public Long getId() {
        return id;
    }

    /** @param id the unique identifier of the role. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return the name of the role. */
    public String getRoleName() {
        return roleName;
    }

    /** @param roleName the name of the role. */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
