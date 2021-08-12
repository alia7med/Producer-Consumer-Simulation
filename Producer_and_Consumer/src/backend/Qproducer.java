package backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Qproducer implements Runnable, Cloneable {
	private BlockingQueue<product> qProducts;
	private List<Machine> machines = new LinkedList<Machine>();
	private ExecutorService service;
	public String name;
	private Controller controller;
	private ArrayList<Integer> idxMs = new ArrayList<Integer>();

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public ArrayList<Integer> getIdxMs() {
		return idxMs;
	}

	public void setService(ExecutorService service) {
		this.service = service;
	}

	public Qproducer(ExecutorService service) throws InterruptedException {
		this.qProducts = new LinkedBlockingQueue<product>();
		this.service = service;
	}

	synchronized public void run() {
		try {
			if (!qProducts.isEmpty()) {
				//controller.drawAll();
			}
			toMachines();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * check if the queuq producer is not empty if so it passes this products to its
	 * available machines
	 */
	synchronized public void toMachines() throws InterruptedException {
		while (!qProducts.isEmpty() && !machines.isEmpty()) {

			product v;

			for (int i = 0; i < machines.size(); i++) {
				if (machines.get(i).getProductNumber() == 0) {
					v = qProducts.peek();
					machines.get(i).setCurrentProduct(v);
					qProducts.take();
					if (!machines.get(i).isOpened()) {
						//controller.drawAll();
						service.execute(machines.get(i));
					}
					break;
				}

			}

		}
	}

	public BlockingQueue<product> getqProducts() {
		return qProducts;
	}

	/**
	 * add product to this queue
	 */
	public void addqProduct(product qProduct) {

		try {
			this.qProducts.put(qProduct);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Machine> getMachines() {
		return machines;
	}

	public void setMachines(List<Machine> machines) {
		this.machines = machines;
	}

	public void addMachine(Machine machine,int idx) {
		this.machines.add(machine);
		this.idxMs.add(idx);
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setqProducts(BlockingQueue<product> qProducts) {
		this.qProducts = qProducts;
	}
}
