package backend;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class memento {
	private List<Machine> machines;
	private List<Qproducer> producers;
	private BlockingQueue<product> products;

	public memento(List<Machine> machines, List<Qproducer> producers, BlockingQueue<product> products) {
		this.machines = machines;
		this.producers = producers;
		this.products = products;
	}

	public List<Machine> getMachines() {
		return machines;
	}

	public List<Qproducer> getProducers() {
		return producers;
	}

	public BlockingQueue<product> getProducts() {
		return products;
	}
}
