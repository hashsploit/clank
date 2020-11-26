if _CPU == nil then error("Please load 'core' instead of directly accessing this file!") return end

function starts_with(str, start)
	return str:sub(1, #start) == start
end

function ends_with(str, ending)
	return ending == "" or str:sub(-#ending) == ending
end

-- URL-encode a string
function url_decode(str)
	str = str:gsub("+", " ")
	str = str:gsub("%%(%x%x)", function(h)
		return string.char(tonumber(h,16))
	end)
	str = str:gsub("\r\n", "\n")
	return str
end

-- Decode a URL-encoded string
function url_encode(str)
	if str then
		str = str:gsub("\n", "\r\n")
		str = str:gsub("([^%w %-%_%.%~])", function(c)
			return ("%%%02X"):format(string.byte(c))
		end)
	str = str:gsub(" ", "+")
	end
	return str	
end


