
package com.develop.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.develop.model.AccessPoint;

/*
 * Interfaz para interactuar con la Base de Datos mediante el uso de JPA
 */
@Repository
public interface AccessPointRepository extends JpaRepository<AccessPoint, String> {
	/*
	 * Consulta para Obtener puntos de acceso mediante el campo Colonia
	 * Dado que el Datasource contiene los valores de la Colonia en mayuscula
	 * se utiliza UPPER para distinguir ese valor.
	 */
	@Query(value = "SELECT * FROM access_points WHERE UPPER(colonia) = UPPER(:colonia)", nativeQuery = true)
	Page<AccessPoint> findByColonia(@Param("colonia") String colonia, Pageable pageable);
	
	/*
	 * Consulta para obtener un punto de acceso mediante proximidad
	 * Se utiliza la Formulade Haversine para calcular la distancia entre dos puntos
	 * En este caso los dos puntos proporcionados de lat y lon son utlizados para calcular
	 * valores trigonometricos para posteriormente calcular su punto aproximado y compararlo
	 * en relacion a los valores almacenados en base de datos
	 */
	@Query(value = "SELECT * FROM access_points a " + "WHERE a.latitud IS NOT NULL " + "AND a.longitud IS NOT NULL "
			+ "AND a.latitud NOT IN ('NA') " + "AND a.longitud NOT IN ('NA') " + "AND a.longitud BETWEEN -180 AND 180 "
			+ "ORDER BY (6371 * ACOS( " + "    COS(RADIANS(:lat)) * COS(RADIANS(a.latitud)) * "
			+ "    COS(RADIANS(a.longitud) - RADIANS(:lon)) + " + "    SIN(RADIANS(:lat)) * SIN(RADIANS(a.latitud)) "
			+ ")) ASC", countQuery = "SELECT COUNT(*) FROM access_points a " + "WHERE a.latitud IS NOT NULL "
					+ "AND a.longitud IS NOT NULL " + "AND a.latitud NOT IN ('NA') " + "AND a.longitud NOT IN ('NA') "
					+ "AND a.longitud BETWEEN -180 AND 180",
		    nativeQuery = true)
	Page<AccessPoint> findByProximity(@Param("lat") Double lat, @Param("lon") Double lon, Pageable pageable);
	
}
