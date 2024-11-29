import pygame
import json

# Initialize Pygame
pygame.init()
# Initialize Pygame mixer
pygame.mixer.init()
sound = pygame.mixer.Sound('..\\save.mp3')
# Constants
SCREEN_WIDTH, SCREEN_HEIGHT = 800, 600
GRID_SIZE = 500
CELL_SIZE = 64
VISIBLE_CELLS_X = SCREEN_WIDTH // CELL_SIZE
VISIBLE_CELLS_Y = SCREEN_HEIGHT // CELL_SIZE

# Colors
WHITE = (255, 255, 255)
GRAY = (200, 200, 200)
BLACK = (0, 0, 0)

# Load project folder
project_path = open('..\\project_location.txt', 'r').read()

# Image for Empty Areas Not Textured on
NULL_IMAGE = pygame.image.load(project_path+"\\block0.png")

# Image for Empty Areas in Layer 1 Not Textured on
# NULL_IMAGE_SPRITE = pygame.image.load(project_path+"\\sprite0.png").subsurface(pygame.Rect(0, 0, 64, 64))

# The Event Sprite we will use to know where we placed down an event
EV_IMAGE = pygame.image.load(project_path+"\\ev.png")

# Image for blocks (64x64 pixels)
BLOCK_IMAGE = pygame.image.load(project_path+"\\block0.png")
# Initialize font for rendering text
font = pygame.font.SysFont("Courier New", 36)  # Default font, size 36
# Set up screen
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT), pygame.RESIZABLE)

pygame.display.set_caption("World Editor")

# Create a grid with empty cells (0 means empty, 1 means occupied by image)
grid0 = [[None for _ in range(GRID_SIZE)] for _ in range(GRID_SIZE)]
layer0_encoded = [["block0.png" for _ in range(GRID_SIZE)] for _ in range(GRID_SIZE)] #this is for the images so i can store it in a place
grid1 = [[None for _ in range(GRID_SIZE)] for _ in range(GRID_SIZE)]
layer1_encoded = [["sprite0.png" for _ in range(GRID_SIZE)] for _ in range(GRID_SIZE)]

event_data = []
# Now we have to load whatever was saved up on the database, if there was anything saved
# Save the map if we press Enter
worlds_data_path = project_path+ "\\worlds_data.json"
with open(worlds_data_path, 'r') as file:
    data = json.load(file)

current_selected_world = current_selected_world = open('..\\.tempdata.txt', 'r').read()
index_to_update = -1
for j, allvars in enumerate(data["world_data"]):
    if allvars["VAR0"].split(":")[1]==current_selected_world:
        
        index_to_update=j
        break
                
if index_to_update!=-1:
    # we selected the world. Now let's check if that world has a map cuz this is where we stored the map info
    if data["world_data"][index_to_update]["VAR7"]:
        # reconstruct array
        comma_string_layer0 = data["world_data"][index_to_update]["VAR7"].split(":")[1]
        flat_list = comma_string_layer0.split(",")
   
        layer0_encoded = [flat_list[i * GRID_SIZE:(i + 1) * GRID_SIZE] for i in range(GRID_SIZE)]
        part1_reconstruct = list(map(lambda imgstr: pygame.image.load(project_path+f"\\{imgstr}"), flat_list))
        grid0 = [part1_reconstruct[i * GRID_SIZE:(i + 1) * GRID_SIZE] for i in range(GRID_SIZE)]

    if data["world_data"][index_to_update]["VAR8"]:
        comma_string_layer1 = data["world_data"][index_to_update]["VAR8"].split(":")[1]
        flat_list = comma_string_layer1.split(",")
   
        layer1_encoded = [flat_list[i * GRID_SIZE:(i + 1) * GRID_SIZE] for i in range(GRID_SIZE)]
        part1_reconstruct = list(map(lambda imgstr: pygame.image.load(project_path+f"\\{imgstr}").subsurface(pygame.Rect(0, 0, 64, 64)), flat_list))
        grid1 = [part1_reconstruct[i * GRID_SIZE:(i + 1) * GRID_SIZE] for i in range(GRID_SIZE)]
    
    if data["world_data"][index_to_update]["VAR9"]:
        part1_reconstruct = data["world_data"][index_to_update]["VAR9"].split(":")[1]
        event_data = part1_reconstruct.split(",")
        if len(event_data)==1: # there needs to be 3 at least or nothing
            event_data = []



grid = grid0
selectedlayer = 0




# Camera starting position (center of the map at 250, 250)
camera_x, camera_y = 250, 250

