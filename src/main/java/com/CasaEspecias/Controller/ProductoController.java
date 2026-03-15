package com.CasaEspecias.Controller;

import com.CasaEspecias.Model.Producto;
import com.CasaEspecias.Services.ProductoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoServices productoServices;

    @GetMapping
    public ResponseEntity<?> getDatosProducto() {
        List<Producto> lista = productoServices.getDatosProductos();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay productos registrados.");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscarNombre/{nombre}")
    public ResponseEntity<?> buscarProductoNombre(@PathVariable String nombre) {
        List<Producto> productos = productoServices.buscarProductoNombre(nombre);
        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron productos con el nombre: " + nombre);
        }
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<?> agregarProducto(@Validated @RequestBody Producto producto, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        // 🔹 Validar tipo de producto
        if (producto.getTipoProducto() == null ||
                (!producto.getTipoProducto().equalsIgnoreCase("ESPECIA")
                        && !producto.getTipoProducto().equalsIgnoreCase("UNITARIO"))) {

            return ResponseEntity.badRequest()
                    .body("El tipoProducto debe ser ESPECIA o UNITARIO.");
        }

        // 🔥 Validar nombre repetido
        if (productoServices.existePorNombre(producto.getNombre())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ya existe un producto con el nombre: " + producto.getNombre());
        }

        Producto nuevo = productoServices.agregarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Integer id,
            @Validated @RequestBody Producto producto,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errores);
        }

        Optional<Producto> existente = productoServices.buscarProductoId(id);

        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El producto con ID " + id + " no fue encontrado.");
        }

        // 🔹 Validar tipo de producto
        if (producto.getTipoProducto() == null ||
                (!producto.getTipoProducto().equalsIgnoreCase("ESPECIA")
                        && !producto.getTipoProducto().equalsIgnoreCase("UNITARIO"))) {

            return ResponseEntity.badRequest()
                    .body("El tipoProducto debe ser ESPECIA o UNITARIO.");
        }

        // 🔥 Validar nombre repetido SOLO si pertenece a otro producto
        Optional<Producto> productoConMismoNombre =
                productoServices.buscarPorNombreExacto(producto.getNombre());

        if (productoConMismoNombre.isPresent()
                && !productoConMismoNombre.get().getId().equals(id)) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ya existe otro producto con el nombre: " + producto.getNombre());
        }

        Producto actualizado = productoServices.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        Optional<Producto> prod = productoServices.buscarProductoId(id);
        if (prod.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto con ID " + id + " no existe.");
        }
        productoServices.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener cantidad total de productos activos
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getProductosActivosCount() {
        long count = productoServices.contarProductos();
        Map<String, Long> response = new HashMap<>();
        response.put("productosCount", count);
        return ResponseEntity.ok(response);
    }

}