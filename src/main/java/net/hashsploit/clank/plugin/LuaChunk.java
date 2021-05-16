package net.hashsploit.clank.plugin;

import java.io.PrintStream;
import java.util.logging.Logger;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

public class LuaChunk {

	private static final Logger logger = Logger.getLogger(LuaChunk.class.getName());
	private static Globals globals;
	private Globals user_globals;
	private LuaValue chunk;
	private String pluginName;
	private String chunkName;
	private String sourcePath;

	// TODO: Read Lua binary (.lub) files.
	
	/**
	 * Load a Lua source code script (.lua) 
	 * @param chunkName
	 * @param sourceCode
	 */
	public LuaChunk(String pluginName, String chunkName, String sourceCode) {
		this.pluginName = pluginName;
		this.chunkName = chunkName;
		this.sourcePath = sourceCode;
		
		loadSource(chunkName, sourceCode);
	}

	private void loadSource(String chunkName, String sourceCode) {
		logger.fine(String.format("Loading Lua script %s ...", chunkName));
		
		// Each script will have it's own set of globals, which should
		// prevent leakage between scripts running on the same server.
		user_globals = new Globals();

		user_globals.load(new JseBaseLib());
		user_globals.load(new PackageLib());
		user_globals.load(new Bit32Lib());
		user_globals.load(new TableLib());
		user_globals.load(new StringLib());
		user_globals.load(new JseMathLib());

		// Clank Library
		user_globals.load(new ClankLib());

		// This library is dangerous as it gives unfettered access to the
		// entire Java VM, so it's not suitable within this lightweight sandbox.
		//user_globals.load(new LuajavaLib());

		// Starting coroutines in scripts will result in threads that are
		// not under the server control, so this library should probably remain out.
		user_globals.load(new CoroutineLib());

		// These are probably unwise and unnecessary for scripts on servers,
		// although some date and time functions may be useful.
		user_globals.load(new JseIoLib());
		user_globals.load(new JseOsLib());

		// Loading and compiling scripts from within scripts may also be
		// prohibited, though in theory it should be fairly safe.
		// disable LuaC to prevent compiling, but keep LoadState to allow loading
		// binaries
		LoadState.install(user_globals);
		LuaC.install(user_globals);
		
		user_globals.STDOUT = new PrintStream(user_globals.STDOUT) {
			@Override
			public void print(String s) {
				super.print(String.format("[%s] %s", pluginName, s));
			}
		};
		
		try {
			final LuaValue chunk = globals.load(sourceCode, chunkName, user_globals);
			
			this.chunk = chunk.checkfunction().call();
			
		} catch (LuaError e) {
			logger.warning("[lua] " + e.getMessage());
		}
		
	}
	
	/**
	 * Get the chunk's name.
	 * @return
	 */
	public String getChunkName() {
		return chunkName;
	}
	
	/**
	 * Get the Lua file source path.
	 * @return
	 */
	public String getSourcePath() {
		return sourcePath;
	}
	
	/**
	 * Get the Lua chunk function value.
	 * @return
	 */
	public LuaValue getChunk() {
		return chunk;
	}
	
	/**
	 * Override Lua STDOUT stream.
	 * @return
	 */
	public void setLuaStdOut(PrintStream out) {
		user_globals.STDOUT = out;
	}
	
	/**
	 * Override Lua STDERR stream.
	 * @return
	 */
	public void setLuaStdErr(PrintStream err) {
		user_globals.STDERR = err;
	}
	
	/**
	 * Basic read-only table whose contents are initialized from another table.
	 */
	private static class ReadOnlyLuaTable extends LuaTable {
		public ReadOnlyLuaTable(LuaValue table) {
			presize(table.length(), 0);
			for (Varargs n = table.next(LuaValue.NIL); !n.arg1().isnil(); n = table.next(n.arg1())) {
				LuaValue key = n.arg1();
				LuaValue value = n.arg(2);
				super.rawset(key, value.istable() ? new ReadOnlyLuaTable(value) : value);
			}
		}

		@Override
		public LuaValue setmetatable(LuaValue metatable) {
			return error("table is read-only");
		}

		@Override
		public void set(int key, LuaValue value) {
			error("table is read-only");
		}

		@Override
		public void rawset(int key, LuaValue value) {
			error("table is read-only");
		}

		@Override
		public void rawset(LuaValue key, LuaValue value) {
			error("table is read-only");
		}

		@Override
		public LuaValue remove(int pos) {
			return error("table is read-only");
		}
	}
	
	static {
		globals = new Globals();
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new StringLib());

		LoadState.install(globals);
		LuaC.install(globals);

		// Set up the LuaString metatable to be read-only since it is shared across all
		// scripts.
		LuaString.s_metatable = new ReadOnlyLuaTable(LuaString.s_metatable);

		LuaBoolean.s_metatable = new ReadOnlyLuaTable(
			LuaValue.tableOf(new LuaValue[] {
				LuaValue.ADD,
				new TwoArgFunction() {
					public LuaValue call(LuaValue x, LuaValue y) {
						return LuaValue.valueOf((x == TRUE ? 1.0 : x.todouble()) + (y == TRUE ? 1.0 : y.todouble()));
					}
				},
			})
		);
	}
	
}
