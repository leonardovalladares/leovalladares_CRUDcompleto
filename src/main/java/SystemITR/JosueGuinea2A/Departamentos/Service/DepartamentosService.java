package SystemITR.JosueGuinea2A.Departamentos.Service;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Entity.DepartamentosEntity;
import SystemITR.JosueGuinea2A.Departamentos.Repository.DepartamentosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartamentosService {

    //Forma 1 de inyección de dependencias y la mas recomendada
    private final DepartamentosRepository repo;
    public DepartamentosService(DepartamentosRepository repo){
        this.repo = repo;
    }

    public DepartamentoDTO nuevoDepartamento(@Valid DepartamentoDTO dto){
        try{
            //1. Convertir a Entity
            DepartamentosEntity entity = convertirAEntity(dto);
            //2. Guardar en la base de datos
            DepartamentosEntity entitySave = repo.save(entity);
            //3. Devolver una respuesta
            return convertirADTO(entitySave);
        }catch (Exception e){
            log.error("Error al ingresar la información del departamento" + e.getMessage());
            return null;
        }
    }

    private DepartamentosEntity convertirAEntity(@Valid DepartamentoDTO dto) {
        DepartamentosEntity objEntity = new DepartamentosEntity();
        objEntity.setNombreDepto(dto.getNombreDepto());
        objEntity.setAbreviatura(dto.getAbreviatura());
        objEntity.setUbicacion(dto.getUbicacion());
        return objEntity;
    }

    private DepartamentoDTO convertirADTO(@Valid DepartamentosEntity entity){
        DepartamentoDTO objDTO = new DepartamentoDTO();
        objDTO.setId(entity.getId());
        objDTO.setNombreDepto(entity.getNombreDepto());
        objDTO.setAbreviatura(entity.getAbreviatura());
        objDTO.setUbicacion(entity.getUbicacion());
        return objDTO;
    }

    public List<DepartamentoDTO> ObtenerTodo() {
        List<DepartamentosEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());

    }

    public DepartamentoDTO buscarDepartamento(Long id) {
        Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean EliminarData(Long id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public DepartamentoDTO actualizar(Long id, @Valid DepartamentoDTO dto) {
        try {
            //1. Buscar si el departamento realmente existe por su id
            Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
            //2. Verificar si el objeto contiene valores(utilizando un if)
            if (entidadOpcional.isPresent()){
                //2.1. Creamos un objeto de tipo entidad
                DepartamentosEntity entidad = entidadOpcional.get();
                //2.2 Converitr y asignar los dto´s (nuevos valores) a la entidad
                entidad.setNombreDepto(dto.getNombreDepto());
                entidad.setAbreviatura(dto.getAbreviatura());
                entidad.setUbicacion(dto.getUbicacion());
                //2.3 Actualizar los datos en la base de datos
                DepartamentosEntity datosguardados = repo.save(entidad);
                //2.4 Retornar la data convertida a DTO de forma previa
                return convertirADTO(datosguardados);
                //3. si no trae datios retornamos un null
            }
            return null;
        }catch (Exception e){
            log.error("Opps, ocurrio un error al actualizar la informacion");
            return null;
        }
    }

    public DepartamentoDTO buscarAbreviatura(String abreviatura) {
        try {
            Optional<DepartamentosEntity> registro = repo.findByAbreviatura(abreviatura);
            if (registro.isPresent()){
                return convertirADTO(registro.get());
            }
            log.warn("No existe ningun departamento con abreviatura: " + abreviatura);
            return null;
        }catch (Exception e){
            log.error("Ocurrio un error durante el proceso");
            return null;
        }
    }
}
