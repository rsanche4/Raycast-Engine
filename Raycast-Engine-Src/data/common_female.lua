local event_x = ...
local event_y = select(2, ...)


local speed = 0.01

if REAPI:get_entity_id_by_position(event_x, event_y)=="first_zombie" then
    speed = 0.05
end

local animationsWalk = {"sprite_common_female_walk0.png", "sprite_common_female_walk1.png"}

local animation_index = 1
if REAPI:getFrameNumber()%10 < 5 then
    animation_index = 2
end

REAPI:pathfindToward(speed, event_x, event_y, REAPI:getPlayerX(), REAPI:getPlayerY())

REAPI:edit_entity(REAPI:get_entity_id_by_position(event_x, event_y), animationsWalk[animation_index], REAPI:getMoveTowardX(), REAPI:getMoveTowardY())