package es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions;

/**
 * Excepción que se lanza cuando se intenta crear o cambiar un producto con un
 * atributo no válido, contiene un int con el id de un mensaje descriptivo
 *
 */
public class ProductAttrException extends Exception {
	private static final long serialVersionUID = 8443349472733105703L;
	private final int detailMessageId;

	public ProductAttrException(int detailMessageId) {
		super();
		this.detailMessageId = detailMessageId;
	}

	public int getDetailMessageId() {
		return detailMessageId;
	}
}