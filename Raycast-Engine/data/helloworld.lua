local event_x = ...
local event_y = select(2, ...)
print("Coordinates received: (" .. event_x .. ", " .. event_y .. ")")
REAPI:endScript(event_x, event_y)
