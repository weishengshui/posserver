/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

/**
 * Main executable program. Should contains bootstrap code and parameter parsing
 * only.
 * <p>
 * This code should be kept as short as possible.
 * 
 * @author cyril
 * @since 1.0.0
 */
public class Main {

	/**
	 * Java shutdown hook. Let the application gracefully handles termination
	 * when the operating system request this application to terminate.
	 */
	private static class AppShutdownThread extends Thread {

		Logger log = LoggerFactory.getLogger(Main.class);

		final Application app;
		final Thread mainThread;

		/**
		 * 
		 * @param app
		 * @param mainThread
		 *            the thread which runs the main application.
		 */
		public AppShutdownThread(Application app, Thread mainThread) {
			this.app = app;
			this.mainThread = mainThread;
		}

		@Override
		public void run() {

			log.info("Shutdown hook triggered.");

			// request the application to stop.
			log.info("Requesting message sender to stop...");

			app.requestShutdown();
			mainThread.interrupt();

			log.info("Waiting for main thread to stop...");
			try {
				mainThread.join();
			} catch (Throwable t) {
			}

			log.info("Shutdown hook completed.");
		}

	}

	/**
	 * Main routine. This function should be as simple as possible.
	 * <p>
	 * 
	 * Duty of this function:
	 * <ol>
	 * <li>Bootstrap</li>
	 * <li>Run application.</li>
	 * </ol>
	 * 
	 * @param args
	 *            any program arguments.
	 */
	public static void main(String[] args) {

		//
		// Developers: Please keep this code as short and clean as possible!
		//

		//
		// bootstrap
		//
		BootStrap boot = new BootStrap(args);
		try {
			
			boot.run();
			
		} catch (Throwable e) {
			System.err
					.println("An unexpected error occurred when bootstrapping. See ");
			e.printStackTrace();
			System.exit(1);
		}
		// get the injector (dependency injection readied).
		Injector injector = boot.getInjector();

		//
		// Start application.
		//
		Application app = injector.getInstance(Application.class);

		// install the shutdown hook to allow graceful closing upon
		// abrupt termination.
		AppShutdownThread t = new AppShutdownThread(app, Thread.currentThread());
		Runtime.getRuntime().addShutdownHook(t);

		//
		// run the "main" application!
		//
		try {
			
			app.run();
			
		} catch (Throwable e) {
			// don't let it die without gracefully closing the application.
			System.err.println("Unexpected application termination detected.");
			System.err.println("Stack trace:");
			e.printStackTrace(System.err);
		}

		// gracefully release any resources.
//		boot.shutdown();

		// done
		System.out.println("Main completed");

	}

}