# Main loop
running = True
while running:
    screen.fill(BLACK)

    # Event handling
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_1:
                grid = grid0
                selectedlayer = 0
            elif event.key == pygame.K_2:
                grid = grid1
                selectedlayer = 1
            if event.key == pygame.K_UP:
                camera_y = max(0, camera_y - 1)  # Move up
            elif event.key == pygame.K_DOWN:
                camera_y = min(GRID_SIZE - VISIBLE_CELLS_Y, camera_y + 1)  # Move down
            elif event.key == pygame.K_LEFT:
                camera_x = max(0, camera_x - 1)  # Move left
            elif event.key == pygame.K_RIGHT:
                camera_x = min(GRID_SIZE - VISIBLE_CELLS_X, camera_x + 1)  # Move right
            
            if event.key == pygame.K_RETURN:
                # Save the map if we press Enter
                worlds_data_path = project_path+ "\\worlds_data.json"
                with open(worlds_data_path, 'r') as file:
                    data = json.load(file)

                current_selected_world = current_selected_world = open('..\\.tempdata.txt', 'r').read()
                index_to_update = -1
                for j, allvars in enumerate(data["world_data"]):
                    if allvars["VAR0"].split(":")[1]==current_selected_world:
                        
                        index_to_update=j
                        break
                
                if index_to_update!=-1:
                    
                    #print([item for sublist in layer0_encoded for item in sublist])
                    data["world_data"][index_to_update]["VAR7"] = f"layer0:{",".join([item for sublist in layer0_encoded for item in sublist])}"
                    data["world_data"][index_to_update]["VAR8"] = f"layer1:{",".join([item for sublist in layer1_encoded for item in sublist])}"
                    data["world_data"][index_to_update]["VAR9"] = f"event_data:{",".join(map(str, event_data))}"

                    # Open a file in write mode ('w') and dump JSON data into it
                    with open(worlds_data_path, 'w') as file:
                        json.dump(data, file, indent=4)  # The indent argument adds pretty formatting
                        sound.play()
                
        elif event.type == pygame.VIDEORESIZE:
            # This will update the window size if it's resized
            VISIBLE_CELLS_X = event.w // CELL_SIZE
            VISIBLE_CELLS_Y = event.h // CELL_SIZE
            #screen = pygame.display.set_mode((event.w, event.h), pygame.RESIZABLE)
    

    mouse_pressed = pygame.mouse.get_pressed()
    if mouse_pressed[0] or mouse_pressed[2]:
        BLOCK_IMAGE = pygame.image.load(project_path+"\\"+open('selected_texture.txt', 'r').read())
        
        crop_rect = pygame.Rect(0, 0, 64, 64)  # (x, y, width, height)

        # Crop the image to the top 64x64
        cropped_image = BLOCK_IMAGE.subsurface(crop_rect)

        # Get mouse position and convert to grid coordinates
        mouse_x, mouse_y = pygame.mouse.get_pos()
        grid_x = (mouse_x // CELL_SIZE) + camera_x
        grid_y = (mouse_y // CELL_SIZE) + camera_y

        # Check if clicked within the grid bounds
        if 0 <= grid_x < GRID_SIZE and 0 <= grid_y < GRID_SIZE:
            # Place the image in the clicked cell
            grid[grid_y][grid_x] = cropped_image


            # save up the image paths as well
            if selectedlayer==0:    
                layer0_encoded[grid_y][grid_x] = open('selected_texture.txt', 'r').read()

            if selectedlayer==1:    
                layer1_encoded[grid_y][grid_x] = open('selected_texture.txt', 'r').read()





        if selectedlayer==1 and mouse_pressed[2]: # we are in layer1 and we right clicked
            
            import event_editor
            file_selected_location = event_editor.run_event_editor()

            if file_selected_location:

                if file_selected_location == "delete_script.lua":
                    
                    
                    for i in range(0, len(event_data), 3):
                        script, y, x = event_data[i:i+3]
                        if int(y) == grid_y and int(x) == grid_x:
                            # Remove the script with the matching coordinates
                            del event_data[i:i+4]
                            # Exit after deletion
                else:
                    # Check if the coordinates already exist in event_data
                    updated = False
                    for i in range(0, len(event_data), 3):
                        script, y, x = event_data[i:i+3]
                        if int(y) == grid_y and int(x) == grid_x:
                            # Update the name of the script for the existing coordinates
                            event_data[i] = file_selected_location
                            updated = True
                            break
                    if not updated:
                        # Add the new script and coordinates at the end
                        event_data.extend([file_selected_location, grid_y, grid_x])


    # Draw grid cells with camera offset
    for y in range(VISIBLE_CELLS_Y):
        for x in range(VISIBLE_CELLS_X):
            # Calculate grid position with camera offset
            grid_x = camera_x + x
            grid_y = camera_y + y
            
            # if layer0_encoded[grid_y][grid_x]=="block0.png" or layer1_encoded[grid_y][grid_x]=="sprite0.png":
            #     screen.blit(NULL_IMAGE, (x * CELL_SIZE, y * CELL_SIZE))  
            # else:  
            screen.blit(grid[grid_y][grid_x], (x * CELL_SIZE, y * CELL_SIZE))

            if selectedlayer==1 and layer1_encoded[grid_y][grid_x]=="sprite0.png":
                screen.blit(NULL_IMAGE, (x * CELL_SIZE, y * CELL_SIZE))

            if len(event_data)>=3 and selectedlayer==1:
                
                for c in range(0, len(event_data), 3):

                    script, evy, evx = event_data[c:c+3]
                    if int(evy) == grid_y and int(evx) == grid_x:
                        # Draw the event icon so we know we have an event there
                        screen.blit(EV_IMAGE, (x * CELL_SIZE, y * CELL_SIZE))
                        break
                    
            
            # Draw the cell border
            pygame.draw.rect(screen, WHITE, (x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE), 1)

    # Get the current mouse position and convert it to grid coordinates
    mouse_x, mouse_y = pygame.mouse.get_pos()
    grid_x = (mouse_x // CELL_SIZE) + camera_x
    grid_y = (mouse_y // CELL_SIZE) + camera_y

    # Render the mouse position (grid coordinates)
    position_text = f"Layer:{selectedlayer}; Location:({grid_y},{grid_x})"

    text_surface = font.render(position_text, True, WHITE)


    # Blit the text onto the screen at top-left corner
    screen.blit(text_surface, (10, 10))

    pygame.display.flip()

# Quit Pygame
pygame.quit()



