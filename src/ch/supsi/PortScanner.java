package ch.supsi;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PortScanner {

	public PortScanner() {
		final ExecutorService es = Executors.newFixedThreadPool(100);
		final String ip = "127.0.0.1";
		final int timeout = 200;
		final List<Future<Boolean>> futures = new ArrayList<>();
		for (int port = 1; port <= 65535; port++) {
			futures.add(portIsOpen(es, ip, port, timeout));
		}
		es.shutdown();
		int openPorts = 0;
		for (final Future<Boolean> f : futures) {
			try {
				if (f.get()) {
					openPorts++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("There are " + openPorts + " open ports on host " + ip + " (probed with a timeout of " + timeout + "ms)");
	}
	public Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
		return es.submit(new Callable<Boolean>() {
			@Override public Boolean call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					System.out.println("Port " + port + " is open");
					return true;
				} catch (Exception ex) {
					return false;
				}
			}
		});
	}
	
//	public void scanPorts(String ip){
//	int[] ports = { 
//		    80, 21, 22,
//		    23, 161, 5060
//		};
//	
//	for (int i=0; i<ports.length; i++) {
//		try {
//			Socket socket = new Socket();
//			socket.connect(new InetSocketAddress("localhost", ports[i]), 1000);
//			socket.close();
//			System.out.println("Port " + ports[i] + " is open");
//		} catch (Exception ex) {
//		}
//	}
//}

}





