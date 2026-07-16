package SystemITR.JosueGuinea2A.Departamentos.Controller;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea2A.Response.ApiResponse;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.zip.DeflaterOutputStream;

@Slf4j
@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin
public class DepartamentoController {

    private final DepartamentosService service;

    public DepartamentoController(DepartamentosService service) {
        this.service = service;
    }

    /**
     * Nuevo recursos : Ingresar información -> POST
     * Obtener recursos: GET
     * Actualizar recursos: PUT / PATCH
     * Eliminar recursos: DELETE
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentoDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentoDTO json){
        try{
            DepartamentoDTO dto = service.nuevoDepartamento(json);
            if (dto != null){
                ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(true, "Datos ingresados exitosamente", json);
                return ResponseEntity.ok(respuesta);
            }
            log.warn("Intento de insercion fallido: " + json);
            ApiResponse<DepartamentoDTO> respuestaFallida = new ApiResponse<>(false, "intento fallido de insercion", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaFallida);
        }catch (Exception e){
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", json);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentoDTO>>> obtenerDatos(){
        try{
            List<DepartamentoDTO> lista = service.ObtenerTodo();
            if(lista != null){
                log.info("Datos de departamentos consultados exitosamente");
                ApiResponse<List<DepartamentoDTO>> respuestaExito = new ApiResponse<>(true, "Datos encontrados", lista);
                return ResponseEntity.ok(respuestaExito);
            }
            log.info("Datos no encontrados");
            ApiResponse<List<DepartamentoDTO>>  respuestaNoEncontrada = new ApiResponse<>(false , "Datos encontrados", null );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaNoEncontrada);
        }catch (Exception e){

            e.printStackTrace();
            ApiResponse<List<DepartamentoDTO>> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> obtenerDatosPorId(@PathVariable Long id){
        try{
            DepartamentoDTO dto = service.buscarDepartamento(id);
            if (dto != null){
                log.info("Se obtuvieron los datos del departamento: " + dto);
                ApiResponse<DepartamentoDTO> respuestaExito = new ApiResponse<>(true, "Se obtuvieron los datos del departamento con ID:  " + id, dto);
                return ResponseEntity.ok(respuestaExito);
            }

            log.info("Datos no encontrados con ID: " + id);
            ApiResponse<DepartamentoDTO> respuestaNoEncontrada = new ApiResponse<>(false, "Datos no encontrados con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.error("Error critico, al obtener el departamento con ID: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuestaError = new ApiResponse<>(false, "Error critico, al obtener el departamento con ID: " + id);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> EliminarDepartamento(@PathVariable Long id){
        try {
            boolean respuesta = service.EliminarData(id);
            if (respuesta){
                ApiResponse<Void> respuestaExitosa = new ApiResponse<>(true, "Departamento con ID: " + id + " Eliminado.");
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);
            }
            log.info("Departamento con ID:  " + id + " No fue encontrado");
            ApiResponse<Void> respuestaNoEncontrada = new ApiResponse<>(false, "Departamento con ID:  " + id + " No fue encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);

        }catch (Exception e){
            log.error("Error critico, al eliminar el Departameto de ID: " + id);
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(false, "Error critico, al eliminar el Departameto de ID: " + id);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<DepartamentoDTO>> ActualizarData(@PathVariable Long id, @Valid @RequestBody DepartamentoDTO dto){
        try{
           DepartamentoDTO data = service.actualizar(id, dto);
           if (data != null){
               log.info("Departamento con ID: " + id + " Ha sido actualizado..");
               ApiResponse<DepartamentoDTO> respuestaExitosa = new ApiResponse<>(true, "Departamento con ID: " + id + " Ha sido actualizado..");
               return ResponseEntity.ok(respuestaExitosa);
           }

           log.warn("No se pudo realizar la actualizacion del departamento con ID: " + id );
           ApiResponse<DepartamentoDTO> respuestaNoCompletada = new ApiResponse<>(false,"No se pudo realizar la actualizacion del departamento con ID: " + id );
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoCompletada);

        }catch (Exception e){
            log.error("Error critico, al actualizar el Departameto de ID: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuestaError = new ApiResponse<>(false, "Error critico, al actulizar el Departameto de ID: " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> buscarPorAbreviatura(@PathVariable String abreviatura){
        try{
            DepartamentoDTO data = service.buscarAbreviatura(abreviatura);
            if (data != null){
                log.info("Departamento encontrado con abreviatura: " + abreviatura);
                ApiResponse<DepartamentoDTO> respuestaExito = new ApiResponse<>(true,"Departamento encontrado con abreviatura: " + abreviatura, data);
                return ResponseEntity.ok(respuestaExito);
            }
            log.warn("Departamento no encontrado: " + abreviatura);
            ApiResponse<DepartamentoDTO> respuestaNoencontrada = new ApiResponse<>(false, "Departamento no encontrado: " + abreviatura);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoencontrada);

        }catch (Exception e){
            log.info("Error al intentar buscar por abreviatura");
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
