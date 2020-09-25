package net.hashsploit.clank.lua;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.hashsploit.clank.server.MediusClient;

public class ClankLib extends TwoArgFunction {
	
	private Executor luaExecutor = Executors.newSingleThreadExecutor();
	private LuaFunction onConnectCallback;

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaTable luaClank = new LuaTable();
		
		// Getters
		luaClank.set("sleep", new sleep());
		luaClank.set("getConfig", new get_config());
		
		// Callbacks
		luaClank.set("onConnection", new on_connect_callback());
		//luaClank.set("onMessage", new chat_callback());
		
		
		
		LuaTable mt = LuaValue.tableOf(new LuaValue[] { INDEX, luaClank });
		env.set("clank", luaClank);
		env.get("package").get("loaded").set("bot", luaClank);

		if (LuaString.s_metatable == null) {
			LuaString.s_metatable = mt;
		}

		return luaClank;
	}
	
	/**
	 * Have the current thread sleep (delay) for a specified amount of time in milliseconds.
	 */
	class sleep extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue x) {
			int ms = x.checkint();

			if (ms < 0) {
				throw new LuaError("clank.sleep(n) requires n as a positive integer.");
			}

			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				return LuaValue.error("Sleep Interrupted!");
			}

			return LuaValue.NIL;
		}
	}
	
	class get_config extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			return null;
		}
	}
	
	class on_connect_callback extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue func) {
			if (func.isnil()) {
				onConnectCallback = null;
				return LuaValue.NIL;
			}
			LuaFunction f = func.checkfunction();
			onConnectCallback = f;
			return LuaValue.NIL;
		}
	}
	
	/**
	 * Callback for newly connected clients.
	 * @param clank client object
	 */
	public synchronized void onConnectCallback(MediusClient client) {
		if (onConnectCallback != null && !onConnectCallback.isnil()) {
			luaExecutor.execute(() -> {
				onConnectCallback.call(LuaValue.userdataOf(client));
			});
		}
	}
	
}
