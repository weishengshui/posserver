/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.hander.ServerSessionHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.MessageCoderFactory;
import com.chinarewards.utils.appinfo.AppInfo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Bootstrap class contains bootstrapping code.
 * <p>
 * 
 * The important methods are the {@link BootStrap#run()} and {@link #shutdown()}
 * , which should be called during application startup and termination
 * respectively.
 * 
 * @author cyril
 * @since 1.0.0
 */
public class BootStrap {

	Logger log = LoggerFactory.getLogger(getClass());

	private static final String APP_NAME = "POSv2 Server";

	/**
	 * Container for all dependency injection required objects.
	 */
	Injector injector;

	/**
	 * Command line arguments (raw).
	 */
	final String[] args;

	/**
	 * Contains parsed command line arguments, from <code>args</code>.
	 */
	CommandLine cl;
	
	/**
	 * socket server address
	 */
	InetSocketAddress serverAddr;

	/**
	 * acceptor
	 */
	IoAcceptor acceptor;

	/**
	 * Creates an instance of BootStrap.
	 * 
	 * @param args
	 */
	public BootStrap(String... args) {
		this.args = args;
	}

	/**
	 * Obtains the Guice injector created.
	 * 
	 * @return
	 */
	public Injector getInjector() {
		return injector;
	}

	protected void printAppVersion() {
		// print application version.
		AppInfo appInfo = AppInfo.getInstance();
		System.out
				.println(APP_NAME + ": version " + appInfo.getVersionString());

	}

	/**
	 * Starts the bootstrap sequence.
	 */
	public void run() throws Exception {

		// print application version.
		printAppVersion();

		// parse command line arguments.
		parseCmdArgs();

		// print some text.
		log.info("Bootstrapping...");

		// create the dependency injection environment.
		createGuice();

		// save the command line arguments
		initAppPreference();

		// start the persistence services
		PersistService ps = injector.getInstance(PersistService.class);
		ps.start();

		//start mina server
		startMinaServer();
		log.info("Bootstrapping completed");

	}

	/**
	 * Call this method to shutdown the system, including any system initialized
	 * in the {@link #run()} method.
	 * <p>
	 */
	public void shutdown() {

		log.info("Shutdown sequence began");

		// shut down in reverse order as found in method run()!

		shutdownJpa();

		stopMinaServer();
		
		log.info("Shutdown sequence done");
	}

	protected void shutdownJpa() {

		PersistService ps = injector.getInstance(PersistService.class);
		try {
			log.info("Shutting down persistence service");
			ps.stop();
		} catch (Throwable t) {
			log.warn("Error occurred when shutting down persistence service", t);
		}

	}
	
	/**
	 * stop MinaServer
	 */
	protected void stopMinaServer() {
		acceptor.unbind(serverAddr);
		acceptor.dispose();
	}

	/**
	 * Parse command line arguments.
	 */
	protected void parseCmdArgs() {

		Options opts = buildCmdArgOpts();
		CommandLineParser parser = new SimpleParser();
		try {
			cl = parser.parse(opts, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			printHelp(opts);
			System.exit(1);
		}

		// print help message and quit.
		if (cl.hasOption("help")) {
			printHelp(opts);
			System.exit(0);
		}

		// print version and quit.
		if (cl.hasOption("version")) {
			AppInfo appInfo = AppInfo.getInstance();
			System.out.println(BootStrap.APP_NAME + " version " + appInfo.getVersionString());
			System.exit(0);
		}
		
		// create the configuration object.

	}

	/**
	 * United way to print command usage.
	 * 
	 * @param options
	 */
	protected void printHelp(Options options) {
		HelpFormatter f = new HelpFormatter();
		f.printHelp("java -jar posnet.jar", options, true);
	}

	@SuppressWarnings("static-access")
	protected Options buildCmdArgOpts() {

		// for usage, see link: http://commons.apache.org/cli/usage.html

		Options main = new Options();

		// create Options object
		{
			// // Options options = new Options();
			// Options options = main;
			//
			// options.addOption(OptionBuilder.withLongOpt("db").isRequired()
			// .withArgName("database").hasArg().withDescription("数据库名称")
			// .create());
			// options.addOption(OptionBuilder.withLongOpt("dbtype").isRequired()
			// .withArgName("database_type").hasArg()
			// .withDescription("数据库类型。目前支持：mysql").create());
			// options.addOption(OptionBuilder.withLongOpt("dbhost").isRequired()
			// .withArgName("host").hasArg().withDescription("数据库主机/IP地址")
			// .create());
			// options.addOption(OptionBuilder.withLongOpt("dbuser").isRequired()
			// .withArgName("username").hasArg().withDescription("数据库用户名")
			// .create());
			// options.addOption(OptionBuilder.withLongOpt("dbpass").isRequired()
			// .withArgName("password").hasArg().withDescription("数据库密码")
			// .create());
			// // Sina microblog related
			// options.addOption(OptionBuilder.withLongOpt("sinauser")
			// .withArgName("username").isRequired().hasArg()
			// .withDescription("新浪微博用户名").create());
			// options.addOption(OptionBuilder.withLongOpt("sinapass")
			// .withArgName("password").isRequired().hasArg()
			// .withDescription("新浪微博密码").create());
			// //
			// options.addOption(OptionBuilder.withLongOpt("reply-per-loop")
			// .hasArg().withDescription("每个循环的最大数量的回复")
			// .withType(new Integer(1)).create());
			//
			// //
			// options.addOption(OptionBuilder.withLongOpt("pause-per-reply")
			// .hasArg().withDescription("回复间隔时间")
			// .withType(new Integer(1)).create());
			//
			// OptionGroup group = new OptionGroup();
			// // --comment
			// group.addOption(OptionBuilder.withLongOpt("comment")
			// .withArgName("text").isRequired().hasArg()
			// .withDescription("该评论内容将回复所有微博").create("c"));
			//
			// group.addOption(OptionBuilder.withLongOpt("commentfile")
			// .withArgName("path").isRequired().hasArg()
			// .withDescription("评论文件路径").create("cf"));
			// options.addOptionGroup(group);

		}

		// optional parameters
		{
			// --verbose
			main.addOption(OptionBuilder.withLongOpt("verbose").hasArg()
					.withDescription("详细级别.支持0,1,小数等调试信息。默认为0").create());

		}

		// help mesasge
		{
			Option help = OptionBuilder.withArgName("help").withLongOpt("help")
					.withDescription("打印消息").create('h');
			Options options = main;
			options.addOption(help);
		}

		// print version
		{
			main.addOption(OptionBuilder.withLongOpt("version")
					.withDescription("显示版本").create());
		}

		return main;
	}

	/**
	 * Initialize the app preference. This must be done AFTER the Guice injector
	 * is created.
	 */
	protected void initAppPreference() {

		AppPreference pref = injector.getInstance(AppPreference.class);

		// initialize the Application Preference object using command line
		// arguments.
		pref.setWeiboUsername(cl.getOptionValue("sinauser"));
		pref.setWeiboPassword(cl.getOptionValue("sinapass"));
		pref.setDb(cl.getOptionValue("db"));
		pref.setDbType(cl.getOptionValue("dbtype"));
		pref.setDbUsername(cl.getOptionValue("dbuser"));
		pref.setDbPassword(cl.getOptionValue("dbpass"));
		// TODO allow command line arguments for specifying province and city
		pref.setSinaProvinceId("44");
		pref.setSinaCityId("3");
		if (cl.getOptionValue("reply-per-loop") != null) {
			pref.setReplyPerLoop(new Integer(cl
					.getOptionValue("reply-per-loop")));
		}
		if (cl.getOptionValue("pause-per-reply") != null) {
			pref.setPausePerReply(new Integer(cl
					.getOptionValue("pause-per-reply")));
		}

		// set comment and commentfile
		if (cl.getOptionValue("comment") != null) {
			pref.setMicroblogComments(cl.getOptionValues("comment"));
		}
		if (cl.getOptionValue("commentfile") != null) {
			pref.setCommentFile(cl.getOptionValue("commentfile"));
		}

		// verbose level
		{
			String raw = cl.getOptionValue("verbose");
			int level = 0;
			if (raw != null) {
				try {
					level = Integer.parseInt(raw);
				} catch (NumberFormatException e) {
					// use default
				}
				if (level < 0 || level > 1) {
					level = 0;
				}
			}

			com.chinarewards.qqgbvpn.main.LogConfig l = injector
					.getInstance(com.chinarewards.qqgbvpn.main.LogConfig.class);
			l.setVerboseLevel(level);
		}

	}

	/**
	 * Create the guice injector environment.
	 */
	protected void createGuice() {

		log.info("Initializing dependency injection environment...");

		// prepare the persistence module
		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		Properties props = buildJpaProperties();
		jpaModule.properties(props);

		// prepare Guice injector
		log.debug("Bootstraping Guice injector...");
		injector = Guice.createInjector(new AppModule(), jpaModule);

	}
	
	/**
	 * start MinaServer
	 * 
	 * @throws IOException
	 */
	protected void startMinaServer() throws IOException {

		// the TCP port to listen
		int port = new PosNetworkProperties().getSearverPort();

		// =============== server side ===================
		serverAddr = new InetSocketAddress(port);

		acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());

		// not this
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageCoderFactory(injector)));

		// Login filter.
		acceptor.getFilterChain().addLast("login", new LoginFilter());

		acceptor.setHandler(new ServerSessionHandler(injector));
		acceptor.setCloseOnDeactivation(true);

		// acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(serverAddr);

		log.info(
				"Server running, port is {}",
				port);
	}

	protected Properties buildJpaProperties() {

		DatabaseProperties p = new DatabaseProperties();
		return p.getProperties();

	}

}
