/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;
import java.util.List;
/**
 *
 * @author Oscar
 */
public interface IPersistencia <T>{
     void agregar(T entity);  
    void actualizar(T entity);  
    void eliminar(Long id);  
    T obtenerPorId(Long id);  
    List<T> obtenerTodos();  
}
