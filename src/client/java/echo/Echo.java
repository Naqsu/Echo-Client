package echo;

import echo.api.event.EventBus;
import echo.api.module.ModuleManager;
import echo.core.config.ConfigManager;
import echo.core.config.PresetManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Echo {

	public static final String NAME = "Echo";
	public static final String VERSION = "1.0.0";
	private static final Echo INSTANCE = new Echo();
	private final Logger logger = LogManager.getLogger(NAME);

	// Yarn: gameDirectory -> runDirectory
	public static final File CLIENT_DIR = new File(Wrapper.getMinecraft().runDirectory, NAME);

	private EventBus eventBus;
	private ModuleManager moduleManager;
	private ConfigManager configManager;
	private PresetManager presetManager;
	private echo.ui.web.HotSwapService hotSwapService;

	private Echo() {}

	public static Echo getInstance() {
		return INSTANCE;
	}

	public void init() {
		logger.info("[Echo] Starting initialization...");
		Wrapper.getMinecraft().getWindow().setTitle("Echo Client 1.20.1 | Yarn");
		echo.ui.web.HtmlLoader.init();

		this.hotSwapService = new echo.ui.web.HotSwapService();
		this.hotSwapService.start();

		if (!CLIENT_DIR.exists()) {
			CLIENT_DIR.mkdirs();
		}

		this.eventBus = new EventBus();
		this.moduleManager = new ModuleManager();
		this.moduleManager.init();

		this.configManager = new ConfigManager();
		this.configManager.loadConfig("default");

		this.eventBus.register(this);
		this.presetManager = new PresetManager();

		// RenderUtil.initShaders(); // <-- USUŃ LUB ZAKOMENTUJ TO, BO NIE MA TEJ METODY JESZCZE

		logger.info("[Echo] Initialized successfully.");
	}

	public void shutdown() {
		logger.info("[Echo] Shutting down...");
		if (this.configManager != null) {
			this.configManager.saveConfig("default");
		}
		this.eventBus.unregister(this);
	}

	public EventBus getEventBus() { return eventBus; }
	public ModuleManager getModuleManager() { return moduleManager; }
	public ConfigManager getConfigManager() { return configManager; }
	public PresetManager getPresetManager() { return presetManager; }
}