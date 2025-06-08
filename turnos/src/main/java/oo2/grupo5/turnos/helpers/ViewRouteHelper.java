package oo2.grupo5.turnos.helpers;

public class ViewRouteHelper {
	 private ViewRouteHelper(){}

	    //Servicio
	    public static final String SERVICIO_LIST = "servicio/list";
	    public static final String SERVICIO_FORM = "servicio/form";
	    public static final String SERVICIO_ADMIN_LIST = "servicio/admin-list";
	    
	    //Ubicacion
	    public static final String UBICACION_LIST = "ubicacion/list";
	    public static final String UBICACION_ADMIN_LIST = "ubicacion/admin-list";
	    public static final String UBICACION_FORM = "ubicacion/form";
	    
	    //Cliente
	    public static final String CLIENTE_ADMIN_LIST = "cliente/admin-list";
	    public static final String CLIENTE_LIST = "cliente/list";
	    public static final String CLIENTE_FORM = "cliente/form";
	    
	    //Empleado
	    public static final String EMPLEADO_LIST = "empleado/list";
	    public static final String EMPLEADO_ADMIN_LIST = "empleado/admin-list";
	    public static final String EMPLEADO_FORM = "empleado/form";
	    
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
}
