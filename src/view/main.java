package view;

import DAO.DAOManager;
import controlador.Controlador;
import models.*;
import comunicaciones.Comunicaciones;
import utils.Menus;
import utils.Utils;

import java.time.LocalDate;
import java.util.*;

public class main {
    public static final Scanner S = new Scanner(System.in);

    public static void main(String[] args) {

        DAOManager dao = DAOManager.getSinglentonInstance();

        Controlador controlador = new Controlador();

        if (!controlador.buscaDatosPrueba()) iniciaDatosPrueba(controlador);

        do {
            Menus.portada();

            System.out.println("            Bienvenidos a nuestra tienda online");
            System.out.println("============================================================");
            Object user = menuInicio(controlador);
            if (user != null) {
                menuUsuario(controlador, user);
            }
        } while (true);
    }

    private static Object menuInicio(Controlador controlador) {
        Object usuarioLogueado = null;
        do {
            if (!controlador.accesoInvitado()) usuarioLogueado = menuSinAccesoInvitado(controlador, usuarioLogueado);
            else usuarioLogueado = menuAccesoInvitado(controlador, usuarioLogueado);
            Utils.pulsaContinuar();
            Utils.limpiarpantalla();
        } while (usuarioLogueado == null);
        return usuarioLogueado;
    }

