package oo2.grupo5.turnos.helpers;

public class ViewRouteHelper {
	 private ViewRouteHelper(){}

	    //Servicio
	    public static final String SERVICIO_LIST = "servicio/list";
	    public static final String SERVICIO_FORM = "servicio/form";
	    public static final String SERVICIO_ADMIN_LIST = "servicio/admin-list";
	    public static final String SERVICIO_DETALLE = "servicio/detalle";

	    //Ubicacion
	    public static final String UBICACION_LIST = "ubicacion/list";
	    public static final String UBICACION_ADMIN_LIST = "ubicacion/admin-list";
	    public static final String UBICACION_FORM = "ubicacion/form";
	    public static final String UBICACION_DETALLE = "ubicacion/detalle";

	    //Cliente
	    public static final String CLIENTE_ADMIN_LIST = "cliente/admin-list";
	    public static final String CLIENTE_LIST = "cliente/list";
	    public static final String CLIENTE_FORM = "cliente/form";
	    public static final String CLIENTE_DETALLE = "cliente/detalle";

	    
	    //Empleado
	    public static final String EMPLEADO_LIST = "empleado/list";
	    public static final String EMPLEADO_ADMIN_LIST = "empleado/admin-list";
	    public static final String EMPLEADO_FORM = "empleado/form";
	    public static final String EMPLEADO_DETALLE = "empleado/detalle";

	    //Auth
	    public static final String USER_LOGIN = "authentication/login";
	    public static final String USER_REGISTER = "authentication/register";
	    
	    //Turno
	    public static final String TURNO_EMPLEADO = "turno/elegirEmpleado";
	    public static final String TURNO_SERVICIO = "turno/elegirServicio";
	    public static final String TURNO_FECHA = "turno/elegirFecha";
	    public static final String TURNO_HORARIO = "turno/elegirHorario";
	    public static final String TURNO_CONFIRMAR = "turno/confirmarTurno";
	    public static final String TURNO_LISTA = "turno/listaTurnos";
	      
	    //Home
	    public static final String HOME_INDEX = "home/index";
	    
	    //error
	    public static final String ERROR_NOT_FOUND_SERVICIO = "error/not-found-servicio";
	    public static final String ERROR_NOT_FOUND_CLIENTE = "error/not-found-cliente";
	    public static final String ERROR_NOT_FOUND_EMPLEADO = "error/not-found-empleado";
	    public static final String ERROR_NOT_FOUND_UBICACION = "error/not-found-ubicacion";

}
