package backend;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class program {
	private Boolean endProgram = false;
	private List<Machine> machines;
	private List<Qproducer> producers;
	private ExecutorService service;
	private BlockingQueue<product> products;
	private int fullsize;
	private Random random = new Random();
	private Controller controller;

	public program(List<Machine> machines, List<Qproducer> producers, ExecutorService service,
			BlockingQueue<product> products) {
		this.machines = machines;
		this.producers = producers;
		this.service = service;
		this.products = products;
		this.fullsize = this.products.size();

	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Boolean getEndProgram() {
		return endProgram;
	}

	public void openThreads() {
		for (int i = 0; i < machines.size(); i++){
			machines.get(i).setController(controller);
		}
		for (int i = 0; i < producers.size(); i++){
			producers.get(i).setController(controller);
		}
		service.execute(new Runnable() {
			public void run() {

				end();

			}
		});

		while (!products.isEmpty()) {
			int k = random.nextInt(products.size() / 2 + 1) + 1;
			for (int j = 0; j < k; j++) {
				try {
					producers.get(0).addqProduct(products.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			service.execute(producers.get(0));
			try {
				long s = random.nextInt(3000) + 2000;
				Thread.sleep(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void end() {
		while (true) {
			BlockingQueue<product> x = producers.get(producers.size() - 1).getqProducts();
			if (x.size() == fullsize) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				controller.drawAll();
				endProgram = true;
				service.shutdown();
				break;
			}
		}
	}

	public List<Machine> getMachines() {
		return machines;
	}

	public List<Qproducer> getProducers() {
		return producers;
	}

	public ExecutorService getService() {
		return service;
	}

	public BlockingQueue<product> getProducts() {
		return products;
	}
}
