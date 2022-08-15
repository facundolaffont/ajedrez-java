package ajedrez.modelo;

import java.io.Serializable;

public enum EnumError implements Serializable {
	SIN_ERROR, // Valor de inicialización para los objetos Error.
	PARTIDA_EN_PAUSA,
	TURNO_INVALIDO,
	PIEZA_INVALIDA,
	NO_HAY_PIEZA,
	NO_HAY_PARTIDA,
	CASILLA_INVALIDA,
	SALTO_NO_PERMITIDO,
	PIEZA_PROPIA,
	RIESGO_DE_JAQUE,
	OK_MOVIMIENTO_Y_CAPTURA,
	CASILLA_OCUPADA,
	MOVIMIENTO_INVALIDO,
	MOVIMIENTO_INVALIDO_POR_JAQUE,
	CORONACION_EN_PROCESO,
	CAMBIO_DE_COLOR_CANCELADO,
	ESTADO_DE_PARTIDA_INCORRECTO,
	FALTAN_JUGADORES,
	SELECCION_REMOVIDA,
	TABLA_CANCELADA,
	SIN_IP_CLIENTE,
	SIN_PUERTO_CLIENTE,
	SIN_IP_SERVER,
	SIN_PUERTO_SERVER,
	VALOR_NULO,
	MENOR_A_CERO,
	VALOR_INVALIDO,
	ERROR_DE_COMUNICACION,
	ERROR_DE_RMI,
	JUGADOR_EXISTENTE,
	SALA_LLENA,
	SIN_CONEXION,
	CONEXION_EXISTENTE,
	CLIENTE_SIN_CONFIGURAR,
	ERROR_DESCONOCIDO,
}