package net.hashsploit.clank.plugin;

import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.json.JSONArray;
import org.json.JSONObject;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaDouble;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;

public class ClankLib extends TwoArgFunction {
	
	private static final String PACKAGE_NAME = "clank";
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
		env.set(PACKAGE_NAME, luaClank);
		env.get("package").get("loaded").set(PACKAGE_NAME, luaClank);

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
				throw new LuaError(PACKAGE_NAME + ".sleep(n) requires n as a positive integer.");
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
			JsonObject json = Clank.getInstance().getConfig().getJsonObject();
			return jsonToLuaTable(json);
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
	
	/**
	 * Converts a JSON object (recursive) into a Lua table
	 * @param object
	 * @return
	 */
	private static LuaValue jsonToLuaTable(JsonObject json) {
		
		if (!json.isJsonNull()) {
			
			LuaTable table = new LuaTable();
			
			for (final String key : json.keySet()) {
				final JsonElement value = json.get(key);

				if(value.isJsonPrimitive()) {
					JsonPrimitive jsonPrimitive = value.getAsJsonPrimitive();
					if(jsonPrimitive.isNumber()) {
						if(jsonPrimitive.getAsString().matches("[-+]?[0-9]*\\\\.[0-9]+")) {
							table.entry(LuaString.valueOf(key), LuaDouble.valueOf(jsonPrimitive.getAsDouble()));
						} else {
							table.entry(LuaString.valueOf(key), LuaInteger.valueOf(jsonPrimitive.getAsInt()));
						}
					} else if (jsonPrimitive.isBoolean()) {
						table.entry(LuaString.valueOf(key), LuaBoolean.valueOf(jsonPrimitive.getAsBoolean()));
					} else if(jsonPrimitive.isString()) {
						table.entry(LuaString.valueOf(key), LuaString.valueOf(jsonPrimitive.getAsBoolean()));
					}
				} else if(value.isJsonArray()) {
					JsonArray array = value.getAsJsonArray();
					Iterator<JsonElement> objs = array.iterator();
					LuaTable subTable = new LuaTable();

					while (objs.hasNext()) {
						JsonElement jsonElement = objs.next();
						if (!jsonElement.isJsonNull()) {
							subTable.add(jsonToLuaTable(jsonElement.getAsJsonObject()));
						}
					}

					table.entry(LuaString.valueOf(key), subTable);
				}
				return table;
			}
		}

		return LuaValue.NIL;
	}
	
}
