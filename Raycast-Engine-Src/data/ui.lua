local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isMenu")=="0" then
    REAPI:addUIToScreen("vignette.png", 0, 0)
end
