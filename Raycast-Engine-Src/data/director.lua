local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isGame") == 1 then
    local base_x = 250.5
    local base_y = 255
    for i = 0, 0 do
        local zombie_id = "zombie_" .. i
        local sprite = "sprite_zom_walk0_femalestudent1.png"
        local script = "common_female" .. i .. ".lua"
        REAPI:add_entity(zombie_id, sprite, base_x, base_y + i, script)
    end
    REAPI:endScript("director.lua")
end
