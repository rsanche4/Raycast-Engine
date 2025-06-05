local event_x = ...
local event_y = select(2, ...)

local speed = 0.1

local animationsWalk = {"sprite_common_female_walk0.png", "sprite_common_female_walk1.png"}

local animation_index = 1
if REAPI:getFrameNumber()%10 < 5 then
    animation_index = 2
end

local movexy = REAPI:pathfindToward(REAPI:get_entity_id_by_position(event_x, event_y), event_x, event_y, REAPI:getPlayerX(), REAPI:getPlayerY(), speed)

REAPI:edit_entity(REAPI:get_entity_id_by_position(event_x, event_y), animationsWalk[animation_index], REAPI:decodeXfromPathString(movexy), REAPI:decodeYfromPathString(movexy))