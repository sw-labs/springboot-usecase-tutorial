package com.bicicletas.trayectos.logica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bicicletas.trayectos.dataAccess.TrayectosRepository;
import com.bicicletas.trayectos.dataAccess.UbicacionesRepository;
import com.bicicletas.trayectos.modelo.Trayecto;
import com.bicicletas.trayectos.modelo.Ubicacion;

import jakarta.transaction.Transactional;

@SpringBootTest
public class Cu002_AgregarUbicacionATrayectoTests {

    @Autowired
    TrayectosRepository trayectos;

    @Autowired
    UbicacionesRepository ubicaciones;

    @Autowired
    TrayectosService servicio;

    @Test
    @Transactional
    public void agregarUbicacionATrayecto_almacenaUbicacion() {

        try { 

            // -- Arrange: Prepara la prueba

                // inicia un trayecto
                UUID idTrayecto = servicio.iniciarTrayecto(27.0, 24.0);

            // -- Act: Ejecuta la operación que se debe probar

                // agrega una ubicación al trayecto
                UUID idUbicacion = servicio.agregarUbicacion(idTrayecto, 27.1, 24.1);

            // -- Assert: Revisa el resultado

                Trayecto t = trayectos.findById(idTrayecto).get();

                assertEquals(2, t.getUbicaciones().size(), "No agregó la ubicación al trayecto");

                Ubicacion u = ubicaciones.findById(idUbicacion).get();

                assertEquals(27.1, u.getLongitud(), "No se almacenó bien la longitud");
                assertEquals(24.1, u.getLatitud(), "No se almacenó bien la latitud");


        } catch (Exception e) {

                fail("se generó una excepción: " + e.getMessage());

        }
    }


    @Test
    @Transactional
    public void agregarUbicacionATrayectoNoExistente_falla() {
        
        try {
            // -- Arrange: Prepara la prueba

                // no hago nada
                UUID idTrayecto = UUID.randomUUID();

            // -- Act: Ejecuta la operación que se debe probar

                // agrego la ubicación a un trayecto que no existe
                servicio.agregarUbicacion(idTrayecto, 27.0, 24.0);
            
            // -- Assert: Revisa el resultado

                fail("No falló y debió generar excepción");
            
        } catch (Exception e) {
            // ok
        }
    }

    @Test
    @Transactional
    public void agregarUbicacionATrayectoNoActivo_falla() {
        
        try {
        // -- Arrange: Prepara la prueba

            // inicio un trayecto
            UUID idTrayecto = servicio.iniciarTrayecto(27.0, 24.0);

            Trayecto t = trayectos.findById(idTrayecto).get();
            t.setEnProceso(false);

        // -- Act: Ejecuta la operación que se debe probar

            servicio.agregarUbicacion(idTrayecto,24.1, 27.1);

        // -- Assert: Revisa el resultado

            fail("debio fallar y no falló");
            
        } catch (Exception e) {
            // ok
        }
    }

}
