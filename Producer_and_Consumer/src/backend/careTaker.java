package backend;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class careTaker {
	private memento m ;

	public void setMemento(List<Machine> machines, List<Qproducer> producers,
						   BlockingQueue<product> products) {
		List<Machine> ms = cloneM(machines);
		List<Qproducer> qs = cloneQ(producers);
		//BlockingQueue<product> ps = cloneP(products);
		this.m = new memento(ms,qs,new LinkedBlockingQueue<product>(products));
	}
	private ExecutorService createService(){
		ExecutorService s = Executors.newCachedThreadPool();
		for(Machine m : m.getMachines())
			m.setService(s);
		for(Qproducer q:m.getProducers())
			q.setService(s);
		return s;
	}
	public program getState(){
		ExecutorService s = createService();
		this.linking();
		return  new program(m.getMachines(), m.getProducers(),s,
				m.getProducts());

	}
	private List<Machine> cloneM(List<Machine> m){
		List<Machine> machines = new LinkedList<Machine>();
		for(int i=0;i<m.size();i++){
			try {
				machines.add((Machine) m.get(i).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return machines;
	}
	private List<Qproducer> cloneQ(List<Qproducer> m){
		List<Qproducer> qs = new LinkedList<Qproducer>();
		for(int i=0;i<m.size();i++){
			try {
				qs.add((Qproducer) m.get(i).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return qs;
	}
	private   void linking(){
		List<Machine> ms ;
		// link q with its ms
		for(Qproducer q : m.getProducers()){
			ms = new LinkedList<Machine>();
			for(Integer a : q.getIdxMs())
				ms.add(m.getMachines().get(a));
			q.setMachines(ms);

		}
		// link m with its qto
		for(Machine M : m.getMachines())
			M.setQto(m.getProducers().get(M.getIdxQto()));

	}

}
