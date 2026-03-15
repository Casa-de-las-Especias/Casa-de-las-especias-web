package com.CasaEspecias.Repository;

import com.CasaEspecias.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
    Optional<Producto> findByNombre(String nombre);

}
