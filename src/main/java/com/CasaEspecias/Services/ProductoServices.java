package com.CasaEspecias.Services;

import com.CasaEspecias.Model.Producto;
import com.CasaEspecias.Repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServices {

    private final ProductoRepository productoRepository;

    // Constructor para poner implementar el metodo de buscar por nombre del repository
    public ProductoServices(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getDatosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarProductoId(Integer id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarProductoNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto agregarProducto(Producto producto) {

        String nombreNormalizado = producto.getNombre().toUpperCase();

        if (productoRepository.existsByNombre(nombreNormalizado)) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + nombreNormalizado);
        }

        // 🔹 Solo generar presentaciones si es ESPECIA
        if ("ESPECIA".equalsIgnoreCase(producto.getTipoProducto())) {
            producto.generarPresentaciones();
        }

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Integer id) {

        Optional<Producto> productoOpt = productoRepository.findById(id);

        if (productoOpt.isPresent()) {
            productoRepository.deleteById(id);
        }
    }


    public Producto actualizarProducto(Integer id, Producto productoActualizado) {

        Optional<Producto> productoOpt = productoRepository.findById(id);

        if (productoOpt.isPresent()) {

            Producto productoExistente = productoOpt.get();
            String nombreNormalizado = productoActualizado.getNombre().toUpperCase();

            // 🔥 Buscar si existe otro producto con ese nombre
            Optional<Producto> productoConMismoNombre =
                    productoRepository.findByNombre(nombreNormalizado);

            if (productoConMismoNombre.isPresent()
                    && !productoConMismoNombre.get().getId().equals(id)) {

                throw new RuntimeException("Ya existe otro producto con el nombre: " + nombreNormalizado);
            }

            productoExistente.setNombre(productoActualizado.getNombre());
            productoExistente.setTipoProducto(productoActualizado.getTipoProducto());
            productoExistente.setStockCantidad(productoActualizado.getStockCantidad());
            productoExistente.setStockUnidad(productoActualizado.getStockUnidad());
            productoExistente.setImagenURL(productoActualizado.getImagenURL());

            productoExistente.setKilo(productoActualizado.getKilo());
            productoExistente.setMinimoGramos(productoActualizado.getMinimoGramos());

            // 🔹 Campos para productos UNITARIOS
            productoExistente.setPrecioUnitario(productoActualizado.getPrecioUnitario());
            productoExistente.setUnidadCantidad(productoActualizado.getUnidadCantidad());
            productoExistente.setUnidadMedida(productoActualizado.getUnidadMedida());

            // 🔹 Solo generar presentaciones si es ESPECIA
            if ("ESPECIA".equalsIgnoreCase(productoExistente.getTipoProducto())) {
                productoExistente.generarPresentaciones();
            }

            return productoRepository.save(productoExistente);
        }

        throw new RuntimeException("Producto no encontrado con ID: " + id);
    }

    public boolean existePorNombre(String nombre) {
        return productoRepository.existsByNombre(nombre.toUpperCase());
    }

    public Optional<Producto> buscarPorNombreExacto(String nombre) {
        return productoRepository.findByNombre(nombre.toUpperCase());
    }

    public long contarProductos() {
        return productoRepository.count();
    }

}