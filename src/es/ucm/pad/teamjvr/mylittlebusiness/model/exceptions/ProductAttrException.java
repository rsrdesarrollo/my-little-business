package es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions;

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