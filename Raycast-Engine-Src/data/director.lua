local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isGame") == 1 then
    local base_x = 250.5
    local base_y = 243
    for i = 0, 9 do
        local zombie_id = "zombie_" .. i
        local sprite = "sprite_common_female_walk0.png"
        local script = "common_female.lua"
        REAPI:add_entity(zombie_id, sprite, base_x, base_y + i, script)
    end
    REAPI:endScript("director.lua")
end
