local cpu = require("../cpu/core")

--
-- In Amplitude there's only one location, no need to modify
--
local locations = {}

-- Valid City ID's in UYA begin at 0x28 (40)
locations[1] = {
	name = "AmpLand"
}

print("info", "Amplitude Medius World Locations: \n" .. tabular(locations, nil, true))

--
-- Chat/World channels
--
local channels = {}
channels[1] = {lobby_name="AmpLand"}

