package com.cenfotec.graphite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

@SpringBootApplication
public class GraphiteApplication {
	public static void main(String[] args) {
		SpringApplication.run(GraphiteApplication.class, args);
		Graphite graphite = new Graphite(new InetSocketAddress("mygraphiteserver.com", 2003));
		graphite.connect();
		MetricRegistry registry = new MetricRegistry();
		GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
				.prefixedWith("mymetrics.requests")
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.filter(MetricFilter.ALL)
				.build(graphite);
		reporter.start(1, TimeUnit.SECONDS);
		Meter loginRequests = registry.meter(name("successful"));
		loginRequests.mark();
		reporter.report();

	}


}