    // Funcion que si no tenemos acceso
    private static Object menuSinAccesoInvitado(Controlador controlador, Object usuarioLogueado) {
        int op = 0;
        boolean excepcion = false;
        do {
            excepcion = false;
            System.out.print("""
                    1. Registrarse
                    2. Iniciar sesión
                    Marque su opción:""");
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                excepcion = true;
                System.out.println("Introduzca un valor numérico");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (excepcion);

        switch (op) {
            case 1: // Registrase
                registraCliente(controlador);
                break;
            case 2: // Iniciar sesión
                usuarioLogueado = iniciaSesion(controlador);
                if (usuarioLogueado == null) System.out.println("No se ha encontrado ningún usuario...");
                break;
            default: // Op incorrecta
                System.out.println("Opción incorrecta...");
                break;
        }
        return usuarioLogueado;
    }

    // Funcion que si tenemos acceso
    private static Object menuAccesoInvitado(Controlador controlador, Object usuarioLogueado) {
        int op = 0;
        boolean excepcion = false;
        do {
            excepcion = false;
            System.out.print("""
                    1. Ver el catálogo
                    2. Registrarse
                    3. Iniciar sesión
                    Marque su opción:""");
            try {
                op = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                excepcion = true;
                System.out.println("Introduzca un valor numérico");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (excepcion);

        switch (op) {
            case 1: // Ver el catálogo
                verCatalogo(controlador);
                break;
            case 2: // Registrase
                registraCliente(controlador);
                break;
            case 3: // Iniciar sesión
                usuarioLogueado = iniciaSesion(controlador);
                if (usuarioLogueado == null) System.out.println("No se ha encontrado ningún usuario...");
                break;
            default: // Op incorrecta
                System.out.println("Opción incorrecta...");
                break;
        }
        return usuarioLogueado;
    }

    // Función que simula un mock
    private static void iniciaDatosPrueba(Controlador controlador) {
        boolean datosIniciados = false;

        System.out.println("¿Quieres iniciar el programa con datos de prueba? (S/N)");
        String iniciaMockTeclado = S.nextLine();

        if (iniciaMockTeclado.equalsIgnoreCase("s")) {
            datosIniciados = true;
            controlador.mock(datosIniciados);
            System.out.println("""
                    Iniciando usuarios de prueba...
                    Cliente:
                        Email: cliente@cliente
                        Nombre: cliente
                        Clave: cliente
                    
                    Trabajador:
                        Email: trabajador@trabajador
                        Nombre: trabajador
                        Clave: trabajador
                    """);
            Utils.pulsaContinuar();
            Utils.limpiarpantalla();
        } else Utils.limpiarpantalla();

    }

    private static Object iniciaSesion(Controlador controlador) {
        Object usuario = null;
        String email, pass;

        System.out.print("Introduce tu email: ");
        email = S.nextLine();
        System.out.print("Introduce tu clave: ");
        pass = S.nextLine();

        usuario = controlador.login(email, pass);

        return usuario;
    }

    private static void registraCliente(Controlador controlador) {
        System.out.println("¿Estás seguro que deseas registrarte? (S/N)");
        String respuesta = S.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            boolean bandera = false;

            String email = compruebaCorreo(controlador);
            System.out.print("Introduce la clave: ");
            String clave = S.nextLine();
            System.out.print("Introduce tu nombre: ");
            String nombre = S.nextLine();
            System.out.print("Introduce la localidad: ");
            String localidad = S.nextLine();
            System.out.print("Introduce la provincia: ");
            String provincia = S.nextLine();
            System.out.print("Introduce la dirección: ");
            String direccion = S.nextLine();

            int movil = 0;

            do {
                System.out.print("Introduce el número de teléfono: ");
                try {
                    movil = Integer.parseInt(S.nextLine());
                    bandera = true;
                } catch (NumberFormatException e) {
                    System.out.println("Debes introducer números...");
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                }
            } while (!bandera);

            Cliente cliente = controlador.nuevoCliente(email, clave, nombre, localidad, provincia, direccion, movil);
            if (cliente != null) {
                controlador.generaToken(cliente);
                System.out.println("Registrado correctamente...");
                menuClientes(controlador, cliente);
            } else System.out.println("Ha ocurrido un error...");
        }
    }

    // Función en el que ves el catálogo
    private static void verCatalogoAdmin(Controlador controlador) {
        int cont = 0;
        for (Producto producto : controlador.getCatalogo()) {
            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
            if (producto.getRelevancia() > 9)
                System.out.println("╠══════════════════════════════ PROMO ESPECIAL ════════════════════════════════╣");
            System.out.println("║\t- ID: " + producto.getId());
            System.out.println("║\t- Marca: " + producto.getMarca() + " - Modelo: " + producto.getModelo());
            System.out.println("║\t- Descripción: " + producto.getDescripcion());
            System.out.println("║\t- Precio: " + producto.getPrecio());
            cont++;
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
            if (cont == 5) {
                Utils.pulsaContinuar();
                cont = 0;
            }
        }
    }

    // Función en el que ves el catálogo
    private static void verCatalogo(Controlador controlador) {
        int cont = 0;
        for (Producto producto : controlador.getCatalogo()) {
            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║\t- Marca: " + producto.getMarca() + " - Modelo: " + producto.getModelo());
            System.out.println("║\t- Descripción: " + producto.getDescripcion());
            System.out.println("║\t- Precio: " + producto.getPrecio());
            cont++;
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
            if (cont == 5) {
                Utils.pulsaContinuar();
                cont = 0;
            }
        }
    }

    // Función que nos llevara a un menu específico (Admin, Trabajador o Cliente)
    private static void menuUsuario(Controlador controlador, Object user) {
        if (user instanceof Admin) menuAdmin(controlador, user);
        if (user instanceof Trabajador) menuTrabajadores(controlador, user);
        if (user instanceof Cliente) menuClientes(controlador, user);
    }

    // Menu del cliente
    private static void menuClientes(Controlador controlador, Object user) {
        String op;
        Cliente clienteRecibido = (Cliente) user;
        for (Cliente cliente : controlador.getClientes()) {
            if (((Cliente) user).getId() == cliente.getId()) {
                compruebaToken(controlador, cliente);
                if (cliente.isValid()) {
                    do {
                        Menus.menuCliente(controlador, cliente);
                        op = S.nextLine();
                        switch (op) {
                            case "1"://Consultar el catálogo de productos
                                Utils.limpiarpantalla();
                                consultaCatalogo(controlador);
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                            case "2"://Realizar un pedido
                                Utils.limpiarpantalla();
                                realizaPedidoMenu(controlador, cliente);
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                            case "3"://Ver mis pedidos
                                Utils.limpiarpantalla();
                                verMisPedidosCliente(controlador, cliente);
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                            case "4"://Ver mis datos personales
                                Utils.limpiarpantalla();
                                pintaPerfilCliente(cliente);
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                            case "5"://Modificar mis datos personales
                                Utils.limpiarpantalla();
                                modificaDatosPersonalesCliente(controlador, cliente);
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                            case "6":// Salir
                                controlador.guardaCierreSesion(cliente);
                                Utils.animacionFinSesion();
                                Utils.limpiarpantalla();
                                break;
                            default://Opción no existente
                                Utils.limpiarpantalla();
                                System.out.println("Opción incorrecta...");
                                Utils.pulsaContinuar();
                                Utils.limpiarpantalla();
                                break;
                        }
                    } while (cliente.isValid() && !op.equals("6"));
                }
            }
        } // Bucle de clientes
    }

    // Menu del trabajador
    private static void menuTrabajadores(Controlador controlador, Object user) {
        String op;
        Trabajador trabajadorRecibido = (Trabajador) user;
        for (Trabajador trabajador : controlador.getTrabajadores()) {
            if (((Trabajador) user).getId() == trabajador.getId()) {
                do {
                    Menus.menuTrabajador(controlador, trabajador);
                    op = S.nextLine();
                    switch (op) {
                        case "1": //Consultar los pedidos que tengo asignados
                            Utils.limpiarpantalla();
                            consultaPedidoAsignados(controlador, trabajador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "2": //Modificar el estado de un pedido
                            Utils.limpiarpantalla();
                            modificaPedido(controlador, trabajador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "3": //Consultar el catálogo de productos
                            Utils.limpiarpantalla();
                            verCatalogo(controlador);
                            Utils.limpiarpantalla();
                            break;
                        case "4": //Modificar un producto
                            Utils.limpiarpantalla();
                            modificaProducto(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "5": //Ver el histórico de pedidos terminados
                            Utils.limpiarpantalla();
                            historicoPedidosTerminados(controlador, trabajador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "6": //Ver mi perfil
                            Utils.limpiarpantalla();
                            pintaPerfilTrabajador(trabajador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "7": //Modificar mis datos personales
                            Utils.limpiarpantalla();
                            modificaDatosPersonalesTrabajador(controlador, trabajador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "8": //Salir
                            controlador.guardaCierreSesion(trabajador);
                            Utils.animacionFinSesion();
                            Utils.limpiarpantalla();
                            break;
                        default://Opción no existente
                            System.out.println("Opción incorrecta...");
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                    }
                } while (!op.equals("8"));
            }
        } // Bucle de trabajadores
    }

    // Menu del admin
    private static void menuAdmin(Controlador controlador, Object user) {
        String op;
        Admin adminRecibido = (Admin) user;
        for (Admin admin : controlador.getAdmins()) {
            if (((Admin) user).getId() == admin.getId()) {
                do {
                    estadisticasApp(controlador);
                    System.out.println();
                    Menus.menuAdministrador(controlador, admin);
                    op = S.nextLine();
                    switch (op) {
                        case "1": //Ver to el catálogo
                            Utils.limpiarpantalla();
                            verCatalogoAdmin(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "2": //Editar un producto
                            Utils.limpiarpantalla();
                            modificaProducto(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "3": //Ver un resumen de todos los Clientes
                            Utils.limpiarpantalla();
                            resumenClientes(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "4": //Ver un resumen de todos los Pedidos
                            Utils.limpiarpantalla();
                            resumenPedidosAdmin(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "5": // Ver un resumen de todos los Trabajadores
                            Utils.limpiarpantalla();
                            resumenTrabajadores(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "6": //Ver las estadísticas de la aplicación
                            Utils.limpiarpantalla();
                            estadisticasApp(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "7": //Cambiar el estado de un pedido
                            Utils.limpiarpantalla();
                            modificaPedido(controlador, admin);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "8": //Dar de alta un trabajador
                            Utils.limpiarpantalla();
                            altaTrabajador(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "9": //Dar de baja un trabajador
                            Utils.limpiarpantalla();
                            bajaTrabajador(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "10": //Asignar un pedido a un trabajador
                            Utils.limpiarpantalla();
                            asignaPedido(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "11": // Muestra la configuracion de nuestro programa (del properties)
                            Utils.limpiarpantalla();
                            verConfiguracionPrograma(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "12": // Envia todos los pedidos en un Excel
                            Utils.limpiarpantalla();
                            enviaExcel(controlador, admin);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "13": // Realiza copia de seguridad
                            Utils.limpiarpantalla();
                            menuCopiaSeguridad(controlador);
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                        case "14": //Salir
                            controlador.guardaCierreSesion(admin);
                            Utils.animacionFinSesion();
                            Utils.limpiarpantalla();
                            break;
                        default://Opción no existente
                            System.out.println("Opción incorrecta...");
                            Utils.pulsaContinuar();
                            Utils.limpiarpantalla();
                            break;
                    }
                } while (!op.equals("14"));
            }
        } // Bucle de admin
    }

    // Función que envia un correo con todos los pedidos que se han hecho
    private static void enviaExcel(Controlador controlador, Admin admin) {
        if (controlador.getTodosPedidos().isEmpty()) System.out.println("No se han realizado pedidos...");
        else {
            System.out.print("Introduce el correo al que quieres enviarlo: ");
            String correo = S.nextLine();

            controlador.adjuntaCorreosExcel(correo);
        }
    }

    // Menú de copias de seguridad
    private static void menuCopiaSeguridad(Controlador controlador) {
        System.out.print("""
                1. Realizar copia de seguridad
                2. Recuperar copia de seguridad
                Introduce una opción:""");
        String op = S.nextLine();

        switch (op) {
            case "1": //Realizar copia de seguridad
                realizaCopiaSeguridad(controlador);
                break;
            case "2": //Recuperar copia de seguridad
                recuperaCopiaSeguridad(controlador);
                break;
            default:
                System.out.println("Opción incorrecta...");
                break;
        }
    }

    // Función que recupera una copia de seguridad
    private static void recuperaCopiaSeguridad(Controlador controlador) {
        System.out.println("¿Quieres recuperarla de la ruta por defecto? (S/N)");
        String respuesta = S.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            if (controlador.recuperaBackup()) System.out.println("Copia recuperada con éxito...");
            else System.out.println("Error al recuperar la copia de seguridad...");
        } else if (respuesta.equalsIgnoreCase("n")) {
            System.out.print("Introduce la ruta donde está la copia (Ejemplo de ruta: C:/Users/Jl/Desktop/): ");
            String rutaBackup = S.nextLine();
            if (controlador.recuperaBackup(rutaBackup)) System.out.println("La copia ha sido recuperada con éxito...");
            else System.out.println("Error al recuperar la copia de seguridad...");
        } else System.out.println("Respuesta incorrecta...");
    }

    // Función que crea una copia de seguridad
    private static void realizaCopiaSeguridad(Controlador controlador) {
        System.out.println("¿Quieres usar la ruta por defecto? (S/N)");
        String respuesta = S.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            if (controlador.creaBackup()) System.out.println("La copia ha sido un exito...");
            else System.out.println("Ha ocurrido un error...");

        } else if (respuesta.equalsIgnoreCase("n")) {
            System.out.print("Introduce la ruta donde quieres hacer la copia de seguridad (Ejemplo de ruta: C:/Users/Jl/Desktop/):");
            String rutaBackup = S.nextLine();

            if (controlador.creaBackup(rutaBackup)) System.out.println("La copia ha sido un exito...");
            else System.out.println("Ha ocurrido un error...");
        } else System.out.println("Respuesta incorrecta...");
    }

    // Función del administrador que ve la configuración del programa
    private static void verConfiguracionPrograma(Controlador controlador) {
        ArrayList<String> configuracion = controlador.configuracionPrograma();

        for (String linea : configuracion) {
            System.out.println(linea);
        }
    }

    // Función que muestra el historial de pedidos terminados
    private static void historicoPedidosTerminados(Controlador controlador, Trabajador trabajador) {
        if (trabajador.numPedidosCompletados() == 0) System.out.println("No tienes pedidos terminados...");
        else {
            ArrayList<PedidoClienteDataClass> pedidosTerminados = controlador.getPedidosCompletadosTrabajador(trabajador.getId());
            if (pedidosTerminados.isEmpty()) System.out.println("No tienes pedidos terminados...");
            else {
                int cont = 1;

                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                 PEDIDOS TERMINADOS                 ║
                        ╚════════════════════════════════════════════════════╝""");
                for (PedidoClienteDataClass p : pedidosTerminados) {
                    System.out.println(cont + ".- " + p);
                    cont++;
                }
            }
        }
    }

    // Función que hace que el administrador elija un pedido para asignar a un trabajador
    private static void asignaPedido(Controlador controlador) {
        ArrayList<Pedido> pedidosSinAsignar = controlador.pedidosSinTrabajador();

        if (pedidosSinAsignar.isEmpty())
            System.out.println("No se ha realizado ningún pedido o no hay pedidos para asignar...");
        else if (controlador.getTrabajadores().isEmpty()) System.out.println("No hay trabajadores...");
        else {
            Pedido pedidoTemp = null;
            Trabajador trabajadorTemp = null;
            int cont = 1;

            pintaPedidosSinAsignar(controlador, pedidosSinAsignar);

            System.out.print("Introduce el pedido que deseas asignar: ");
            String pedidoSeleccionado = S.nextLine();

            try {
                pedidoTemp = pedidosSinAsignar.get(Integer.parseInt(pedidoSeleccionado) - 1);
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Error al elegir pedido...");
            }

            for (Trabajador t : controlador.getTrabajadores()) {
                pintaResumenTrabajador(cont, t);
                cont++;
            }

            System.out.print("Introduce el trabajador para asignar un pedido: ");
            String trabajadorSeleccionado = S.nextLine();
            try {
                trabajadorTemp = controlador.getTrabajadores().get(Integer.parseInt(trabajadorSeleccionado) - 1);
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Error al elegir trabajador...");
            }


            if (pedidoTemp == null || trabajadorTemp == null) System.out.println("No se han encontrado los datos...");
            else {
                if (controlador.asignaPedido(pedidoTemp.getId(), trabajadorTemp.getId())) {
                    System.out.println("Pedido asignado a " + trabajadorTemp.getNombre() + " con éxito...");
                    Comunicaciones.enviaMensajeTelegramTrabajador(trabajadorTemp.getNombre() + " se te ha asignado el pedido: " + pedidoTemp.getId());

                    PedidoClienteDataClass dataTemp = null;
                    for (Trabajador t : controlador.getTrabajadores()) {
                        for (PedidoClienteDataClass p : controlador.getPedidosAsignadosTrabajador(t.getId())) {
                            if (p.getIdPedido() == pedidoTemp.getId()) dataTemp = p;
                        }
                    }
                    Comunicaciones.enviaCorreoPedidoAsignacion(trabajadorTemp.getEmail(), "ASIGNACIÓN DE PEDIDOS", dataTemp);
                } else System.out.println("Ha ocurrido un error...");
            }

        }
    }

    // Función que pinta los pedidos sin asignar
    private static void pintaPedidosSinAsignar(Controlador controlador, ArrayList<Pedido> pedidosSinAsignar) {
        pintaPedidosSinData(controlador, pedidosSinAsignar);
    }

    // Función que consulta los pedidos asignados del trabajador
    private static void consultaPedidoAsignados(Controlador controlador, Trabajador trabajador) {
        if (trabajador.numPedidosPendientes() == 0) System.out.println("No tienes pedidos pendientes...");
        else {
            ArrayList<PedidoClienteDataClass> pedidosAsignados = controlador.getPedidosAsignadosTrabajador(trabajador.getId());

            if (pedidosAsignados.isEmpty()) System.out.println("No tienes pedidos pendientes...");
            else {
                int cont = 1;

                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                 PEDIDOS ASIGNADOS                  ║
                        ╚════════════════════════════════════════════════════╝""");
                for (PedidoClienteDataClass p : pedidosAsignados) {
                    System.out.println(cont + ".- " + p);
                    cont++;
                }
            }
        }
    }

    // Función que muestra los pedidos que se han realizado
    private static void resumenPedidosAdmin(Controlador controlador) {
        if (controlador.getTodosPedidos().isEmpty()) System.out.println("No se han realizado pedidos...");
        else {
            ArrayList<Pedido> pedidosCancelados = controlador.devuelvePedidosCancelados();
            ArrayList<Pedido> pedidosPendientes = controlador.devuelvePedidosPendientes();

            if (!pedidosCancelados.isEmpty()) {
                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                PEDIDOS CANCELADOS                  ║
                        ╚════════════════════════════════════════════════════╝""");
                for (Pedido p : pedidosCancelados) {
                    System.out.println(p);
                }
            }
            if (!pedidosPendientes.isEmpty()) {
                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                PEDIDOS PENDIENTES                  ║
                        ╚════════════════════════════════════════════════════╝""");
                for (Pedido p : pedidosPendientes) {
                    System.out.println(p);
                }
            }
        }
    }

    // Submenu para realizar un pedido
    private static void realizaPedidoMenu(Controlador controlador, Cliente cliente) {
        String op;

        do {
            System.out.printf("""
                    Actualmente tiene %d productos en su carro.
                    1. Inserta un producto en el carro
                    2. Ver el carro
                    3. Eliminar un producto del carro
                    4. Confirmar el pedido
                    5. Cancelar el pedido
                    6. Salir
                    Introduce una opción:""", cliente.numProductosCarro());
            op = S.nextLine();

            switch (op) {
                case "1": //Inserta un producto en el carro
                    Utils.limpiarpantalla();
                    insertaProducto(controlador, cliente);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "2": //Ver el carro
                    Utils.limpiarpantalla();
                    verCarroCliente(cliente);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "3": //Eliminar un producto del carro
                    Utils.limpiarpantalla();
                    eliminaProducto(controlador, cliente);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "4": //Confirmar el pedido
                    Utils.limpiarpantalla();
                    confirmaPedido(controlador, cliente);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "5": //Cancelar el pedido
                    Utils.limpiarpantalla();
                    cancelaPedido(controlador, cliente);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "6": //Salir
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción incorrecta...");
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
            }
        } while (!op.equals("6"));
    }

    // Función que cancela un pedido del cliente
    private static void cancelaPedido(Controlador controlador, Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("No hay productos en el carro...");
        else {
            System.out.println("¿Deseas cancelar el pedido? (S/N)");
            String cancelaPedido = S.nextLine();

            if (cancelaPedido.equalsIgnoreCase("s"))
                if (controlador.cancelaPedidoCliente(cliente.getId()))
                    System.out.println("El pedido se ha cancelado con éxito...");

        }
    }

    // Función que confirma un pedido del cliente
    private static void confirmaPedido(Controlador controlador, Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("No tienes productos en el carro...");
        else {
            System.out.printf("El total a pagar con IVA es: %.2f\n", cliente.precioCarroSinIva(Utils.IVA));
            System.out.println("¿Deseas confirmar el pedido? (S/N)");
            String confirmaPedido = S.nextLine();

            if (confirmaPedido.equalsIgnoreCase("s")) {
                if (controlador.confirmaPedidoCliente(cliente.getId()))
                    System.out.println("El pedido se ha realizado con éxito...");
                Comunicaciones.enviaCorreoPedidoCliente(cliente.getEmail(), "PEDIDO REALIZADO CON ÉXITO", cliente.getPedidos().getLast());
            } else System.out.println("La confirmación del pedido se ha cancelado...");
        }
    }

    // Función que elimina un producto del carrito
    private static void eliminaProducto(Controlador controlador, Cliente cliente) {
        if (cliente.getCarro().isEmpty()) System.out.println("No hay productos en el carrito...");
        else {
            Producto temp = null;
            int cont = 1;

            System.out.println("""
                    ╔════════════════════════════════════════════════════╗
                    ║                      CARRITO                       ║
                    ╚════════════════════════════════════════════════════╝""");
            for (Producto p : cliente.getCarro()) {
                System.out.println("\t" + cont + ".- " + p.getMarca() + " - " + p.getModelo() + " (" + p.getPrecio() + ")");
                cont++;
            }
            System.out.printf("Total con IVA: %.2f\n\n", cliente.precioCarroSinIva(Utils.IVA));


            System.out.print("Introduce el producto que quieres quitar: ");
            String productoSeleccionado = S.nextLine();

            try {
                temp = cliente.getCarro().get(Integer.parseInt(productoSeleccionado) - 1);
                temp = controlador.buscaProductoById(temp.getId());
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Error al elegir pedido...");
            }

            if (temp == null) System.out.println("No se ha encontrado ningún producto...");
            else {
                if (controlador.quitaProductoCarroCliente(cliente, temp.getId()))
                    System.out.println("El producto se ha eliminado del carrito...");
                else System.out.println("Ha ocurrido un error al añadir el producto al carrito...");
            }
        }
    }

    // Función que ve el carro de un cliente
    private static void verCarroCliente(Cliente cliente) {
        if (cliente.numProductosCarro() == 0) System.out.println("El carro está vacío...");
        else {
            System.out.println("""
                    ╔════════════════════════════════════════════════════╗
                    ║                      CARRITO                       ║
                    ╚════════════════════════════════════════════════════╝""");


            Map<Integer, Integer> contador = new HashMap<>();

            for (Producto p : cliente.getCarro()) {
                contador.put(p.getId(), contador.getOrDefault(p.getId(), 0) + 1);
            }

            Set<Integer> productosImpresos = new HashSet<>();
            for (Producto p : cliente.getCarro()) {
                if (!productosImpresos.contains(p.getId())) {
                    int cantidad = contador.get(p.getId());
                    System.out.println("\t- " + p.getMarca() + " - " + p.getModelo() + " (" + p.getPrecio() + ") (" + cantidad + ")");
                    productosImpresos.add(p.getId());
                }
            }

            System.out.printf("Total con IVA: %.2f\n", cliente.precioCarroSinIva(Utils.IVA));

            /*for (Producto p : cliente.getCarro()) {
                int contProducto = 0;
                for (Producto productoRepetido : cliente.getCarro()) {
                    if (p.getId() == productoRepetido.getId()) contProducto++;
                }
                System.out.println("\t- " + p.getMarca() + " - " + p.getModelo() + " (" + p.getPrecio() + ") (" + (contProducto) + ")");
            }

            System.out.printf("Total con IVA: %.2f\n", cliente.precioCarroSinIva(Utils.IVA));*/
        }
    }

    // Función que inserta un producto en el carro
    private static void insertaProducto(Controlador controlador, Cliente cliente) {
        Producto temp = null;
        String productoSeleccionado;
        int cont = 1;

        for (Producto p : controlador.getCatalogo()) {
            System.out.println(cont + ".- " + p.getMarca() + " - " + p.getModelo() + " (" + p.getPrecio() + ")");
            cont++;
        }

        System.out.print("Introduce el número del producto: ");
        productoSeleccionado = S.nextLine();

        try {
            temp = controlador.getCatalogo().get(Integer.parseInt(productoSeleccionado) - 1);
            temp = controlador.buscaProductoById(temp.getId());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Error al elegir pedido...");
        }

        if (temp != null && controlador.addProductoCarrito(cliente, temp.getId()))
            System.out.println("El producto se ha añadido al carrito correctamente...");
        else System.out.println("No se ha encontrado ningún producto...");
    }

    // Función que simula un submenu para buscar productos en el catálogo
    private static void consultaCatalogo(Controlador controlador) {
        String op;
        do {
            System.out.print("""
                    1. Ver todo el catálogo
                    2. Búsqueda por marca
                    3. Búsqueda por modelo
                    4. Búsqueda por descripción
                    5. Búsqueda por término
                    6. Búsqueda por precio
                    7. Salir
                    Introduce la opción que deseas:""");
            op = S.nextLine();

            switch (op) {
                case "1": //Ver to el catálogo
                    Utils.limpiarpantalla();
                    verCatalogo(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "2": //Búsqueda por marca
                    Utils.limpiarpantalla();
                    buscaProductosByMarca(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "3": //Búsqueda por modelo
                    Utils.limpiarpantalla();
                    buscaProductosByModelo(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "4": //Búsqueda por descripción
                    Utils.limpiarpantalla();
                    buscaProductosByDescripcion(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "5": //Búsqueda por término
                    Utils.limpiarpantalla();
                    buscaProductosByTermino(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "6": //Búsqueda por precio
                    Utils.limpiarpantalla();
                    buscaProductosByPrecio(controlador);
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
                case "7": //Salir
                    break;
                default:
                    System.out.println("Opción incorrecta...");
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                    break;
            }
        } while (!op.equals("7"));

    }

    // Función que busca los productos por precio
    private static void buscaProductosByPrecio(Controlador controlador) {
        float precioMin = Integer.MAX_VALUE, precioMax = Integer.MIN_VALUE;
        boolean continuar = false;

        do {
            System.out.print("Introduce el precio mínimo: ");
            try {
                precioMin = Float.parseFloat(S.nextLine());
                continuar = true;
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (!continuar);

        continuar = false;

        do {
            System.out.print("Introduce el precio máximo: ");
            try {
                precioMax = Float.parseFloat(S.nextLine());
                continuar = true;
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (!continuar);

        pintaProductos(controlador.buscaProductosByPrecio(precioMin, precioMax));
    }

    // Función que busca los productos por un término
    private static void buscaProductosByTermino(Controlador controlador) {
        System.out.print("Introduce la descripción: ");
        String terminoTeclado = S.nextLine();

        pintaProductos(controlador.buscaProductosByTermino(terminoTeclado));
    }

    // Función que busca los productos por su descripción
    private static void buscaProductosByDescripcion(Controlador controlador) {
        System.out.print("Introduce la descripción: ");
        String descripcionTeclado = S.nextLine();

        pintaProductos(controlador.buscaProductosByDescripcion(descripcionTeclado));
    }

    // Función que busca los productos por su modelo
    private static void buscaProductosByModelo(Controlador controlador) {
        System.out.print("Introduce el nombre del modelo: ");
        String modeloTeclado = S.nextLine();

        pintaProductos(controlador.buscaProductosByModelo(modeloTeclado));
    }

    // Función que busca los productos por su marca
    private static void buscaProductosByMarca(Controlador controlador) {
        System.out.print("Introduce el nombre de la marca: ");
        String marcaTeclado = S.nextLine();

        pintaProductos(controlador.buscaProductosByMarca(marcaTeclado));
    }

    // Función que pinta un producto de los productos
    private static void pintaProductos(ArrayList<Producto> productos) {
        int cont = 0;
        for (Producto producto : productos) {
            System.out.println();
            System.out.println("============================================================================\n");
            if (producto.getRelevancia() > 9) System.out.println("************ Promo especial ************");
            System.out.println("- ID: " + producto.getId());
            System.out.println("- Marca: " + producto.getMarca() + " - Modelo: " + producto.getModelo());
            System.out.println("- Descripción: " + producto.getDescripcion());
            System.out.println("- Precio: " + producto.getPrecio());
            cont++;
            System.out.println("\n============================================================================\n");
            if (cont == 5) {
                Utils.pulsaContinuar();
                cont = 0;
            }
        }
    }

    //Función que pinta todos los pedidos realizados por un cliente concreto
    private static void verMisPedidosCliente(Controlador controlador, Cliente cliente) {
        if (cliente.getPedidos().isEmpty()) System.out.println("No has realizado ningún pedido...");
        else {
            ArrayList<Pedido> pedidosCancelados = controlador.verPedidosCancelados(cliente.getId());
            ArrayList<Pedido> pedidosPendientes = controlador.verPedidosPendientes(cliente.getId());

            if (!pedidosCancelados.isEmpty()) {
                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                PEDIDOS CANCELADOS                  ║
                        ╚════════════════════════════════════════════════════╝""");
                for (Pedido p : pedidosCancelados) {
                    System.out.println(p);
                }
            }
            if (!pedidosPendientes.isEmpty()) {
                System.out.println("""
                        ╔════════════════════════════════════════════════════╗
                        ║                PEDIDOS PENDIENTES                  ║
                        ╚════════════════════════════════════════════════════╝""");
                for (Pedido p : pedidosPendientes) {
                    System.out.println(p);
                }
            }
        }
    }

    // Función que modifica los datos personales de un Cliente
    private static void modificaDatosPersonalesCliente(Controlador controlador, Cliente cliente) {
        int telefonoTeclado = -2;

        System.out.print("""
                MODIFICACIÓN DE DATOS:
                Introduce el nuevo nombre del cliente:\s""");
        String nombreTeclado = S.nextLine();
        System.out.print("Introduce la nueva clave del cliente: ");
        String contraTeclado = S.nextLine();
        String correoTeclado = compruebaCorreo(controlador);
        System.out.print("Introduzca la nueva localidad (Introduzca 'no' para dejar mismos datos): ");
        String localidadTeclado = S.nextLine();
        System.out.print("Introduzca la nueva provincia (Introduzca 'no' para dejar mismos datos): ");
        String provinciaTeclado = S.nextLine();
        System.out.print("Introduzca la nueva dirección (Introduzca 'no' para dejar mismos datos): ");
        String direccionTeclado = S.nextLine();
        do {
            System.out.print("Introduzca su nuevo teléfono (-1 para dejar mismos datos): ");
            try {
                telefonoTeclado = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un valor numérico...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (telefonoTeclado == -2);

        if (!controlador.modificaDatosPersonalesCliente(nombreTeclado, contraTeclado, correoTeclado, localidadTeclado,
                provinciaTeclado, direccionTeclado, telefonoTeclado, cliente))
            System.out.println("Ha ocurrido un error...");
        else {
            //Generamos el token después de la modificación de datos
            controlador.generaToken(cliente);

            System.out.println("Tus datos han sido modificados...");
            Utils.pulsaContinuar();
            Utils.limpiarpantalla();
            // Hacemos que introduzca el token nuevo, ya que ha cambiado sus datos personales
            compruebaToken(controlador, cliente);
        }
    }

    private static void pintaPerfilCliente(Cliente c) {
        System.out.printf("""
                ╔═════════════════════════════════════════════════════════════════════╗
                ║                           PERFIL CLIENTE                            ║
                ╠═════════════════════════════════════════════════════════════════════╣
                ║ Nombre: %s
                ║ Email: %s
                ║ Localidad: %s
                ║ Provincia: %s
                ║ Dirección: %s
                ║ Número de Teléfono: %d
                ╚═════════════════════════════════════════════════════════════════════╝
                """, c.getNombre(), c.getEmail(), c.getLocalidad(), c.getProvincia(), c.getDireccion(), c.getMovil());
    }

    //Métdo de que comprueba el token de un cliente
    private static void compruebaToken(Controlador controlador, Cliente cliente) {
        //Clientes
        if (!cliente.isValid()) {
            System.out.print("Introduce tu token para registrarte: ");
            String tokenTeclado = S.nextLine();
            if (controlador.compruebaToken(cliente, tokenTeclado)) {
                System.out.println("Token correcto...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            } else System.out.println("Token incorrecto...");
        }
    }

    // Función que da a elegir como se quiere dar de baja al Trabajador, si mediante su ID o en un menú de selección
    private static void bajaTrabajador(Controlador controlador) {
        if (controlador.getTrabajadores().isEmpty()) System.out.println("No hay trabajadores para dar de baja...");
        else {
            Trabajador temp = eligeTrabajadorByMenu(controlador);

            if (temp == null) System.out.println("No se ha encontrado ningún trabajador...");
            else {
                System.out.println("El trabajador es: ");
                System.out.println(temp);

                System.out.print("¿Deseas eliminar al trabajador? (S/N): ");
                String respuesta = S.nextLine();

                if (respuesta.equalsIgnoreCase("N")) System.out.println("Cancelando baja...");
                else if (respuesta.equalsIgnoreCase("S")) {
                    if (temp.numPedidosPendientes() == 0 && controlador.borraTrabajador(temp)) {
                        System.out.println("Dado de baja correctamente...");

                        ArrayList<Pedido> pedidosSinAsignar = controlador.pedidosSinTrabajador();
                        Trabajador candidato = controlador.buscaTrabajadorCandidatoParaAsignar();

                        if (!pedidosSinAsignar.isEmpty() && candidato != null) {
                            for (Pedido p : pedidosSinAsignar) {
                                controlador.asignaPedido(p.getId(), candidato.getId());
                            }

                            for (Pedido pedido : controlador.getTodosPedidos()) {
                                for (PedidoClienteDataClass pDataClass : controlador.getPedidosAsignadosTrabajador(candidato.getId())) {
                                    if (pedido.getId() == pDataClass.getIdPedido())
                                        Comunicaciones.enviaCorreoPedidoAsignacion(candidato.getEmail(), "ASIGNACIÓN DE PEDIDOS", pDataClass);
                                }
                            }

                        }
                    } else System.out.println("No se ha podido borrar el trabajador...");
                } else System.out.println("Respuesta incorrecta...");
            }

        }
    }

    // Función que elimina un trabajador mediante un menú de selección
    private static Trabajador eligeTrabajadorByMenu(Controlador controlador) {
        int eligeTrabajador = -1, cont = 1;

        for (Trabajador t : controlador.getTrabajadores()) {
            System.out.println(cont + ".- ID: " + t.getId() + " ; Nombre: " + t.getNombre() + " ; Correo: " + t.getEmail() + " ; Móvil: " + t.getMovil());
            cont++;
        }

        System.out.print("Selecciona al trabajador: ");
        try {
            eligeTrabajador = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número...");
        }
        if (eligeTrabajador > 0 && eligeTrabajador <= controlador.getTrabajadores().size())
            return controlador.getTrabajadores().get(eligeTrabajador - 1);

        return null;
    }

    // Función que muestra el resumen de los trabajadores al admin
    private static void resumenTrabajadores(Controlador controlador) {
        if (controlador.getTrabajadores().isEmpty()) System.out.println("No hay trabajadores dados de alta...");
        else {
            System.out.println("""
                    ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                  RESUMEN TRABAJADORES                                       ║
                    ╠═════════════════════════════════════════════════════════════════════════════════════════════╣""");

            for (Trabajador t : controlador.getTrabajadores()) {
                System.out.println("║ ID: " + t.getId());
                System.out.println("║ Nombre: " + t.getNombre());
                System.out.println("║ Correo: " + t.getEmail());
                System.out.println("║ Móvil: " + t.getMovil());
                System.out.println("╠═════════════════════════════════════════════════════════════════════════════════════════════╣");
            }
            System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }

    // Función que pinta el resumen de un solo trabajador
    private static void pintaResumenTrabajador(int cont, Trabajador t) {
        System.out.println(cont + ".- ID: " + t.getId() + " .- " + t.getNombre() + "; Móvil: " + t.getMovil() + " - Correo: " + t.getEmail() + "\n");
    }

    // Función que muestra el resumen de los clientes al admin
    private static void resumenClientes(Controlador controlador) {
        if (controlador.getClientes().isEmpty()) System.out.println("No hay clientes registrados...");
        else {
            System.out.println("""
                    ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                     RESUMEN CLIENTES                                        ║
                    ╠═════════════════════════════════════════════════════════════════════════════════════════════╣""");

            for (Cliente c : controlador.getClientes()) {
                System.out.println("║ ID: " + c.getId());
                System.out.println("║ Nombre: " + c.getNombre());
                System.out.println("║ Localidad: " + c.getLocalidad() + "(" + c.getProvincia() + ")");
                System.out.println("║ Correo: " + c.getEmail());
                System.out.println("║ Móvil: " + c.getMovil());
                System.out.println("╠═════════════════════════════════════════════════════════════════════════════════════════════╣");
            }
            System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }

    // Función que muestra el número de clientes, trabajadores, pedidos, pedidos pendientes, pedidos completados o cancelados
    // y pedidos sin asignar
    private static void estadisticasApp(Controlador controlador) {
        String mensaje;
        if (controlador.numPedidosSinTrabajador() == 0) mensaje = "No hay pedidos para asignar.";
        else if (controlador.numPedidosSinTrabajador() == 1) {
            mensaje = "Tenemos " + controlador.numPedidosSinTrabajador() + " pedido sin asignar. Debe asignarlos a un trabajador.";
        } else
            mensaje = "Tenemos " + controlador.numPedidosSinTrabajador() + " pedidos sin asignar. Debe asignarlos a un trabajador.";

        System.out.println("Bienvenido Administrador. " + mensaje);
        System.out.printf(""" 
                        ╔════════════════════════════════════════════════════╗
                        ║               Estadísticas de la APP               ║
                        ╠════════════════════════════════════════════════════╣
                        ║ Número de clientes:%4d                            ║
                        ║ Número de trabajadores:%4d                        ║
                        ║ Número de pedidos:%4d                             ║
                        ║ Número de pedidos pendientes:%4d                  ║
                        ║ Número de pedidos completados o cancelados:%4d    ║
                        ║ Número de pedidos sin asignar:%4d                 ║
                        ╚════════════════════════════════════════════════════╝
                        """, controlador.getClientes().size(), controlador.getTrabajadores().size(),
                controlador.numPedidosTotales(), controlador.numPedidosPendientes(), controlador.numPedidosCompletadosCancelados(),
                controlador.pedidosSinTrabajador().size());
    }

    // Función que pide los datos para crear un nuevo trabajador
    private static void altaTrabajador(Controlador controlador) {
        boolean bandera = false;

        System.out.print("Introduce el nombre del nuevo trabajador: ");
        String nombreTeclado = S.nextLine();
        System.out.print("Introduce la clave del nuevo trabajador: ");
        String pass = S.nextLine();
        String email = compruebaCorreo(controlador);

        int movil = 0;
        do {
            System.out.print("Introduce el móvil del trabajador: ");
            try {
                movil = Integer.parseInt(S.nextLine());
                bandera = true;
            } catch (NumberFormatException e) {
                System.out.println("Debes introducer números...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (!bandera);

        if (controlador.nuevoTrabajador(email, pass, nombreTeclado, movil))
            System.out.println("Trabajador dado de alta correctamente...");
        else System.out.println("Ha ocurrido un error...");
    }

    // Función que modifica los datos de un trabajador
    private static void modificaDatosPersonalesTrabajador(Controlador controlador, Trabajador trabajador) {
        int telefonoTeclado = -2;

        System.out.print("""
                MODIFICACIÓN DE DATOS:
                Introduce el nuevo nombre del trabajador:\s""");
        String nombreTeclado = S.nextLine();
        System.out.print("Introduce la nueva clave del trabajador: ");
        String contraTeclado = S.nextLine();
        String correoTeclado = compruebaCorreo(controlador);
        do {
            System.out.print("Introduzca su nuevo teléfono (-1 para dejar mismos datos): ");
            try {
                telefonoTeclado = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un valor numérico...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (telefonoTeclado == -2);

        if (!controlador.modificaDatosPersonalesTrabajador(nombreTeclado, contraTeclado,
                correoTeclado, telefonoTeclado, trabajador)) System.out.println("Ha ocurrido un error...");
        else System.out.println("Tus datos han sido modificados...");
    }

    // Función que crea un correo con sus validaciones
    private static String compruebaCorreo(Controlador controlador) {
        boolean correoDistinto = false;
        String correoTeclado;
        do {  //Bucle que comprobará que el correo nuevo no se repita con el de otra persona
            System.out.print("Introduzca el correo electrónico:");
            correoTeclado = S.nextLine();
            if (!controlador.compruebaCorreos(correoTeclado)) {
                correoDistinto = true;
                if (!correoTeclado.contains("@") || (!correoTeclado.contains(".com") && !correoTeclado.contains(".es"))) {
                    System.out.println("El correo esta mal introducido...");
                    correoDistinto = false;
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                }
            } else {
                System.out.println("Este correo ya está en uso, introduzca uno nuevo...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (!correoDistinto);
        return correoTeclado;
    }

    // Función que pinta el perfil de un trabajador
    private static void pintaPerfilTrabajador(Trabajador t) {
        System.out.printf("""
                ╔═════════════════════════════════════════════════════════════════════╗
                ║                           PERFIL TRABAJADOR                         ║
                ╠═════════════════════════════════════════════════════════════════════╣
                ║ Nombre: %s
                ║ Email: %s
                ║ Número de Teléfono: %d
                ║ Número de Pedidos Asignados: %d
                ╚═════════════════════════════════════════════════════════════════════╝
                """, t.getNombre(), t.getEmail(), t.getMovil(), t.numPedidosPendientes());
    }

    // Función que modifica el estado de un pedido, añade un comentario al pedido o cambia fecha
    private static void modificaPedido(Controlador controlador, Object usuario) {
        Trabajador trabajador = buscaTrabajador(controlador, usuario);
        Admin admin = buscaAdmin(controlador, usuario);
        Pedido temp = null;
        if (trabajador != null) temp = seleccionaPedidoTrabajador(controlador, trabajador);
        if (admin != null) temp = seleccionaPedidoAdmin(controlador, admin);

        if (temp == null) System.out.println("No se ha encontrado ningún pedido...");
        else {
            boolean pedidoModificado = false;

            System.out.println("¿Quieres modificar el estado del pedido? (S/N)");
            String respuestaEst = S.nextLine();
            if (respuestaEst.equalsIgnoreCase("s")) {
                modificaEstadoPedido(controlador, temp);
                pedidoModificado = true;
            }

            System.out.println("¿Quieres añadir un comentario al pedido? (S/N)");
            String respuestaCom = S.nextLine();
            if (respuestaCom.equalsIgnoreCase("s")) {
                aniadeComentarioPedido(controlador, temp);
                pedidoModificado = true;
            }

            System.out.println("¿Quieres modificar la fecha de entrega del pedido? (S/N)");
            String respuestaFec = S.nextLine();
            if (respuestaFec.equalsIgnoreCase("s")) {
                cambiaFechaPedido(controlador, temp);
                pedidoModificado = true;
            }

            if (pedidoModificado) {
                System.out.println("Pedido modificado correctamente...");
                controlador.enviaCorreoPedidoModificadoCliente(temp.getId());
            }
        }
    }

    // Función que cambia la fecha de un pedido
    private static void cambiaFechaPedido(Controlador controlador, Pedido temp) {
        LocalDate nuevaFecha = nuevaFecha();
        if (controlador.cambiaFechaEntregaPedido(temp.getId(), nuevaFecha))
            System.out.println("Fecha actualizada correctamente...");
        else System.out.println("Ha ocurrido un error...");
    }

    // Función que devuelve una fecha
    private static LocalDate nuevaFecha() {
        int dia = -1, mes = -1, anio = -1;

        do {
            System.out.print("Introduce el día de la nueva fecha: ");
            try {
                dia = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un valor numérico...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (dia < 1 || dia > 31);

        do {
            System.out.print("Introduce el mes de la nueva fecha: ");
            try {
                mes = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un valor numérico...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (mes < 1 || mes > 12);

        do {
            System.out.print("Introduce el año de la nueva fecha: ");
            try {
                anio = Integer.parseInt(S.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un valor numérico...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (anio < 2025 || anio > 2090);

        return LocalDate.of(anio, mes, dia);
    }

    // Función que busca al admin
    private static Admin buscaAdmin(Controlador controlador, Object usuario) {
        for (Admin a : controlador.getAdmins()) {
            if (usuario.equals(a)) return a;
        }
        return null;
    }

    // Función que busca un trabajador
    private static Trabajador buscaTrabajador(Controlador controlador, Object usuario) {
        for (Trabajador t : controlador.getTrabajadores()) {
            if (usuario.equals(t)) return t;
        }
        return null;
    }

    // Función que añade un comentario de un pedido
    private static void aniadeComentarioPedido(Controlador controlador, Pedido temp) {
        System.out.print("Introduce el comentario para el pedido: ");
        String comentarioTeclado = S.nextLine();

        // Le enviamos al cliente que su correo ha sido modificado
        if (controlador.cambiaComentarioPedido(temp.getId(), comentarioTeclado))
            System.out.println("Se ha añido un comentario al pedido correctamente...");
        else System.out.println("Ha ocurrido un error...");
        Utils.pulsaContinuar();
        Utils.limpiarpantalla();
    }

    // Función que modifica el estado de un pedido
    private static void modificaEstadoPedido(Controlador controlador, Pedido temp) {
        int estadoTeclado = -1;
        boolean continuar = false;
        pintaPedidoUnico(temp);
        do {
            System.out.print("""
                    Selecciona el nuevo estado:
                    1. En preparación
                    2. Enviado
                    3. Cancelado
                    Introduce el nuevo estado:""");
            try {
                estadoTeclado = Integer.parseInt(S.nextLine());
                continuar = true;
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número...");
                Utils.pulsaContinuar();
                Utils.limpiarpantalla();
            }
        } while (!continuar);
        if (controlador.cambiaEstadoPedido(temp.getId(), estadoTeclado))
            System.out.println("El estado se ha modificado con éxito...");
        else System.out.println("Ha ocurrido un error...");
        Utils.pulsaContinuar();
        Utils.limpiarpantalla();
    }

    // Función que selecciona un pedido desde el administrador
    private static Pedido seleccionaPedidoAdmin(Controlador controlador, Admin admin) {
        ArrayList<Pedido> pedidos = controlador.getTodosPedidosSinGestionar();

        if (pedidos == null) return null;
        if (pedidos.isEmpty()) return null;

        pintaPedidosSinData(controlador, pedidos);

        System.out.print("Introduce el pedido: ");
        String pedidoSeleccionado = S.nextLine();

        Pedido pedidoElegido = null;
        try {
            pedidoElegido = pedidos.get(Integer.parseInt(pedidoSeleccionado) - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Error al elegir pedido...");
        }

        if (pedidoElegido == null) return null;

        return controlador.buscaPedidoById(pedidoElegido.getId());
    }

    // Función que pinta un pedido sin data
    private static void pintaPedidosSinData(Controlador controlador, ArrayList<Pedido> pedidos) {
        int cont = 1;
        if (pedidos.isEmpty()) System.out.println("No tienes pedidos...");
        else {
            for (Pedido p : pedidos) {
                System.out.println(cont + ".- " + p);
                cont++;
                Utils.pulsaContinuar();
            }
        }
    }

    // Función que pinta un único pedido
    private static void pintaPedidoUnico(Pedido temp) {
        System.out.println(temp);
    }

    // Función de menú de selección de un pedido
    private static Pedido seleccionaPedidoTrabajador(Controlador controlador, Trabajador trabajador) {
        ArrayList<PedidoClienteDataClass> pedidosData = controlador.getPedidosAsignadosTrabajador(trabajador.getId());
        int cont = 1;

        if (pedidosData == null) return null;
        if (pedidosData.isEmpty()) return null;

        for (PedidoClienteDataClass p : pedidosData) {
            System.out.println(cont + " .- " + p);
            cont++;
            Utils.pulsaContinuar();
        }

        System.out.print("Introduce el pedido: ");
        String pedidoSeleccionado = S.nextLine();

        PedidoClienteDataClass pedidoData = null;
        try {
            pedidoData = pedidosData.get(Integer.parseInt(pedidoSeleccionado) - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Error al elegir pedido...");
        }

        if (pedidoData == null) return null;
        Pedido pedidoTemp = controlador.buscaPedidoById(pedidoData.getIdPedido());

        if (pedidoTemp == null) return null;
        return pedidoTemp;
    }

    // Función que modifica un producto del catálogo
    public static void modificaProducto(Controlador controlador) {
        int id = -1;
        float precioTeclado = -2;
        boolean bandera = false;


        System.out.print("Selecciona la ID del producto: ");
        try {
            id = Integer.parseInt(S.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un valor númerico...");
        }
        Producto producto = controlador.buscaProductoById(id);

        if (producto == null) System.out.println("No se ha encontrado ningún producto");
        else {
            System.out.println("Has elegido: " + producto.getDescripcion() + " - " + producto.getMarca() + " - " + producto.getModelo()
                    + " - " + producto.getPrecio() + "€");

            System.out.print("Introduzca una marca nueva (introduce 'no' para dejar el anterior): ");
            String marcaTeclado = S.nextLine();
            System.out.print("Introduzca un modelo nuevo (introduce 'no' para dejar el anterior): ");
            String modeloTeclado = S.nextLine();
            System.out.print("Introduzca una descripción nueva (introduce 'no' para dejar el anterior): ");
            String descripcionTeclado = S.nextLine();
            do {
                System.out.print("Introduzca un precio nuevo (introduce '-1' para dejar el anterior): ");
                try {
                    precioTeclado = Float.parseFloat(S.nextLine());
                    bandera = true;
                } catch (NumberFormatException e) {
                    System.out.println("Debes introducir un número");
                    Utils.pulsaContinuar();
                    Utils.limpiarpantalla();
                }
            } while (!bandera);

            Producto modificado = new Producto(producto.getId(),
                    ((!marcaTeclado.equalsIgnoreCase("no")) ? marcaTeclado : producto.getMarca()),
                    ((!modeloTeclado.equalsIgnoreCase("no")) ? modeloTeclado : producto.getModelo()),
                    ((!descripcionTeclado.equalsIgnoreCase("no")) ? descripcionTeclado : producto.getDescripcion()),
                    ((precioTeclado < 0) ? producto.getPrecio() : precioTeclado), producto.getRelevancia());

            if (controlador.editarProducto(modificado)) System.out.println("Producto modificado con éxito...");
            else System.out.println("Ha ocurrido un error...");
        }
    }
}