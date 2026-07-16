package SystemITR.JosueGuinea2A.Departamentos.Repository;

import SystemITR.JosueGuinea2A.Departamentos.Entity.DepartamentosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Optional;

@Repository
public interface DepartamentosRepository extends JpaRepository<DepartamentosEntity, Long> {
    Optional<DepartamentosEntity> findByAbreviatura(String abreviatura);
}
