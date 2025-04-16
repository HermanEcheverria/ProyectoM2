package com.unis.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PAGE_CONTENT")
public class PageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pageContentSeq")
    @SequenceGenerator(name = "pageContentSeq", sequenceName = "PAGE_CONTENT_SEQ", allocationSize = 1)
    @Column(name = "ID_CONTENT")
    private Long idContent;

    @Column(name = "PAGE_NAME", nullable = false, length = 100)
    private String pageName;

    @Column(name = "SECTION_NAME", nullable = false, length = 100)
    private String sectionName;

    @Column(name = "CONTENT_TITLE", length = 200)
    private String contentTitle;

    @Lob
    @Column(name = "CONTENT_BODY", nullable = false)
    private String contentBody;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @Column(name = "MODIFIED_BY", nullable = false)
    private Long modifiedBy;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "REJECTION_REASON", length = 500)
    private String rejectionReason;

    @Column(name = "EDITOR_EMAIL", length = 150)
private String editorEmail;


    // Constructor vacío requerido por Hibernate
    public PageContent() {
    }

    // Getters y Setters

    public Long getIdContent() {
        return idContent;
    }

    public void setIdContent(Long idContent) {
        this.idContent = idContent;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getEditorEmail() {
        return editorEmail;
    }
    
    public void setEditorEmail(String editorEmail) {
        this.editorEmail = editorEmail;
    }
    
}
