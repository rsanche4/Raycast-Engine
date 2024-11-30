import tkinter as tk
import os
from PIL import Image, ImageTk
import json

sep = os.path.sep

# Create the main window
root = tk.Tk()

root.title("Skybox Selector")
from screeninfo import get_monitors
w = get_monitors()[0].width//2
h = get_monitors()[0].height
posx = 0
posy = 0
root.geometry(f"{w}x{h}+{posx}+{posy}")
# root.resizable(False, False)

# Define the directory path
place_of_res = open(".."+sep+"project_location.txt", "r").read()

# Get all files in the directory
files = os.listdir(place_of_res)

# Filter out only the block#.png files
image_files = [f for f in files if f.startswith("skybox") and f.endswith(".png")]

image_files_blocks = [f for f in files if f.startswith("block") and f.endswith(".png")]

image_files = image_files + image_files_blocks

# Desired image size
target_size = (800*4//5, 600//5)  # Adjust to your preferred size

# Load and resize the images
images = []
for image_file in image_files:
    if "block" in image_file:
        target_size = (128, 128) 

    image_path = os.path.join(place_of_res, image_file)
    
    # Open, resize, and convert the image to PhotoImage
    img1 = Image.open(image_path)
    img1 = img1.resize(target_size)  # Resize the image
    tk_image = ImageTk.PhotoImage(img1)  # Convert to Tkinter-compatible image
    
    images.append(tk_image)



current_image = images[0]

# Function to generate and display a random number
def showimgselected(i, index):
    global current_image
    global place_of_res
    current_image=i
    blockchosen.config(image=current_image)
    # and also save that image in the database so we know we chose that
    current_selected_world = open(".."+sep+".tempdata.txt", "r").read()

    if current_selected_world=="":
        return
    
    worlds_data_path = place_of_res+ ""+sep+"worlds_data.json"

    with open(worlds_data_path, "r") as file:
        data = json.load(file)  # Parse the JSON file into a Python dictionary 

    # Check if it"s there is anything in there, if not insert. otherwise already there by that name edit it, otherwise then push it.
    index_to_update = -1
    for j, allvars in enumerate(data["world_data"]):
        if allvars["VAR0"].split(":")[1]==current_selected_world:
            
            index_to_update=j
            break
    
    if index_to_update!=-1:
        
        
            
        data["world_data"][index_to_update]["VAR6"] = f"skybox_id:{image_files[index]}"

        # Open a file in write mode ("w") and dump JSON data into it
        with open(worlds_data_path, "w") as file:
            json.dump(data, file, indent=4)  # The indent argument adds pretty formatting


# Create a canvas and a scrollbar
canvas = tk.Canvas(root)
scrollbar = tk.Scrollbar(root, orient="vertical", command=canvas.yview)
canvas.configure(yscrollcommand=scrollbar.set)

# Create a frame inside the canvas to hold all the buttons
button_frame = tk.Frame(canvas)

# Place the button frame on the canvas
canvas.create_window((0, 0), window=button_frame, anchor="nw")

# Define the grid dimensions (number of columns in each row)
num_columns = 1  # Adjust this number to change the grid"s width

# Add buttons for each image inside the button frame in a grid layout
for index, img in enumerate(images):
    row = index // num_columns
    col = index % num_columns
    button = tk.Button(button_frame, image=img, command= lambda i=img, ind=index: showimgselected(i, ind))
    button.grid(row=row, column=col, padx=10, pady=10)

# Update the scrollable region
button_frame.update_idletasks()  # Update the frame size
canvas.config(scrollregion=canvas.bbox("all"))

# The showing of which block we have chosen
blockchosen = tk.Label(root, image=current_image, bd=2, relief="solid")
blockchosen.pack()
# Pack the canvas and scrollbar
canvas.pack(side="left", fill="both", expand=True)
scrollbar.pack(side="right", fill="y")

root.mainloop()
