--
-- Clank Plugin Utils (CPU)
-- A basic utility framework for plugins to use
--
_CPU="Clank Plugin Utils"
_CPU_VERSION="0.1.0"

require("utils")
require("logger")
local tabular = require("tabular")

if clank == nil then
	print("warn", "Running " .. _VERSION .. " without the %{greenbg}Clank API%{reset} implementation. Functionality may be severely limited!")
else
	print("debug", _CPU .. " v" .. _CPU_VERSION .. " (implementing " .. clank.name() .. " v" .. clank.version() .. ")")
end

--print("debug", "Global Table:\n" .. tabular(_G, nil, true))

