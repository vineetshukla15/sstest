package com.vineet.ss.test.dtq;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class DistributedQueue<IT, OT> {

	private final BlockingQueue<IT> inQueue = new LinkedBlockingQueue<IT>(10);
	private final BlockingQueue<OT> outQueue = new LinkedBlockingQueue<OT>();
	private final ClientProxyProcess<IT, OT> workerProxy;
	private final ServerSocketRunnable serverSocketRunnable;

	public DistributedQueue(ClientProxyProcess<IT, OT> workerProxy, int serverPortNumber) {
		this.workerProxy = workerProxy;
		serverSocketRunnable = new ServerSocketRunnable(serverPortNumber);
	}

	public void start() {
		System.out.println("Starting DistributedProcessQueue");
		new Thread(serverSocketRunnable).start();
	}

	public void put(IT e) throws InterruptedException {
		inQueue.put(e);
	}

	public OT take() throws InterruptedException {
		return outQueue.take();
	}

	class ServerSocketRunnable implements Runnable {
		private final int serverPortNumber;

		public ServerSocketRunnable(int serverPortNumber) {
			this.serverPortNumber = serverPortNumber;
		}

		private final ConcurrentHashMap<SocketChannel, ClientProxy<IT, OT>> clientsMap = new ConcurrentHashMap<>();

		@Override
		public void run() {
			ServerSocketChannel ssc = null;
			try {
				Selector selector = SelectorProvider.provider().openSelector();

				ssc = ServerSocketChannel.open();
				ssc.configureBlocking(false);

				InetSocketAddress isa = new InetSocketAddress(serverPortNumber);
				ssc.socket().bind(isa);

				ssc.register(selector, SelectionKey.OP_ACCEPT);

				while (selector.select() > 0) {
					Set<SelectionKey> selectKeys = selector.selectedKeys();
					Iterator<SelectionKey> keyIterator = selectKeys.iterator();

					while (keyIterator.hasNext()) {
						SelectionKey key = (SelectionKey) keyIterator.next();
						keyIterator.remove();

						if (key.isAcceptable()) {
							ServerSocketChannel readyServerSocketChannel = (ServerSocketChannel) key.channel();

							SocketChannel clientSocketChannel = readyServerSocketChannel.accept();
							clientSocketChannel.configureBlocking(false);
							clientSocketChannel.register(selector, SelectionKey.OP_WRITE);

							clientsMap.put(clientSocketChannel,
									new ClientProxy<IT, OT>(clientSocketChannel, workerProxy, inQueue, outQueue));
						} else if (key.isWritable()) {
							SocketChannel clientSocketChannel = (SocketChannel) key.channel();
							ClientProxy<IT, OT> clientProxy = clientsMap.get(clientSocketChannel);
							clientProxy.write();

							if (!clientProxy.isUnfinishedWrite()) {
								clientSocketChannel.register(selector, SelectionKey.OP_READ);
							}
						} else if (key.isReadable()) {
							SocketChannel clientSocketChannel = (SocketChannel) key.channel();
							ClientProxy<IT, OT> clientProxy = clientsMap.get(clientSocketChannel);
							clientProxy.read();

							if (!clientProxy.isUnfinishedRead()) {
								clientSocketChannel.register(selector, SelectionKey.OP_WRITE);
							}
						}
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				System.out.println("Closing DistributedProcessQueue");
				try {
					if (ssc != null)
						ssc.close();
				} catch (IOException e) {
					System.out.println("Exception when closing ServerSocket");
				}
			}
		}
	}
}
