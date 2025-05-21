local event_x = ...
local event_y = select(2, ...)
REAPI:addUIToScreen("menu.png", 0, 0)
REAPI:displayText("Raycast", 50, 30, "font_16px.png")
REAPI:displayText("Engine Demo", 50, 50, "font_16px.png")
REAPI:displayText("Start", 50, 100, "font_8px.png")
REAPI:displayText("Exit", 50, 120, "font_8px.png")