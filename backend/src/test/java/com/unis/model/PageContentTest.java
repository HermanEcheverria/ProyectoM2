package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class PageContentTest {

    @Test
    void setIdContent_y_getIdContent() {
        PageContent content = new PageContent();
        Long id = 99L;
        content.setIdContent(id);

        assertEquals(id, content.getIdContent());
    }

    @Test
    void getIdContent_conReflection() throws Exception {
        PageContent content = new PageContent();

        var f = PageContent.class.getDeclaredField("idContent");
        f.setAccessible(true);
        f.set(content, 123L);

        assertEquals(123L, content.getIdContent());
    }

    @Test
    void settersYgettersGenerales() {
        PageContent content = new PageContent();
        content.setPageName("Historia");
        content.setSectionName("Institucional");
        content.setContentTitle("Nuestra Historia");
        content.setContentBody("<p>Texto</p>");
        content.setImage(new byte[]{1, 2, 3});
        Timestamp ts = Timestamp.from(Instant.now());
        content.setLastModifiedDate(ts);
        content.setModifiedBy(10L);
        content.setStatus("PUBLISHED");
        content.setRejectionReason("N/A");
        content.setEditorEmail("editor@test.com");

        assertEquals("Historia", content.getPageName());
        assertEquals("Institucional", content.getSectionName());
        assertEquals("Nuestra Historia", content.getContentTitle());
        assertEquals("<p>Texto</p>", content.getContentBody());
        assertArrayEquals(new byte[]{1, 2, 3}, content.getImage());
        assertEquals(ts, content.getLastModifiedDate());
        assertEquals(10L, content.getModifiedBy());
        assertEquals("PUBLISHED", content.getStatus());
        assertEquals("N/A", content.getRejectionReason());
        assertEquals("editor@test.com", content.getEditorEmail());
    }
}
