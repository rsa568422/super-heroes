package com.w2m.superheroes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.superheroes.models.SuperHero;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.w2m.superheroes.data.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestTemplateIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void test_findById_id1() {
        ResponseEntity<SuperHero> response = this.client.getForEntity(getUri("/super-heroes/1"), SuperHero.class);
        SuperHero superHero = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertNotNull(superHero),
                () -> assertEquals(1L, superHero.getId()),
                () -> assertEquals("Spiderman", superHero.getName())
        );
    }

    @Test
    @Order(2)
    void test_findById_id7() {
        ResponseEntity<SuperHero> response = this.client.getForEntity(getUri("/super-heroes/7"), SuperHero.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    void test_save() {
        SuperHero newHero = new SuperHero(null, "Super mock");

        ResponseEntity<SuperHero> response1 = this.client.postForEntity(getUri("/super-heroes"), newHero, SuperHero.class);

        SuperHero superHero1 = response1.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response1.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, response1.getHeaders().getContentType()),
                () -> assertNotNull(superHero1),
                () -> assertTrue(superHero1.getId() > 6),
                () -> assertEquals("Super mock", superHero1.getName())
        );

        superHero1.setName("Supermock");
        ResponseEntity<SuperHero> response2 = this.client.postForEntity(getUri("/super-heroes"), superHero1, SuperHero.class);
        SuperHero superHero2 = response2.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response2.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, response2.getHeaders().getContentType()),
                () -> assertNotNull(superHero2),
                () -> assertEquals(superHero1.getId(), superHero2.getId()),
                () -> assertEquals("Supermock", superHero2.getName())
        );

        ResponseEntity<SuperHero[]> response3 = this.client.getForEntity(getUri("/super-heroes"), SuperHero[].class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response3.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, response3.getHeaders().getContentType()),
                () -> assertNotNull(response3.getBody())
        );

        List<SuperHero> superHeroes = Arrays.asList(response3.getBody());

        assertAll(
                () -> assertEquals(7, superHeroes.size()),
                () -> assertTrue(superHeroes.contains(SPIDERMAN())),
                () -> assertTrue(superHeroes.contains(SUPERMAN())),
                () -> assertTrue(superHeroes.contains(MANOLITO())),
                () -> assertTrue(superHeroes.contains(superHero2))
        );

    }

    private String getUri(String uri) {
        return String.format("http://localhost:%d%s", this.port, uri);
    }

}
