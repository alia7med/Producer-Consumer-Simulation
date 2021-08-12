package backend;

import java.util.concurrent.ExecutorService;

public class Machine implements Runnable, Cloneable {
	private int productNumber;
	private product currentProduct;
	private Qproducer qto;
	private int executeTime;
	private boolean opened;
	private ExecutorService service;
	private Controller controller ;
	public String name;
	private  int idxQto;

	public int getIdxQto() {
		return idxQto;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}


	public void setService(ExecutorService service) {
		this.service = service;
	}

	public Machine(int exexutetime, Qproducer qto, ExecutorService service) {
		this.productNumber = 0;
		this.executeTime = exexutetime;
		this.opened = false;
		this.qto = qto;
		this.service = service;
	}

	synchronized public void run() {
		try {

			opened = true;
			// while (!qfrom.isEmpty() && opened) {
			if (productNumber != 0 && currentProduct != null) {
				controller.drawAll();
				Thread.sleep(executeTime);
				qto.addqProduct(currentProduct);
				controller.drawAll();
				service.execute(qto);
				UpdateState();

			}

			// }
			opened = false;

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * notify the queue that this machine is available
	 */
	private void UpdateState() {

		productNumber = 0;
	}

	public product getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(product currentProduct) {
		this.currentProduct = currentProduct;
		this.productNumber = this.currentProduct.getNumber();
	}

	public int getProductNumber() {
		return productNumber;
	}

	public Qproducer getQto() {
		return qto;
	}

	public void setQto(Qproducer qto) {
		this.qto = qto;
	}

	/**
	 * @return boolean true if this thread is already opend
	 */
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setIdxQto(int idxQto) {
		this.idxQto = idxQto;
	}
}
