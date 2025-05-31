local event_x = ...
local event_y = select(2, ...)

if REAPI:readTempVar("isGame")==1 then
    REAPI:add_entity("first_zombie", "sprite_common_female_walk0.png", 251, 243, "common_female.lua")
    REAPI:add_entity("second_zombie", "sprite_common_female_walk0.png", 250, 245, "common_female.lua")
    REAPI:endScript("director.lua")
end
