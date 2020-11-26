if _CPU == nil then error("Please load 'core' instead of directly accessing this file!") return end

local ansicolors = require("ansicolors")
local original_print = print
local levels = {
	{name="debug", color="blue"},
	{name="info", color="reset"},
	{name="warn", color="yellow"},
	{name="error", color="red"}
}

-- Logger
_G.print = function(level, msg)
	if msg == nil and not level == nil then
		msg = level
		level = "info"
	end
	if level == nil then
		level = "info"
	end
	if msg == nil then
		msg = "nil"
	end
	if not clank == nil then
		local final_level = "info"
		for k, v in ipairs(levels) do
			if level == v.name then
				final_level = level
			end
		end
		clank.print(final_level, ansicolors(msg))
	else
		local final_msg = ""
		for k, v in ipairs(levels) do
			if level == v.name then
				final_msg = os.date("%a %b %d %Y %H:%M:%S") .. " [%{" .. v.color .. "}" .. level .. "%{reset}] " .. msg .. "%{reset}"
			end
		end
		original_print(ansicolors(final_msg))
	end
end



